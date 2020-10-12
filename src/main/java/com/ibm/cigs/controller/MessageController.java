package com.ibm.cigs.controller;

import java.io.IOException;

import com.ibm.cigs.entity.MessageEntity;
import com.ibm.cigs.service.CloudantService;
import com.ibm.cigs.service.MessageService;
import com.ibm.cigs.service.WatsonService;
import com.twilio.rest.api.v2010.account.Message;

//import jdk.nashorn.internal.runtime.regexp.joni.WarnCallback;
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
    @Autowired
    private WatsonService assistantService;

    @PostMapping(path = "/whatsapp/recv", consumes = "application/x-www-form-urlencoded")
    public String receiveMessage(MessageEntity messageEntity) throws Exception {
        System.out.println(messageEntity.getBody());
        String[] responses = assistantService.processMessage(messageEntity);
        String sessionId = responses[0];
        //dump incoming message and session ID;
        //cloudantService.insertDocument(messageEntity, sessionId);
        //dump outgoing message and session ID;
        //cloudantService.insertDocument(messageEntity, sessionId, responses[1]);
        return responses[1];
    }

    @PostMapping(path = "/whatsapp/send", consumes = "application/json")
    public String sendMessage(@RequestBody Request req) {
        Message message = messageService.sendMessageToUser(req.getBody(), req.getNumber());
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