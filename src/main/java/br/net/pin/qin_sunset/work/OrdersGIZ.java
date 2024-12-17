package br.net.pin.qin_sunset.work;

import java.io.File;
import br.com.pointel.jarch.flow.Pace;
import br.net.pin.qin_sunset.core.Authed;
import br.net.pin.qin_sunset.core.Issued;
import br.net.pin.qin_sunset.core.IssuedLogger;
import br.net.pin.qin_sunset.core.IssuedWriter;
import br.net.pin.qin_sunset.core.IssuedWriter.Destiny;
import br.net.pin.qin_sunset.core.WayToRun;
import br.net.pin.qin_sunset.swap.Execute;

public class OrdersGIZ {
    public static String list(WayToRun way, Authed forAuthed) {
        var gizDir = new File(way.airCfg.setup.serverFolder, "giz");
        if (forAuthed.isMaster()) {
            return Utils.listFilesWithExtension(gizDir, ".giz");
        }
        var result = new StringBuilder();
        for (var access : forAuthed.getAccess()) {
            if (access.allowGiz != null) {
                if (new File(gizDir, access.allowGiz.path).exists()) {
                    result.append(access.allowGiz.path);
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }

    public static Issued run(Authed forAuthed, Execute execution) throws Exception {
        var gizMap = forAuthed.getGizMap();
        var script = gizMap.getScript(execution.exec);
        var joinErrs = execution.joinErrs != null ? execution.joinErrs : false;
        var issued = new Issued(joinErrs);
        var logger = new IssuedLogger(issued, execution.logLevel);
        var pace = new Pace(logger);
        issued.setPace(pace);
        new Thread() {
            @Override
            public void run() {
                synchronized (script) {
                    try {
                        var binding = script.getBinding();
                        binding.setVariable("args", execution.args);
                        var out = new IssuedWriter(issued, Destiny.OUT);
                        var err = new IssuedWriter(issued, Destiny.ERR);
                        binding.setProperty("out", out);
                        binding.setProperty("err", err);
                        binding.setProperty("pace", pace);
                        var result = script.run();
                        if (result instanceof Integer resultCode) {
                            issued.setResultCode(resultCode);
                        } else {
                            issued.setResultCode(0);
                        }
                    } catch (Exception e) {
                        issued.addErrLine(e.getMessage());
                        issued.setResultCode(-1);
                    } finally {
                        issued.setDone();
                    }
                }
            };
        }.start();
        return issued;
    }
}
