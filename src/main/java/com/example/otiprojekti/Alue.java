package com.example.otiprojekti;

import java.util.Objects;

public class Alue {
    private int alueID;
    private String alueenNimi;

    //alustaja

    public Alue(int id, String nimi) { // TEMP
        this.alueID = id;
        this.alueenNimi = nimi;
    }

    public int getAlueID() {
        return alueID;
    }

    public void setAlueID(int alueID) {
        this.alueID = alueID;
    }

    public String getAlueenNimi() {
        return alueenNimi;
    }

    public void setAlueenNimi(String alueenNimi) {
        this.alueenNimi = alueenNimi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alue alue = (Alue) o;
        return getAlueID() == alue.getAlueID() &&
                Objects.equals(getAlueenNimi(), alue.getAlueenNimi());
    }


    @Override
    public String toString() {
        String str = "Alueen ID on " + alueID + "\n Alueen nimi on " + alueenNimi;
        return str;
    }


}
