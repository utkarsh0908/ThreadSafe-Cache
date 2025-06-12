package com.cache;

// Simple logger class to print logs in dev
public class Logger {
    private final boolean isLoggerOn;

    Logger(boolean isLoggerOn) {
        this.isLoggerOn = isLoggerOn;
    }

    public void warn(String msg) {
        if (isLoggerOn) {
            System.err.println("[WARN]: " + msg);
        }
    }

    public void error(String msg) {
        if (isLoggerOn) {
            System.err.println("[Error]: " + msg);
        }
    }

    public void debug(String msg) {
        if (isLoggerOn) {
            System.out.println("[DEBUG]: " + msg);
        }
    }
}
