package br.com.pointel.qin_sunset.work;

import java.io.IOException;
import java.nio.file.Files;
import br.com.pointel.jarch.mage.WizBase;
import br.com.pointel.qin_sunset.Service;
import br.com.pointel.qin_sunset.core.AirWays;
import br.com.pointel.qin_sunset.core.Setup;
import br.com.pointel.qin_sunset.core.WayToRun;

public class OrdersWay {

    private OrdersWay() {}
    
    public static String newSetup(Service oldService, WayToRun oldWayToRun, Setup newSetup) throws IOException {
        newSetup.fixDefaults();
        Files.writeString(oldWayToRun.airWays.setupFile.toPath(), newSetup.toString());
        var newAirWays = new AirWays(newSetup, oldWayToRun.airWays.setupFile, 
                        oldWayToRun.airWays.bases, oldWayToRun.airWays.basesFile,
                        oldWayToRun.airWays.users, oldWayToRun.airWays.usersFile,
                        oldWayToRun.airWays.groups, oldWayToRun.airWays.groupsFile);
        var newWayToRun = new WayToRun(newAirWays, oldWayToRun.authedMap, oldWayToRun.stores);
        new Thread("Service Restart") {
            @Override
            public void run() {
                try {
                    WizBase.sleep(3000);
                    oldService.stop();
                    new Service(newWayToRun).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return "Setup updated. Service will be restarted.";
    }

}
