package com.vidlus.qin_sunset.swap;

import com.vidlus.jarch.data.Data;

public class PathKindName implements Data {

    public PathKind kind;
    public String name;

    public PathKindName() {
    }

    public PathKindName(PathKind kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    @Override
    public PathKindName clone() {
        return (PathKindName) this.deepClone();
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

    public static PathKindName fromChars(String chars) {
        return Data.fromChars(chars, PathKindName.class);
    }

}
