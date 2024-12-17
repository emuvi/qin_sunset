package br.com.pointel.qin_sunset.hook;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.com.pointel.qin_sunset.work.OrdersAPP;
import br.com.pointel.qin_sunset.work.Runner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesAPP {
    public static void init(ServletContextHandler context) {
        initGet(context);
        initList(context);
    }

    private static void initGet(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var reqURL = req.getPathInfo().substring(1);
                if (reqURL == null || reqURL.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a path name");
                    return;
                }
                reqURL = URLDecoder.decode(reqURL, "UTF-8");
                var way = Runner.getWay(req);
                var reqFile = new File(way.airCfg.setup.serverFolder, "app/" + reqURL);
                if (!reqFile.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND,
                                    "There is no file at: "
                                                    + reqFile);
                    return;
                }
                if (reqURL.startsWith("qinpel-app/")) {
                    OrdersAPP.send(reqFile, resp);
                    return;
                }
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                var appName = reqURL;
                var idxSlash = appName.indexOf('/');
                if (idxSlash != -1) {
                    appName = appName.substring(0, idxSlash);
                }
                if (!authed.allowAPP(appName)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to the application: "
                                                    + appName);
                    return;
                }
                OrdersAPP.send(reqFile, resp);
            }
        }), "/app/*");
    }

    private static void initList(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWay(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersAPP.list(way, authed));
            }
        }), "/list/app");
    }
}
