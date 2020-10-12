package com.ibm.cigs.interfaces;

import com.ibm.cigs.entity.MessageEntity;

public interface AssistantInterface {
    /**
     *
     * @param messageEntity represents incoming message from twilio web hook, see entity/MessageEntity.java
     * @return string denoting the session ID of the ongoing conversation
     * @throws Exception may throw an exception, which should be allowed to propagate across the call stack, handled by global handler
     */
    String getSession(MessageEntity messageEntity) throws Exception;

    /**
     *
     * @param messageEntity represents incoming message from twilio web hook
     * @return String denoting the response from respective chat assistant
     * @throws Exception may throw an exception, which should be allowed to propagate across the call stack, handled by global handler
     */
    String[] processMessage(MessageEntity messageEntity) throws Exception;

    /**
     *
     * @param messageEntity represents incoming message from twilio web hook, see entity/MessageEntity.java
     * @return Boolean value representing if the session is still alive or not
     * @throws Exception may throw an exception, which should be allowed to propagate across the call stack, handled by global handler
     */
    Boolean isConversationOver(MessageEntity messageEntity) throws Exception;
}
