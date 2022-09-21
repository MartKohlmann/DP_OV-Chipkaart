package Domein;

public class Adres {
    private int id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private int reizigerId;
    private Reiziger reiziger;

    public Adres(){}
    public Adres(int id, String postcode, String huisnummer, String straat, String woonplaats, int reizigerId) {
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reizigerId = reizigerId;
    }

    public int getId() {
        return id;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getStraat() {
        return straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setReiziger(Reiziger reiziger){
        this.reiziger = reiziger;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public String toString() {
        if (reiziger != null){
            String tussenvoegsel= reiziger.getTussenvoegsel();
            if (tussenvoegsel == null) {
                tussenvoegsel = "";
            }
            return "Reiziger        {#" +  reiziger.getId() + " " + reiziger.getVoorletters() + ". " + tussenvoegsel + " "+ reiziger.getAchternaam() + ", " + reiziger.getGeboortedatum() + ", Adres {#" + reizigerId + " " + id + " " + postcode + "}}";
        } else {
            return "Adres{" +
                    "id=" + id +
                    ", postcode='" + postcode + '\'' +
                    ", huisnummer='" + huisnummer + '\'' +
                    ", straat='" + straat + '\'' +
                    ", woonplaats='" + woonplaats + '\'' +
                    ", reizigerId=" + reizigerId +
                    '}';
        }
    }
}
