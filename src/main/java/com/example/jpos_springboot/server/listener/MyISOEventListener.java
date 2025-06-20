package com.example.jpos_springboot.server.listener;

import java.util.EventObject;

import org.jpos.iso.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyISOEventListener implements ISOServerEventListener {
    
    private static ISOChannel currentConnection;

    @Override
    public void handleISOServerEvent(EventObject eventObject) {
        if (eventObject instanceof ISOServerAcceptEvent event) {
            ISOChannel channel = event.getISOChannel();

            if (currentConnection != null && currentConnection != channel && currentConnection.isConnected()) {
                try {
                    currentConnection.disconnect();
                } catch (Exception e) {
                    log.error("Error: ", e);
                }
                currentConnection = channel;
                log.info("{} Connected", channel.getName());
            }
        }

        else if (eventObject instanceof ISOServerClientDisconnectEvent event) {
            log.info("{} Disconnected", event.getISOChannel().getName());
        }
    }
}
