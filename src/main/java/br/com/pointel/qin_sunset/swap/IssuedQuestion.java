package br.com.pointel.qin_sunset.swap;

import com.google.gson.Gson;

public class IssuedQuestion {
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static IssuedQuestion fromString(String json) {
        return new Gson().fromJson(json, IssuedQuestion.class);
    }
}
