package br.com.pointel.qin_sunset.swap;

import com.google.gson.Gson;

public class PathKindName {
    public PathKind kind;
    public String name;

    public PathKindName(PathKind kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PathKindName fromString(String json) {
        return new Gson().fromJson(json, PathKindName.class);
    }
}
