package Domein;

import java.sql.ResultSet;
import java.sql.Date;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;

    public Reiziger(){

    }

    public Reiziger(int reiziger_id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.achternaam = achternaam;
        this.id = reiziger_id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
//        String[] a = geboortedatum.split("-");
//        int jaar = Integer.parseInt(a[0]);
//        int maand = Integer.parseInt(a[1]);
//        int dag = Integer.parseInt(a[2]);
//        this.geboortedatum = new Date(jaar, maand, dag);
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }
    public String getNaam(){
        if (tussenvoegsel == null) {
            tussenvoegsel = "";
        }
        return voorletters + " " + tussenvoegsel + " " + achternaam;
    }

    public String toString() {
        if (tussenvoegsel == null) {
            this.tussenvoegsel = "";
        }
        return "        #" +  id + ": " + voorletters + ". " + tussenvoegsel + " "+ achternaam + " (" + geboortedatum + ")";
    }
}
