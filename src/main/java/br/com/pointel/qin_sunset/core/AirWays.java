package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.Bases;

public class AirWays {

    public final Setup setup;
    public final Bases bases;
    public final Users users;
    public final Groups groups;

    public AirWays(Setup setup, Bases bases, Users users, Groups groups) {
        this.setup = setup;
        this.bases = bases;
        this.users = users;
        this.groups = groups;
    }

}
