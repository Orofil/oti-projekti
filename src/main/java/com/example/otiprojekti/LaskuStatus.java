package com.example.otiprojekti;

public enum LaskuStatus {
    EI_LAHETETTY(0, "Ei lähetetty"),
    LAHETETTY(1, "Lähetetty"),
    MAKSETTU(2, "Maksettu");

    public final int id;
    public final String teksti;

    LaskuStatus(int id, String teksti) {
        this.id = id;
        this.teksti = teksti;
    }

    @Override
    public String toString() {
        return teksti;
    }

    public static LaskuStatus valueOf(int id) {
        LaskuStatus status = null;
        switch (id) {
            case 0 -> status = EI_LAHETETTY;
            case 1 -> status = LAHETETTY;
            case 2 -> status = MAKSETTU;
        }
        return status;
    }
}
