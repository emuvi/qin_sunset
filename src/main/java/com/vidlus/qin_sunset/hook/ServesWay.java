package com.vidlus.qin_sunset.hook;

import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.vidlus.qin_sunset.core.Groups;
import com.vidlus.qin_sunset.core.Setup;
import com.vidlus.qin_sunset.core.Users;
import com.vidlus.qin_sunset.work.OrdersWay;
import com.vidlus.qin_sunset.work.Runner;

import com.vidlus.jarch.data.Bases;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesWay {

    private ServesWay() {}

    public static void init(ServletContextHandler context) {
        initSetup(context);
        initBases(context);
        initUsers(context);
        initGroups(context);
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
                    setup = Setup.fromChars(Files.readString(setupFile.toPath()));
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
                var newSetup = Setup.fromChars(body);
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
                    bases = Bases.fromChars(Files.readString(basesFile.toPath()));
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
                var newBases = Bases.fromChars(body);
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

    private static void initUsers(ServletContextHandler context) {
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
                Users users;
                var usersFile = wayToRun.airWays.usersFile;
                if (usersFile.exists()) {
                    users = Users.fromChars(Files.readString(usersFile.toPath()));
                } else {
                    users = new Users();
                }
                resp.setContentType("application/json");
                resp.getWriter().print(users.toString());
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
                var newUsers = Users.fromChars(body);
                if (newUsers == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid users");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersWay.newUsers(service, wayToRun, newUsers));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }), "/way/users");
    }

    private static void initGroups(ServletContextHandler context) {
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
                Groups groups;
                var groupsFile = wayToRun.airWays.groupsFile;
                if (groupsFile.exists()) {
                    groups = Groups.fromChars(Files.readString(groupsFile.toPath()));
                } else {
                    groups = new Groups();
                }
                resp.setContentType("application/json");
                resp.getWriter().print(groups.toString());
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
                var newGroups = Groups.fromChars(body);
                if (newGroups == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid groups");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersWay.newGroups(service, wayToRun, newGroups));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }), "/way/groups");
    }
}
