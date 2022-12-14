package com.example.firebase.service;

import com.example.firebase.dto.Notification;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PushNotificationService {
    private final RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Value("${firebase.key}")
    private String key;
    public PushNotificationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    public String sendNotification(String token, Notification notification){
        HttpEntity<String> httpEntity = null;
        ResponseEntity<String> responseEntity = null;
        HttpHeaders httpHeaders = null;
        HttpStatusCode httpStatus = null;
        String inputString = null;
        String response = null;

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set(HttpHeaders.AUTHORIZATION,"key=" + key);
        inputString = prepareNotificationContent(token,notification);
        String url = "https://fcm.googleapis.com/fcm/send";
        httpEntity = new HttpEntity<>(inputString,httpHeaders);
        responseEntity = this.restTemplate.postForEntity(url,httpEntity,String.class);
        httpStatus = responseEntity.getStatusCode();
        if(httpStatus==HttpStatus.OK){
            response = responseEntity.getBody();
            logger.info(responseEntity.getBody());
            logger.info("success");
        }
        else{
            logger.info("failed");
        }
        return response;
    }

    public String prepareNotificationContent(String token,Notification notification){
        Map<String, Object> inputMap = null;
        Map<String,Object>  notificationContentMap = null;
        String inputString = null;
        String notificationTitle = null;
        String notificationBody = null;

        inputMap = new HashMap<>();
        notificationContentMap = new HashMap<>();
        if(notification!=null){
            notificationTitle = notification.getTitle();
            notificationBody = notification.getBody();
        }
        notificationContentMap.put("title",notificationTitle);
        notificationContentMap.put("body",notificationBody);
        notificationContentMap.put("sound","default");
        notificationContentMap.put("show_in_foreground",true);
        inputMap.put("to",token);
        inputMap.put("priority","high");
        inputMap.put("notification",notification);
        Gson gson = new Gson();
        inputString = gson.toJson(inputMap);

        return inputString;
    }

}
