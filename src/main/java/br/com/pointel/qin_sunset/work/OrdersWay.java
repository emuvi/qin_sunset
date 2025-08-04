package br.com.pointel.qin_sunset.work;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.pointel.jarch.data.Bases;
import br.com.pointel.jarch.mage.WizBase;
import br.com.pointel.qin_sunset.Service;
import br.com.pointel.qin_sunset.core.AirWays;
import br.com.pointel.qin_sunset.core.Setup;
import br.com.pointel.qin_sunset.core.WayToRun;
import jakarta.servlet.ServletException;

public class OrdersWay {

    private static final Logger LOG = LoggerFactory.getLogger(OrdersWay.class);

    private OrdersWay() {}
    
    public static String newSetup(Service oldService, WayToRun oldWayToRun, Setup newSetup) throws ServletException {
        try {
            Files.writeString(oldWayToRun.airWays.setupFile.toPath(), newSetup.toString(), StandardCharsets.UTF_8);
            newSetup.fixDefaults();
            var newAirWays = new AirWays(newSetup, oldWayToRun.airWays.setupFile, 
                            oldWayToRun.airWays.bases, oldWayToRun.airWays.basesFile,
                            oldWayToRun.airWays.users, oldWayToRun.airWays.usersFile,
                            oldWayToRun.airWays.groups, oldWayToRun.airWays.groupsFile);
            var newWayToRun = new WayToRun(newAirWays, oldWayToRun.authedMap, oldWayToRun.stores);
            new Thread("Service Restart") {
                @Override
                public void run() {
                    try {
                        WizBase.sleep(1000);
                        oldService.stop();
                        WizBase.sleep(1000);
                        new Service(newWayToRun).start();
                    } catch (Exception e) {
                        LOG.error("Could not restart the service", e);
                    }
                }
            }.start();
            return "Server setup updated. Service will be restarted.";
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String newBases(Service oldService, WayToRun oldWayToRun, Bases newBases) throws ServletException {
        try {
            Files.writeString(oldWayToRun.airWays.basesFile.toPath(), newBases.toString(), StandardCharsets.UTF_8);
            newBases.fixDefaults();
            var newAirWays = new AirWays(oldWayToRun.airWays.setup, oldWayToRun.airWays.setupFile, 
                            newBases, oldWayToRun.airWays.basesFile,
                            oldWayToRun.airWays.users, oldWayToRun.airWays.usersFile,
                            oldWayToRun.airWays.groups, oldWayToRun.airWays.groupsFile);
            var newWayToRun = new WayToRun(newAirWays, oldWayToRun.authedMap);
            new Thread("Service Restart") {
                @Override
                public void run() {
                    try {
                        WizBase.sleep(1000);
                        oldService.stop();
                        WizBase.sleep(1000);
                        new Service(newWayToRun).start();
                    } catch (Exception e) {
                        LOG.error("Could not restart the service", e);
                    }
                }
            }.start();
            return "Server bases updated. Service will be restarted.";
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
