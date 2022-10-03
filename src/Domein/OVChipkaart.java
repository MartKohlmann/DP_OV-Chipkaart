package Domein;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OVChipkaart {
    private int kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private int reizigerId;
    private Reiziger reiziger;
    private List<Product> productList = new ArrayList<>();

    public OVChipkaart(){}
    public OVChipkaart(int kaartnummer, Date geldigTot, int klasse, double saldo, int reizigerId) {
        this.kaartnummer = kaartnummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reizigerId = reizigerId;
    }

    public int getReizigerId() {
        return reizigerId;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public double getSaldo() {
        return saldo;
    }

    public int getKaartnummer() {
        return kaartnummer;
    }

    public int getKlasse() {
        return klasse;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public List<Product> getProductList() {
        return productList;
    }
    public void voegProductToe(Product product) {
        if (!productList.contains(product)) {
            productList.add(product);
            if (!product.getOvChipkaartList().contains(this)) {
                product.voegOVChipkaartToe(this);
            }
        }
    }
    public void verwijderProduct(Product product) {
        productList.remove(product);
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaartnummer=" + kaartnummer +
                ", geldigTot=" + geldigTot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", reizigerId=" + reizigerId +
                ", reiziger=" + reiziger +
                '}';
    }

    public void setKaartnummer(int kaartnummer) {
        this.kaartnummer = kaartnummer;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public void setReizigerId(int reizigerId) {
        this.reizigerId = reizigerId;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void updateProduct(Product product) {
        for (Product p : productList) {
            if (!p.equals(product)) {
                p.setNaam(product.getNaam());
                p.setBeschrijving(product.getBeschrijving());
                p.setPrijs(product.getPrijs());
            }
        }
    }
}
