package com.vidlus.qin_sunset.hook;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.vidlus.qin_sunset.swap.Execute;
import com.vidlus.qin_sunset.work.OrdersGiz;
import com.vidlus.qin_sunset.work.Runner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesGiz {

    private ServesGiz() {}

    public static void init(ServletContextHandler context) {
        initList(context);
        initRun(context);
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
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesGiz)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow script access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersGiz.list(wayToRun, authed));
            }
        }), "/list/giz");
    }

    private static void initRun(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesGiz)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow script access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var execute = Execute.fromChars(body);
                if (execute.name == null || execute.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a executable");
                    return;
                }
                if (!authed.isAllowedGiz(execute.name)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the command: " + execute.name);
                    return;
                }
                var gizDir = new File(wayToRun.airWays.setup.serverFolder, "giz");
                execute.name = new File(gizDir, execute.name).getAbsolutePath();
                try {
                    var issued = OrdersGiz.run(authed, execute);
                    var issuedToken = authed.newIssued(issued);
                    resp.setContentType("text/plain");
                    resp.getWriter().print(issuedToken);
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
        }), "/giz/run");
    }

}
