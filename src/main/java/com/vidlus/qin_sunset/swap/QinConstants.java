package com.vidlus.qin_sunset.swap;

public enum QinConstants {

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

}
