package com.example.otiprojekti;

import java.util.Objects;

public class Posti {
    private String postiNro;
    private String toimipaikka;

    public Posti(String postiNro, String toimipaikka) {
        this.postiNro = postiNro;
        this.toimipaikka = toimipaikka;
    }

    public String getPostiNro() {
        return postiNro;
    }

    public void setPostiNro(String postiNro) {
        this.postiNro = postiNro;
    }

    public String getToimipaikka() {
        return toimipaikka;
    }

    public void setToimipaikka(String toimipaikka) {
        this.toimipaikka = toimipaikka;
    }

    @Override
    public String toString() {
        return "Posti:" +
                "\npostiNro = " + postiNro +
                "\ntoimipaikka = " + toimipaikka;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posti posti = (Posti) o;
        return Objects.equals(postiNro, posti.postiNro) &&
                Objects.equals(toimipaikka, posti.toimipaikka);
    }
}
