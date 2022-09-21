package Domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReizigerDAOPsql implements ReizigerDAO{
    private Connection connection;
    private AdresDAO adao;
    private OVChipkaartDAO ovdao;

    public ReizigerDAOPsql(Connection conn) {
        this.connection = conn;
    }
    public boolean save(Reiziger reiziger){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select reiziger_id from reiziger");
//            ResultSet myRss = myStmt.executeQuery("select * from adres");
//            while (myRs.next()) {
////                if (Integer.parseInt(myRs.getString("reiziger_id")) ==  Integer.parseInt(myRss.getString("reiziger_id"))){
////                    reiziger.setAdres()
////                }
////                Adres adres = adao.findByReiziger(reiziger.getId());
////                reiziger.setAdres(adres);
//
//            }
            while (myRs.next()) {
                if (reiziger.getId() == myRs.getInt("reiziger_id")) {
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
            if (reiziger.getAdres() != null) {
                adao.save(reiziger.getAdres());
            }
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
            if (reiziger.getAdres() != null) {
                adao.update(reiziger.getAdres());
            }
//            adao.update(reiziger.getAdres());
//            reiziger.setAdres(reiziger.getAdres());
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
            if (reiziger.getAdres() != null) {
                adao.delete(reiziger.getAdres());
            }
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
            PreparedStatement st = connection.prepareStatement("select * from reiziger where reiziger_id = ?");
            st.setInt(1, id);
            ResultSet myRs = st.executeQuery();
            while (myRs.next()) {
                Reiziger reiziger = new Reiziger(myRs.getInt("reiziger_id"), myRs.getString("voorletters"), myRs.getString("tussenvoegsel"), myRs.getString("achternaam"), myRs.getDate("geboortedatum"));

                myRs.close();
                myStmt.close();
                return reiziger;
            }

//                    Adres adres = adao.findByReiziger(Integer.parseInt(myRs.getString("reiziger_id")));
//                    reiziger.setAdres(adres);
//                    if (reiziger.getAdres() != null) {
//                        reiziger.setAdres(adao.findByReiziger(id));
//                    }

        } catch (Exception e){
                e.printStackTrace();
            }
        return null;
        };
    public List<Reiziger> findByGbdatum(String datum){
        try {
            Statement myStmt = connection.createStatement();
            PreparedStatement st = connection.prepareStatement("select * from reiziger where geboortedatum = ?");
            Date date = java.sql.Date.valueOf(datum);
            st.setDate(1, date);
            ResultSet myRs = st.executeQuery();
            List<Reiziger> reizigers = new ArrayList<>();
            while (myRs.next()) {
                Reiziger reiziger = new Reiziger(myRs.getInt("reiziger_id"), myRs.getString("voorletters"), myRs.getString("tussenvoegsel"), myRs.getString("achternaam"), myRs.getDate("geboortedatum"));
//                    Adres adres = adao.findByReiziger(Integer.parseInt(myRs.getString("reiziger_id")));
                reiziger.setAdres(adao.findByReiziger(reiziger.getId()));
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
//                Adres adres = adao.findByReiziger(Integer.parseInt(myRs.getString("reiziger_id")));
//                reiziger.setAdres(adres);
                reiziger.setAdres(adao.findByReiziger(reiziger.getId()));
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
    public void setAdao(AdresDAO adao){
        this.adao = adao;
    }

    public void setOvdao(OVChipkaartDAO ovdao) {
        this.ovdao = ovdao;
    }
}
