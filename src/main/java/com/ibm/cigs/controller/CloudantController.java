package com.ibm.cigs.controller;

import java.util.List;

import com.ibm.cigs.entity.Document;
import com.ibm.cigs.service.CloudantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CloudantController {

    @Autowired
    CloudantService service;

    @GetMapping(path = "/instances")
    public List<String> GetAllRunningInstances() {
        return service.getAllDBInstances();
    }

    @GetMapping(path = "/messages/all")
    public List<Document> getAllMessages() {
        return service.queryByReceiverNumber("whatsapp:+919022149921");
    }

    @GetMapping(value = { "/messages/from/{number}", "/messages/from/{number}/{limit}" })
    public List<Document> getMessagesFrom(@PathVariable("number") String number,
            @PathVariable(name = "limit", required = false) Integer limit) {
        if (limit != null) {
            return service.queryBySenderNumber(number, limit);
        }
        return service.queryBySenderNumber(number);
    }

    @GetMapping(value = { "/messages/to/{number}", "/messages/to/{number}/{limit}" })
    public List<Document> getMessagesTo(@PathVariable(name = "number") String number,
            @PathVariable(name = "limit", required = false) Integer limit) {
        if (limit != null) {
            return service.queryByReceiverNumber(number, limit);
        }
        return service.queryByReceiverNumber(number);
    }

}