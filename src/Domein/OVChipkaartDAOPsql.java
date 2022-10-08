package Domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OVChipkaartDAOPsql implements OVChipkaartDAO{
    private Connection connection;
    private ReizigerDAO rdao;
    private ProductDAO pdao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.connection = conn;
    }
    public boolean save(OVChipkaart ovChipkaart){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select kaart_nummer from ov_chipkaart");
            while (myRs.next()) {
                if (Integer.parseInt(myRs.getString("kaart_nummer")) == ovChipkaart.getKaartnummer()) {
                    return false;
                }
            }
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, ovChipkaart.getKaartnummer());
            preparedStatement.setDate(2, (Date) ovChipkaart.getGeldigTot());
            preparedStatement.setInt(3, ovChipkaart.getKlasse());
            preparedStatement.setDouble(4, ovChipkaart.getSaldo());
            preparedStatement.setInt(5, ovChipkaart.getReizigerId());
            preparedStatement.executeUpdate();
            if (!ovChipkaart.getProductList().isEmpty()) {
                for (Product product : ovChipkaart.getProductList()) {
                    product.voegOVChipkaartToe(ovChipkaart);
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)");
                    ps.setInt(1, ovChipkaart.getKaartnummer());
                    ps.setInt(2, product.getProduct_nummer());
                    ps.executeUpdate();
                    pdao.save(product);
                }
            }

            myRs.close();
            myStmt.close();
            preparedStatement.close();
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?");
            st.setDate(1, (Date) ovChipkaart.getGeldigTot());
            st.setInt(2, ovChipkaart.getKlasse());
            st.setDouble(3, ovChipkaart.getSaldo());
            st.setInt(4, ovChipkaart.getReizigerId());
            st.setInt(5, ovChipkaart.getKaartnummer());
            ovChipkaart.setReiziger(rdao.findById(ovChipkaart.getReizigerId()));
            if (!ovChipkaart.getProductList().isEmpty()) {
                for (Product p : ovChipkaart.getProductList()) {
                    if (!p.getOvChipkaartList().contains(ovChipkaart)) {
                        p.voegOVChipkaartToe(ovChipkaart);
                    }
                }
            }
            List<Product> o = pdao.findByOVChipkaart(ovChipkaart);
            for (Product oud : o) {
                if (oud.getOvChipkaartList().contains(ovChipkaart)){
                    oud.verwijderOVChipkaart(ovChipkaart);
                }
            }


            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }    }

    public boolean delete(OVChipkaart ovChipkaart) {
        try {
            PreparedStatement st = connection.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");
            st.setInt(1, ovChipkaart.getKaartnummer());
            if (!ovChipkaart.getProductList().isEmpty()) {
                for (Product product : ovChipkaart.getProductList()) {
                    product.verwijderOVChipkaart(ovChipkaart);
                }
            }
            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }    }

    public OVChipkaart findById(int id) {
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from ov_chipkaart");
            while (myRs.next()) {
                if (Objects.equals(myRs.getString("kaart_nummer"), String.valueOf(id))) {
                    OVChipkaart ovChipkaart = new OVChipkaart(myRs.getInt("kaart_nummer"), myRs.getDate("geldig_tot"), myRs.getInt("klasse"), myRs.getDouble("saldo"), myRs.getInt("reiziger_id"));
                    Reiziger reiziger = rdao.findById(myRs.getInt("reiziger_id"));
                    ovChipkaart.setReiziger(reiziger);
                    for (Product p : pdao.findByOVChipkaart(ovChipkaart)) {
                        ovChipkaart.voegProductToe(p);
                    }
                    return ovChipkaart;
                }
            }
            myRs.close();
            myStmt.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<OVChipkaart> findAll() {
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from ov_chipkaart");
            List<OVChipkaart> ovchipkaarten = new ArrayList<>();
            while (myRs.next()) {
                OVChipkaart ovChipkaart = new OVChipkaart(Integer.parseInt(myRs.getString("kaart_nummer")), myRs.getDate("geldig_tot"), myRs.getInt("klasse"), myRs.getDouble("saldo"), myRs.getInt("reiziger_id"));
                Reiziger reiziger = rdao.findById(myRs.getInt("reiziger_id"));
                ovChipkaart.setReiziger(reiziger);
                for (Product p : pdao.findByOVChipkaart(ovChipkaart)) {

                    ovChipkaart.voegProductToe(p);
                }

                ovchipkaarten.add(ovChipkaart);
            }
            myRs.close();
            myStmt.close();
            return ovchipkaarten;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    ;

    public void setRdao(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    public void setPdao(ProductDAO pdao) {
        this.pdao = pdao;
    }
}
