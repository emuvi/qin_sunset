package com.vidlus.qin_sunset.work;

import java.io.File;

import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.Issued;
import com.vidlus.qin_sunset.core.IssuedLogger;
import com.vidlus.qin_sunset.core.IssuedWriter;
import com.vidlus.qin_sunset.core.WayToRun;
import com.vidlus.qin_sunset.core.IssuedWriter.Destiny;
import com.vidlus.qin_sunset.swap.Execute;

import com.vidlus.jarch.flow.Pace;

public class OrdersGiz {

    private OrdersGiz() {}
    
    public static String list(WayToRun wayToRun, Authed forAuthed) {
        var gizDir = new File(wayToRun.airWays.setup.serverFolder, "giz");
        if (forAuthed.isMaster()) {
            return Utils.listFilesWithExtension(gizDir, ".giz");
        }
        var result = new StringBuilder();
        for (var access : forAuthed.getAllowList()) {
            if (access.allowGiz != null) {
                if (new File(gizDir, access.allowGiz.path).exists()) {
                    result.append(access.allowGiz.path);
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }

    public static Issued run(Authed forAuthed, Execute execute) throws Exception {
        var gizMap = forAuthed.getGizMap();
        var script = gizMap.getScript(execute.name);
        var joinErrs = execute.joinErrs != null ? execute.joinErrs : false;
        var issued = new Issued(joinErrs);
        var logger = new IssuedLogger(issued, execute.logLevel);
        var pace = new Pace(logger);
        issued.setPace(pace);
        new Thread() {
            @Override
            public void run() {
                synchronized (script) {
                    try {
                        var binding = script.getBinding();
                        binding.setVariable("args", execute.argList);
                        try (var out = new IssuedWriter(issued, Destiny.OUT);
                                var err = new IssuedWriter(issued, Destiny.ERR);) {
                            binding.setProperty("out", out);
                            binding.setProperty("err", err);
                            binding.setProperty("pace", pace);
                            var result = script.run();
                                
                            if (result instanceof Integer resultCode) {
                                issued.setResultCode(resultCode);
                            } else {
                                issued.setResultCode(0);
                            }
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
