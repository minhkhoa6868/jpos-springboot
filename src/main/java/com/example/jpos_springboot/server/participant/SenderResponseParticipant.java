package com.example.jpos_springboot.server.participant;

import java.io.IOException;
import java.io.Serializable;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jpos_springboot.server.constant.Constants;

public class SenderResponseParticipant implements TransactionParticipant {

    private static final Logger log = LoggerFactory.getLogger(SenderResponseParticipant.class);

    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        ISOMsg respMsg = (ISOMsg) ctx.get(Constants.RESPONSE_KEY);
        String bit39 = respMsg.getString(39);

        if (bit39 == null) {
            try {
                // set to unknown error
                respMsg.set(39, "06");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ctx.put(Constants.RESPONSE_KEY, respMsg);
        return PREPARED;
    }

    @Override
    public void commit(long l, Serializable serializable) {
        log.info("committing at sender response participant...");
        sendMessage((Context) serializable);
    }

    @Override
    public void abort(long l, Serializable serializable) {
        log.info("aborting at sender response participant...");
        sendMessage((Context) serializable);
    }

    private void sendMessage(Context context) {
        ISOSource source = (ISOSource) context.get(Constants.RESOURCE_KEY);
        ISOMsg respMsg = (ISOMsg) context.get(Constants.RESPONSE_KEY);

        try {
            source.send(respMsg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
}
