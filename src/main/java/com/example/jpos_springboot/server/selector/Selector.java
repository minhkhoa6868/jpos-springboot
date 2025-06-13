package com.example.jpos_springboot.server.selector;

import java.io.Serializable;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jpos_springboot.server.constant.Constants;

public class Selector implements GroupSelector, Configurable {
    
    private static final Logger log = LoggerFactory.getLogger(Selector.class);
    private Configuration configuration;

    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        this.configuration = configuration;
    }

    @Override 
    public String select(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        ISOMsg respMsg = (ISOMsg) ctx.get(Constants.RESPONSE_KEY);
        String selector = "";

        try {
            selector = configuration.get(respMsg.getMTI());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return selector;
    }

    @Override
    public int prepare(long l, Serializable serializable) {
        return PREPARED | NO_JOIN | READONLY;
    }

    @Override 
    public void commit(long l, Serializable serializable) {
        log.info("committing at Select...");
    }

    @Override
    public void abort(long l, Serializable serializable) {
        log.info("aborting at Select...");
    }
}
