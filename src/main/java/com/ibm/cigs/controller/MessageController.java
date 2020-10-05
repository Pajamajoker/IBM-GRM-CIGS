package com.ibm.cigs.controller;

import java.io.IOException;

import com.ibm.cigs.entity.MessageEntity;
import com.ibm.cigs.service.CloudantService;
import com.ibm.cigs.service.MessageService;
import com.twilio.rest.api.v2010.account.Message;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private CloudantService cloudantService;
    @Autowired
    private MessageService messageService;

    @PostMapping(path = "/whatsapp/recv", consumes = "application/x-www-form-urlencoded")
    public String RecieveMessage(MessageEntity messageEntity) throws IOException {
        //cloudantService.insertDocument(messageEntity);
        System.out.println(messageEntity.getBody());
        String response=messageService.sendMessageToWatson(messageEntity);
        return response;
    }

    @PostMapping(path = "/whatsapp/send", consumes = "application/json")
    public String SendMessage(@RequestBody Request req) {
        Message message = messageService.sendMessageToUser(req.getBody(), req.getNumber());
        //cloudantService.insertDocument(message);
        return message.getBody();
    }

}

class Request {
    String body, number;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Request [body=" + body + ", number=" + number + "]";
    }

}
/*
 * { SmsMessageSid: SmsSid: SmsStatus: Body: To: NumSegments: MessageSid:
 * AccountSid: From: ApiVersion: }
 */