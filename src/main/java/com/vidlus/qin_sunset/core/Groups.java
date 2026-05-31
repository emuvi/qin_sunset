package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.Data;
import com.vidlus.jarch.data.DataListArray;

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
