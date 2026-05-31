package com.vidlus.qin_sunset.swap;

import com.vidlus.jarch.data.Data;

public class IssuedQuestion implements Data {

    public String token;
    public Boolean askCreatedAt;
    public Boolean askOutLines;
    public Integer askOutLinesFrom;
    public Integer askOutLinesUntil;
    public Boolean askOutLinesSize;
    public Boolean askErrLines;
    public Integer askErrLinesFrom;
    public Integer askErrLinesUntil;
    public Boolean askErrLinesSize;
    public Boolean askResultCode;
    public Boolean askIsDone;
    public Boolean askHasOut;
    public Boolean askHasErr;
    public Boolean askFinishedAt;


    public IssuedQuestion() {
    }

    public IssuedQuestion(String token, Boolean askCreatedAt, Boolean askOutLines, Integer askOutLinesFrom, Integer askOutLinesUntil, Boolean askOutLinesSize, Boolean askErrLines, Integer askErrLinesFrom, Integer askErrLinesUntil, Boolean askErrLinesSize, Boolean askResultCode, Boolean askIsDone, Boolean askHasOut, Boolean askHasErr, Boolean askFinishedAt) {
        this.token = token;
        this.askCreatedAt = askCreatedAt;
        this.askOutLines = askOutLines;
        this.askOutLinesFrom = askOutLinesFrom;
        this.askOutLinesUntil = askOutLinesUntil;
        this.askOutLinesSize = askOutLinesSize;
        this.askErrLines = askErrLines;
        this.askErrLinesFrom = askErrLinesFrom;
        this.askErrLinesUntil = askErrLinesUntil;
        this.askErrLinesSize = askErrLinesSize;
        this.askResultCode = askResultCode;
        this.askIsDone = askIsDone;
        this.askHasOut = askHasOut;
        this.askHasErr = askHasErr;
        this.askFinishedAt = askFinishedAt;
    }

    @Override
    public IssuedQuestion clone() {
        return (IssuedQuestion) this.deepClone();
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

    public static IssuedQuestion fromChars(String chars) {
        return Data.fromChars(chars, IssuedQuestion.class);
    }

}
