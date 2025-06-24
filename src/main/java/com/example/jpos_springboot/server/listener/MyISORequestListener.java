package com.example.jpos_springboot.server.listener;

import java.util.HashMap;
import java.util.Map;

import org.jpos.core.*;
import org.jpos.iso.*;
import org.jpos.space.LocalSpace;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;

import com.example.jpos_springboot.server.constant.Constants;
import com.example.jpos_springboot.server.utils.JPosUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyISORequestListener implements ISORequestListener, Configurable {

    private String spaceN;
    private Long timeout;
    private String queueN;
    private LocalSpace<String, Context> space;
    private Map<String, String> additionalContextEntries = null;

    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        spaceN = configuration.get("space");
        timeout = configuration.getLong("spaceTimeout");
        queueN = configuration.get("queue");
        space = (LocalSpace<String, Context>) SpaceFactory.getSpace(spaceN);
        Map<String, String> m = new HashMap<>();
        configuration.keySet()
                        .stream()
                        .filter(s -> s.startsWith("ctx."))
                        .forEach(s -> m.put(s.substring(4), configuration.get(s)));

        if (m.size() > 0) additionalContextEntries = m;
    }
    
    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        Context context = new Context();

        try {
            // receive any message
            log.info("Received message: {}", JPosUtil.getISOMessage(isoMsg));
            // byte[] packed = isoMsg.pack();
            // log.info("Hex string: {}", ISOUtil.hexString(packed));

            // ISOMsg unpacked = new ISOMsg();
            // unpacked.setPackager(isoMsg.getPackager());
            // unpacked.unpack(packed);

            // log.info("Received message (after pack): {}", JPosUtil.getISOMessage(unpacked));
            // JPosUtil.compareISOMsg(isoMsg, unpacked);

            try {
                // put request key
                context.put(Constants.REQUEST_KEY, isoMsg);
                // put resource key
                context.put(Constants.RESOURCE_KEY, isoSource);
                // put additional context entries
                if (additionalContextEntries != null) {
                    additionalContextEntries.entrySet().forEach(e -> context.put(e.getKey(), e.getValue()));
                }
                // write new entry to space
                try {
                    space.out(queueN, context, timeout);
                } catch (Exception e) {
                    log.error("Error writing to space", e);
                }

            } catch (Exception e) {
                log.error("Error handling network message", e);
            }

            if (!"0800".equals(isoMsg.getMTI())) {
                return false;
            }

        } catch (Exception e) {
            log.error("Error processing ISO 8583 message", e);
        }

        return true;
    }
}