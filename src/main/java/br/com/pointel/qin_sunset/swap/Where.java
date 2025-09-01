package br.com.pointel.qin_sunset.swap;

import br.com.pointel.jarch.data.Data;

public class Where implements Data {

    public String path;

    public Where(String path) {
        this.path = path;
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

    public static Where fromChars(String chars) {
        return Data.fromChars(chars, Where.class);
    }
    
}
