package br.com.pointel.qin_sunset.hook;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import com.google.gson.Gson;
import br.com.pointel.jarch.data.DeedsCrud;
import br.com.pointel.jarch.data.Delete;
import br.com.pointel.jarch.data.Insert;
import br.com.pointel.jarch.data.Order;
import br.com.pointel.jarch.data.Registry;
import br.com.pointel.jarch.data.Select;
import br.com.pointel.jarch.data.Update;
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
                if (registry.head == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry head");
                    return;
                }
                if (registry.head.name == null || registry.head.name
                                .isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry head name");
                    return;
                }
                resp.setContentType("application/json");
                resp.getWriter().print(new Gson().toJson(OrdersREG.regCan(authed,
                                registry)));
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
                var insert = Insert.fromString(body);
                if (insert.registry == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry");
                    return;
                }
                if (insert.registry.base == null || insert.registry.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry base");
                    return;
                }
                if (insert.registry.head == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry");
                    return;
                }
                if (insert.registry.head.name == null
                                || insert.registry.head.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry name");
                    return;
                }
                var allowed = authed.allowREG(insert.registry, DeedsCrud.INSERT);
                if (!allowed.head) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to deed this registry");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regNew(way, insert, allowed.tail));
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
                var select = Select.fromString(body);
                if (select.registry == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry");
                    return;
                }
                if (select.registry.base == null || select.registry.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry base");
                    return;
                }
                if (select.registry.head == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry");
                    return;
                }
                if (select.registry.head.name == null
                                || select.registry.head.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry name");
                    return;
                }
                var allowed = authed.allowREG(select.registry, DeedsCrud.SELECT);
                if (!allowed.head) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to deed this registry");
                    return;
                }
                applyAlwaysOrderByIfHas(way, authed, select);
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regAsk(way, select, allowed.tail));
            }

            private void applyAlwaysOrderByIfHas(WayToRun way, Authed authed, Select select) {
                var always_order = OrdersUtils.askParams(way, authed,
                                Params.ALWAYS_ORDER_BY_IF_HAS.toString());
                if (always_order != null && !always_order.isEmpty()) {
                    var source = select.registry.head.getSource();
                    for (var always_order_by : always_order.split(",")) {
                        var always_order_by_parts = always_order_by.split(" ");
                        var always_order_by_name = always_order_by_parts[0].trim();
                        var always_order_by_desc = false;
                        if (always_order_by_parts.length > 1) {
                            if (always_order_by_parts[1].trim().toUpperCase().equals(
                                            "DESC")) {
                                always_order_by_desc = true;
                            }
                        }
                        var found = false;
                        for (var field : select.fields) {
                            if (always_order_by_name.equals(field.name)) {
                                if (select.orders == null) {
                                    select.orders = new ArrayList<>();
                                }
                                var sourceAndName = always_order_by_name;
                                if (!sourceAndName.contains(".")) {
                                    sourceAndName = source + "." + sourceAndName;
                                }
                                select.orders.add(new Order(sourceAndName,
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
                var update = Update.fromString(body);
                if (update.registry == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry");
                    return;
                }
                if (update.registry.base == null || update.registry.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry base");
                    return;
                }
                if (update.registry.head == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry");
                    return;
                }
                if (update.registry.head.name == null
                                || update.registry.head.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry name");
                    return;
                }
                var allowed = authed.allowREG(update.registry, DeedsCrud.UPDATE);
                if (!allowed.head) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to deed this registry");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regSet(way, update, allowed.tail));
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
                var delete = Delete.fromString(body);
                if (delete.registry == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry");
                    return;
                }
                if (delete.registry.base == null || delete.registry.base.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry base");
                    return;
                }
                if (delete.registry.head == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry");
                    return;
                }
                if (delete.registry.head.name == null
                                || delete.registry.head.name.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                    "You must provide a registry registry name");
                    return;
                }
                var allowed = authed.allowREG(delete.registry, DeedsCrud.DELETE);
                if (!allowed.head) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                                    "You don't have access to deed this registry");
                    return;
                }
                resp.setContentType("text/plain");
                resp.getWriter().print(OrdersREG.regDel(way, delete, allowed.tail));
            }
        }), "/reg/del");
    }
}
