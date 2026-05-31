package com.vidlus.qin_sunset.core;

import java.util.Objects;
import com.google.gson.Gson;
import com.vidlus.jarch.data.Strain;
import com.vidlus.jarch.flow.FixVals;

public class AllowedReg implements FixVals {

    public Boolean allowed;
    public Strain strained;

    public AllowedReg() {}

    public AllowedReg(Boolean allowed, Strain strained) {
        this.allowed = allowed;
        this.strained = strained;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AllowedReg)) {
            return false;
        }
        AllowedReg allowedReg = (AllowedReg) o;
        return Objects.equals(allowed, allowedReg.allowed)
                        && Objects.equals(strained, allowedReg.strained);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowed, strained);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static AllowedReg fromString(String json) {
        return new Gson().fromJson(json, AllowedReg.class);
    }

}
