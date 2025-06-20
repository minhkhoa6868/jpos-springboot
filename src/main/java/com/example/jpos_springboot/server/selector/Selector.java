package com.example.jpos_springboot.server.selector;

import java.io.Serializable;

import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;
import org.springframework.util.StringUtils;

import com.example.jpos_springboot.server.constant.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Selector implements GroupSelector, Configurable {
    
    private Configuration configuration;
    private String defaultGroup;

    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        this.configuration = configuration;
        defaultGroup = configuration.get("default", "");
    }

    @Override 
    public String select(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        ISOMsg respMsg = (ISOMsg) ctx.get(Constants.REQUEST_KEY);
        String selector = "";

        try {
            selector = configuration.get(respMsg.getMTI());
        } catch (Exception e) {
            log.error("Error: ", e);
        }

        return StringUtils.hasText(selector) ? selector : defaultGroup;
    }

    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        ISOSource source = (ISOSource) ctx.get(Constants.RESOURCE_KEY);

        if (source == null || !source.isConnected()) {
            return ABORTED;
        }

        return PREPARED | NO_JOIN | READONLY;
    }
}
