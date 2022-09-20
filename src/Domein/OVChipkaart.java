package Domein;

import java.util.Date;

public class OVChipkaart {
    private int kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private int reizigerId;
    private Reiziger reiziger;

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
}
