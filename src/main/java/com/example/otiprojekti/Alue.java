package com.example.otiprojekti;

import java.util.Objects;

public class Alue {
    private final int ID;
    private String nimi;

    /**
     * @param ID
     * @param nimi Maksimipituus 40
     */
    public Alue(int ID, String nimi) {
        this.ID = ID;
        this.nimi = nimi;
    }

    public int getID() {
        return ID;
    }

    public String getNimi() {
        return nimi;
    }

    /**
     * @param nimi Maksimipituus 40
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alue alue = (Alue) o;
        return getID() == alue.getID() &&
                Objects.equals(getNimi(), alue.getNimi());
    }

    @Override
    public String toString() {
        String str = "Alueen ID on " + ID + "\n Alueen nimi on " + nimi;
        return str;
    }
}
