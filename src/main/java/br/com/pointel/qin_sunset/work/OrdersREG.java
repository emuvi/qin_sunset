package br.com.pointel.qin_sunset.work;

import java.io.StringWriter;
import java.util.Objects;
import br.com.pointel.jarch.data.Registry;
import br.com.pointel.jarch.data.Strain;
import br.com.pointel.jarch.data.ToDelete;
import br.com.pointel.jarch.data.ToInsert;
import br.com.pointel.jarch.data.ToSelect;
import br.com.pointel.jarch.data.ToUpdate;
import br.com.pointel.jarch.flow.CSVMaker;
import br.com.pointel.jarch.flow.CSVWrite;
import br.com.pointel.qin_sunset.core.AllowReg;
import br.com.pointel.qin_sunset.core.Authed;
import br.com.pointel.qin_sunset.core.WayToRun;
import jakarta.servlet.ServletException;

public class OrdersReg {

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

    public static String regNew(WayToRun wayToRun, ToInsert toInsert, Strain strain)
                    throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toInsert.base)) {
            return eOrm.insert(toInsert.insert, strain);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String regAsk(WayToRun wayToRun, ToSelect toSelect, Strain strain)
                    throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toSelect.base)) {
            var result = eOrm.select(toSelect.select, strain);
            var maker = new CSVMaker(result, toSelect.select.fieldList);
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

    public static String regSet(WayToRun wayToRun, ToUpdate toUpdate, Strain strain)
                    throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toUpdate.base)) {
            return Objects.toString(eOrm.update(toUpdate.update, strain));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String regDel(WayToRun wayToRun, ToDelete toDelete, Strain strain)
                    throws ServletException {
        try (var eOrm = wayToRun.stores.getEOrm(toDelete.base)) {
            return Objects.toString(eOrm.delete(toDelete.delete, strain));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
