package br.com.pointel.qin_sunset.hook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import br.com.pointel.jarch.data.Bases;
import br.com.pointel.qin_sunset.core.Setup;
import br.com.pointel.qin_sunset.work.OrdersWay;
import br.com.pointel.qin_sunset.work.Runner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesWay {
    public static void init(ServletContextHandler context) {
        initSetup(context);
        initBases(context);
    }

    private static void initSetup(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                if (!authed.isMaster()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be a master user");
                    return;
                }
                Setup setup;
                var setupFile = wayToRun.airWays.setupFile;
                if (setupFile.exists()) {
                    setup = Setup.fromString(Files.readString(setupFile.toPath()));
                } else {
                    setup = new Setup();
                }
                resp.setContentType("application/json");
                resp.getWriter().print(setup.toString());
            }

            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                var service = Runner.getService(req);
                if (service == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a service");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                if (!authed.isMaster()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be a master user");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var newSetup = Setup.fromString(body);
                if (newSetup == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid setup");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersWay.newSetup(service, wayToRun, newSetup));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }), "/way/setup");
    }

    private static void initBases(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                if (!authed.isMaster()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be a master user");
                    return;
                }
                Bases bases;
                var basesFile = wayToRun.airWays.basesFile;
                if (basesFile.exists()) {
                    bases = Bases.fromString(Files.readString(basesFile.toPath()));
                } else {
                    bases = new Bases();
                }
                resp.setContentType("application/json");
                resp.getWriter().print(bases.toString());
            }

            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                var service = Runner.getService(req);
                if (service == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a service");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                if (!authed.isMaster()) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be a master user");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var newBases = Bases.fromString(body);
                if (newBases == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bases");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersWay.newBases(service, wayToRun, newBases));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }), "/way/bases");
    }
}
