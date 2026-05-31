package com.vidlus.qin_sunset.hook;

import java.io.IOException;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.vidlus.qin_sunset.work.OrdersBas;
import com.vidlus.qin_sunset.work.Runner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesBas {

    private ServesBas() {}

    public static void init(ServletContextHandler context) {
        initList(context);
    }

    private static void initList(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersBas.list(wayToRun, authed));
            }
        }), "/list/base");
    }
}
