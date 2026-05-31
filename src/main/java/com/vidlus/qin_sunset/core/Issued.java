package com.vidlus.qin_sunset.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.vidlus.jarch.flow.Pace;

public class Issued {

    private final Long createdAt;
    private final List<String> outLines;
    private final ReadWriteLock outLock;
    private final boolean joinErrs;
    private final List<String> errLines;
    private final ReadWriteLock errLock;
    private volatile Integer resultCode;
    private volatile Boolean isDone;
    private volatile Boolean hasOut;
    private volatile Boolean hasErr;
    private volatile Long finishedAt;
    private volatile Pace pace;

    public Issued() {
        this(false);
    }

    public Issued(boolean joinErrs) {
        this.createdAt = System.nanoTime();
        this.outLines = new ArrayList<>();
        this.outLock = new ReentrantReadWriteLock();
        this.joinErrs = joinErrs;
        this.errLines = !joinErrs ? new ArrayList<>() : null;
        this.errLock = !joinErrs ? new ReentrantReadWriteLock() : null;
        this.resultCode = null;
        this.isDone = false;
        this.hasOut = false;
        this.hasErr = false;
        this.finishedAt = null;
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public String getOutLines() {
        try {
            this.outLock.readLock().lock();
            return String.join("\n", this.outLines);
        } finally {
            this.outLock.readLock().unlock();
        }
    }

    public String[] getOutLinesFrom(Integer from) {
        return this.getOutLinesFrom(from, null);
    }

    public String[] getOutLinesFrom(Integer from, Integer until) {
        try {
            this.outLock.readLock().lock();
            if (until == null) {
                until = this.outLines.size();
            }
            var length = Math.min(this.outLines.size(), until) - from;
            var result = new String[length];
            var index = 0;
            for (int i = from; i < length + from; i++) {
                result[index] = this.outLines.get(i);
                index++;
            }
            return result;
        } finally {
            this.outLock.readLock().unlock();
        }
    }

    public int getOutLinesSize() {
        try {
            this.outLock.readLock().lock();
            return this.outLines.size();
        } finally {
            this.outLock.readLock().unlock();
        }
    }

    public void addOutLine(String out) {
        try {
            this.outLock.writeLock().lock();
            this.outLines.add(out);
            this.hasOut = true;
        } finally {
            this.outLock.writeLock().unlock();
        }
    }

    public String getErrLines() {
        if (this.joinErrs) {
            return null;
        }
        try {
            this.errLock.readLock().lock();
            return String.join("\n", this.errLines);
        } finally {
            this.errLock.readLock().unlock();
        }
    }

    public String[] getErrLinesFrom(Integer from) {
        if (this.joinErrs) {
            return null;
        }
        return this.getErrLinesFrom(from, null);
    }

    public String[] getErrLinesFrom(Integer from, Integer until) {
        if (this.joinErrs) {
            return null;
        }
        try {
            this.errLock.readLock().lock();
            if (until == null) {
                until = this.errLines.size();
            }
            var length = Math.min(this.errLines.size(), until) - from;
            var result = new String[length];
            var index = 0;
            for (int i = from; i < length + from; i++) {
                result[index] = this.errLines.get(i);
                index++;
            }
            return result;
        } finally {
            this.errLock.readLock().unlock();
        }
    }

    public int getErrLinesSize() {
        if (this.joinErrs) {
            return 0;
        }
        try {
            this.errLock.readLock().lock();
            return this.errLines.size();
        } finally {
            this.errLock.readLock().unlock();
        }
    }

    public void addErrLine(String err) {
        if (this.joinErrs) {
            addOutLine(err);
            return;
        }
        try {
            this.errLock.writeLock().lock();
            this.errLines.add(err);
            this.hasErr = true;
        } finally {
            this.errLock.writeLock().unlock();
        }
    }

    public Integer getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(Integer code) {
        this.resultCode = code;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void setDone() {
        this.isDone = true;
        this.finishedAt = System.nanoTime();
    }

    public boolean hasOut() {
        return this.hasOut;
    }

    public boolean hasErr() {
        return this.hasErr;
    }

    public Long getFinishedAt() {
        return this.finishedAt;
    }

    public Pace getPace() {
        return pace;
    }

    public void setPace(Pace pace) {
        this.pace = pace;
    }
}
