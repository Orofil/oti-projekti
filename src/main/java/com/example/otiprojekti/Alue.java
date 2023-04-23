package com.example.otiprojekti;

public class Alue {
    private int alueID;
    private String alueenNimi;

    //alustaja
    public Alue() {}

    public Alue(int id, String nimi) {
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
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
