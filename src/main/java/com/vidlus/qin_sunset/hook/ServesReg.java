package com.vidlus.qin_sunset.hook;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.WayToRun;
import com.vidlus.qin_sunset.swap.QinConfigs;
import com.vidlus.qin_sunset.work.OrdersReg;
import com.vidlus.qin_sunset.work.OrdersUtl;
import com.vidlus.qin_sunset.work.Runner;

import com.vidlus.jarch.data.Deeds;
import com.vidlus.jarch.data.Order;
import com.vidlus.jarch.data.Registry;
import com.vidlus.jarch.data.ToDelete;
import com.vidlus.jarch.data.ToInsert;
import com.vidlus.jarch.data.ToSelect;
import com.vidlus.jarch.data.ToUpdate;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesReg {

    private ServesReg() {}

    public static void init(ServletContextHandler context) {
        initRegTop(context);
        initRegSee(context);
        initRegCan(context);
        initRegNew(context);
        initRegAsk(context);
        initRegSet(context);
        initRegDel(context);
    }

    private static void initRegTop(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesReg)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow register access");
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
                var base = IOUtils.toString(req.getReader());
                if (base == null || base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a base");
                    return;
                }
                var result = OrdersReg.regTop(wayToRun, base);
                resp.setContentType("application/json");
                resp.getWriter().print(result.toString());
            }
        }), "/reg/top");
    }

    private static void initRegSee(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesReg)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow register access");
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
                var registry = Registry.fromChars(body);
                if (registry == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry");
                    return;
                }
                if (registry.base == null || registry.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry base");
                    return;
                }
                if (registry.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry table head");
                    return;
                }
                if (registry.tableHead.name == null || registry.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry table head name");
                    return;
                }
                var result = OrdersReg.regSee(wayToRun, registry);
                resp.setContentType("application/json");
                resp.getWriter().print(result.toString());
            }
        }), "/reg/see");
    }

    private static void initRegCan(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesReg)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow register access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var registry = Registry.fromChars(body);
                if (registry == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry");
                    return;
                }
                if (registry.base == null || registry.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry base");
                    return;
                }
                if (registry.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry table head");
                    return;
                }
                if (registry.tableHead.name == null || registry.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a registry table head name");
                    return;
                }
                var result = OrdersReg.regCan(authed, registry);
                resp.setContentType("application/json");
                resp.getWriter().print(result.toString());
            }
        }), "/reg/can");
    }

    private static void initRegNew(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesReg)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow register access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toInsert = ToInsert.fromChars(body);
                if (toInsert.base == null || toInsert.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a base");
                    return;
                }
                if (toInsert.insert.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head");
                    return;
                }
                if (toInsert.insert.tableHead.name == null || toInsert.insert.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head name");
                    return;
                }
                var allowedReg = authed.isAllowedReg(toInsert.getRegistry(), Deeds.Insert);
                if (!allowedReg.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access this operation");
                    return;
                }
                var result = OrdersReg.regNew(wayToRun, toInsert, allowedReg.strained);
                resp.setContentType("text/plain");
                resp.getWriter().print(result);
            }
        }), "/reg/new");
    }

    private static void initRegAsk(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesReg)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow register access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toSelect = ToSelect.fromChars(body);
                if (toSelect.base == null || toSelect.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a base");
                    return;
                }
                if (toSelect.select.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head");
                    return;
                }
                if (toSelect.select.tableHead.name == null || toSelect.select.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head name");
                    return;
                }
                var allowedReg = authed.isAllowedReg(toSelect.getRegistry(), Deeds.Select);
                if (!allowedReg.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to this operation");
                    return;
                }
                applyAlwaysOrderByIfHas(wayToRun, authed, toSelect);
                var result = OrdersReg.regAsk(wayToRun, toSelect, allowedReg.strained);
                resp.setContentType("text/plain");
                resp.getWriter().print(result);
            }

            private void applyAlwaysOrderByIfHas(WayToRun wayToRun, Authed authed, ToSelect toSelect) {
                var always_order = OrdersUtl.askConfig(wayToRun, authed, QinConfigs.AlwaysOrderByIfHas.toString());
                if (always_order != null && !always_order.isEmpty()) {
                    var source = toSelect.select.tableHead.getCatalogSchemaName();
                    for (var always_order_by : always_order.split(",")) {
                        var always_order_by_parts = always_order_by.split(" ");
                        var always_order_by_name = always_order_by_parts[0].trim();
                        var always_order_by_desc = false;
                        if (always_order_by_parts.length > 1) {
                            if (always_order_by_parts[1].trim().toUpperCase().equals("DESC")) {
                                always_order_by_desc = true;
                            }
                        }
                        var found = false;
                        for (var field : toSelect.select.fieldList) {
                            if (always_order_by_name.equals(field.name)) {
                                if (toSelect.select.orderList == null) {
                                    toSelect.select.orderList = new ArrayList<>();
                                }
                                var sourceAndName = always_order_by_name;
                                if (!sourceAndName.contains(".")) {
                                    sourceAndName = source + "." + sourceAndName;
                                }
                                toSelect.select.orderList.add(new Order(sourceAndName,
                                                always_order_by_desc));
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                }
            }
        }), "/reg/ask");
    }

    private static void initRegSet(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesReg)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow register access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toUpdate = ToUpdate.fromChars(body);
                if (toUpdate.base == null || toUpdate.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a base");
                    return;
                }
                if (toUpdate.update.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head");
                    return;
                }
                if (toUpdate.update.tableHead.name == null || toUpdate.update.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head name");
                    return;
                }
                var allowed = authed.isAllowedReg(toUpdate.getRegistry(), Deeds.Update);
                if (!allowed.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to this operation");
                    return;
                }
                var result = OrdersReg.regSet(wayToRun, toUpdate, allowed.strained);
                resp.setContentType("text/plain");
                resp.getWriter().print(result);
            }
        }), "/reg/set");
    }

    private static void initRegDel(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                var wayToRun = Runner.getWayToRun(req);
                if (wayToRun == null) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server does not have a way to run");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesBas)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow database access");
                    return;
                }
                if (!Boolean.TRUE.equals(wayToRun.airWays.setup.servesReg)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Server does not allow register access");
                    return;
                }
                var authed = Runner.getAuthed(wayToRun, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toDelete = ToDelete.fromChars(body);
                if (toDelete.base == null || toDelete.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a base");
                    return;
                }
                if (toDelete.delete.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head");
                    return;
                }
                if (toDelete.delete.tableHead.name == null
                                || toDelete.delete.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You must provide a table head name");
                    return;
                }
                var allowed = authed.isAllowedReg(toDelete.getRegistry(), Deeds.Delete);
                if (!allowed.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to this operation");
                    return;
                }
                var result = OrdersReg.regDel(wayToRun, toDelete, allowed.strained);
                resp.setContentType("text/plain");
                resp.getWriter().print(result);
            }
        }), "/reg/del");
    }
}
