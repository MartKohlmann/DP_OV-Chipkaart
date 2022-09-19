import Domein.*;

import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection;
    public static void main(String[] args) {
        try {
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(getConnection());
            testReizigerDAO(reizigerDAOPsql);
            AdresDAOPsql adresDAOPsql = new AdresDAOPsql(getConnection());
            testAdresDAO(adresDAOPsql);
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

        // Haal alle reizigers op uit de database
        List<Adres> adresList = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adresList) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
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
        System.out.println(adao.findAll());
        System.out.println(adao.findById(78));
    }
}
