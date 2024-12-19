package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.EOrm;
import br.com.pointel.jarch.data.ESql;

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
