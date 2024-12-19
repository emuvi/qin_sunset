package br.com.pointel.qin_sunset.work;

import br.com.pointel.qin_sunset.core.Authed;
import br.com.pointel.qin_sunset.core.WayToRun;

public class OrdersBas {
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
