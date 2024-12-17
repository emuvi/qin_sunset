package br.com.pointel.qin_sunset.work;

import br.com.pointel.qin_sunset.core.Authed;
import br.com.pointel.qin_sunset.core.Group;
import br.com.pointel.qin_sunset.core.WayToRun;
import br.com.pointel.qin_sunset.swap.Logged;
import br.com.pointel.qin_sunset.swap.TryAuth;
import jakarta.servlet.http.HttpServletRequest;

public class Runner {
    public static WayToRun getWay(HttpServletRequest req) {
        return (WayToRun) req.getServletContext().getAttribute("QinSunset.Way");
    }

    public static Logged tryEnter(TryAuth tryAuth, WayToRun way, HttpServletRequest req) {
        for (var user : way.airCfg.users) {
            if (user.name.equals(tryAuth.name) && user.pass.equals(tryAuth.pass)) {
                var token = req.getSession().getId();
                Group group = null;
                if (!user.group.isEmpty()) {
                    for (var grouped : way.airCfg.groups) {
                        if (user.group.equals(grouped.name)) {
                            group = grouped;
                            break;
                        }
                    }
                }
                var authed = new Authed(user, group, way);
                way.authedMap.addAuthed(token, authed);
                return new Logged(token, authed.getLang());
            }
        }
        return null;
    }

    public static Authed getAuthed(WayToRun way, HttpServletRequest req) {
        return way.authedMap.getAuthed(Runner.getToken(req));
    }

    public static String getToken(HttpServletRequest req) {
        return req.getSession().getId();
    }
}
