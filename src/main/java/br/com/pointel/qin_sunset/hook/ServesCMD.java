package br.com.pointel.qin_sunset.hook;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.com.pointel.qin_sunset.swap.Execute;
import br.com.pointel.qin_sunset.work.OrdersCMD;
import br.com.pointel.qin_sunset.work.Runner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesCMD {
    public static void init(ServletContextHandler context) {
        initList(context);
        initRun(context);
    }

    private static void initList(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWayToRun(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersCMD.list(way, authed));
            }
        }), "/list/cmd");
    }

    private static void initRun(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWayToRun(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var execute = Execute.fromString(body);
                if (execute.exec == null || execute.exec.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a executable");
                    return;
                }
                if (!authed.isAllowedCmd(execute.exec)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to the command: "
                                                    + execute.exec);
                    return;
                }
                try {
                    var issued = OrdersCMD.run(execute);
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
