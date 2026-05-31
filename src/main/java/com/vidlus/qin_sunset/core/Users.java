package com.vidlus.qin_sunset.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vidlus.jarch.data.Data;
import com.vidlus.jarch.data.DataListArray;

public class Users extends DataListArray<User> {

    private static Logger log = LoggerFactory.getLogger(Users.class);

    @Override
    public void fixDefaults() {
        var hasRoot = false;
        for (var user : this) {
            user.fixDefaults();
            if (user.name.equals("root")) {
                hasRoot = true;
            }
        }
        if (!hasRoot) {
            var root = new User();
            root.name = "root";
            root.pass = "14e32f44d229cdb580e90db646f87d78062b79d4";
            root.master = true;
            try {
                root.fixNulls();
            } catch (Exception e) {
                log.error("Error on fixing root user", e);
            }
            this.add(root);
        }
        this.removeIf(user -> user.name.isEmpty());
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

    public static Users fromChars(String chars) {
        return Data.fromChars(chars, Users.class);
    }

}
