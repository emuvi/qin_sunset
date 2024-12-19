package br.com.pointel.qin_sunset.core;

import java.util.Objects;
import com.google.gson.Gson;
import br.com.pointel.jarch.data.Registry;
import br.com.pointel.jarch.data.Strain;

public class AllowReg {

    public Registry registry;
    public Boolean all;
    public Boolean insert;
    public Boolean select;
    public Boolean update;
    public Boolean delete;
    public Strain strain;

    public AllowReg() {}

    public AllowReg(Registry registry, Boolean all, Boolean insert, Boolean select, Boolean update,
                    Boolean delete, Strain strain) {
        this.registry = registry;
        this.all = all;
        this.insert = insert;
        this.select = select;
        this.update = update;
        this.delete = delete;
        this.strain = strain;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AllowReg)) {
            return false;
        }
        AllowReg allowReg = (AllowReg) o;
        return Objects.equals(registry, allowReg.registry)
                        && Objects.equals(all, allowReg.all)
                        && Objects.equals(insert, allowReg.insert)
                        && Objects.equals(select, allowReg.select)
                        && Objects.equals(update, allowReg.update)
                        && Objects.equals(delete, allowReg.delete)
                        && Objects.equals(strain, allowReg.strain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registry, all, insert, select, update, delete, strain);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static AllowReg fromString(String json) {
        return new Gson().fromJson(json, AllowReg.class);
    }

}
