package com.example.jpos_springboot;

import java.io.FileInputStream;

import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.channel.XMLChannel;
import org.jpos.iso.packager.XMLPackager;

public class ISOTestClient {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java ISOTestClient <xml-file>");
            return;
        }

        String filename = args[0];
        // Load ISOImg
        ISOMsg msg = new ISOMsg();
        msg.setPackager(new XMLPackager());
        msg.unpack(new FileInputStream(filename));

        ISOChannel channel = new XMLChannel("localhost", 10000, new XMLPackager());
        channel.connect();

        channel.send(msg);
        ISOMsg respMsg = channel.receive();
        respMsg.dump(System.out, "");

        channel.disconnect();
    }
}
