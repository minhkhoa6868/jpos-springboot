package com.example.jpos_springboot.server.activemq;

// import org.jpos.iso.ISOMsg;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MQSender {

    private final JmsTemplate jmsTemplate;

    public void sendMessage(String destination, String resMsg) {
        jmsTemplate.convertAndSend(destination, resMsg);
    }
}
