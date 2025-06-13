package com.example.jpos_springboot.server.participant;

import java.io.Serializable;

import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jpos_springboot.server.constant.Constants;

public class TransactionResponseParticipant implements TransactionParticipant {

    private static final Logger log = LoggerFactory.getLogger(TransactionResponseParticipant.class);

    // do the same like network participant but the response MTI will choose base on request MTI 200
    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;

        // set response for network check
        ISOMsg respMsg = (ISOMsg) ctx.get(Constants.RESPONSE_KEY);

        try {
            respMsg.set(39, "00");
            ctx.put(Constants.RESPONSE_KEY, respMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return PREPARED;
    }

    @Override
    public void commit(long l, Serializable serializable) {
        log.info("committing at transaction response participant...");
    }

    @Override
    public void abort(long l, Serializable serializable) {
        log.info("aborting at transaction response participant...");
    }
}
