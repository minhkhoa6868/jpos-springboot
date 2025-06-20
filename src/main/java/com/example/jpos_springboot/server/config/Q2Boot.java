package com.example.jpos_springboot.server.config;

import org.jpos.q2.Q2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Q2Boot {
    private Q2 q2;

    private final InitQBean initQBean;
    private final ApplicationArguments args;

    @PostConstruct
    public void init() {
        q2 = new Q2(args.getSourceArgs());
        initQBean.setServer(q2);
        q2.start();
        initQBean.start();
    }

    @PreDestroy
    public void destroy() {
        if (initQBean != null) {
            initQBean.stop();
        }

        if (q2 != null) {
            q2.shutdown(true);
        }

        q2 = null;
    }
}
