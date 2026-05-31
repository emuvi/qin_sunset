package com.vidlus.qin_sunset.hook;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import com.vidlus.qin_sunset.work.OrdersPub;
import com.vidlus.qin_sunset.work.Runner;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesPub extends HttpServlet {

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var wayToRun = Runner.getWayToRun(req);
        if (wayToRun == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
            return;
        }
        if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesPub)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow public access");
            return;
        }
        var basePath = new File(wayToRun.airWays.setup.serverFolder, "pub");
        if (!basePath.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no folder at: " + basePath);
            return;
        }
        var reqFile = req.getPathInfo();
        if (reqFile == null || reqFile.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
            return;
        }
        var file = new File(basePath, URLDecoder.decode(reqFile, "UTF-8"));
        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no file at: " + file);
            return;
        }
        OrdersPub.send(req, resp, file, false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var wayToRun = Runner.getWayToRun(req);
        if (wayToRun == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
            return;
        }
        if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesPub)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow public access");
            return;
        }
        var basePath = new File(wayToRun.airWays.setup.serverFolder, "pub");
        if (!basePath.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no folder at: " + basePath);
            return;
        }
        var reqFile = req.getPathInfo();
        if (reqFile == null || reqFile.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a path");
            return;
        }
        var file = new File(basePath, URLDecoder.decode(reqFile, "UTF-8"));
        if (!file.exists()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "There is no file at: " + file);
            return;
        }
        OrdersPub.send(req, resp, file, true);
    }

}
