import Domein.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection;
    public static void main(String[] args) {
        try {
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(getConnection());
            AdresDAOPsql adresDAOPsql = new AdresDAOPsql(getConnection());
            OVChipkaartDAOPsql ovChipkaartDAOPsql = new OVChipkaartDAOPsql(getConnection());
            ProductDAOPsql productDAOPsql = new ProductDAOPsql(getConnection());
            ovChipkaartDAOPsql.setRdao(reizigerDAOPsql);
            ovChipkaartDAOPsql.setPdao(productDAOPsql);
            reizigerDAOPsql.setOvdao(ovChipkaartDAOPsql);
            reizigerDAOPsql.setAdao(adresDAOPsql);
            adresDAOPsql.setRdao(reizigerDAOPsql);
            productDAOPsql.setOvdao(ovChipkaartDAOPsql);
            testReizigerDAO(reizigerDAOPsql);
            testAdresDAO(adresDAOPsql);
            testOVChipkaartDAO(ovChipkaartDAOPsql);
            testProductDAO(productDAOPsql, adresDAOPsql, ovChipkaartDAOPsql, reizigerDAOPsql);
            closeConnection(getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static Connection getConnection() throws SQLException {
        if (connection == null) {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "postgres", "MartKohlmann");
            connection = conn;
        }
        return connection;
    }
    private static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        Reiziger a = new Reiziger(17, "B", "", "Giet", java.sql.Date.valueOf(gbdatum));
        Reiziger b = new Reiziger(21, "G", "den", "Bloem", java.sql.Date.valueOf(gbdatum));
        Reiziger c = new Reiziger(22, "D", "den", "Gort", java.sql.Date.valueOf(gbdatum));
        rdao.save(a);
        rdao.save(b);
        rdao.save(c);

        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
        Reiziger s = new Reiziger(77, "S", "van der", "Boer", java.sql.Date.valueOf(gbdatum));
        rdao.update(s);

        rdao.delete(b);
        System.out.println(rdao.findByGbdatum("1981-03-14"));
        System.out.println(rdao.findAll());
        System.out.println(rdao.findById(3));
    }
    private static void testAdresDAO(AdresDAOPsql adao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        List<Adres> adresList = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adresList) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw adres aan en persisteer deze in de database
        Adres a2 = new Adres(77, "1234GB", "23", "Jan de Lange Laan", "Gelderen", 17);
        Adres a3 = new Adres(78, "2342EX", "1", "Jan de Hoge Laan", "Dongen", 22);
        Adres a4 = new Adres(79, "1231EG", "16", "Jan de Korte Laan", "Frederin", 77);


        adao.save(a2);
        adao.save(a4);

        System.out.print("[Test] Eerst " + adresList.size() + " adresList, na AdresDAO.save() ");
        adao.save(a3);
        adresList = adao.findAll();
        System.out.println(adresList.size() + " adresList\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
        Adres a5 = new Adres(79, "1231EG", "18", "Jan de Korte Laan", "Frederin", 77);
        adao.update(a5);

        adao.delete(a2);
        System.out.println(adao.findByReiziger(22));
        System.out.println();
        System.out.println(adao.findById(78));
        System.out.println();
        for (Adres a : adao.findAll()){
            System.out.println( a.toString());
        }
    }
    private static void testOVChipkaartDAO(OVChipkaartDAOPsql ovChipkaartDAOPsql) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Haal alle ovchipkaarten op uit de database
        List<OVChipkaart> ovChipkaarten = ovChipkaartDAOPsql.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende chipkaarten:");
        for (OVChipkaart a : ovChipkaarten) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw ovchipkaart aan en persisteer deze in de database
        OVChipkaart ov1 = new OVChipkaart(12345, java.sql.Date.valueOf("2022-08-09"), 2, 30.00, 5);
        ovChipkaartDAOPsql.save(ov1);
        OVChipkaart ov2 = new OVChipkaart(54321, java.sql.Date.valueOf("2022-08-10"), 1, 15.00, 4);
        OVChipkaart ov3 = new OVChipkaart(69693, java.sql.Date.valueOf("2022-07-09"), 1, 35.00, 3);
        Product a2 = new Product(8, "Studentenkaart", "Door de weeks gratis reizen", 25.00);

        ov3.voegProductToe(a2);
        ovChipkaartDAOPsql.save(ov3);
        System.out.print("[Test] Eerst " + ovChipkaarten.size() + " ovchipkaarten, na OVChipkaart.save() ");
        ovChipkaartDAOPsql.save(ov2);
        ovChipkaarten = ovChipkaartDAOPsql.findAll();
        System.out.println(ovChipkaarten.size() + " ovchipkaarten\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
        OVChipkaart ov4 = new OVChipkaart(69693, java.sql.Date.valueOf("2022-07-09"), 1, 50.00, 3);
        ovChipkaartDAOPsql.update(ov4);

        ovChipkaartDAOPsql.delete(ov2);
        System.out.println(ovChipkaartDAOPsql.findById(69693));
        System.out.println();
        System.out.println();
        for (OVChipkaart a : ovChipkaartDAOPsql.findAll()){
            System.out.println( a.toString());
        }
    }
    private static void testProductDAO(ProductDAOPsql productDAOPsql, AdresDAOPsql adao, OVChipkaartDAOPsql ovdao, ReizigerDAOPsql rdao) throws SQLException {
        System.out.println("\n---------- Test ProductDAO -------------");

        // Haal alle producten op uit de database
        List<Product> productList = productDAOPsql.findAll();
        System.out.println("[Test] ProductDAOPsql.findAll() geeft de volgende producten:");
        for (Product a : productList) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw product aan en persisteer deze in de database
        Product a2 = new Product(8, "Studentenkaart", "Door de weeks gratis reizen", 35.00);
        Product a3 = new Product(9, "Weekendkaart", "Weekend kaart reizen", 27.00);
        Product a4 = new Product(10, "Weekkaart", "Week kaart reizen", 30.00);
        Product a5 = new Product(11, "Dagkaart", "Dag kaart reizen", 10.00);
        OVChipkaart ov3 = new OVChipkaart(69696, java.sql.Date.valueOf("2022-07-08"), 1, 50.00, 99);
        OVChipkaart ov4 = new OVChipkaart(112233, java.sql.Date.valueOf("2021-06-08"), 1, 70.00, 99);

        String gbdatum = "1981-03-14";
        Reiziger a1 = new Reiziger(99, "B", "", "GIETS", java.sql.Date.valueOf(gbdatum));
//        Reiziger a11 = new Reiziger(100, "B", "", "GIETSers", java.sql.Date.valueOf(gbdatum));
        Adres a10 = new Adres(80, "1294GZ", "19", "Jan de KORTE Laan", "UTRECHTEN", 99);
//        Adres a12 = new Adres(81, "1290KZ", "12", "Jan de KLEINE Laan", "UTRECHTEN", 100);
        List<OVChipkaart> ovChipkaartList = new ArrayList<>();
        ovChipkaartList.add(ov3);
        ovChipkaartList.add(ov4);
        a1.setOvChipkaarten(ovChipkaartList);
        a1.setAdres(a10);
        a10.setReiziger(a1);
        ov3.setReiziger(a1);
        ov4.setReiziger(a1);
        rdao.save(a1);
        adao.save(a10);
        ovdao.save(ov3);
        ovdao.save(ov4);

        productDAOPsql.save(a2);
        productDAOPsql.save(a4);
        productDAOPsql.save(a5);
        System.out.println("Product 8 voor het toevoegen van een chipkaart: ");
        System.out.println(a2);
        a2.voegOVChipkaartToe(ov3);
        System.out.println();
        System.out.println("Product 8 na het toevoegen van een chipkaart: ");
        System.out.println(a2);
        productDAOPsql.update(a2);
        System.out.println("Alle producten na het updaten van product 8 (product 8 heeft nu een chipkaart)");
        for (Product p : productDAOPsql.findAll()) {
            System.out.println(p);
        }
        System.out.println();
        System.out.println("Alle producten van chipkaart 35283: ");
        System.out.println(productDAOPsql.findByOVChipkaart(ovdao.findById(35283)));
        System.out.println();

        System.out.print("[Test] Eerst " + productList.size() + " producten, na ProductDAO.save() ");
        productDAOPsql.save(a3);
        productList = productDAOPsql.findAll();
        System.out.println(productList.size() + " producten\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.

        System.out.println("Product 8 voor het updaten van de prijs: ");
        System.out.println(a2);
//        Product a7 = new Product(8, "Studentenkaart", "Door de weeks gratis reizen", 25.00);
        a2.setPrijs(25.00);
        productDAOPsql.update(a2);
        System.out.println("Alle producten na het updaten van product 8 (product 8 heeft nu een andere prijs)");
        for (Product p : productDAOPsql.findAll()) {
            System.out.println(p);
        }
        System.out.println("Product 8 voor het toevoegen van 1 extra chipkaart");
        System.out.println(a2);
        System.out.println();
        System.out.println("Product 8 na het toevoegen van 1 extra chipkaart");

        a2.voegOVChipkaartToe(ov4);
        System.out.println(a2);
        System.out.println();
        productDAOPsql.update(a2);
        System.out.println("Alle producten na het updaten van product 8 (product 8 heeft nu 2 chipkaarten)");
        for (Product p : productDAOPsql.findAll()) {
            System.out.println(p);
        }
        System.out.println();
        System.out.println("ALle producten na het verwijderen van een chipkaart");
//        Product a20 = new Product(8, "Studentenkaart", "Door de weeks gratis reizen", 35.00);
//        a20.voegOVChipkaartToe(ov3);
//        productDAOPsql.update(a20);
        a2.verwijderOVChipkaart(ov3);
        productDAOPsql.update(a2);
        for (Product ppp : productDAOPsql.findAll()) {
            System.out.println(ppp);
        }
        System.out.println();
        System.out.println("Alle producten voor het verwijderen van product 8: ");
        for (Product p : productDAOPsql.findAll()) {
            System.out.println(p);
        }
        System.out.println();
        System.out.println("Alle producten na het verwijderen van product 8: ");
        productDAOPsql.delete(a2);
        for (Product p : productDAOPsql.findAll()) {
            System.out.println(p);
        }
    }
}
