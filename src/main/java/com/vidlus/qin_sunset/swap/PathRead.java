package com.vidlus.qin_sunset.swap;

import com.google.gson.Gson;

import com.vidlus.jarch.data.Data;

public class PathRead implements Data {

    public String path;
    public Boolean base64;
    public Integer rangeStart;
    public Integer rangeLength;

    public PathRead() {
    }

    public PathRead(String path, Boolean base64, Integer rangeStart, Integer rangeLength) {
        this.path = path;
        this.base64 = base64;
        this.rangeStart = rangeStart;
        this.rangeLength = rangeLength;
    }

    @Override
    public PathRead clone() {
        return (PathRead) this.deepClone();
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

    public static PathRead fromChars(String chars) {
        return Data.fromChars(chars, PathRead.class);
    }
    
}
