package com.vidlus.qin_sunset.work;

import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.WayToRun;

public class OrdersBas {

    private OrdersBas() {}
    
    public static String list(WayToRun wayToRun, Authed forAuthed) {
        var result = new StringBuilder();
        for (var base : wayToRun.airWays.bases) {
            var name = base.getName();
            if (forAuthed.isAllowedBas(name, false)) {
                result.append(name).append("\n");
            }
        }
        return result.toString();
    }

}
