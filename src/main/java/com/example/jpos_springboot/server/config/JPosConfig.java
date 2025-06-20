package com.example.jpos_springboot.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "jpos")
@Getter
@Setter
public class JPosConfig {
    private Server server = new Server();

    @Getter
    @Setter
    public static class Server {
        private int port;
        private long timeout;
    }
}
