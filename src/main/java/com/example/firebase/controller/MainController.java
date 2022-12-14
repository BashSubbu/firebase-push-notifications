package com.example.firebase.controller;

import com.example.firebase.dto.Notification;
import com.example.firebase.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {
    @Autowired
    private PushNotificationService pushNotificationService;
    @PostMapping("/v1/send-notification")
    public ResponseEntity<String> sendPushNotification(@RequestParam String token, @RequestBody Notification notification){
        String output = null;
        output = pushNotificationService.sendNotification(token,notification);
        return new ResponseEntity<>(output,HttpStatus.OK);
    }
}
