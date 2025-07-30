package br.com.pointel.qin_sunset.swap;

import com.google.gson.Gson;

public class Transfer {
    public String origin;
    public String destiny;

    public Transfer(String origin, String destiny) {
        this.origin = origin;
        this.destiny = destiny;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Transfer fromString(String json) {
        return new Gson().fromJson(json, Transfer.class);
    }
}
