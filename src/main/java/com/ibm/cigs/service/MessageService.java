package com.ibm.cigs.service;

import com.ibm.cigs.entity.MessageEntity;
import com.ibm.watson.assistant.v2.model.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import java.util.HashMap;
import java.util.List;

@Service
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Value("${twilio.accountId}")
    private String twilioAccountId;
    @Value("${twilio.authToken}")
    private String twilioAuthToken;
    @Value("${twilio.phoneNumber}")
    private String twilioPhoneNumber;

    public Message sendMessageToUser(String body, String number) {
        String toPhoneNumber="whatsapp:" + number;
        String fromPhoneNumber="whatsapp:"+twilioPhoneNumber;

        Twilio.init(twilioAccountId,twilioAuthToken); // replace with Twilio credentials
        Message message = Message.creator( new com.twilio.type.PhoneNumber(toPhoneNumber),   // replace with "To" whatsapp phone number
                new com.twilio.type.PhoneNumber(fromPhoneNumber),body)                       //replace with "From" whatsapp phone number
                .create();
        logger.info("Sent message on: " + message.getMessagingServiceSid() + "\nmessage SID:" + message.getSid());
        return message;
    }
}