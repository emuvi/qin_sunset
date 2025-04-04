package br.com.pointel.qin_sunset.hook;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.com.pointel.qin_sunset.swap.OnePath;
import br.com.pointel.qin_sunset.swap.PathRead;
import br.com.pointel.qin_sunset.swap.PathWrite;
import br.com.pointel.qin_sunset.swap.TwoPath;
import br.com.pointel.qin_sunset.work.OrdersDir;
import br.com.pointel.qin_sunset.work.Runner;
import br.com.pointel.qin_sunset.work.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesDir {
    public static void init(ServletContextHandler context) {
        initDirList(context);
        initDirNew(context);
        initDirCopy(context);
        initDirMove(context);
        initDirDel(context);
        initFileRead(context);
        initFileWrite(context);
        initFileAppend(context);
        initFileCopy(context);
        initFileMove(context);
        initFileDel(context);
    }

    private static void initDirList(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var onePath = OnePath.fromString(body);
                if (onePath.path == null || onePath.path.isEmpty()) {
                    onePath.path = ".";
                }
                var path = Utils.resolveFile(onePath.path, authed.getHome());
                if (!authed.isAllowedDir(path.getAbsolutePath(), false)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the path: " + path);
                    return;
                }
                if (!path.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no path at: " + path);
                    return;
                }
                if (!path.isDirectory()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "It is not a directory the path: " + path);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.dirList(path));
            }
        }), "/dir/list");
    }

    private static void initDirNew(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var onePath = OnePath.fromString(body);
                if (onePath.path == null || onePath.path.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
                    return;
                }
                var path = Utils.resolveFile(onePath.path, authed.getHome());
                if (!authed.isAllowedDir(path.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the path: " + path);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.dirNew(path));
            }
        }), "/dir/new");
    }

    private static void initDirCopy(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var twoPath = TwoPath.fromString(body);
                if (twoPath.origin == null || twoPath.origin.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a origin");
                    return;
                }
                if (twoPath.destiny == null || twoPath.destiny.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a destiny");
                    return;
                }
                var origin = Utils.resolveFile(twoPath.origin, authed.getHome());
                var destiny = Utils.resolveFile(twoPath.destiny, authed.getHome());
                if (!authed.isAllowedDir(origin.getAbsolutePath(), false)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the origin: " + origin);
                    return;
                }
                if (!authed.isAllowedDir(destiny.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the destiny: " + destiny);
                    return;
                }
                if (!origin.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no origin at: " + origin);
                    return;
                }
                if (!origin.isDirectory()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "It is not a directory the origin: " + origin);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.dirCopy(origin, destiny));
            }
        }), "/dir/copy");
    }

    private static void initDirMove(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var twoPath = TwoPath.fromString(body);
                if (twoPath.origin == null || twoPath.origin.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a origin");
                    return;
                }
                if (twoPath.destiny == null || twoPath.destiny.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a destiny");
                    return;
                }
                var origin = Utils.resolveFile(twoPath.origin, authed.getHome());
                var destiny = Utils.resolveFile(twoPath.destiny, authed.getHome());
                if (!authed.isAllowedDir(origin.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the origin: " + origin);
                    return;
                }
                if (!authed.isAllowedDir(destiny.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the destiny: " + destiny);
                    return;
                }
                if (!origin.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no origin at: " + origin);
                    return;
                }
                if (!origin.isDirectory()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "It is not a directory the origin: " + origin);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.dirMove(origin, destiny));
            }
        }), "/dir/move");
    }

    private static void initDirDel(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var onePath = OnePath.fromString(body);
                if (onePath.path == null || onePath.path.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
                    return;
                }
                var path = Utils.resolveFile(onePath.path, authed.getHome());
                if (!authed.isAllowedDir(path.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the path: " + path);
                    return;
                }
                if (!path.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no path at: " + path);
                    return;
                }
                if (!path.isDirectory()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "It is not a directory the path: " + path);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.dirDel(path));
            }
        }), "/dir/del");
    }

    private static void initFileRead(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var pathRead = PathRead.fromString(body);
                if (pathRead.path == null || pathRead.path.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
                    return;
                }
                var path = Utils.resolveFile(pathRead.path, authed.getHome());
                if (!authed.isAllowedDir(path.getAbsolutePath(), false)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the path: " + path);
                    return;
                }
                pathRead.base64 = pathRead.base64 != null ? pathRead.base64 : false;
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.fileRead(path, pathRead.base64,
                                pathRead.rangeStart, pathRead.rangeLength));
            }
        }), "/file/read");
    }

    private static void initFileWrite(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var pathWrite = PathWrite.fromString(body);
                if (pathWrite.path == null || pathWrite.path.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
                    return;
                }
                var path = Utils.resolveFile(pathWrite.path, authed.getHome());
                if (!authed.isAllowedDir(path.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the path: " + path);
                    return;
                }
                pathWrite.base64 = pathWrite.base64 != null ? pathWrite.base64 : false;
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.fileWrite(path, pathWrite.base64,
                                pathWrite.data, pathWrite.rangeStart));
            }
        }), "/file/write");
    }

    private static void initFileAppend(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var pathWrite = PathWrite.fromString(body);
                if (pathWrite.path == null || pathWrite.path.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
                    return;
                }
                var path = Utils.resolveFile(pathWrite.path, authed.getHome());
                if (!authed.isAllowedDir(path.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the path: " + path);
                    return;
                }
                pathWrite.base64 = pathWrite.base64 != null ? pathWrite.base64 : false;
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.fileAppend(path, pathWrite.base64,
                                pathWrite.data));
            }
        }), "/file/append");
    }

    private static void initFileCopy(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var twoPath = TwoPath.fromString(body);
                if (twoPath.origin == null || twoPath.origin.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a origin");
                    return;
                }
                if (twoPath.destiny == null || twoPath.destiny.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a destiny");
                    return;
                }
                var origin = Utils.resolveFile(twoPath.origin, authed.getHome());
                var destiny = Utils.resolveFile(twoPath.destiny, authed.getHome());
                if (!authed.isAllowedDir(origin.getAbsolutePath(), false)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the origin: " + origin);
                    return;
                }
                if (!authed.isAllowedDir(destiny.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the destiny: " + destiny);
                    return;
                }
                if (!origin.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no origin at: " + origin);
                    return;
                }
                if (!origin.isFile()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "It is not a file the origin: " + origin);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.fileCopy(origin, destiny));
            }
        }), "/file/copy");
    }

    private static void initFileMove(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var twoPath = TwoPath.fromString(body);
                if (twoPath.origin == null || twoPath.origin.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a origin");
                    return;
                }
                if (twoPath.destiny == null || twoPath.destiny.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a destiny");
                    return;
                }
                var origin = Utils.resolveFile(twoPath.origin, authed.getHome());
                var destiny = Utils.resolveFile(twoPath.destiny, authed.getHome());
                if (!authed.isAllowedDir(origin.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the origin: " + origin);
                    return;
                }
                if (!authed.isAllowedDir(destiny.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the destiny: " + destiny);
                    return;
                }
                if (!origin.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no origin at: " + origin);
                    return;
                }
                if (!origin.isFile()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "It is not a file the origin: " + origin);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.fileMove(origin, destiny));
            }
        }), "/file/move");
    }

    private static void initFileDel(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesDir)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow directory access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var onePath = OnePath.fromString(body);
                if (onePath.path == null || onePath.path.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
                    return;
                }
                var path = Utils.resolveFile(onePath.path, authed.getHome());
                if (!authed.isAllowedDir(path.getAbsolutePath(), true)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to mutate the path: " + path);
                    return;
                }
                if (!path.exists()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no path at: " + path);
                    return;
                }
                if (!path.isFile()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "It is not a file the path: " + path);
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersDir.fileDel(path));
            }
        }), "/file/del");
    }
}
