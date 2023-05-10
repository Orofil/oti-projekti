package com.example.otiprojekti;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Varaus {
    private final int ID;
    private Asiakas asiakas;
    private Mokki mokki;
    /**
     * Avain on {@link Palvelu}, arvo on palvelun lukumäärä.
     */
    private HashMap<Palvelu, Integer> palvelut;

    private LocalDateTime varattuPvm;
    private LocalDateTime vahvistusPvm;
    private LocalDateTime varausAlkuPvm;
    private LocalDateTime varausLoppuPvm;

    public Varaus(int ID,
                  Asiakas asiakas,
                  Mokki mokki,
                  HashMap<Palvelu, Integer> palvelut,
                  LocalDateTime varattuPvm,
                  LocalDateTime vahvistusPvm,
                  LocalDateTime varausAlkuPvm,
                  LocalDateTime varausLoppuPvm) {
        this.ID = ID;
        this.asiakas = asiakas;
        this.mokki = mokki;
        this.palvelut = palvelut;

        this.varattuPvm = varattuPvm;
        this.vahvistusPvm = vahvistusPvm;
        this.varausAlkuPvm = varausAlkuPvm;
        this.varausLoppuPvm = varausLoppuPvm;
    }

    public int getID() {
        return ID;
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

    public HashMap<Palvelu, Integer> getPalvelut() {
        return palvelut;
    }

    public ArrayList<Palvelu> getPalvelutArrayListina() {
        HashMap<Palvelu, Integer> map
                = getPalvelut();
        Set<Palvelu> keySet = map.keySet();
        ArrayList<Palvelu> listOfKeys
                = new ArrayList<Palvelu>(keySet);
        return listOfKeys;
    }

    public ArrayList<Integer> getPalvelunLkmArrayListina() {
        HashMap<Palvelu, Integer> map
                = getPalvelut();
        Collection<Integer> values = map.values();
        ArrayList<Integer> listOfValues
                = new ArrayList<>(values);
        return listOfValues;
    }

    public void setPalvelut(HashMap<Palvelu, Integer> palvelut) {
        this.palvelut = palvelut;
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

    /**
     * Onko tietty mökki varattu tietyllä välillä tässä varauksessa.
     * @param m Mökki
     * @param paivaAlku Varauksen alkupäivä
     * @param paivaLoppu Varauksen loppupäivä
     * @return Onko mökki varattu jonain päivänä tällä välillä
     */
    public boolean varattuValilla(Mokki m, LocalDate paivaAlku, LocalDate paivaLoppu) {
        return getMokki().equals(m) &&
                !(getVarausLoppuPvm().isBefore(paivaAlku.atStartOfDay()) ||
                getVarausAlkuPvm().isAfter(paivaLoppu.plusDays(1).atStartOfDay()));
    }

    @Override
    public String toString() {
        return "Varaus:" +
                "\nvarausID=" + ID +
                "\nmokki=" + mokki +
                "\npalvelut=" + palvelut +
                "\nvarattuPvm=" + varattuPvm +
                "\nvarausAlkuPvm=" + varausAlkuPvm +
                "\nvarausLoppuPvm=" + varausLoppuPvm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Varaus varaus = (Varaus) o;
        return ID == varaus.ID &&
                asiakas == varaus.asiakas &&
                mokki == varaus.mokki &&
                palvelut.equals(varaus.palvelut) &&
                Objects.equals(varattuPvm, varaus.varattuPvm) &&
                Objects.equals(vahvistusPvm, varaus.vahvistusPvm) &&
                Objects.equals(varausAlkuPvm, varaus.varausAlkuPvm) &&
                Objects.equals(varausLoppuPvm, varaus.varausLoppuPvm);
    }
}
