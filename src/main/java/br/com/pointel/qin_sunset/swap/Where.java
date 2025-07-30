package br.com.pointel.qin_sunset.swap;

import com.google.gson.Gson;

public class Where {
    public String path;

    public Where(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Where fromString(String json) {
        return new Gson().fromJson(json, Where.class);
    }
}
