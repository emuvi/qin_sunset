package br.com.pointel.qin_sunset.swap;

import br.com.pointel.jarch.data.Data;
import br.com.pointel.jarch.data.DataListArray;

public class PathList implements Data {

    public String path;
    public DataListArray<PathKindName> list;

    public PathList() {
    }

    public PathList(String path, DataListArray<PathKindName> list) {
        this.path = path;
        this.list = list;
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

    public static PathList fromChars(String chars) {
        return Data.fromChars(chars, PathList.class);
    }

}
