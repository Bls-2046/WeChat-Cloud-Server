package com.tencent.wxcloudrun.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class Https {

    private static final RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplate();
    }

    /**
     * 发送 GET 请求，返回 JSON 数据
     *
     * @param url    请求 URL
     * @param params 请求参数（可选）
     * @param headers 请求头（可选）
     * @return JSON 格式的响应数据
     */
    public static JSONObject get(String url, Map<String, String> params, HttpHeaders headers) {
        // 构建 URL 和查询参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        // 创建 HTTP 实体
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送 GET 请求
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );

        // 将响应体转换为 JSONObject
        return new JSONObject(response.getBody());
    }

    /**
     * 发送 POST 请求，返回 JSON 数据
     *
     * @param url    请求 URL
     * @param body   请求体（JSON 格式）
     * @param headers 请求头（可选）
     * @return JSON 格式的响应数据
     */
    public static Object post(String url, JSONObject body, HttpHeaders headers) {
        // 创建 HTTP 实体
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        // 发送 POST 请求
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        // 将响应体转换为 JSONObject
        // 获取原始响应数据
        String rawResponse = response.getBody();

        // 根据响应数据的格式返回相应的类型
        if (rawResponse == null || rawResponse.trim().isEmpty()) {
            return null; // 空响应
        } else if (rawResponse.startsWith("{")) {
            // 解析为 JSONObject
            return new JSONObject(rawResponse);
        } else if (rawResponse.startsWith("[")) {
            // 解析为 JSONArray
            return new JSONArray(rawResponse);
        } else {
            // 返回原始字符串
            return rawResponse;
        }
    }
}
