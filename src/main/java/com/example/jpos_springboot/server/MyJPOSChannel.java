package com.example.jpos_springboot.server;

import java.io.IOException;

import org.jpos.iso.BaseChannel;
import org.jpos.iso.ISOException;

public class MyJPOSChannel extends BaseChannel {
    
    @Override
    protected int getMessageLength() throws IOException, ISOException {
        int len = serverIn.readShort();
        if (len < 2) return 0;
        return len - 2;
    }

    @Override
    protected void sendMessageLength(int len) throws IOException {
        serverOut.writeShort(len + 2);
    }
}
