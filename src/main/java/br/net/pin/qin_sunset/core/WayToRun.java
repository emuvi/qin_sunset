package br.net.pin.qin_sunset.core;

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
