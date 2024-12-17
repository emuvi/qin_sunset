package br.net.pin.qin_sunset.core;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.dbcp2.BasicDataSource;
import br.com.pointel.jarch.data.Helped;
import br.com.pointel.jarch.data.Helper;
import br.com.pointel.jarch.mage.WizChars;

public class Storage {
    private final AirCfg air;
    private final Map<String, Stored> stores;

    public Storage(AirCfg air) throws Exception {
        this.air = air;
        if (this.air.setup.servesBAS) {
            this.stores = new ConcurrentHashMap<>();
            for (var base : this.air.bases) {
                var helper = base.getHelper();
                var source = new BasicDataSource();
                source.setUrl(base.getUrl());
                var user = base.getUser();
                if (!WizChars.isEmpty(user)) {
                    source.setUsername(user);
                }
                var pass = base.getPass();
                if (!WizChars.isEmpty(pass)) {
                    source.setPassword(pass);
                }
                source.setMinIdle(this.air.setup.storeMinIdle);
                source.setMaxIdle(this.air.setup.storeMaxIdle);
                source.setMaxTotal(this.air.setup.storeMaxTotal);
                this.stores.put(base.getName(), new Stored(helper, source));
            }
        } else {
            this.stores = null;
        }
    }

    public Connection getLink(String ofBase) throws Exception {
        if (this.stores == null) {
            throw new Exception("No stores are served.");
        }
        var stored = this.stores.get(ofBase);
        if (stored == null) {
            throw new Exception("Base " + ofBase + " not found");
        }
        return stored.source.getConnection();
    }

    public Helped getHelp(String onBase) throws Exception {
        if (this.stores == null) {
            throw new Exception("No stores are served.");
        }
        var stored = this.stores.get(onBase);
        if (stored == null) {
            throw new Exception("Base " + onBase + " not found");
        }
        var connection = stored.source.getConnection();
        connection.setAutoCommit(true);
        return new Helped(connection, stored.helper);
    }

    private static class Stored {
        public final Helper helper;
        public final BasicDataSource source;

        public Stored(Helper helper, BasicDataSource source) {
            this.helper = helper;
            this.source = source;
        }
    }
}
