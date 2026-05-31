package com.vidlus.qin_sunset.hook;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.vidlus.qin_sunset.work.OrdersApp;
import com.vidlus.qin_sunset.work.Runner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesApp {

    private ServesApp() {}

    public static void init(ServletContextHandler context) {
        initList(context);
        initGet(context);
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
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesApp)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow application access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersApp.list(wayToRun, authed));
            }
        }), "/list/app");
    }

    private static void initGet(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesApp)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow application access");
                    return;
                }
                var reqURL = req.getPathInfo().substring(1);
                if (reqURL == null || reqURL.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path name");
                    return;
                }
                reqURL = URLDecoder.decode(reqURL, "UTF-8");
                var reqFile = new File(wayToRun.airWays.setup.serverFolder, "app/" + reqURL);
                if (!reqFile.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no file at: " + reqFile);
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var appName = reqURL;
                var idxSlash = appName.indexOf('/');
                if (idxSlash != -1) {
                    appName = appName.substring(0, idxSlash);
                }
                if (!authed.isAllowedApp(appName)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the application: " + appName);
                    return;
                }
                OrdersApp.send(reqFile, resp);
            }
        }), "/app/*");
    }
}
