package com.ibm.cigs.service;

import com.ibm.cigs.entity.MessageEntity;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    public void sendMessageToWatson(MessageEntity messageEntity) {
    }

    public Message sendMessageToUser(String body, String number) {
        Twilio.init(System.getenv("ACCOUNT_SID"), System.getenv("AUTH_TOKEN"));
        Message message = Message.creator(new PhoneNumber(number), new PhoneNumber("whatsapp:+14155238886"), body)
                .create();
        logger.info("Sent message on: " + message.getMessagingServiceSid() + "\nmessage SID:" + message.getSid());
        return message;
    }
}