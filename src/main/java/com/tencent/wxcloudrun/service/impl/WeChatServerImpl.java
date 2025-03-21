package com.tencent.wxcloudrun.service.impl;

import com.tencent.wxcloudrun.dto.ScheduleRequest;
import com.tencent.wxcloudrun.dto.ScheduleResponse;
import com.tencent.wxcloudrun.service.WeChatServer;
import com.tencent.wxcloudrun.utils.Https;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeChatServerImpl implements WeChatServer {
    private static String ACCESS_TOKEN;

    @Override
    public ScheduleResponse.ReminderMessage sendReminderMessage(ScheduleRequest request) {
        String eventTitle = request.getReminderMessage().getEventTitle();
        LocalDateTime eventDateTime = request.getReminderMessage().getEventDateTime();
        String EventDescription = request.getReminderMessage().getEventDescription();

        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + ACCESS_TOKEN;

        System.out.println(url);

        JSONObject body = new JSONObject();
        body.put("touser", request.getReminderMessage().getUserID()); // 用户的 OpenID
        body.put("msgtype", "text"); // 消息类型

        JSONObject content = new JSONObject();
        String message = "【" + eventTitle + "/" + eventDateTime + "】 " + EventDescription;
        content.put("content", message); // 消息内容
        body.put("text", content);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject response = (JSONObject) Https.post(url, body, headers);

        if (response != null) {
            if (response.optInt("errcode", -1) != 0) {
                return null;
            }
        }

        ScheduleResponse.ReminderMessage reminderMessage = new ScheduleResponse.ReminderMessage();
        reminderMessage.setUserID(request.getReminderMessage().getUserID());
        reminderMessage.setEventTitle(eventTitle);
        reminderMessage.setEventDateTime(eventDateTime);
        reminderMessage.setEventDescription(EventDescription);

        return reminderMessage;
    }

    // 获得 token
    @Scheduled(fixedRate = 5000000)
    public static void getAccessToken() throws Exception {

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", System.getenv("WECHAT_APPID"));
        params.put("secret", System.getenv("WECHAT_SECRET"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject response = Https.get("https://api.weixin.qq.com/cgi-bin/token", params, headers);

        String access_token = response.getString("access_token");

        if (access_token == null) {
            throw new Exception("获取数据失败");
        }

        WeChatServerImpl.setACCESS_TOKEN(access_token);
    }

    private static void setACCESS_TOKEN(String access_token) {
        ACCESS_TOKEN = access_token;
    }

    private static String getACCESS_TOKEN() {
        return ACCESS_TOKEN;
    }
}
