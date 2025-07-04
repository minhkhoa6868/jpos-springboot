package com.example.jpos_springboot.server.activemq;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.example.jpos_springboot.server.config.ApplicationContextProvider;
import com.example.jpos_springboot.server.service.ChannelRouteService;
import com.example.jpos_springboot.server.utils.ISOSourceHolder;
import com.example.jpos_springboot.server.utils.JPosUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class CSBOmnicardMPAY {

    @JmsListener(destination = "CSBOmnicardMPAY")
    public void receiveMessage(String response) throws ISOException {
        try {
            ISOMsg resMsg = JPosUtil.jsonToIsoMsg(response);

            log.info("CSBOmnicardMPAY receives message from MQ: {}", JPosUtil.getISOMessage(resMsg));

            // sleep 1s
            try {
                log.info("OmnicardMPAY processing...");
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            ChannelRouteService channelRouteService = ApplicationContextProvider.getBean(ChannelRouteService.class);
            String destionName = channelRouteService.getDestinationName(9161);
            String queueType = channelRouteService.getQueueType(9161);
            log.info("Send to {} through {}", destionName, queueType);

            ISOSource source = ISOSourceHolder.get(resMsg.getString(11));
            ISOSourceHolder.put(resMsg.getString(11), source);
            MQSender sender = ApplicationContextProvider.getBean(MQSender.class);
            log.info("Message to send: {}", JPosUtil.getISOMessage(resMsg));
            sender.sendMessage(destionName, response);
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage());
        }
    }
}
