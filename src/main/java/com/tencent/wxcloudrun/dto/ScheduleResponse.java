package com.tencent.wxcloudrun.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleResponse {
    private int status;
    private String message;
    private ReminderMessage reminderMessage;

    @Data
    public static class ReminderMessage {
        private String userID;
        private String eventTitle;
        private LocalDateTime eventDateTime;
        private String eventDescription;
    }
}
