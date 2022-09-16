import Domein.Reiziger;
import Domein.ReizigerDAO;
import Domein.ReizigerDAOPsql;

import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection;
    public static void main(String[] args) {
        try {
            ReizigerDAOPsql reizigerDAOPsql = new ReizigerDAOPsql(getConnection());
            testReizigerDAO(reizigerDAOPsql);
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
        rdao.save(a);
        rdao.save(b);

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
}
