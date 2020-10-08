package com.ibm.cigs.service;

import com.ibm.cigs.entity.MessageEntity;
import com.ibm.cigs.interfaces.AssistantInterface;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.assistant.v2.Assistant;
import com.ibm.watson.assistant.v2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

@Service
public class WatsonService implements AssistantInterface {
    private Logger logger = LoggerFactory.getLogger(WatsonService.class);
    private HashMap<String, SessionResponse> sessionMap = new HashMap<>();


    @Value("${watson.apikey}")
    private String apikey;
    @Value("${watson.serviceUrl}")
    private String serviceUrl;
    @Value("${assistant.assistantId}")
    private String assistantId;
    @Value("${assistant.versionDate}")
    private String versionDate;

    private String sessionId = null;
    Authenticator authenticator;
    Assistant assistant;

    @PostConstruct
    public void init() {
        this.authenticator = new IamAuthenticator(apikey);
        this.assistant = new Assistant(versionDate, authenticator);
        this.assistant.setServiceUrl(serviceUrl);
    }

    @Override
    public String getSession(MessageEntity messageEntity) throws Exception {
        String phoneNumber = messageEntity.getFrom();

        if (sessionMap.containsKey(phoneNumber)) {
            SessionResponse session = sessionMap.get(phoneNumber);
            return session.getSessionId();
        } else {
            CreateSessionOptions createSessionOptions = new CreateSessionOptions.Builder(assistantId).build();
            SessionResponse session = assistant.createSession(createSessionOptions).execute().getResult();
            sessionId = session.getSessionId();
            System.out.println(phoneNumber);
            sessionMap.put(phoneNumber, session);
        }
        return null;
    }


    @Override
    public String[] processMessage(MessageEntity messageEntity) throws Exception {
        String sessionId = getSession(messageEntity);
        Boolean isOver = isConversationOver(messageEntity);

        String defaultResponse = "We're sorry, we do not know how to response to: \"" + messageEntity.getBody() + "\"\nWhat else can we help you with?";
        if (!isOver) {
            MessageInputOptions inputOptions = new MessageInputOptions.Builder()
                    .returnContext(true)
                    .build();
            MessageInput input = new MessageInput.Builder()
                    .text(messageEntity.getBody())
                    .options(inputOptions)
                    .build();
            // Start conversation with message from messageEntity.
            MessageOptions messageOptions = new MessageOptions.Builder(assistantId,
                    sessionId).input(input).build();
            //Get response from Watson Assistant
            MessageResponse response = assistant.message(messageOptions).execute().getResult();
            //print response from Watson to console, assumes single text response
            List<RuntimeResponseGeneric> responseGeneric = response.getOutput().getGeneric();
            
            if(responseGeneric.size() > 0) {
                return new String[]{sessionId, responseGeneric.get(0).text()};
            }else{
                return new String[]{sessionId, defaultResponse};
            }
        } else {
            return new String[]{"", "Thank you for your time!"};
        }
    }

    @Override
    public Boolean isConversationOver(MessageEntity messageEntity) throws Exception {
        String phoneNumber = messageEntity.getFrom();
        String messageBody = messageEntity.getBody();
        if (messageBody.equals("done") || messageBody.equals("Done")) {
            DeleteSessionOptions deleteSessionOptions = new DeleteSessionOptions.Builder(assistantId, sessionId).build();
            assistant.deleteSession(deleteSessionOptions).execute();
            sessionMap.remove(phoneNumber);
            return true;
        }
        return false;
    }
}
