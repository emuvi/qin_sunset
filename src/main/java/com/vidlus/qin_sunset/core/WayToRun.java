package com.vidlus.qin_sunset.core;

import com.vidlus.jarch.data.Bases;
import com.vidlus.jarch.data.Storage;

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

    public WayToRun(AirWays airWays, AuthedMap authedMap) {
        this.airWays = airWays;
        this.authedMap = authedMap;
        this.stores = Boolean.TRUE.equals(airWays.setup.servesBas)
            ? new Storage(airWays.bases)
            : new Storage(new Bases());
    }

    public WayToRun(AirWays airWays, Storage stores) {
        this.airWays = airWays;
        this.authedMap = new AuthedMap();
        this.stores = stores;
    }

    public WayToRun(AirWays airWays, AuthedMap authedMap, Storage stores) {
        this.airWays = airWays;
        this.authedMap = authedMap;
        this.stores = stores;
    }

}
