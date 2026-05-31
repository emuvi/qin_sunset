package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.EOrm;
import com.vidlus.jarch.data.ESql;

public class Giz {

    private final WayToRun wayToRun;

    public Giz(WayToRun wayToRun) {
        this.wayToRun = wayToRun;
    }

    public EOrm getEOrm(String onBaseName) throws Exception {
        return this.wayToRun.stores.getEOrm(onBaseName);
    }

    public ESql getESql(String onBaseName) throws Exception {
        return this.wayToRun.stores.getESql(onBaseName);
    }

}
