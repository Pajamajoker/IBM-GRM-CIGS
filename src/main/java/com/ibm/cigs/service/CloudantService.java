package com.ibm.cigs.service;

import java.util.List;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.cloudant.client.api.query.QueryBuilder;

import static com.cloudant.client.api.query.Expression.*;
import static com.cloudant.client.api.query.Operation.*;

import com.ibm.cigs.entity.Document;
import com.ibm.cigs.entity.MessageEntity;
import com.twilio.rest.api.v2010.account.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CloudantService {

    @Autowired
    private CloudantClient client;

    @Value("${cloudant.dbname}")
    private String dbname;

    private Logger logger = LoggerFactory.getLogger(CloudantService.class);

    public List<String> getAllDBInstances() {
        List<String> instances = client.getAllDbs();
        logger.info("Following instances are live: " + instances.toString());
        return instances;
    }

   // @Async("threadPoolExecutor")
    public void insertDocument(MessageEntity messageEntity, String sessionId) {
        Database database = client.database(dbname, false);
        Document document = new Document(messageEntity, sessionId);
        Response ID = database.save(document);
        logger.info("Saved message: " + document.toString() + " with ID " + ID);
    }

   // @Async("threadPoolExecutor")
    public void insertDocument(MessageEntity messageEntity, String sessionId, String body) {
        Database database = client.database("practice_db", false);
        Document document = new Document(messageEntity, sessionId, body);
        Response ID = database.save(document);
        logger.info("Saved message: " + document.toString() + " with ID " + ID);
    }

    public List<Document> getAllMessages() {
        Database database = client.database("practice_db", false);
        List<Document> documents = database.query(new QueryBuilder(or()).build(), Document.class).getDocs();
        logger.info("Retrieved Documents: " + documents.toString());
        return documents;
    }

    public List<Document> queryByReceiverNumber(String Number) {
        Database database = client.database("practice_db", false);
        List<Document> documents = database.query(new QueryBuilder(eq("To", Number)).build(), Document.class).getDocs();
        logger.info("Retrieved Documents: " + documents.toString());
        return documents;
    }

    public List<Document> queryByReceiverNumber(String Number, int limit) {
        Database database = client.database("practice_db", false);
        List<Document> documents = database
                .query(new QueryBuilder(eq("To", Number)).limit(limit).build(), Document.class).getDocs();
        logger.info("Retrieved Documents: " + documents.toString());
        return documents;
    }

    public List<Document> queryBySenderNumber(String Number) {
        Database database = client.database("practice_db", false);
        List<Document> documents = database.query(new QueryBuilder(eq("From", Number)).build(), Document.class)
                .getDocs();
        logger.info("Retrieved Documents: " + documents.toString());
        return documents;
    }

    public List<Document> queryBySenderNumber(String Number, int limit) {
        Database database = client.database("practice_db", false);
        List<Document> documents = database
                .query(new QueryBuilder(eq("From", Number)).limit(limit).build(), Document.class).getDocs();
        logger.info("Retrieved Documents: " + documents.toString());
        return documents;
    }

}