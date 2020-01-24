package god.help.me.service;

import god.help.me.domain.Drink;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Connection.*;

public class DrinkServiceJDBC implements DrinkService {

    private final String DB_URL = "jdbc:hsqldb:hsql://localhost/workdb";
    private final String CREATE_TABLE_DRINK = "CREATE TABLE Drink (id INTEGER IDENTITY PRIMARY KEY, idkategoria int, nazwa varchar(40), cena decimal, opis varchar(225), isAvailable bit)";
    private final String INSERT_DRINK = "INSERT INTO Drink(idkategoria, nazwa, cena, opis, isAvailable) VALUES(?, ?, ?, ?, ?)";
    private final String SELECT_DRINKS = "SELECT id, idkategoria, nazwa, cena, opis, isAvailable FROM Drink";
    private final String SELECT_DRINK_BY_ID = "SELECT id, idkategoria, nazwa, cena, opis, isAvailable FROM Drink WHERE id=?";
    private final String DELETE_DRINK_BY_ID = "DELETE FROM Drink WHERE id=?";
    private final String UPDATE_DRINK_BY_ID = "UPDATE Drink SET idkategoria=?, nazwa=?, cena=?, opis=?, isAvailable=? WHERE id=?";
    private Connection connection;

    private PreparedStatement insertDrinkPST;
    private PreparedStatement selectDrinkPST;
    private PreparedStatement selectDrinkByIdPST;
    private PreparedStatement deleteDrinkByIdPST;
    private PreparedStatement updateDrinkByIdPST;


    public DrinkServiceJDBC() {
        try {

            connection = DriverManager.getConnection(DB_URL);

            connection.setAutoCommit(false);

            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);

            int isolation = connection.getTransactionIsolation();

            connection.setTransactionIsolation(TRANSACTION_READ_UNCOMMITTED);

            boolean tableExists = false;

            while (rs.next()) {
                if ("Drink".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }

            if (!tableExists) {
                PreparedStatement createTableDrinkPST = connection.prepareStatement(CREATE_TABLE_DRINK);
                createTableDrinkPST.executeUpdate();
                connection.commit();
            }

            insertDrinkPST = connection.prepareStatement(INSERT_DRINK);
            selectDrinkPST = connection.prepareStatement(SELECT_DRINKS);
            selectDrinkByIdPST = connection.prepareStatement(SELECT_DRINK_BY_ID);
            deleteDrinkByIdPST = connection.prepareStatement(DELETE_DRINK_BY_ID);
            updateDrinkByIdPST = connection.prepareStatement(UPDATE_DRINK_BY_ID);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }


    public void addDrink(Drink drink){
        try {

            insertDrinkPST.setInt(1, drink.getKategoriaid());
            insertDrinkPST.setString(2, drink.getNazwa());
            insertDrinkPST.setBigDecimal(3, drink.getCena());
            insertDrinkPST.setString(4, drink.getOpis());
            insertDrinkPST.setBoolean(5, drink.isAvailable());

            int c = insertDrinkPST.executeUpdate();

            if (c > 0) {
                System.out.println("Udało się dodać" + drink);
                connection.commit();
            } else {
                System.out.println("Nie udało się dodać drinku.");
            }

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public List<Drink> findDrink(int id) {
        List<Drink> result = new ArrayList<Drink>();
        try {
            selectDrinkByIdPST.setInt(1, id);
            ResultSet rs = selectDrinkByIdPST.executeQuery();
            while (rs.next()) {
                int idnew = rs.getInt("id");
                int idk = rs.getInt("idkategoria");
                String nazwa = rs.getString("nazwa");
                BigDecimal cena = rs.getBigDecimal("cena");
                String opis = rs.getString("opis");
                Boolean isAvailable = rs.getBoolean("isAvailable");

                Drink Drink = new Drink(idnew, idk, nazwa, cena, opis, isAvailable);
                result.add(Drink);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("got here 3");
        return result;
    }


    public void updateDrink(Drink drink, int id) {
        try {

            updateDrinkByIdPST.setInt(1, drink.getKategoriaid());
            updateDrinkByIdPST.setString(2, drink.getNazwa());
            updateDrinkByIdPST.setBigDecimal(3, drink.getCena());
            updateDrinkByIdPST.setString(4, drink.getOpis());
            updateDrinkByIdPST.setBoolean(5, drink.isAvailable());
            updateDrinkByIdPST.setInt(6, id);

            int c = updateDrinkByIdPST.executeUpdate();

            if (c > 0) {
                System.out.println("Udało się aktualizować rekord w Drink.");
                connection.commit();
            } else {
                System.out.println("Nie udało się zaktualizować rekordu w Drink. Sprawdź, czy podany rekord istnieje.");
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void deleteDrink(int id) {
        try {
            deleteDrinkByIdPST.setInt(1, id);
            int c = deleteDrinkByIdPST.executeUpdate();
            if (c != 0) {
                System.out.println("Udało się usunąć rekord w Drink.");
                connection.commit();
            } else {
                System.out.println("Nie ma rekordu do usunięcią!");
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public ArrayList<Drink> getDrinks() {
        ArrayList<Drink> result = new ArrayList<Drink>();

        try {
            ResultSet rs = selectDrinkPST.executeQuery();

            while (rs.next()) {
                int idnew = rs.getInt("id");
                int idk = rs.getInt("idkategoria");
                String nazwa = rs.getString("nazwa");
                BigDecimal cena = rs.getBigDecimal("cena");
                String opis = rs.getString("opis");
                Boolean isAvailable = rs.getBoolean("isAvailable");

                Drink drink = new Drink(idnew, idk, nazwa, cena, opis, isAvailable);
                result.add(drink);
            }


        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }

        return result;

    }

//    public void addDrinks(List<Drink> drinks) {
//
//        try {
//            connection.setAutoCommit(false);
//            for (Drink drink : drinks) {
//
//                insertDrinkPST.setInt(1, drink.getKategoriaid());
//                insertDrinkPST.setString(2, drink.getNazwa());
//                insertDrinkPST.setBigDecimal(3, drink.getCena());
//                insertDrinkPST.setString(4, drink.getOpis());
//                insertDrinkPST.setBoolean(5, drink.isAvailable());
//                int c = insertDrinkPST.executeUpdate();
//
//                if (c > 0) {
//                    System.out.println("Dodałem: " + drink);
//                } else {
//                    System.out.println("Nie udało się dodać listy drinków.");
//                }
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            connection.commit();
//        } catch (SQLException e) {
//
//            try {
//                connection.rollback();
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//            e.printStackTrace();
//        }
//    }

}
