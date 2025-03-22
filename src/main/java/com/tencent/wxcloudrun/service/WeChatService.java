package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.ScheduleRequest;
import com.tencent.wxcloudrun.dto.ScheduleResponse;

public interface WeChatService {
    ScheduleResponse.ReminderMessage sendReminderMessage(ScheduleRequest request);

    String getAccessToken();
}
