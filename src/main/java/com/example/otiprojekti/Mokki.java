package com.example.otiprojekti;

public class Mokki {
    private int mokkiID;
    private int alueID;
    private int postiNro;
    private String mokkiNimi;
    private String katuosoite;
    private double hinta;
    private String kuvaus;
    private int hloMaara;
    private String varustelu;

    //Alustaja tiedoille
    public Mokki(int mokkiID,int alueID, int postiNro,String mokkiNimi, String katuosoite, double hinta, String kuvaus,
                 int hloMaara, String varustelu){
        this.mokkiID=mokkiID;
        this.alueID=alueID;
        this.postiNro=postiNro;
        this.mokkiNimi=mokkiNimi;
        this.katuosoite=katuosoite;
        this.hinta=hinta;
        this.kuvaus=kuvaus;
        this.hloMaara=hloMaara;
        this.varustelu=varustelu;
    }

    //TEMP
    public Mokki(int id, String nimi) {
        this.mokkiID = id;
        this.mokkiNimi = nimi;
    }

    //Palauttaa mökin ID:n
    public int getMokkiID() {
        return mokkiID;
    }

    //Palauttaa alueen ID:n
    public int getAlueID() {
        return alueID;
    }

    //Luodaan getterit ja setterit muille kentille
    public int getPostiNro() {
        return postiNro;
    }


    public void setPostiNro(int postiNro) {
        this.postiNro = postiNro;
    }

    public String getMokkiNimi() {
        return mokkiNimi;
    }

    public void setMokkiNimi(String mokkiNimi) {
        this.mokkiNimi = mokkiNimi;
    }

    public String getKatuosoite() {
        return katuosoite;
    }

    public void setKatuosoite(String katuosoite) {
        this.katuosoite = katuosoite;
    }

    public double getHinta() {
        return hinta;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public int getHloMaara() {
        return hloMaara;
    }

    public void setHloMaara(int hloMaara) {
        this.hloMaara = hloMaara;
    }

    public String getVarustelu() {
        return varustelu;
    }

    public void setVarustelu(String varustelu) {
        this.varustelu = varustelu;
    }

    //Tostring metodi
    public String toString(){
        String str= "Mökin ID on: " + mokkiID + "\nAlueen ID: "+ alueID+ "\nPostinro: "+ postiNro+"\nMökin nimi: "+
                mokkiNimi+"\nKatuosoite: "+ katuosoite+ "\nHinta: "+ hinta + "\nKuvaus: "+ kuvaus+ "\nHenkilömäärä: "+
                hloMaara+"\nVarustelu: "+ varustelu;
        return str;

    }
    //Equals metodi luokalle mökkien vertailemista varten
    public boolean equals(Mokki mokki1){
        if ((mokkiID==mokki1.mokkiID)&&(alueID==mokki1.alueID)&&(postiNro==mokki1.postiNro)&&
                (mokkiNimi==mokki1.mokkiNimi)&&(katuosoite==mokki1.katuosoite)&&(hinta==mokki1.hinta)
                &&(kuvaus==mokki1.kuvaus)&&(hloMaara==mokki1.hloMaara)&&(varustelu==mokki1.varustelu)){
            return true;
        }
        else {
            return false;
        }
    }
}
