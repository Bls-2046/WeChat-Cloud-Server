package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.dto.ScheduleRequest;
import com.tencent.wxcloudrun.dto.ScheduleResponse;
import com.tencent.wxcloudrun.service.WeChatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat/api")
public class WeChatController {
    private final WeChatServer weChatServer;

    @Autowired
    public WeChatController(WeChatServer weChatServer) {
        this.weChatServer = weChatServer;
    }

    // 公众号发送日志消息
    @PostMapping("/send-reminder-message")
    public ScheduleResponse sendReminderMessage(@RequestBody ScheduleRequest request) {

        ScheduleResponse response = new ScheduleResponse();
        try {
            // 对请求服务器的密钥进行验证
            if (ScheduleRequest.verifyRequest(request.getRequestKey()) || request.getRequestKey() == null) {
                response.setStatus(403);
                response.setMessage("拒绝外部服务器访问");
                return response;
            }

            ScheduleResponse.ReminderMessage reminderMessage = weChatServer.sendReminderMessage(request);

            // 若返回值为空, 说明后端未正确传递日志信息
            if (reminderMessage == null) {
                response.setStatus(400);
                response.setMessage("未设置用户日志数据");
                return response;
            }

        } catch (Exception e) {
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return response;
        }

        // 发送成功
        response.setStatus(200);
        response.setMessage("send success");
        return response;
    }
}
