package com.example.jpos_springboot.server.participant;

import java.io.Serializable;

import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import com.example.jpos_springboot.server.constant.Constants;

public class TransactionResponseParticipant implements TransactionParticipant {

    // do the same like network participant but the response MTI will choose base on request MTI 200
    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        ISOMsg reqMsg = (ISOMsg) ctx.get(Constants.REQUEST_KEY);
        ISOMsg respMsg = (ISOMsg) reqMsg.clone();
        // set outgoing and incoming response message
        respMsg.setDirection(2);

        try {
            respMsg.setResponseMTI();
            respMsg.set(39, "00");
            ctx.put(Constants.RESPONSE_KEY, respMsg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return PREPARED | NO_JOIN;
    }
}
