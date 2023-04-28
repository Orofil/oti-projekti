package com.example.otiprojekti;

import java.time.LocalDateTime;
import java.util.Objects;

public class Varaus {
    private int varausID;
    private int asiakasID;
    private int mokkiID;

    private LocalDateTime varattuPvm;
    private LocalDateTime vahvistusPvm;
    private LocalDateTime varausAlkuPvm; // TODO nämä on vähän eri tavalla nimetty kuin tietokannassa, muutetaanko vastaamaan sitä?
    private LocalDateTime varausLoppuPvm;

    public Varaus(int varausID,
                  int asiakasID,
                  int mokkiID,
                  LocalDateTime varattuPvm,
                  LocalDateTime vahvistusPvm,
                  LocalDateTime varausAlkuPvm,
                  LocalDateTime varausLoppuPvm) {
        this.varausID = varausID;
        this.asiakasID = asiakasID;
        this.mokkiID = mokkiID;

        this.varattuPvm = varattuPvm;
        this.vahvistusPvm = vahvistusPvm;
        this.varausAlkuPvm = varausAlkuPvm;
        this.varausLoppuPvm = varausLoppuPvm;
    }

    public int getVarausID() {
        return varausID;
    }

    public int getAsiakasID() {
        return asiakasID;
    }

    public void setAsiakasID(int asiakasID) {
        this.asiakasID = asiakasID;
    }

    public int getMokkiID() {
        return mokkiID;
    }

    public void setMokkiID(int mokkiID) {
        this.mokkiID = mokkiID;
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
                ", mokkiID=" + mokkiID +
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
                asiakasID == varaus.asiakasID &&
                mokkiID == varaus.mokkiID &&
                Objects.equals(varattuPvm, varaus.varattuPvm) &&
                Objects.equals(vahvistusPvm, varaus.vahvistusPvm) &&
                Objects.equals(varausAlkuPvm, varaus.varausAlkuPvm) &&
                Objects.equals(varausLoppuPvm, varaus.varausLoppuPvm);
    }
}
