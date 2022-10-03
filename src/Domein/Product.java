package Domein;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<OVChipkaart> ovChipkaartList = new ArrayList<>();

    public Product(int product_nummer, String naam, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public String getNaam() {
        return naam;
    }

    public double getPrijs() {
        return prijs;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setOvChipkaartList(List<OVChipkaart> ovChipkaartList) {
        this.ovChipkaartList = ovChipkaartList;
    }

    public List<OVChipkaart> getOvChipkaartList() {
        return ovChipkaartList;
    }
    public void voegOVChipkaartToe(OVChipkaart ovChipkaart) {
        if (!ovChipkaartList.contains(ovChipkaart)) {
            ovChipkaartList.add(ovChipkaart);
            if (!ovChipkaart.getProductList().contains(this)){
                ovChipkaart.voegProductToe(this);
            }
        }
    }
    public void verwijderOVChipkaart(OVChipkaart ovChipkaart) {
        ovChipkaartList.remove(ovChipkaart);
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String toString() {
        if (!ovChipkaartList.isEmpty()) {
            String s = "";
            for (OVChipkaart ovChipkaart : ovChipkaartList) {
                s += "         " + ovChipkaart;
//                System.out.println(ovChipkaart);
//                System.out.println("a");
            }
//            System.out.println("hoi");
            return "Product{" +
                    "product_nummer=" + product_nummer +
                    ", naam='" + naam + '\'' +
                    ", beschrijving='" + beschrijving + '\'' +
                    ", prijs=" + prijs +
                    '}' + s;
        } else {
            return "Product{" +
                    "product_nummer=" + product_nummer +
                    ", naam='" + naam + '\'' +
                    ", beschrijving='" + beschrijving + '\'' +
                    ", prijs=" + prijs +
                    '}';
        }
    }

    public void updateOVChipkaart(OVChipkaart ovChipkaart) {
        for (OVChipkaart ov : ovChipkaartList) {
            if (!ov.equals(ovChipkaart)) {
                ov.setKlasse(ovChipkaart.getKlasse());
                ov.setSaldo(ovChipkaart.getSaldo());
                ov.setReizigerId(ovChipkaart.getReizigerId());
                ov.setGeldigTot(ovChipkaart.getGeldigTot());
            }
        }
    }
}
