package com.tencent.wxcloudrun.service;

import com.tencent.wxcloudrun.dto.ScheduleRequest;
import com.tencent.wxcloudrun.dto.ScheduleResponse;

public interface WeChatServer {
    ScheduleResponse.ReminderMessage sendReminderMessage(ScheduleRequest request);
}
