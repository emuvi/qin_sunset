package br.com.pointel.qin_sunset.swap;

import br.com.pointel.jarch.data.Data;

public enum QinConstants implements Data {

    DevTools("DevTools"),

    QinBases("QinBases"),

    QinBaseSelected("QinBaseSelected"),

    QinSetup("QinSetup"),

    LangPtBr("pt-BR");

    private final String value;

    private QinConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.toChars();
    }

    public static QinConstants fromChars(String chars) {
        return Data.fromChars(chars, QinConstants.class);
    }

}
