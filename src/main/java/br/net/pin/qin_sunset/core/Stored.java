package br.net.pin.qin_sunset.core;

import org.apache.commons.dbcp2.BasicDataSource;
import br.com.pointel.jarch.data.Helper;

public class Stored {

    public final Helper helper;
    public final BasicDataSource source;

    public Stored(Helper helper, BasicDataSource source) {
        this.helper = helper;
        this.source = source;
    }

}
