package com.tencent.wxcloudrun.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class WechatAuthController {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.secret}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/wechat/callback")
    public String handleWechatCallback(@RequestParam String code) {
        // 1. 使用 code 获取 access_token 和 openid
        String tokenUrl = String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                appid, secret, code
        );
        TokenResponse tokenResponse = restTemplate.getForObject(tokenUrl, TokenResponse.class);

        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            return "获取 access_token 失败";
        }

        // 2. 使用云调用获取用户信息
        String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info";
        UserInfo userInfo = restTemplate.getForObject(
                userInfoUrl + "?access_token={accessToken}&openid={openid}&lang=zh_CN",
                UserInfo.class,
                tokenResponse.getAccessToken(),
                tokenResponse.getOpenid()
        );

        if (userInfo == null) {
            return "获取用户信息失败";
        }

        System.out.println(userInfo);

        // 3. 将用户信息保存到数据库
        saveUserInfoToDatabase(userInfo);

        return "用户信息保存成功";
    }

    private void saveUserInfoToDatabase(UserInfo userInfo) {

    }
}

@Data
class TokenResponse {
    private String accessToken;
    private String openid;
}

@Data
class UserInfo {
    private String openid;
    private String nickname;
    private String headimgurl;
}
