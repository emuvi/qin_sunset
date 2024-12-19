package br.com.pointel.qin_sunset.hook;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import br.com.pointel.jarch.data.CrudDeeds;
import br.com.pointel.jarch.data.Order;
import br.com.pointel.jarch.data.Registry;
import br.com.pointel.jarch.data.ToDelete;
import br.com.pointel.jarch.data.ToInsert;
import br.com.pointel.jarch.data.ToSelect;
import br.com.pointel.jarch.data.ToUpdate;
import br.com.pointel.qin_sunset.core.Authed;
import br.com.pointel.qin_sunset.core.Params;
import br.com.pointel.qin_sunset.core.WayToRun;
import br.com.pointel.qin_sunset.work.OrdersREG;
import br.com.pointel.qin_sunset.work.OrdersUtils;
import br.com.pointel.qin_sunset.work.Runner;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServesREG {

    public static void init(ServletContextHandler context) {
        initRegCan(context);
        initRegNew(context);
        initRegAsk(context);
        initRegSet(context);
        initRegDel(context);
    }

    private static void initRegCan(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWay(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var registry = Registry.fromString(body);
                if (registry == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry");
                    return;
                }
                if (registry.base == null || registry.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry base");
                    return;
                }
                if (registry.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry table head");
                    return;
                }
                if (registry.tableHead.name == null || registry.tableHead.name
                                .isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry table head name");
                    return;
                }
                resp.setContentType("application/json");
                resp.getWriter().print(OrdersREG.regCan(authed, registry).toString());
            }
        }), "/reg/can");
    }

    private static void initRegNew(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWay(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toInsert = ToInsert.fromString(body);
                if (toInsert.base == null || toInsert.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a base");
                    return;
                }
                if (toInsert.insert.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head");
                    return;
                }
                if (toInsert.insert.tableHead.name == null
                                || toInsert.insert.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head name");
                    return;
                }
                var allowedReg = authed.allowREG(toInsert.getRegistry(), CrudDeeds.INSERT);
                if (!allowedReg.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access this operation");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regNew(way, toInsert, allowedReg.strained));
            }
        }), "/reg/new");
    }

    private static void initRegAsk(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWay(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toSelect = ToSelect.fromString(body);
                if (toSelect.base == null || toSelect.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a base");
                    return;
                }
                if (toSelect.select.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head");
                    return;
                }
                if (toSelect.select.tableHead.name == null
                                || toSelect.select.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head name");
                    return;
                }
                var allowedReg = authed.allowREG(toSelect.getRegistry(), CrudDeeds.SELECT);
                if (!allowedReg.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to this operation");
                    return;
                }
                applyAlwaysOrderByIfHas(way, authed, toSelect);
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regAsk(way, toSelect, allowedReg.strained));
            }

            private void applyAlwaysOrderByIfHas(WayToRun way, Authed authed, ToSelect toSelect) {
                var always_order = OrdersUtils.askParams(way, authed, Params.ALWAYS_ORDER_BY_IF_HAS
                                .toString());
                if (always_order != null && !always_order.isEmpty()) {
                    var source = toSelect.select.tableHead.getSource();
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
            protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWay(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toUpdate = ToUpdate.fromString(body);
                if (toUpdate.base == null || toUpdate.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a base");
                    return;
                }
                if (toUpdate.update.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head");
                    return;
                }
                if (toUpdate.update.tableHead.name == null
                                || toUpdate.update.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head name");
                    return;
                }
                var allowed = authed.allowREG(toUpdate.getRegistry(), CrudDeeds.UPDATE);
                if (!allowed.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to this operation");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regSet(way, toUpdate, allowed.strained));
            }
        }), "/reg/set");
    }

    private static void initRegDel(ServletContextHandler context) {
        context.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                            throws ServletException, IOException {
                var way = Runner.getWay(req);
                var authed = Runner.getAuthed(way, req);
                if (authed == null) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You must be logged");
                    return;
                }
                var body = IOUtils.toString(req.getReader());
                var toDelete = ToDelete.fromString(body);
                if (toDelete.base == null || toDelete.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a base");
                    return;
                }
                if (toDelete.delete.tableHead == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head");
                    return;
                }
                if (toDelete.delete.tableHead.name == null
                                || toDelete.delete.tableHead.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a table head name");
                    return;
                }
                var allowed = authed.allowREG(toDelete.getRegistry(), CrudDeeds.DELETE);
                if (!allowed.allowed) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to this operation");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regDel(way, toDelete, allowed.strained));
            }
        }), "/reg/del");
    }
}
