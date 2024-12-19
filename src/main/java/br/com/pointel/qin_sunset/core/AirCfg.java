package br.com.pointel.qin_sunset.core;

import br.com.pointel.jarch.data.Bases;

public class AirCfg {

    public final Setup setup;
    public final Bases bases;
    public final Users users;
    public final Groups groups;

    public AirCfg(Setup setup, Bases bases, Users users, Groups groups) {
        this.setup = setup;
        this.bases = bases;
        this.users = users;
        this.groups = groups;
    }

}
