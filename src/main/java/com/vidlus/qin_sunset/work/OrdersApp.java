package com.vidlus.qin_sunset.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

import com.vidlus.qin_sunset.core.Authed;
import com.vidlus.qin_sunset.core.WayToRun;

import jakarta.servlet.http.HttpServletResponse;

public class OrdersApp {

    private OrdersApp() {}

    public static void send(File file, HttpServletResponse resp) throws IOException {
        resp.setContentType(Utils.getMimeType(file.getName()));
        resp.setContentLength((int) file.length());
        try (var input = new FileInputStream(file)) {
            IOUtils.copy(input, resp.getOutputStream());
        }
    }

    public static String list(WayToRun wayToRun, Authed forAuthed) {
        var appsDir = new File(wayToRun.airWays.setup.serverFolder, "app");
        if (forAuthed.isMaster()) {
            return Utils.listFolder(appsDir, true, false, null);
        }
        var result = new StringBuilder();
        for (var access : forAuthed.getAllowList()) {
            if (access.allowApp != null) {
                if (new File(appsDir, access.allowApp.name).exists()) {
                    result.append(access.allowApp.name);
                    result.append("\n");
                }
            }
        }
        return result.toString();
    }

}
