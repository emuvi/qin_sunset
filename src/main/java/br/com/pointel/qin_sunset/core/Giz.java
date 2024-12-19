package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.EOrm;
import br.com.pointel.jarch.data.ESql;

public class Giz {

    private final WayToRun way;

    public Giz(WayToRun way) {
        this.way = way;
    }

    public EOrm getEOrm(String onBaseName) throws Exception {
        return this.way.stores.getEOrm(onBaseName);
    }

    public ESql getESql(String onBaseName) throws Exception {
        return this.way.stores.getESql(onBaseName);
    }

}
