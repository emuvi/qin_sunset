package com.vidlus.qin_sunset.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.Issued;
import com.vidlus.qin_sunset.core.IssuedLogger;
import com.vidlus.qin_sunset.core.WayToRun;
import com.vidlus.qin_sunset.swap.Execute;

import com.vidlus.jarch.flow.Pace;

public class OrdersCmd {

    private OrdersCmd() {}

    public static String list(WayToRun wayToRun, Authed forAuthed) {
        var cmdsDir = new File(wayToRun.airWays.setup.serverFolder, "cmd");
        if (forAuthed.isMaster()) {
            return Utils.listFolder(cmdsDir, false, true, null);
        }
        var result = new StringBuilder();
        for (var access : forAuthed.getAllowList()) {
            if (access.allowCmd != null) {
                if (new File(cmdsDir, access.allowCmd.name).exists()) {
                    result.append(access.allowCmd.name);
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }

    public static Issued run(WayToRun wayToRun, Execute execution) throws Exception {
        var joinErrs = execution.joinErrs != null ? execution.joinErrs : false;
        var issued = new Issued(joinErrs);
        var logger = new IssuedLogger(issued, execution.logLevel);
        var pace = new Pace(logger);
        issued.setPace(pace);
        var builder = new ProcessBuilder();
        var cmdsDir = new File(wayToRun.airWays.setup.serverFolder, "cmd");
        var cmdPath = new File(cmdsDir, execution.name);
        builder.command().add(cmdPath.getAbsolutePath());
        if (execution.argList != null) {
            builder.command().addAll(execution.argList);
        }
        builder.redirectErrorStream(false);
        var process = builder.start();
        if (execution.inputList != null) {
            new Thread("Cmd " + execution.name + " Input") {
                @Override
                public void run() {
                    try {
                        var writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                        for (var input : execution.inputList) {
                            writer.write(input);
                            writer.newLine();
                            writer.flush();
                        }
                    } catch (Exception e) {
                        issued.addErrLine("Exception on put Input: " + e.getMessage());
                    }
                }
            }.start();
        }
        new Thread("Cmd " + execution.name + " Output") {
            public void run() {
                var reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        issued.addOutLine(line);
                    }
                } catch (Exception e) {
                    issued.addErrLine("Exception on get Output: " + e.getMessage());
                }
            }
        }.start();
        new Thread("Cmd " + execution.name + " Error") {
            @Override
            public void run() {
                var reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        issued.addErrLine(line);
                    }
                } catch (Exception e) {
                    issued.addErrLine("Exception on get Error: " + e.getMessage());
                }
            }
        }.start();
        new Thread("Cmd " + execution.name + " Result") {
            @Override
            public void run() {
                try {
                    var resultCode = process.waitFor();
                    issued.setResultCode(resultCode);
                } catch (Exception e) {
                    issued.addErrLine("Exception on get Result Code: " + e.getMessage());
                } finally {
                    issued.setDone();
                }
            }
        }.start();
        return issued;
    }

}
