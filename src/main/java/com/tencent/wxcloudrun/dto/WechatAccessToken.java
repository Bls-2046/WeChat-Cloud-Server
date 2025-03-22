package com.tencent.wxcloudrun.dto;

import lombok.Data;

@Data
public class WechatAccessToken {
    private String access_token; // Access Token
    private int expires_in;      // 有效期（秒）
}
