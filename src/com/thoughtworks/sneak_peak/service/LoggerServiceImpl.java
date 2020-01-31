package com.thoughtworks.sneak_peak.service;

/**
 * @author thimmv
 */
public class LoggerServiceImpl implements LoggerService {

    public void log(String message) {
        System.out.println(message);
    }
}
