package com.vidlus.qin_sunset.work;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vidlus.qin_sunset.Service;
import com.vidlus.qin_sunset.core.AirWays;
import com.vidlus.qin_sunset.core.Groups;
import com.vidlus.qin_sunset.core.Setup;
import com.vidlus.qin_sunset.core.Users;
import com.vidlus.qin_sunset.core.WayToRun;

import com.vidlus.jarch.data.Bases;
import com.vidlus.jarch.mage.WizThread;
import jakarta.servlet.ServletException;

public class OrdersWay {

    private static final Logger log = LoggerFactory.getLogger(OrdersWay.class);

    private OrdersWay() {}
    
    public static String newSetup(Service oldService, WayToRun oldWayToRun, Setup newSetup) throws ServletException {
        try {
            Files.writeString(oldWayToRun.airWays.setupFile.toPath(), newSetup.toString(), StandardCharsets.UTF_8);
            var newAirWays = new AirWays(newSetup, oldWayToRun.airWays.setupFile, 
                            oldWayToRun.airWays.bases, oldWayToRun.airWays.basesFile,
                            oldWayToRun.airWays.users, oldWayToRun.airWays.usersFile,
                            oldWayToRun.airWays.groups, oldWayToRun.airWays.groupsFile);
            var newWayToRun = new WayToRun(newAirWays, oldWayToRun.authedMap, oldWayToRun.stores);
            new Thread("Service Restart Setup") {
                @Override
                public void run() {
                    try {
                        WizThread.sleep(1000);
                        oldService.stop();
                        WizThread.sleep(1000);
                        startService(newWayToRun);
                    } catch (Exception e) {
                        log.error("Could not restart the service from Setup update.", e);
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
            var newAirWays = new AirWays(oldWayToRun.airWays.setup, oldWayToRun.airWays.setupFile, 
                            newBases, oldWayToRun.airWays.basesFile,
                            oldWayToRun.airWays.users, oldWayToRun.airWays.usersFile,
                            oldWayToRun.airWays.groups, oldWayToRun.airWays.groupsFile);
            var newWayToRun = new WayToRun(newAirWays, oldWayToRun.authedMap);
            new Thread("Service Restart Bases") {
                @Override
                public void run() {
                    try {
                        WizThread.sleep(1000);
                        oldService.stop();
                        WizThread.sleep(1000);
                        startService(newWayToRun);
                    } catch (Exception e) {
                        log.error("Could not restart the service from Bases update.", e);
                    }
                }
            }.start();
            return "Server bases updated. Service will be restarted.";
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String newUsers(Service oldService, WayToRun oldWayToRun, Users newUsers) throws ServletException {
        try {
            Files.writeString(oldWayToRun.airWays.usersFile.toPath(), newUsers.toString(), StandardCharsets.UTF_8);
            var newAirWays = new AirWays(oldWayToRun.airWays.setup, oldWayToRun.airWays.setupFile, 
                            oldWayToRun.airWays.bases, oldWayToRun.airWays.basesFile,
                            newUsers, oldWayToRun.airWays.usersFile,
                            oldWayToRun.airWays.groups, oldWayToRun.airWays.groupsFile);
            var newWayToRun = new WayToRun(newAirWays);
            new Thread("Service Restart Users") {
                @Override
                public void run() {
                    try {
                        WizThread.sleep(1000);
                        oldService.stop();
                        WizThread.sleep(1000);
                        startService(newWayToRun);
                    } catch (Exception e) {
                        log.error("Could not restart the service from Users update.", e);
                    }
                }
            }.start();
            return "Server users updated. Service will be restarted.";
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static String newGroups(Service oldService, WayToRun oldWayToRun, Groups newGroups) throws ServletException {
        try {
            Files.writeString(oldWayToRun.airWays.groupsFile.toPath(), newGroups.toString(), StandardCharsets.UTF_8);
            var newAirWays = new AirWays(oldWayToRun.airWays.setup, oldWayToRun.airWays.setupFile, 
                            oldWayToRun.airWays.bases, oldWayToRun.airWays.basesFile,
                            oldWayToRun.airWays.users, oldWayToRun.airWays.usersFile,
                            newGroups, oldWayToRun.airWays.groupsFile);
            var newWayToRun = new WayToRun(newAirWays);
            new Thread("Service Restart Groups") {
                @Override
                public void run() {
                    try {
                        WizThread.sleep(1000);
                        oldService.stop();
                        WizThread.sleep(1000);
                        startService(newWayToRun);
                    } catch (Exception e) {
                        log.error("Could not restart the service from Groups update.", e);
                    }
                }
            }.start();
            return "Server groups updated. Service will be restarted.";
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static void startService(WayToRun wayToRun) throws Exception {
        log.info("Starting...");
        wayToRun.airWays.setup.fixNulls();
        wayToRun.airWays.bases.fixNulls();
        wayToRun.airWays.users.fixNulls();
        wayToRun.airWays.groups.fixNulls();
        log.info("Setup: {}", wayToRun.airWays.setup);
        log.info("Bases: {}", wayToRun.airWays.bases);
        log.info("Users: {}", wayToRun.airWays.users);
        log.info("Groups: {}", wayToRun.airWays.groups);
        wayToRun.airWays.setup.fixEnvs();
        wayToRun.airWays.bases.fixEnvs();
        wayToRun.airWays.users.fixEnvs();
        wayToRun.airWays.groups.fixEnvs();
        new Service(wayToRun).start();
    }

}
