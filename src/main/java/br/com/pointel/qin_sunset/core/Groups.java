package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.Data;
import br.com.pointel.jarch.data.DataListArray;

public class Groups extends DataListArray<Group> {
    
    @Override
    public void fixDefaults() {
        this.removeIf(group -> group.name.isEmpty());
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

    public static Groups fromChars(String chars) {
        return Data.fromChars(chars, Groups.class);
    }
    
}
