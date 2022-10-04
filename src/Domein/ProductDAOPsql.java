package Domein;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection connection;
//    private AdresDAO adao;
    private OVChipkaartDAO ovdao;

    public ProductDAOPsql(Connection conn) {
        this.connection = conn;
    }
    public boolean save(Product product){
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select product_nummer from product");
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
                if (product.getProduct_nummer() == myRs.getInt("product_nummer")) {
                    return false;
                }
            }
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)");
            preparedStatement.setInt(1, product.getProduct_nummer());
            preparedStatement.setString(2, product.getNaam());
            preparedStatement.setString(3, product.getBeschrijving());
            preparedStatement.setDouble(4, product.getPrijs());
            preparedStatement.executeUpdate();
            if (!product.getOvChipkaartList().isEmpty()) {
                for (OVChipkaart ovChipkaart : product.getOvChipkaartList()) {
                    ovChipkaart.voegProductToe(product);
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)");
                    ps.setInt(1, ovChipkaart.getKaartnummer());
                    ps.setInt(2, product.getProduct_nummer());
                    ps.executeUpdate();
                    ovdao.update(ovChipkaart);
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
    };
    public boolean update(Product product){
        try {
            PreparedStatement st = connection.prepareStatement("UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");
            st.setString(1, product.getNaam());
            st.setString(2, product.getBeschrijving());
            st.setDouble(3, product.getPrijs());
            st.setInt(4, product.getProduct_nummer());
            if (!product.getOvChipkaartList().isEmpty()){
                for (OVChipkaart ovChipkaart : product.getOvChipkaartList()){
                    if (!ovChipkaart.getProductList().isEmpty()){
                        for (Product p : ovChipkaart.getProductList()){
                            if (p.getProduct_nummer() == product.getProduct_nummer() && !ovChipkaart.equals(ovdao.findById(ovChipkaart.getKaartnummer())) ) {
                                ovChipkaart.updateProduct(product);
                                PreparedStatement pss = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?) ON CONFLICT DO NOTHING");
                                pss.setInt(1, ovChipkaart.getKaartnummer());
                                pss.setInt(2, product.getProduct_nummer());
                                pss.executeUpdate();
                                pss.close();
                                ovdao.update(ovChipkaart);
                                break;
                            }
                        }
                    }
                    else {
                        ovChipkaart.voegProductToe(product);
                    }
                }
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
    public boolean delete(Product product){
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            ps.setInt(1, product.getProduct_nummer());
            ps.executeUpdate();
            ps.close();
            PreparedStatement st = connection.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
            st.setInt(1, product.getProduct_nummer());
            if (!product.getOvChipkaartList().isEmpty()) {
                for (OVChipkaart ovChipkaart : product.getOvChipkaartList()) {
                    ovChipkaart.verwijderProduct(product);
                }
            }

            st.executeUpdate();
            st.close();
            return true;
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {

        try {
            Statement myStmt = connection.createStatement();
            List<Product> productList = new ArrayList<>();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs FROM product p INNER JOIN ov_chipkaart_product o ON o.product_nummer = p.product_nummer WHERE o.kaart_nummer = ?");
            preparedStatement.setInt(1, ovChipkaart.getKaartnummer());
            ResultSet myRs = preparedStatement.executeQuery();
            while (myRs.next()) {
                Product product = new Product(myRs.getInt("product_nummer"), myRs.getString("naam"), myRs.getString("beschrijving"), myRs.getInt("prijs"));
                product.voegOVChipkaartToe(ovChipkaart);

                productList.add(product);
            }
            myRs.close();


            myStmt.close();
            return productList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public List<Product> findAll() {
        try {
            Statement myStmt = connection.createStatement();
            ResultSet myRs = myStmt.executeQuery("select * from product");
            List<Product> productList = new ArrayList<>();
            while (myRs.next()) {
                Product product = new Product(myRs.getInt("product_nummer"), myRs.getString("naam"), myRs.getString("beschrijving"), myRs.getInt("prijs"));
                for (OVChipkaart ovChipkaart : ovdao.findAll()) {

                    for (Product p : ovChipkaart.getProductList()){
                        if (p.getProduct_nummer() == product.getProduct_nummer()) {
                            product.voegOVChipkaartToe(ovChipkaart);
                            break;
                        }
                    }
                }
                productList.add(product);
            }
            myRs.close();
            myStmt.close();
            return productList;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void setOvdao(OVChipkaartDAO ovdao) {
        this.ovdao = ovdao;
    }
}
