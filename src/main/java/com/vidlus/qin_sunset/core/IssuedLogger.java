package com.vidlus.qin_sunset.core;

import java.util.Date;
import org.slf4j.helpers.MarkerIgnoringBase;

public class IssuedLogger extends MarkerIgnoringBase {

    private static final int LEVEL_ERROR = 0;
    private static final int LEVEL_WARN = 1;
    private static final int LEVEL_INFO = 2;
    private static final int LEVEL_DEBUG = 3;
    private static final int LEVEL_TRACE = 4;

    private final Issued issued;
    private volatile Integer level;

    public IssuedLogger(Issued issued) {
        this(issued, LEVEL_INFO);
    }

    public IssuedLogger(Issued issued, Integer level) {
        this.issued = issued;
        this.level = level != null ? level : LEVEL_INFO;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    private boolean isLevelEnabled(int level) {
        return this.level != null ? this.level >= level : false;
    }

    private String log(String level, String msg) {
        return String.format("<%s> %d [%s] - %s", 
                Thread.currentThread().getName(), new Date().getTime(), level, msg);
    }

    private String log(String level, Throwable t) {
        return String.format("<%s> %d [%s] - %s", 
                Thread.currentThread().getName(), new Date().getTime(), level, t.getMessage());
    }

    @Override
    public boolean isTraceEnabled() {
        return isLevelEnabled(LEVEL_TRACE);
    }

    @Override
    public void trace(String msg) {
        if (isTraceEnabled()) {
            issued.addOutLine(log("TRACE", msg));
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (isTraceEnabled()) {
            issued.addOutLine(log("TRACE", String.format(format, arg)));
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (isTraceEnabled()) {
            issued.addOutLine(log("TRACE", String.format(format, arg1, arg2)));
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (isTraceEnabled()) {
            issued.addOutLine(log("TRACE", String.format(format, arguments)));
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (isTraceEnabled()) {
            issued.addOutLine(log("TRACE", t));
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return isLevelEnabled(LEVEL_DEBUG);
    }

    @Override
    public void debug(String msg) {
        if (isDebugEnabled()) {
            issued.addOutLine(log("DEBUG", msg));
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (isDebugEnabled()) {
            issued.addOutLine(log("DEBUG", String.format(format, arg)));
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (isDebugEnabled()) {
            issued.addOutLine(log("DEBUG", String.format(format, arg1, arg2)));
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (isDebugEnabled()) {
            issued.addOutLine(log("DEBUG", String.format(format, arguments)));
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (isDebugEnabled()) {
            issued.addOutLine(log("DEBUG", t));
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return isLevelEnabled(LEVEL_INFO);
    }

    @Override
    public void info(String msg) {
        if (isInfoEnabled()) {
            issued.addOutLine(log("INFO", msg));
        }
    }

    @Override
    public void info(String format, Object arg) {
        if (isInfoEnabled()) {
            issued.addOutLine(log("INFO", String.format(format, arg)));
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (isInfoEnabled()) {
            issued.addOutLine(log("INFO", String.format(format, arg1, arg2)));
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        if (isInfoEnabled()) {
            issued.addOutLine(log("INFO", String.format(format, arguments)));
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        if (isInfoEnabled()) {
            issued.addOutLine(log("INFO", t));
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return isLevelEnabled(LEVEL_WARN);
    }

    @Override
    public void warn(String msg) {
        if (isWarnEnabled()) {
            issued.addOutLine(log("WARN", msg));
        }
    }

    @Override
    public void warn(String format, Object arg) {
        if (isWarnEnabled()) {
            issued.addOutLine(log("WARN", String.format(format, arg)));
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (isWarnEnabled()) {
            issued.addOutLine(log("WARN", String.format(format, arguments)));
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (isWarnEnabled()) {
            issued.addOutLine(log("WARN", String.format(format, arg1, arg2)));
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (isWarnEnabled()) {
            issued.addOutLine(log("WARN", t));
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return isLevelEnabled(LEVEL_ERROR);
    }

    @Override
    public void error(String msg) {
        if (isErrorEnabled()) {
            issued.addErrLine(log("ERROR", msg));
        }
    }

    @Override
    public void error(String format, Object arg) {
        if (isErrorEnabled()) {
            issued.addErrLine(log("ERROR", String.format(format, arg)));
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (isErrorEnabled()) {
            issued.addErrLine(log("ERROR", String.format(format, arg1, arg2)));
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        if (isErrorEnabled()) {
            issued.addErrLine(log("ERROR", String.format(format, arguments)));
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        if (isErrorEnabled()) {
            issued.addErrLine(log("ERROR", t));
        }
    }

}
