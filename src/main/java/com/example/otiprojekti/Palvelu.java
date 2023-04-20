package com.example.otiprojekti;

public class Palvelu {
    private int palveluID;
    private int alueID;
    private String palvelunNimi;
    private String palvelunTyyppi;
    private String palvelunKuvaus;
    private double palvelunHinta;
    private double palvelunAlv;

    //Alustaja perustiedoille
    public Palvelu(int palveluID, int alueID, String palvelunNimi,String palvelunTyyppi, String palvelunKuvaus,
                   double palvelunHinta, double palvelunAlv){
        this.palveluID=palveluID;
        this.alueID=alueID;
        this.palvelunNimi=palvelunNimi;
        this.palvelunTyyppi=palvelunTyyppi;
        this.palvelunKuvaus=palvelunKuvaus;
        this.palvelunHinta=palvelunHinta;
        this.palvelunAlv=palvelunAlv;
    }
    //Getterit palveluidlle ja alueidlle

    public int getPalveluID() {
        return palveluID;
    }

    public int getAlueID() {
        return alueID;
    }
    //Luodaan getterit ja setterit muille kentille

    public String getPalvelunNimi() {
        return palvelunNimi;
    }

    public void setPalvelunNimi(String palvelunNimi) {
        this.palvelunNimi = palvelunNimi;
    }

    public String getPalvelunTyyppi() {
        return palvelunTyyppi;
    }

    public void setPalvelunTyyppi(String palvelunTyyppi) {
        this.palvelunTyyppi = palvelunTyyppi;
    }

    public String getPalvelunKuvaus() {
        return palvelunKuvaus;
    }

    public void setPalvelunKuvaus(String palvelunKuvaus) {
        this.palvelunKuvaus = palvelunKuvaus;
    }

    public double getPalvelunHinta() {
        return palvelunHinta;
    }

    public void setPalvelunHinta(double palvelunHinta) {
        this.palvelunHinta = palvelunHinta;
    }

    public double getPalvelunAlv() {
        return palvelunAlv;
    }

    public void setPalvelunAlv(double palvelunAlv) {
        this.palvelunAlv = palvelunAlv;
    }

    //Tostring metodi palvelun tiedoille
    //Tostring metodi
    public String toString(){
        String str= "Palvelun ID on: " + palveluID + "\nAlueen ID: "+ alueID+ "\nPalvelun nimi: "+ palvelunNimi+
                "\nPalvelun tyyppi: "+ palvelunTyyppi+"\nPalvelun kuvaus: "+ palvelunKuvaus+ "\nPalvelun hinta: "
                + palvelunHinta + "\nPalvelun Alv: "+ palvelunAlv;
        return str;

    }
    //Equals metodi luokalle mökkien vertailemista varten
    public boolean equals(Palvelu palvelu1){
        if ((palveluID==palvelu1.palveluID)&&(alueID==palvelu1.alueID)&&(palvelunNimi==palvelu1.palvelunNimi)&&
                (palvelunTyyppi==palvelu1.palvelunTyyppi)&&(palvelunKuvaus==palvelu1.palvelunKuvaus)&&
                (palvelunHinta==palvelu1.palvelunHinta) &&(palvelunAlv==palvelu1.palvelunAlv)){
            return true;
        }
        else {
            return false;
        }
    }
}
