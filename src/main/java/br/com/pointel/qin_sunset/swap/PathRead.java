package br.com.pointel.qin_sunset.swap;

import com.google.gson.Gson;

public class PathRead {
    public String path;
    public Boolean base64;
    public Integer rangeStart;
    public Integer rangeLength;

    public PathRead(String path, Boolean base64, Integer rangeStart, Integer rangeLength) {
        this.path = path;
        this.base64 = base64;
        this.rangeStart = rangeStart;
        this.rangeLength = rangeLength;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PathRead fromString(String json) {
        return new Gson().fromJson(json, PathRead.class);
    }
}
