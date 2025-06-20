package com.example.jpos_springboot.server.participant;

import java.io.Serializable;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;

import com.example.jpos_springboot.server.constant.Constants;
import com.example.jpos_springboot.server.utils.JPosUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SenderResponseParticipant implements TransactionParticipant {

    @Override
    public int prepare(long l, Serializable serializable) {
        Context ctx = (Context) serializable;
        ISOSource source = (ISOSource) ctx.get(Constants.RESOURCE_KEY);
        return source != null && source.isConnected() ? PREPARED : FAIL;
    }

    @Override
    public void commit(long l, Serializable serializable) {
        // log.info("committing at sender response participant...");
        sendMessage((Context) serializable);
    }

    @Override
    public void abort(long l, Serializable serializable) {
        // log.info("aborting at sender response participant...");
    }

    private void sendMessage(Context context) {
        ISOSource source = (ISOSource) context.get(Constants.RESOURCE_KEY);
        ISOMsg respMsg = (ISOMsg) context.get(Constants.RESPONSE_KEY);

        try {
            if (context.getResult().hasInhibit()) {
                log.warn("RESPONSE INHIBITED");
            }

            else if (respMsg == null) {
                log.error("No response!");
            }

            else if (source == null || !source.isConnected()) {
                log.error("ISOSource is null or disconnected - cannot send response!");
            }

            else {
                log.info("Response message: {}", JPosUtil.getISOMessage(respMsg));
                source.send(respMsg);
            }
            
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
