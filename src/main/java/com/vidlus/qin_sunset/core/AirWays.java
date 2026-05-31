package com.vidlus.qin_sunset.core;

import java.io.File;
import com.vidlus.jarch.data.Bases;

public class AirWays {

    public final Setup setup;
    public final File setupFile;
    public final Bases bases;
    public final File basesFile;
    public final Users users;
    public final File usersFile;
    public final Groups groups;
    public final File groupsFile;

    public AirWays(Setup setup, File setupFile, Bases bases, File basesFile, Users users, File usersFile, Groups groups, File groupsFile) {
        this.setup = setup;
        this.setupFile = setupFile;
        this.bases = bases;
        this.basesFile = basesFile;
        this.users = users;
        this.usersFile = usersFile;
        this.groups = groups;
        this.groupsFile = groupsFile;
    }

}
