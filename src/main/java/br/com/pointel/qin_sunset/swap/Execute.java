package br.com.pointel.qin_sunset.swap;

import java.util.List;

import br.com.pointel.jarch.data.Data;

public class Execute implements Data {

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
    public boolean equals(Object that) {
        return this.deepEquals(that);
    }

    @Override
    public int hashCode() {
        return this.deepHash();
    }

    @Override
    public String toString() {
        return this.toChars();
    }

    public static Execute fromChars(String chars) {
        return Data.fromChars(chars, Execute.class);
    }

}
