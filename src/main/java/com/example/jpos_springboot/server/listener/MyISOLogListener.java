package com.example.jpos_springboot.server.listener;

import org.jpos.util.LogEvent;
import org.jpos.util.LogListener;
import org.jpos.util.slf4j.JPOSLogger;
import org.slf4j.event.Level;

import lombok.extern.slf4j.Slf4j;
import static org.slf4j.event.Level.*;

@Slf4j
public class MyISOLogListener extends JPOSLogger implements LogListener {

    public MyISOLogListener(String name) {
        super(name);
    }

    public MyISOLogListener() {
        this(MyISOLogListener.class.getName());
    }

//    @Override
    public synchronized LogEvent log(LogEvent evt) {
        Object obj = evt.getPayLoad().getFirst();
        if (!(obj instanceof Throwable)) {
            return evt;
        }

        log.error(">>>", (Throwable) obj);
        Level level = ERROR;
//        try {
//            level = Level.valueOf(tag.toUpperCase()); // only some tags can be mapped directly to a level
//        } catch (IllegalArgumentException ignore) {}
        StringBuilder format = new StringBuilder();
        format.append("(").append(evt.getRealm()).append(") ");
        format.append("{}");

        for (Object o : evt.getPayLoad()) {
            if (obj.equals(o)) {
                continue;
            }
            final String line = o.toString();
            logOutPut(level, format.toString(), line);
        }
        return null;
    }

    // The slf4j API has no higher-level log method
    protected void logOutPut(Level level, String format, Object... args) {
        switch (level) {
            case ERROR:
                log.error(format, args);
                return;
            case WARN:
                log.warn(format, args);
                return;
            case INFO:
                log.info(format, args);
                return;
            case DEBUG:
                log.debug(format, args);
                return;
            case TRACE:
                log.trace(format, args);
                return;
            default:
        }
    }
}
