package com.example.jpos_springboot.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jpos_springboot.server.repository.ChannelRouteRepository;

@Service
public class ChannelRouteService {
    @Autowired
    private ChannelRouteRepository channelRouteRepository;

    // get destination name
    public String getDestinationName(Integer id) {
        String desName = channelRouteRepository.findDestinationName(id);
        if (desName == null) {
            throw new RuntimeException("Destination not found");
        }

        return desName;
    }

    // get queue typ
    public String getQueueType(Integer id) {
        String queueType = channelRouteRepository.findQueueType(id);
        if (queueType == null) {
            throw new RuntimeException("Queue type not found");
        }

        return queueType;
    }
}
