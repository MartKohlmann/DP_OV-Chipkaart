package Domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReizigerDAOPsql implements ReizigerDAO{
    private Connection connection;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection conn) {
        this.connection = conn;
        this.adao = new AdresDAOPsql(conn);
    }
    public boolean save(Reiziger reiziger){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select reiziger_id from reiziger");
//            ResultSet myRss = myStmt.executeQuery("select * from adres");
            while (myRs.next()) {
//                if (Integer.parseInt(myRs.getString("reiziger_id")) ==  Integer.parseInt(myRss.getString("reiziger_id"))){
//                    reiziger.setAdres()
//                }
                Adres adres = adao.findByReiziger(Integer.parseInt(myRs.getString("reiziger_id")));
                reiziger.setAdres(adres);
                    if (Integer.parseInt(myRs.getString("reiziger_id")) == reiziger.getId()) {
                    return false;
                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, reiziger.getId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, reiziger.getGeboortedatum());
            preparedStatement.executeUpdate();
            myRs.close();
            myStmt.close();
            preparedStatement.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    };
    public boolean update(Reiziger reiziger){
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?");
            st.setString(1, reiziger.getVoorletters());
            st.setString(2, reiziger.getTussenvoegsel());
            st.setString(3, reiziger.getAchternaam());
            st.setDate(4, reiziger.getGeboortedatum());
            st.setInt(5, reiziger.getId());
            Adres adres = adao.findByReiziger(reiziger.getId());
            reiziger.setAdres(adres);
            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
    };
    public boolean delete(Reiziger reiziger){
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
            st.setInt(1, reiziger.getId());
            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
    };
    public Reiziger findById(int id) {
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from reiziger");
            while (myRs.next()) {
                if (Objects.equals(myRs.getString("reiziger_id"), String.valueOf(id))) {
                    String tussenvoegsels = myRs.getString("tussenvoegsel");
                    if (tussenvoegsels == null) {
                        tussenvoegsels = "";
                    }
                    Reiziger reiziger = new Reiziger(Integer.parseInt(myRs.getString("reiziger_id")), myRs.getString("voorletters"), myRs.getString("tussenvoegsel"), myRs.getString("achternaam"), Date.valueOf(myRs.getString("geboortedatum")));
                    Adres adres = adao.findByReiziger(Integer.parseInt(myRs.getString("reiziger_id")));
                    reiziger.setAdres(adres);
                    return reiziger;
                }
            }
            myRs.close();
            myStmt.close();
        } catch (Exception e){
                e.printStackTrace();
            }
        return null;
        };
    public List<Reiziger> findByGbdatum(String datum){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from reiziger");

            Date date = java.sql.Date.valueOf(datum);
            List<Reiziger> reizigers = new ArrayList<>();
            while (myRs.next()) {
                if (myRs.getDate("geboortedatum").equals(date)) {
                    Reiziger reiziger = new Reiziger(Integer.parseInt(myRs.getString("reiziger_id")), myRs.getString("voorletters"), myRs.getString("tussenvoegsel"), myRs.getString("achternaam"), Date.valueOf(myRs.getString("geboortedatum")));
                    Adres adres = adao.findByReiziger(Integer.parseInt(myRs.getString("reiziger_id")));
                    reiziger.setAdres(adres);
                    reizigers.add(reiziger);
                }
            }
            myRs.close();
            myStmt.close();
            return reizigers;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    };
    public List<Reiziger> findAll(){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from reiziger");
            List<Reiziger> reizigers = new ArrayList<>();
            while (myRs.next()) {
                String tussenvoegsels = myRs.getString("tussenvoegsel");
                if (tussenvoegsels == null) {
                    tussenvoegsels = "";
                }
                Reiziger reiziger = new Reiziger(Integer.parseInt(myRs.getString("reiziger_id")), myRs.getString("voorletters"), myRs.getString("tussenvoegsel"), myRs.getString("achternaam"), Date.valueOf(myRs.getString("geboortedatum")));
                Adres adres = adao.findByReiziger(Integer.parseInt(myRs.getString("reiziger_id")));
                reiziger.setAdres(adres);
                reizigers.add(reiziger);
            }
            myRs.close();
            myStmt.close();
            return reizigers;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    };
}
