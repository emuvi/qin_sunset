package br.com.pointel.qin_sunset.core;

import java.util.List;

import br.com.pointel.jarch.data.Data;

public class AllowCmd implements Data {

    public String name;
    public List<String> args;

    public AllowCmd() {
    }

    public AllowCmd(String name, List<String> args) {
        this.name = name;
        this.args = args;
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

    public static AllowCmd fromChars(String chars) {
        return Data.fromChars(chars, AllowCmd.class);
    }

}
