package br.com.pointel.qin_sunset.swap;

import java.util.ArrayList;
import com.google.gson.Gson;

public class PathKindList extends ArrayList<PathKindName> {
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PathKindList fromString(String json) {
        return new Gson().fromJson(json, PathKindList.class);
    }
}
