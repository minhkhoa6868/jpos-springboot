package com.example.jpos_springboot.server.config;

import org.jpos.q2.QBeanSupport;
import org.jpos.util.NameRegistrar;
import org.springframework.stereotype.Component;

@Component
public class InitQBean extends QBeanSupport {
    private final JPosConfig config;

    public InitQBean(JPosConfig config) {
        super();
        this.config = config;
    }

    @Override
    protected void initService() throws Exception {
        super.initService();
        NameRegistrar.register("conf." + JPosConfig.class.getSimpleName(), config);
    }

    @Override
    protected void startService() throws Exception {
        super.startService();
    }

    @Override
    protected void stopService() throws Exception {
        NameRegistrar.unregister("conf." + JPosConfig.class.getSimpleName());
        super.stopService();
    }
}
