package br.com.pointel.qin_sunset.swap;

import br.com.pointel.jarch.data.Data;

public enum PathKind implements Data {

    Folder, File;

    @Override
    public String toString() {
        return this.toChars();
    }

    public static PathKind fromChars(String chars) {
        return Data.fromChars(chars, PathKind.class);
    }

}
