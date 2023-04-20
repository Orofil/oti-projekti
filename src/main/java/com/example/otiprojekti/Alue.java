package com.example.otiprojekti;

import java.util.Objects;

public class Alue {
    private int alueID;
    private String alueenNimi;

    //alustaja
    public Alue(int alueID, String alueenNimi) {}

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
        return super.toString();
    }


}
