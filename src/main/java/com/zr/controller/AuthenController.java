package com.zr.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.zr.common.R;
import com.zr.constant.OauthConstant;
import com.zr.constant.PropertiesConstant;
import com.zr.constant.RedisKeyConstant;
import com.zr.exception.PlatformException;
import com.zr.pojo.dto.*;
import com.zr.pojo.vo.CodeVO;
import com.zr.pojo.vo.LoginVO;
import com.zr.pojo.vo.SsoUserVO;
import com.zr.service.OauthAppInfoService;
import com.zr.service.SsoUserService;
import com.zr.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;


@RestController
@Slf4j
@RequestMapping("/authenticate")
@Api(tags = "登录")
public class AuthenController {

    @Resource
    private SsoUserService ssoUserService;

    @Resource
    private OauthAppInfoService oauthAppInfoService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("登录 有accessToken")
    @PostMapping("/login")
    public R<LoginVO> login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        String redirectUri = loginDTO.getRedirectUri();
        String appId = loginDTO.getAppId();

        // 校验appId
        oauthAppInfoService.isExistClient(appId);

        // 校验accessToken
        String accessToken = request.getHeader(OauthConstant.ACCESSTOKEN);
        checkAndExpireKey(accessToken);

        // 生成code
        String code = generateCode(redirectUri, appId, accessToken);

        String redirectUrL = authRedirectUri(redirectUri, code);

        log.info("appId[{}]登录,跳转地址[{}]", appId, redirectUrL);
        return R.redirectOK(LoginVO.builder().redirectUri(redirectUrL).build());
    }


    @ApiOperation("登录 无accessToken")
    @PostMapping("/doLogin")
    public R<LoginVO> doLogin(@RequestBody @Valid DoLoginDTO doLoginDTO) {
        //校验appid
        oauthAppInfoService.isExistClient(doLoginDTO.getAppId());

        //校验登录
        SsoUserVO ssoUserVO = ssoUserService.login(doLoginDTO.getAccount(), doLoginDTO.getPassword());

        //生成accessToken
        String accessToken = UUID.randomUUID().toString(Boolean.TRUE);

        //存储redis中
        stringRedisTemplate.opsForValue().set(RedisKeyConstant.ACESSTOKEN_KEY.concat(accessToken), JSONUtil.toJsonStr(ssoUserVO), PropertiesUtil.getPropertiesValue(PropertiesConstant.ACCESS_TIMEOUT, Long.class), TimeUnit.SECONDS);

        //生成code
        String code = generateCode(doLoginDTO.getRedirectUri(), doLoginDTO.getAppId(), accessToken);

        String redirectUrL = authRedirectUri(doLoginDTO.getRedirectUri(), code);
        LoginVO loginVO = LoginVO.builder().redirectUri(redirectUrL).accessToken(accessToken).appId(doLoginDTO.getAppId()).ssoUserVO(ssoUserVO).build();

        log.info("appId[{}]登录,跳转地址[{}]", doLoginDTO.getAppId(), redirectUrL);
        return R.redirectOK(loginVO);
    }


    @ApiOperation("登出")
    @PostMapping("/logout")
    public R logout(@RequestBody @Valid LogoutDTO logoutDTO) {
        String key = RedisKeyConstant.ACESSTOKEN_KEY.concat(logoutDTO.getAccessToken());
        String ssoUserJson = stringRedisTemplate.opsForValue().get(key);
        if (ObjectUtil.isEmpty(ssoUserJson)) throw new PlatformException("accessToken过期或者不存在");
        SsoUserVO ssoUserVO = JSONUtil.toBean(ssoUserJson, SsoUserVO.class);
        stringRedisTemplate.delete(key);
        log.info("appId[{}]执行退出 用户[{}-{}]退出成功", logoutDTO.getAppId(), ssoUserVO.getUsername(), ssoUserVO.getAccount());
        return R.ok();
    }


    @ApiOperation("根据code获取accessToken 第三方")
    @PostMapping("/getAccessToken")
    public R<String> getAccessToken(@RequestBody @Valid GetAccessTokenDTO getAccessTokenDTO) {
        //校验客户端信息
        oauthAppInfoService.validate(getAccessTokenDTO.getAppId(), getAccessTokenDTO.getAppSecret());

        String key = RedisKeyConstant.CODE_KEY.concat(getAccessTokenDTO.getCode());
        String codeVOJson = stringRedisTemplate.opsForValue().get(key);
        if (ObjectUtil.isEmpty(codeVOJson)) throw new PlatformException("code过期或者不存在");
        //删除code
        stringRedisTemplate.delete(key);

        CodeVO codeVO = JSONUtil.toBean(codeVOJson, CodeVO.class);

        log.info("appId[{}]校验code成功", getAccessTokenDTO.getAppId());
        return R.ok(codeVO.getAccessToken());
    }


    @ApiOperation("根据accessToken获取用户信息 第三方")
    @PostMapping("/getUserInfo")
    public R<SsoUserVO> getUserInfo(@RequestBody @Valid GetUserInfoDTO getUserInfoDTO) {
        //校验客户端信息
        oauthAppInfoService.validate(getUserInfoDTO.getAppId(), getUserInfoDTO.getAppSecret());

        String ssoUserJson = checkAndExpireKey(getUserInfoDTO.getAccessToken());

        SsoUserVO ssoUserVO = JSONUtil.toBean(ssoUserJson, SsoUserVO.class);

        log.info("appId[{}]校验accessToken成功", getUserInfoDTO.getAppId());
        return R.ok(ssoUserVO);
    }


    /**
     * 校验 & 续期 accessToken
     *
     * @param accessToken accessToken
     * @return
     */
    private String checkAndExpireKey(String accessToken) {
        if (ObjectUtil.isEmpty(accessToken)) throw new PlatformException("accessToken过期或者不存在");
        String key = RedisKeyConstant.ACESSTOKEN_KEY.concat(accessToken);
        String ssoUserJson = stringRedisTemplate.opsForValue().get(key);
        if (ObjectUtil.isEmpty(ssoUserJson)) throw new PlatformException("accessToken过期或者不存在");
        stringRedisTemplate.expire(key, PropertiesUtil.getPropertiesValue(PropertiesConstant.ACCESS_TIMEOUT, Long.class), TimeUnit.SECONDS);
        return ssoUserJson;
    }


    /**
     * 将授权码拼接到回调redirectUri中
     */
    private String authRedirectUri(String redirectUri, String code) {
        StringBuilder sbf = new StringBuilder(redirectUri);
        if (redirectUri.contains("?")) {
            sbf.append("&");
        } else {
            sbf.append("?");
        }
        sbf.append(OauthConstant.CODE).append("=").append(code);
        return sbf.toString();
    }


    /**
     * 生成code
     *
     * @param redirectUri redirectUri
     * @param appId       appId
     * @param accessToken accessToken
     * @return
     */
    private String generateCode(String redirectUri, String appId, String accessToken) {
        // 生成code & 保存redis中
        String code = UUID.randomUUID().toString(Boolean.TRUE);
        CodeVO codeVO = CodeVO.builder().accessToken(accessToken).appId(appId).redirectUri(redirectUri).build();
        stringRedisTemplate.opsForValue().set(RedisKeyConstant.CODE_KEY.concat(code), JSONUtil.toJsonStr(codeVO), PropertiesUtil.getPropertiesValue(PropertiesConstant.CODE_TIMEOUT, Long.class), TimeUnit.SECONDS);
        return code;
    }

}
