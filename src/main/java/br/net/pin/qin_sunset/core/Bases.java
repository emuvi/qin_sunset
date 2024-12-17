package br.net.pin.qin_sunset.core;

import java.util.ArrayList;
import com.google.gson.Gson;
import br.com.pointel.jarch.data.DataWays;
import br.com.pointel.jarch.mage.WizChars;

public class Bases extends ArrayList<DataWays> {

    public void fixDefaults() throws Exception {
        for (var way : this) {
            way.fixNullsAndEnvs();
        }
        this.removeIf(entry -> WizChars.isEmpty(entry.getName()));
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Bases fromString(String json) {
        return new Gson().fromJson(json, Bases.class);
    }

}
