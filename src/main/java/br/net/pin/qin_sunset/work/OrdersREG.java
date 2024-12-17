package br.net.pin.qin_sunset.work;

import java.io.StringWriter;
import br.com.pointel.jarch.data.Delete;
import br.com.pointel.jarch.data.Insert;
import br.com.pointel.jarch.data.Registry;
import br.com.pointel.jarch.data.Select;
import br.com.pointel.jarch.data.Strain;
import br.com.pointel.jarch.data.Update;
import br.com.pointel.jarch.flow.CSVMaker;
import br.com.pointel.jarch.flow.CSVWrite;
import br.net.pin.qin_sunset.core.AllowReg;
import br.net.pin.qin_sunset.core.Authed;
import br.net.pin.qin_sunset.core.Way;
import jakarta.servlet.ServletException;

public class OrdersREG {

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
        for (var allow : authed.getAccess()) {
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

    public static String regNew(Way way, Insert insert, Strain strain)
                    throws ServletException {
        try {
            var helped = way.stores.getHelp(insert.registry.base);
            var resultID = helped.helper.insert(helped.link, insert, strain);
            return resultID;
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String regAsk(Way way, Select select, Strain strain)
                    throws ServletException {
        try {
            var helped = way.stores.getHelp(select.registry.base);
            var result = helped.helper.select(helped.link, select, strain);
            var maker = new CSVMaker(result, select.fields);
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

    public static String regSet(Way way, Update update, Strain strain)
                    throws ServletException {
        try {
            var helped = way.stores.getHelp(update.registry.base);
            var result = helped.helper.update(helped.link, update, strain);
            return result.toString();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String regDel(Way way, Delete delete, Strain strain)
                    throws ServletException {
        try {
            var helped = way.stores.getHelp(delete.registry.base);
            var result = helped.helper.delete(helped.link, delete, strain);
            return result.toString();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
