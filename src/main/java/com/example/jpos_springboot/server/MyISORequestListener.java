package com.example.jpos_springboot.server;

import java.util.UUID;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOServer;
import org.jpos.iso.ISOSource;
import org.jpos.iso.ServerChannel;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.GenericPackager;
// import org.jpos.iso.packager.XMLPackager;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jpos_springboot.server.constant.Constants;

public class MyISORequestListener implements ISORequestListener, Configurable {

    private static final Logger log = LoggerFactory.getLogger(MyISORequestListener.class);
    private Configuration configuration;

    public static void main(String[] args) throws ISOException {
        String hostName = "localhost";
        int portNumber = 10000;
        ISOPackager packager = new GenericPackager("/Users/peace/jpos-springboot/cfg/packager/iso87ascii-binary-bitmap.xml");
        // XMLPackager packager = new XMLPackager();
        ServerChannel channel = new ASCIIChannel(hostName, portNumber, packager);
        ISOServer server = new ISOServer(portNumber, channel, null);
        server.addISORequestListener(new MyISORequestListener());
        log.info("Let's Start !!!!! ISO8583 Service");
        new Thread(server).start();
        log.info("ISO8583 Service Started successfully");
    }

    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        this.configuration = configuration;
    }
    
    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        String spaceN = configuration.get("space");
        Long timeout = configuration.getLong("spaceTimeout");
        String queueN = configuration.get("queue");
        Context context = new Context();
        @SuppressWarnings("unchecked")
        Space<String, Context> space = (Space<String, Context>) SpaceFactory.getSpace(spaceN);

        try {
            // receive any message
            // only show MTI, field 11, field 41
            log.info("Received message: " + isoMsg);
            isoMsg.dump(System.out, "");

            // clone it
            ISOMsg response = (ISOMsg) isoMsg.clone();
            // automatically response
            response.setResponseMTI();
            
            // field 37 for retrieval reference number
            response.set(37, generateRRN());

            // field 38 for authorization code
            response.set(38, generateAuthCode());
            
            String amount = isoMsg.getString(4);
            log.info("amount: " + amount);
            if ("000000009999".equals(amount)) {
                response.set(39, "01"); // do not approve
            }
            else {
                response.set(39, "00"); // approve
            }

            // put request key
            context.put(Constants.REQUEST_KEY, isoMsg);
            // put response key
            context.put(Constants.RESPONSE_KEY, response);
            // put resource key
            context.put(Constants.RESOURCE_KEY, isoSource);

            // send back the response to the client
            // isoSource.send(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // write new entry to space
        space.out(queueN, context, timeout);
        return false;
    }

    private String generateRRN() {
        return String.format("%012d", (int) (Math.random() * 1000000000000L));
    }

    private String generateAuthCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
