package br.com.pointel.qin_sunset.core;

public class AirCfg {

    public final Setup setup;
    public final Users users;
    public final Groups groups;
    public final Bases bases;

    public AirCfg(Setup setup, Users users, Groups groups, Bases bases) {
        this.setup = setup;
        this.users = users;
        this.groups = groups;
        this.bases = bases;
    }

}
