package com.ibm.cigs.service;

import com.ibm.cigs.entity.MessageEntity;
import com.ibm.watson.assistant.v2.model.*;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;

import java.util.List;

@Service
public class MessageService {

    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    public String sendMessageToWatson(MessageEntity messageEntity) {
        //authenticate into Watson Assistant
        Authenticator authenticator = new IamAuthenticator("snxpyAwm_XHrxrnDsP-QsSiBYwTxtg9eU9iwRBJ5N2b_"); // replace with API key
        Assistant service = new Assistant("2019-02-28", authenticator);
        service.setServiceUrl("https://gateway-syd.watsonplatform.net/assistant/api");
        String assistantId = "90c79a7c-332e-41c0-b2da-3bd16b5ea557"; // replace with assistant ID


        // Create session.
        CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistantId).build();
        SessionResponse session = service.createSession(createSessionOptions).execute().getResult();
        System.out.println("here1");
        String sessionId = session.getSessionId();

        //create text
        MessageInput input = new MessageInput.Builder()
                .text(messageEntity.getBody())
                .build();
        // Start conversation with message from messageEntity.
        MessageOptions messageOptions = new MessageOptions.Builder(assistantId,
                sessionId).input(input).build();
        MessageResponse response = service.message(messageOptions).execute().getResult();

        //print response from Watson to console, assumes single text response
        List<RuntimeResponseGeneric> responseGeneric = response.getOutput().getGeneric();
        if(responseGeneric.size() > 0)
        {
            if(responseGeneric.get(0).responseType().equals("text"))
            {
                System.out.println(responseGeneric.get(0).text());
            }
        }

        //delete session after conversation is over
        DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(assistantId, sessionId).build();
        service.deleteSession(deleteSessionOptions).execute();


        return responseGeneric.get(0).text();
    }

    public Message sendMessageToUser(String body, String number) {
        Twilio.init("AC5776387bdd8362efc8b4bde708f6da0b","09df1fe4758c6ce858c0ea84b82827ab"); // replace with Twilio credentials
        Message message = Message.creator( new com.twilio.type.PhoneNumber("whatsapp:+918830446564"),   // replace with "To" whatsapp phone number
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),body)                                  //replace with "From" whatsapp phone number
                .create();
        //logger.info("Sent message on: " + message.getMessagingServiceSid() + "\nmessage SID:" + message.getSid());
        return message;
    }
}