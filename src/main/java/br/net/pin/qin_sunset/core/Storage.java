package br.net.pin.qin_sunset.core;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.dbcp2.BasicDataSource;
import br.com.pointel.jarch.data.Helped;
import br.com.pointel.jarch.mage.WizChars;

public class Storage {

    private Map<String, Stored> stores = null;

    public void start(Bases bases) {
        this.stores = new ConcurrentHashMap<>();
        for (var base : bases) {
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
            source.setMinIdle(base.storeMinIdle);
            source.setMaxIdle(base.storeMaxIdle);
            source.setMaxTotal(base.storeMaxTotal);
            this.stores.put(base.getName(), new Stored(helper, source));
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
}
