package com.vidlus.qin_sunset.hook;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.vidlus.qin_sunset.swap.Execute;
import com.vidlus.qin_sunset.work.OrdersCmd;
import com.vidlus.qin_sunset.work.Runner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesCmd {

    private ServesCmd() {}

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
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesCmd)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow command access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersCmd.list(wayToRun, authed));
            }
        }), "/list/cmd");
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
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesCmd)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow command access");
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
                if (!authed.isAllowedCmd(execute.name)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the command: " + execute.name);
                    return;
                }
                try {
                    var issued = OrdersCmd.run(wayToRun, execute);
                    var issuedToken = authed.newIssued(issued);
                    resp.setContentType("text/plain");
                    resp.getWriter().print(issuedToken);
                } catch (Exception e) {
                    throw new ServletException(e);
                }
            }
        }), "/cmd/run");
    }
}
