package br.com.pointel.qin_sunset.swap;

public enum QinConstants {

    DEV_TOOLS("DevTools"),

    QIN_BASES("QinBases"),

    QIN_BASE_SELECTED("QinBaseSelected"),

    QIN_SETUP("QinSetup"),

    LANG_PT_BR("pt-br");

    private final String name;

    private QinConstants(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
