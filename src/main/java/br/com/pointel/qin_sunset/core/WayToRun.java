package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.Storage;

public class WayToRun {
    public final AirCfg airCfg;
    public final AuthedMap authedMap;
    public final Storage stores;

    public WayToRun(AirCfg airCfg) {
        this.airCfg = airCfg;
        this.authedMap = new AuthedMap();
        this.stores = new Storage();
        if (airCfg.setup.servesBas) {
            this.stores.start(airCfg.bases);
        }
    }

}
