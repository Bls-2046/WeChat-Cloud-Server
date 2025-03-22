package com.tencent.wxcloudrun.config;

import com.tencent.wxcloudrun.service.impl.WeChatServiceImpl;
import org.springframework.context.annotation.Bean;

public class PluginsConfig {
    public PluginsConfig() throws Exception {
        // WeChatServiceImpl.getAccessToken();
    }

    @Bean
    public PluginsConfig initApplication() throws Exception {
        return new PluginsConfig();
    }
}
