package com.vidlus.qin_sunset.swap;

import com.vidlus.jarch.data.Data;

public class IssuedAnswer implements Data {

    public Long createdAt;
    public String outLines;
    public String[] outLinesFrom;
    public Integer outLinesSize;
    public String errLines;
    public String[] errLinesFrom;
    public Integer errLinesSize;
    public Integer resultCode;
    public Boolean isDone;
    public Boolean hasOut;
    public Boolean hasErr;
    public Long finishedAt;

    public IssuedAnswer() {
    }
    
    public IssuedAnswer(Long createdAt, String outLines, String[] outLinesFrom, Integer outLinesSize, String errLines, String[] errLinesFrom, Integer errLinesSize, Integer resultCode, Boolean isDone, Boolean hasOut, Boolean hasErr, Long finishedAt) {
        this.createdAt = createdAt;
        this.outLines = outLines;
        this.outLinesFrom = outLinesFrom;
        this.outLinesSize = outLinesSize;
        this.errLines = errLines;
        this.errLinesFrom = errLinesFrom;
        this.errLinesSize = errLinesSize;
        this.resultCode = resultCode;
        this.isDone = isDone;
        this.hasOut = hasOut;
        this.hasErr = hasErr;
        this.finishedAt = finishedAt;
    }

    @Override
    public IssuedAnswer clone() {
        return (IssuedAnswer) this.deepClone();
    }

    @Override
    public boolean equals(Object that) {
        return this.deepEquals(that);
    }

    @Override
    public int hashCode() {
        return this.deepHash();
    }

    @Override
    public String toString() {
        return this.toChars();
    }

    public static IssuedAnswer fromChars(String chars) {
        return Data.fromChars(chars, IssuedAnswer.class);
    }

}
