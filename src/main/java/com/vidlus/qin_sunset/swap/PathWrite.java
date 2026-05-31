package com.vidlus.qin_sunset.swap;

import com.google.gson.Gson;

import com.vidlus.jarch.data.Data;

public class PathWrite implements Data {

    public String path;
    public Boolean base64;
    public String data;
    public Integer rangeStart;

    public PathWrite() {
    }

    public PathWrite(String path, Boolean base64, String data, Integer rangeStart) {
        this.path = path;
        this.base64 = base64;
        this.data = data;
        this.rangeStart = rangeStart;
    }

    @Override
    public PathWrite clone() {
        return (PathWrite) this.deepClone();
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

    public static PathWrite fromChars(String chars) {
        return Data.fromChars(chars, PathWrite.class);
    }
    
}
