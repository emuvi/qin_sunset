package com.vidlus.qin_sunset.work;

import java.util.Objects;

import com.vidlus.qin_sunset.Service;
import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.Group;
import com.vidlus.qin_sunset.core.WayToRun;
import com.vidlus.qin_sunset.swap.Logged;
import com.vidlus.qin_sunset.swap.TryAuth;

import com.vidlus.jarch.mage.WizString;
import jakarta.servlet.http.HttpServletRequest;

public class Runner {

    public static Service getService(HttpServletRequest req) {
        return (Service) req.getServletContext().getAttribute(Service.KEY_QIN_SUNSET_SERVICE);
    }

    public static WayToRun getWayToRun(HttpServletRequest req) {
        return (WayToRun) req.getServletContext().getAttribute(Service.KEY_QIN_SUNSET_WAY_TO_RUN);
    }

    public static Logged tryEnter(TryAuth tryAuth, WayToRun wayToRun, HttpServletRequest req) {
        for (var user : wayToRun.airWays.users) {
            if (Objects.equals(user.name, tryAuth.name)
                            && Objects.equals(user.pass, tryAuth.pass)) {
                var token = req.getSession().getId();
                Group group = null;
                if (WizString.isNotEmpty(user.group)) {
                    for (var airCfgGroup : wayToRun.airWays.groups) {
                        if (Objects.equals(user.group, airCfgGroup.name)) {
                            group = airCfgGroup;
                            break;
                        }
                    }
                }
                var authed = new Authed(user, group, wayToRun);
                wayToRun.authedMap.addAuthed(token, authed);
                return new Logged(token, authed.getLang());
            }
        }
        return null;
    }

    public static Authed getAuthed(WayToRun wayToRun, HttpServletRequest req) {
        return wayToRun.authedMap.getAuthed(Runner.getToken(req));
    }

    public static String getToken(HttpServletRequest req) {
        return req.getSession().getId();
    }
}
