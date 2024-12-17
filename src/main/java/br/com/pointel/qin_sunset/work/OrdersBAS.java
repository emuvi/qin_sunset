package br.com.pointel.qin_sunset.work;

import br.com.pointel.qin_sunset.core.Authed;
import br.com.pointel.qin_sunset.core.WayToRun;

public class OrdersBAS {
    public static String list(WayToRun way, Authed forAuthed) {
        var result = new StringBuilder();
        for (var base : way.airCfg.bases) {
            var name = base.getName();
            if (forAuthed.allowBAS(name, false)) {
                result.append(name).append("\n");
            }
        }
        return result.toString();
    }
}
