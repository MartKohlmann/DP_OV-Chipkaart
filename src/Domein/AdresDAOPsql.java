package Domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdresDAOPsql implements AdresDAO{
    private Connection connection;

    public AdresDAOPsql(Connection conn) {
        this.connection = conn;
    }
    public boolean save(Adres adres){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select id from adres");
            while (myRs.next()) {
                if (Integer.parseInt(myRs.getString("id")) == adres.getId()) {
                    return false;
                }
            }

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO adres (id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)");
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
            PreparedStatement st = connection.prepareStatement("UPDATE reiziger SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id= ? WHERE id = ?");
            st.setString(1, adres.getPostcode());
            st.setString(2, adres.getHuisnummer());
            st.setString(3, adres.getStraat());
            st.setString(4, adres.getWoonplaats());
            st.setInt(5, adres.getReizigerId());
            st.setInt(6, adres.getId());
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
            PreparedStatement st = connection.prepareStatement("DELETE FROM adres WHERE id = ?");
            st.setInt(1, adres.getId());
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
                if (Objects.equals(myRs.getString("id"), String.valueOf(id))) {
                    Adres adres = new Adres(Integer.parseInt(myRs.getString("id")), myRs.getString("postcode"), myRs.getString("huisnummer"), myRs.getString("straat"), myRs.getString("woonplaats"), Integer.parseInt(myRs.getString("reiziger_id")));
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
    public List<Adres> findByReiziger(int reizigerId){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from adres");
            List<Adres> adresList = new ArrayList<>();
            while (myRs.next()) {
                if (myRs.getInt("reiziger_id") == (reizigerId)) {
                    Adres adres = new Adres(Integer.parseInt(myRs.getString("id")), myRs.getString("postcode"), myRs.getString("huisnummer"), myRs.getString("straat"), myRs.getString("woonplaats"), Integer.parseInt(myRs.getString("reiziger_id")));
                    adresList.add(adres);
                }
            }
            myRs.close();
            myStmt.close();
            return adresList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    };
}