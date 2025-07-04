package com.example.jpos_springboot.server.utils;

import java.util.Map;

import org.jpos.iso.ISOSource;
import java.util.concurrent.ConcurrentHashMap;

public class ISOSourceHolder {
    private static final Map<String, ISOSource> sourceMap = new ConcurrentHashMap<>();

    public static void put(String key, ISOSource source) {
        sourceMap.put(key, source);
    }

    public static ISOSource get(String key) {
        return sourceMap.get(key);
    }

    public static void remove(String key) {
        sourceMap.remove(key);
    }
}
