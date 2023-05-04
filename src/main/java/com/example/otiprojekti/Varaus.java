package com.example.otiprojekti;

import java.time.LocalDateTime;
import java.util.Objects;

public class Varaus {
    private final int varausID;
    private Asiakas asiakas;
    private Mokki mokki;

    private LocalDateTime varattuPvm;
    private LocalDateTime vahvistusPvm;
    private LocalDateTime varausAlkuPvm; // TODO nämä on vähän eri tavalla nimetty kuin tietokannassa, muutetaanko vastaamaan sitä?
    private LocalDateTime varausLoppuPvm;

    public Varaus(int varausID,
                  Asiakas asiakas,
                  Mokki mokki,
                  LocalDateTime varattuPvm,
                  LocalDateTime vahvistusPvm,
                  LocalDateTime varausAlkuPvm,
                  LocalDateTime varausLoppuPvm) {
        this.varausID = varausID;
        this.asiakas = asiakas;
        this.mokki = mokki;

        this.varattuPvm = varattuPvm;
        this.vahvistusPvm = vahvistusPvm;
        this.varausAlkuPvm = varausAlkuPvm;
        this.varausLoppuPvm = varausLoppuPvm;
    }

    public int getVarausID() {
        return varausID;
    }

    public Asiakas getAsiakas() {
        return asiakas;
    }

    public void setAsiakas(Asiakas asiakas) {
        this.asiakas = asiakas;
    }

    public Mokki getMokki() {
        return mokki;
    }

    public void setMokki(Mokki mokki) {
        this.mokki = mokki;
    }

    public LocalDateTime getVarattuPvm() {
        return varattuPvm;
    }

    public void setVarattuPvm(LocalDateTime varattuPvm) {
        this.varattuPvm = varattuPvm;
    }

    public LocalDateTime getVahvistusPvm() {
        return vahvistusPvm;
    }

    public void setVahvistusPvm(LocalDateTime vahvistusPvm) {
        this.vahvistusPvm = vahvistusPvm;
    }

    public LocalDateTime getVarausAlkuPvm() {
        return varausAlkuPvm;
    }

    public void setVarausAlkuPvm(LocalDateTime varausAlkuPvm) {
        this.varausAlkuPvm = varausAlkuPvm;
    }

    public LocalDateTime getVarausLoppuPvm() {
        return varausLoppuPvm;
    }

    public void setVarausLoppuPvm(LocalDateTime varausLoppuPvm) {
        this.varausLoppuPvm = varausLoppuPvm;
    }

    @Override
    public String toString() {
        return "Varaus[" +
                "varausID=" + varausID +
                ", mokkiID=" + mokki +
                ", varattuPvm=" + varattuPvm +
                ", varausAlkuPvm=" + varausAlkuPvm +
                ", varausLoppuPvm=" + varausLoppuPvm +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Varaus varaus = (Varaus) o;
        return varausID == varaus.varausID &&
                asiakas == varaus.asiakas &&
                mokki == varaus.mokki &&
                Objects.equals(varattuPvm, varaus.varattuPvm) &&
                Objects.equals(vahvistusPvm, varaus.vahvistusPvm) &&
                Objects.equals(varausAlkuPvm, varaus.varausAlkuPvm) &&
                Objects.equals(varausLoppuPvm, varaus.varausLoppuPvm);
    }
}
