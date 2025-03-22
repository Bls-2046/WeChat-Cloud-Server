package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class ScheduleRequest {

    private static final String REQUEST_KEY = System.getenv("REQUEST_KEY");

    private String requestKey;
    private String username;
    private ReminderMessage reminderMessage;

    // 请求密钥, 验证是否为指定服务器
    public static Boolean verifyRequest(String requestKey) {
        return Objects.equals(requestKey, REQUEST_KEY);
    }

    @Data
    public static class ReminderMessage {
        private String OpenID;
        private String eventTitle;
        private LocalDateTime eventDateTime;
        private String eventDescription;
    }
}
