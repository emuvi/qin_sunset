package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.Helped;

public class Giz {

    private final WayToRun way;

    public Giz(WayToRun way) {
        this.way = way;
    }

    public Helped getHelp(String onBase) throws Exception {
        return this.way.stores.getHelp(onBase);
    }
}
