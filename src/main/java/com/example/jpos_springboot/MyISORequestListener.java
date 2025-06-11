package com.example.jpos_springboot;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyISORequestListener implements ISORequestListener {

    private static final Logger log = LoggerFactory.getLogger(MyISORequestListener.class);
    
    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        try {
            // receive any message
            // only show MTI, field 11, field 41
            log.info("Received message: " + isoMsg);
            isoMsg.dump(System.out, "");

            // clone it
            ISOMsg response = (ISOMsg) isoMsg.clone();
            // automatically response
            response.setResponseMTI();
            
            String amount = isoMsg.getString(4);
            log.info("amount: " + amount);
            if ("000000009999".equals(amount)) {
                response.set(39, "01"); // do not approve
            }
            else {
                response.set(39, "00"); // approve
            }

            // send back the response to the client
            isoSource.send(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
