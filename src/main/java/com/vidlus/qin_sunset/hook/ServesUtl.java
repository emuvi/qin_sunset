package com.vidlus.qin_sunset.hook;

import java.io.IOException;
import java.net.URLDecoder;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.vidlus.qin_sunset.core.Setup;
import com.vidlus.qin_sunset.swap.IssuedQuestion;
import com.vidlus.qin_sunset.swap.TryAuth;
import com.vidlus.qin_sunset.work.OrdersUtl;
import com.vidlus.qin_sunset.work.Runner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesUtl {

    private ServesUtl() {}

    public static void init(ServletContextHandler context, Setup setup) {
        initPing(context);
        initLang(context);
        initEnter(context);
        initLogged(context);
        initConfig(context);
        initIssued(context);
        initRedirects(context, setup);
    }

    private static void initPing(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                resp.setContentType("text/plain");
                resp.getWriter().print("pong");
            }
        }), "/ping");
    }

    private static void initLang(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(wayToRun.airWays.setup.serverLang);
            }
        }), "/lang");
    }

    private static void initEnter(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var tryAuth = TryAuth.fromChars(body);
                var logged = Runner.tryEnter(tryAuth, wayToRun, req);
                if (logged == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "The user and/or pass is incorrect.");
                    return;
                }
                resp.setContentType("application/json");
                resp.getWriter().print(logged.toString());
            }
        }), "/enter");
    }

    private static void initLogged(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                resp.setContentType("text/plain");
                if (authed != null) {
                    resp.getWriter().print(authed.getUserName());
                } else {
                    resp.getWriter().print("<--NO_USER_LOGGED-->");
                }
            }
        }), "/logged");
    }

    private static void initConfig(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var name = req.getPathInfo().substring(1);
                if (name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a parameter name");
                    return;
                }
                name = URLDecoder.decode(name, "UTF-8");
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersUtl.askConfig(wayToRun, authed, name));
            }
        }), "/config/*");
    }

    private static void initIssued(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                var body = IOUtils.toString(req.getReader());
                var question = IssuedQuestion.fromChars(body);
                if (question.token == null || question.token.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide the issued token");
                    return;
                }
                var issued = authed.getIssued(question.token);
                if (issued == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Couldn't found a issued with the token");
                    return;
                }
                var results = OrdersUtl.askIssued(issued, question);
                resp.setContentType("application/json");
                resp.getWriter().print(results.toString());
            }
        }), "/issued");
    }

    private static void initRedirects(ServletContextHandler context, Setup setup) {
        if (setup.redirectMap == null || setup.redirectMap.isEmpty()) {
            return;
        }
        for (var entry : setup.redirectMap.entrySet()) {
            context.addServlet(new ServletHolder(new HttpServlet() {
                @Override
                protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                    resp.sendRedirect(entry.getValue());
                }
            }), entry.getKey());
        }
    }
}
