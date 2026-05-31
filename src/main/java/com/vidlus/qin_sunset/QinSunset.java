package com.vidlus.qin_sunset;

import java.io.File;
import java.nio.file.Files;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vidlus.qin_sunset.core.AirWays;
import com.vidlus.qin_sunset.core.Groups;
import com.vidlus.qin_sunset.core.Setup;
import com.vidlus.qin_sunset.core.Users;
import com.vidlus.qin_sunset.core.WayToRun;
import com.vidlus.qin_sunset.work.OrdersWay;

import com.vidlus.jarch.data.Bases;

public class QinSunset {

    private static final Logger log = LoggerFactory.getLogger(QinSunset.class);

    public static void main(String[] args) throws Exception {
        var options = cmdOptions();
        var command = new DefaultParser().parse(options, args);
        if (command.hasOption('?')) {
            System.out.println("QinSunset is a command program that servers public files, graphical user interfaces, file system access with authorization, command programs dispatchers and monitoring, databases queries and scripts execution. It is the base of the Vidlus information platform and the backend of the Qinpel, the Quick Interface to Power Intelligence.");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("qin_sunset", options);
            return;
        }
        Setup setup;
        var setupFile = new File("setup.json");
        if (setupFile.exists()) {
            setup = Setup.fromChars(Files.readString(setupFile.toPath()));
        } else {
            setup = new Setup();
        }
        setFromCmd(command, setup);
        Bases bases;
        var basesFile = new File("bases.json");
        if (basesFile.exists()) {
            bases = Bases.fromChars(Files.readString(basesFile.toPath()));
        } else {
            bases = new Bases();
        }
        Users users;
        var usersFile = new File("users.json");
        if (usersFile.exists()) {
            users = Users.fromChars(Files.readString(usersFile.toPath()));
        } else {
            users = new Users();
        }
        Groups groups;
        var groupsFile = new File("groups.json");
        if (groupsFile.exists()) {
            groups = Groups.fromChars(Files.readString(groupsFile.toPath()));
        } else {
            groups = new Groups();
        }
        var airWays = new AirWays(setup, setupFile, bases, basesFile, users, usersFile, groups, groupsFile);
        var wayToRun = new WayToRun(airWays);
        OrdersWay.startService(wayToRun);
    }

    public static Options cmdOptions() {
        var result = new Options();
        result.addOption(Option.builder("?").longOpt("help").desc("Print usage information.").build());
        result.addOption(Option.builder("n").longOpt("name").hasArg().desc("On behalf of what name should we serve?").build());
        result.addOption(Option.builder("l").longOpt("lang").hasArg().desc("On what language should we serve?").build());
        result.addOption(Option.builder("h").longOpt("host").hasArg().desc("On what host should we serve?").build());
        result.addOption(Option.builder("p").longOpt("port").hasArg().desc("On what port should we serve?").build());
        result.addOption(Option.builder("f").longOpt("folder").hasArg().desc("On what folder should we serve?").build());
        result.addOption(Option.builder("u").longOpt("serves-pub").desc("Should we serve public files?").build());
        result.addOption(Option.builder("a").longOpt("serves-app").desc("Should we serve applications?").build());
        result.addOption(Option.builder("d").longOpt("serves-dir").desc("Should we serve directories?").build());
        result.addOption(Option.builder("c").longOpt("serves-cmd").desc("Should we serve commands?").build());
        result.addOption(Option.builder("b").longOpt("serves-bas").desc("Should we serve databases storage?").build());
        result.addOption(Option.builder("r").longOpt("serves-reg").desc("Should we serve register actions?").build());
        result.addOption(Option.builder("g").longOpt("serves-giz").desc("Should we serve GIZ scripts?").build());
        return result;
    }

    public static void setFromCmd(CommandLine command, Setup setup) {
        if (command.hasOption('n')) {
            setup.serverName = command.getOptionValue('n');
        }
        if (command.hasOption('l')) {
            setup.serverLang = command.getOptionValue('l');
        }
        if (command.hasOption('h')) {
            setup.serverHost = command.getOptionValue('h');
        }
        if (command.hasOption('p')) {
            setup.serverPort = Integer.parseInt(command.getOptionValue('p'));
        }
        if (command.hasOption('f')) {
            setup.serverFolder = command.getOptionValue('f');
        }
        if (command.hasOption('u')) {
            setup.servesPub = true;
        }
        if (command.hasOption('a')) {
            setup.servesApp = true;
        }
        if (command.hasOption('d')) {
            setup.servesDir = true;
        }
        if (command.hasOption('c')) {
            setup.servesCmd = true;
        }
        if (command.hasOption('b')) {
            setup.servesBas = true;
        }
        if (command.hasOption('r')) {
            setup.servesReg = true;
        }
        if (command.hasOption('g')) {
            setup.servesGiz = true;
        }
    }

}
