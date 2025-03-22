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

@Service
public class WeChatServerImpl implements WeChatServer {
    private static String ACCESS_TOKEN;

    @Override
    public ScheduleResponse.ReminderMessage sendReminderMessage(ScheduleRequest request) {
        try {
            getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getACCESS_TOKEN() == null) {
            System.out.println("=======================================================================");
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + getACCESS_TOKEN();

        System.out.println(url);

        JSONObject body = new JSONObject();
        body.put("touser", request.getReminderMessage().getOpenID()); // 用户的 OpenID
        body.put("msgtype", "text"); // 消息类型

        String eventTitle = request.getReminderMessage().getEventTitle();
        LocalDateTime eventDateTime = request.getReminderMessage().getEventDateTime();
        String EventDescription = request.getReminderMessage().getEventDescription();

        JSONObject content = new JSONObject();
        String message = "【" + eventTitle + "/" + eventDateTime + "】 " + EventDescription;
        content.put("content", message); // 消息内容
        body.put("text", content);

        System.out.println(body);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject response = (JSONObject) Https.post(url, body, headers);

        System.out.println("=======================================================================" + response);

        if (response != null) {
            System.out.println("=======================================================================" + response.optInt("errcode"));
            if (response.optInt("errcode", -1) != 0) {
                return null;
            }
        }

        ScheduleResponse.ReminderMessage reminderMessage = new ScheduleResponse.ReminderMessage();
        reminderMessage.setUserID(request.getReminderMessage().getOpenID());
        reminderMessage.setEventTitle(eventTitle);
        reminderMessage.setEventDateTime(eventDateTime);
        reminderMessage.setEventDescription(EventDescription);

        return reminderMessage;
    }

    // 获得 token
    @Scheduled(fixedRate = 5000000)
    public static void getAccessToken() throws Exception {

        // 构建请求体
        JSONObject body = new JSONObject();
        body.put("grant_type", "client_credential");
        body.put("appid", System.getenv("WECHAT_APPID"));
        body.put("secret", System.getenv("WECHAT_SECRET"));

        // 构建标头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 发起 post 请求获取 access_token
        JSONObject response = (JSONObject) Https.post("https://api.weixin.qq.com/cgi-bin/token", body, headers);

        String access_token = null;

        if (response != null) {
            access_token = response.getString("access_token");
        }

        if (access_token == null) {
            throw new Exception("access_token 获取失败");
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
