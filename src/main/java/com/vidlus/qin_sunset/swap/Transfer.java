package com.vidlus.qin_sunset.swap;

import com.vidlus.jarch.data.Data;

public class Transfer implements Data {

    public String origin;
    public String destiny;

    public Transfer() {
    }

    public Transfer(String origin, String destiny) {
        this.origin = origin;
        this.destiny = destiny;
    }

    @Override
    public Transfer clone() {
        return (Transfer) this.deepClone();
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

    public static Transfer fromChars(String chars) {
        return Data.fromChars(chars, Transfer.class);
    }
    
}
