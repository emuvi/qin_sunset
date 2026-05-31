package com.vidlus.qin_sunset.work;

import java.io.StringWriter;
import java.util.Objects;

import com.vidlus.qin_sunset.core.AllowReg;
import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.WayToRun;

import com.vidlus.jarch.data.Heads;
import com.vidlus.jarch.data.Registry;
import com.vidlus.jarch.data.Strain;
import com.vidlus.jarch.data.Table;
import com.vidlus.jarch.data.ToDelete;
import com.vidlus.jarch.data.ToInsert;
import com.vidlus.jarch.data.ToSelect;
import com.vidlus.jarch.data.ToUpdate;
import com.vidlus.jarch.flow.CSVMaker;
import com.vidlus.jarch.flow.CSVWrite;
import jakarta.servlet.ServletException;

public class OrdersReg {

    private OrdersReg() {}

    public static Heads regTop(WayToRun wayToRun, String base) throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(base)) {
            return eOrm.getHeads();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static Table regSee(WayToRun wayToRun, Registry registry) throws ServletException {
        try (var eSql = wayToRun.stores.getESql(registry.base)) {
            return registry.tableHead.getTable(eSql.getLink());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static AllowReg regCan(Authed authed, Registry registry) {
        var result = new AllowReg();
        result.registry = registry;
        if (authed.isMaster()) {
            result.all = true;
            result.insert = true;
            result.select = true;
            result.update = true;
            result.delete = true;
            return result;
        }
        result.all = false;
        result.insert = false;
        result.select = false;
        result.update = false;
        result.delete = false;
        for (var allow : authed.getAllowList()) {
            if (allow.allowReg != null && allow.allowReg.registry != null) {
                if (Authed.canAllowResource(allow.allowReg.registry, registry)) {
                    if (allow.allowReg.all != null) {
                        result.all = allow.allowReg.all;
                    }
                    if (allow.allowReg.insert != null) {
                        result.insert = allow.allowReg.insert;
                    }
                    if (allow.allowReg.select != null) {
                        result.select = allow.allowReg.select;
                    }
                    if (allow.allowReg.update != null) {
                        result.update = allow.allowReg.update;
                    }
                    if (allow.allowReg.delete != null) {
                        result.delete = allow.allowReg.delete;
                    }
                }
            }
        }
        return result;
    }

    public static String regNew(WayToRun wayToRun, ToInsert toInsert, Strain strain) throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toInsert.base)) {
            return eOrm.insert(toInsert.insert, strain).id;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String regAsk(WayToRun wayToRun, ToSelect toSelect, Strain strain) throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toSelect.base)) {
            var resultSet = eOrm.select(toSelect.select, strain).resultSet;
            var maker = new CSVMaker(resultSet, toSelect.select.fieldList);
            var build = new StringWriter();
            try (var write = new CSVWrite(build)) {
                String[] line;
                while ((line = maker.makeLine()) != null) {
                    write.writeLine(line);
                }
            }
            return build.toString();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String regSet(WayToRun wayToRun, ToUpdate toUpdate, Strain strain) throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toUpdate.base)) {
            return Objects.toString(eOrm.update(toUpdate.update, strain));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String regDel(WayToRun wayToRun, ToDelete toDelete, Strain strain) throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toDelete.base)) {
            return Objects.toString(eOrm.delete(toDelete.delete, strain));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
