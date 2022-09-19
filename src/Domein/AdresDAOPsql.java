package Domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdresDAOPsql implements AdresDAO{
    private Connection connection;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn) {
        this.connection = conn;
    }
    public boolean save(Adres adres){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select adres_id from adres");
            while (myRs.next()) {
//                Reiziger reiziger = rdao.findById(Integer.parseInt(myRs.getString("reiziger_id")));
//                adres.setReiziger(reiziger);
                if (Integer.parseInt(myRs.getString("adres_id")) == adres.getId()) {
                    return false;
                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, adres.getId());
            preparedStatement.setString(2, adres.getPostcode());
            preparedStatement.setString(3, adres.getHuisnummer());
            preparedStatement.setString(4, adres.getStraat());
            preparedStatement.setString(5, adres.getWoonplaats());
            preparedStatement.setInt(6, adres.getReizigerId());
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
    public boolean update(Adres adres){
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id= ? WHERE adres_id = ?");
            st.setString(1, adres.getPostcode());
            st.setString(2, adres.getHuisnummer());
            st.setString(3, adres.getStraat());
            st.setString(4, adres.getWoonplaats());
            st.setInt(5, adres.getReizigerId());
            st.setInt(6, adres.getId());
            Reiziger reiziger = rdao.findById(adres.getReizigerId());
            adres.setReiziger(reiziger);
            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
    };
    public boolean delete(Adres adres){
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM adres WHERE adres_id = ?");
            st.setInt(1, adres.getId());
            Reiziger reiziger = rdao.findById(adres.getReizigerId());
            adres.setReiziger(reiziger);
            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
    };
    public Adres findById(int id) {
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from adres");
            while (myRs.next()) {
                if (Objects.equals(myRs.getString("adres_id"), String.valueOf(id))) {
                    Adres adres = new Adres(Integer.parseInt(myRs.getString("adres_id")), myRs.getString("postcode"), myRs.getString("huisnummer"), myRs.getString("straat"), myRs.getString("woonplaats"), Integer.parseInt(myRs.getString("reiziger_id")));
                    Reiziger reiziger = rdao.findById(Integer.parseInt(myRs.getString("reiziger_id")));
                    adres.setReiziger(reiziger);
                    return adres;
                }
            }
            myRs.close();
            myStmt.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    };
    public Adres findByReiziger(int reizigerId){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from adres where reiziger_id = ?");
            preparedStatement.setInt(1, reizigerId);
            ResultSet myRs = preparedStatement.executeQuery();
            Adres ad = null;
            while (myRs.next()) {
                if (myRs.getInt("reiziger_id") == (reizigerId)) {
                    ad = new Adres(Integer.parseInt(myRs.getString("adres_id")), myRs.getString("postcode"), myRs.getString("huisnummer"), myRs.getString("straat"), myRs.getString("woonplaats"), Integer.parseInt(myRs.getString("reiziger_id")));
                }
            }
            myRs.close();
            preparedStatement.close();
            return ad;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    };
    public List<Adres> findAll(){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from adres");
            List<Adres> adresList = new ArrayList<>();
            while (myRs.next()) {
                Adres adres = new Adres(Integer.parseInt(myRs.getString("adres_id")), myRs.getString("postcode"), myRs.getString("huisnummer"), myRs.getString("straat"), myRs.getString("woonplaats"), Integer.parseInt(myRs.getString("reiziger_id")));
                Reiziger reiziger = rdao.findById(Integer.parseInt(myRs.getString("reiziger_id")));
                adres.setReiziger(reiziger);
                adresList.add(adres);
            }
            myRs.close();
            myStmt.close();
            return adresList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    };
    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }
}
