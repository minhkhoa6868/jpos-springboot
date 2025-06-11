package com.example.jpos_springboot;

import org.jpos.iso.ISOUtil;
import org.jpos.q2.Q2;
import org.jpos.q2.QBean;
import org.jpos.util.Log;

public class QTest implements QTestMBean, Runnable {
    private long tickInterval = 1000;
    volatile int state;
    private Log log;

    public QTest() {
        super();
        state = -1;
        log = Log.getLog(Q2.LOGGER_NAME, "qtest");
        log.info("constructor");
    }

    public void init() {
        log.info("initService");
        state = STARTING;
    }

    public void start() {
        log.info("startService");
        state = STARTED;
        new Thread(this).start();
    }

    public void stop() {
        log.info("stopService");
        state = STOPPING;
    }

    public void destroy() {
        log.info("destroyService");
        state = DESTROYED;
    }

    public void setTickInterval (long tickInterval) {
        this.tickInterval = tickInterval;
    }

    public long getTickInterval () {
        return tickInterval;
    }

    public void run () {
        for (int tickCount = 0; running(); tickCount++) {
            log.info("tick" + tickCount);
            ISOUtil.sleep(tickInterval);
        }
    }

    public int getState() {
        return state;
    }

    public String getStateAsString () {
        return state >= 0 ? stateString[state] : "Unknown";
    }

    public boolean running () {
        return state == QBean.STARTING || state == QBean.STARTED;
    }
}
