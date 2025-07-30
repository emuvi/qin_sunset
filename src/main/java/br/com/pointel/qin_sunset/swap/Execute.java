package br.com.pointel.qin_sunset.swap;

import java.util.List;
import com.google.gson.Gson;

public class Execute {
    public String name;
    public List<String> args;
    public List<String> inputs;
    public Boolean joinErrs;
    public Integer logLevel;

    public Execute(String name) {
        this(name, null, null, null, null);
    }

    public Execute(String name, List<String> args) {
        this(name, args, null, null, null);
    }

    public Execute(String name, List<String> args, List<String> inputs) {
        this(name, args, inputs, null, null);
    }

    public Execute(String name, List<String> args, List<String> inputs, Boolean joinErrs) {
        this(name, args, inputs, joinErrs, null);
    }

    public Execute(String name, List<String> args, List<String> inputs, Boolean joinErrs, Integer logLevel) {
        this.name = name;
        this.args = args;
        this.inputs = inputs;
        this.joinErrs = joinErrs;
        this.logLevel = logLevel;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static Execute fromString(String json) {
        return new Gson().fromJson(json, Execute.class);
    }
}
