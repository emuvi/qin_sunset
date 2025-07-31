package br.com.pointel.qin_sunset.swap;

import java.util.List;
import com.google.gson.Gson;

public class PathList {
    public String path;
    public List<PathKindName> list;

    public PathList(String path, List<PathKindName> list) {
        this.path = path;
        this.list = list;
    }
    
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PathList fromString(String json) {
        return new Gson().fromJson(json, PathList.class);
    }
}
