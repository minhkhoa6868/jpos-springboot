package com.example.jpos_springboot.server;

import org.jpos.q2.QBean;

public interface QTestMBean extends QBean {
    public void setTickInterval(long tickInterval);
    public long getTickInterval();
}
