package com.example.jpos_springboot;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;

public class MyISORequestListener implements ISORequestListener {
    
    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        System.out.println("Received message: " + isoMsg);
        return true;
    }
}
