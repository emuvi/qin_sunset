package br.com.pointel.qin_sunset.swap;

import com.google.gson.Gson;

public class PathWrite {
    public String path;
    public Boolean base64;
    public String data;
    public Integer rangeStart;

    public PathWrite(String path, Boolean base64, String data, Integer rangeStart) {
        this.path = path;
        this.base64 = base64;
        this.data = data;
        this.rangeStart = rangeStart;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PathWrite fromString(String json) {
        return new Gson().fromJson(json, PathWrite.class);
    }
}
