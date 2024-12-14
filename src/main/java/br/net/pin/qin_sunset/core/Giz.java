package br.net.pin.qin_sunset.core;

import br.com.pointel.jarch.data.Helped;

public class Giz {

    private final Way way;

    public Giz(Way way) {
        this.way = way;
    }

    public Helped getHelp(String onBase) throws Exception {
        return this.way.stores.getHelp(onBase);
    }
}
