package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.Bases;
import br.com.pointel.jarch.data.Storage;

public class WayToRun {

    public final AirWays airWays;
    public final AuthedMap authedMap;
    public final Storage stores;

    public WayToRun(AirWays airWays) {
        this.airWays = airWays;
        this.authedMap = new AuthedMap();
        this.stores = Boolean.TRUE.equals(airWays.setup.servesBas)
            ? new Storage(airWays.bases)
            : new Storage(new Bases());
    }

    public WayToRun(AirWays airWays, AuthedMap authedMap, Storage stores) {
        this.airWays = airWays;
        this.authedMap = authedMap;
        this.stores = stores;
    }

}
