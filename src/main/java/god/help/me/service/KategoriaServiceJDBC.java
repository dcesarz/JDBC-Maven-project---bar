package god.help.me.service;

import god.help.me.domain.Kategoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Connection.*;

public class KategoriaServiceJDBC implements KategoriaService {


    private final String DB_URL = "jdbc:hsqldb:hsql://localhost/workdb";
    private final String CREATE_TABLE_KATEGORIA = "CREATE TABLE Kategoria (id INTEGER IDENTITY PRIMARY KEY, nazwa varchar(40), opis varchar(225), isAlcoholic bit)";
    private final String INSERT_KATEGORIA = "INSERT INTO Kategoria(nazwa, opis, isAlcoholic) VALUES(?, ?, ?)";
    private final String SELECT_KATEGORIE = "SELECT id, nazwa, opis, isAlcoholic FROM Kategoria";
    private final String SELECT_KATEGORIA_BY_ID = "SELECT id, nazwa, opis, isAlcoholic FROM Kategoria WHERE id=?";
    private final String DELETE_KATEGORIA_BY_ID = "DELETE FROM Kategoria WHERE id=?";
    private final String UPDATE_KATEGORIA_BY_ID = "UPDATE Kategoria SET nazwa=?, opis=?, isAlcoholic=? WHERE id=?";

    private Connection connection;

    private PreparedStatement insertKategoriaPST;
    private PreparedStatement selectKategoriaPST;
    private PreparedStatement selectKategoriaByIdPST;
    private PreparedStatement deleteKategoriaByIdPST;
    private PreparedStatement updateKategoriaByIdPST;


    public KategoriaServiceJDBC() {
        try {

            connection = DriverManager.getConnection(DB_URL);

            connection.setAutoCommit(false);

            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);

            int isolation = connection.getTransactionIsolation();

            connection.setTransactionIsolation(TRANSACTION_READ_UNCOMMITTED);

            boolean tableExists = false;

            while (rs.next()) {
                if ("Kategoria".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }

            if (!tableExists) {
                PreparedStatement createTableKategoriaPST = connection.prepareStatement(CREATE_TABLE_KATEGORIA);
                createTableKategoriaPST.executeUpdate();
                connection.commit();
            }

            insertKategoriaPST = connection.prepareStatement(INSERT_KATEGORIA);
            selectKategoriaPST = connection.prepareStatement(SELECT_KATEGORIE);
            selectKategoriaByIdPST = connection.prepareStatement(SELECT_KATEGORIA_BY_ID);
            deleteKategoriaByIdPST = connection.prepareStatement(DELETE_KATEGORIA_BY_ID);
            updateKategoriaByIdPST = connection.prepareStatement(UPDATE_KATEGORIA_BY_ID);

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }


    public void addKategoria(Kategoria kategoria) {
        try {
            connection.setAutoCommit(false);
            //insertKategoriaPST.setInt(1, kategoria.getId());
            insertKategoriaPST.setString(1, kategoria.getNazwa());
            insertKategoriaPST.setString(2, kategoria.getOpis());
            insertKategoriaPST.setBoolean(3, kategoria.isAlcoholic());

            int c = insertKategoriaPST.executeUpdate();
            if(c>0) {
                connection.commit();
                System.out.println("Udało się dodać" + kategoria);
            }
            else
            {
                System.out.println("Nie udało się dodać rekordu do Kategoria.");
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

    public Kategoria findKategoria(int id) {

        Kategoria kategoria = null;
        try {
            selectKategoriaByIdPST.setInt(1, id);
            ResultSet rs = selectKategoriaByIdPST.executeQuery();

            while (rs.next()) {
                int idnew = rs.getInt("id");
                String nazwa = rs.getString("nazwa");
                String opis = rs.getString("opis");
                Boolean isAlcoholic = rs.getBoolean("isAlcoholic");

                kategoria = new Kategoria(idnew, nazwa, opis, isAlcoholic);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kategoria;
    }


    public void updateKategoria(Kategoria kategoria, int id) {
        try {
            //updateKategoriaByIdPST.setInt(1, kategoria.getId());
            updateKategoriaByIdPST.setString(1, kategoria.getNazwa());
            updateKategoriaByIdPST.setString(2, kategoria.getOpis());
            updateKategoriaByIdPST.setBoolean(3, kategoria.isAlcoholic());
            updateKategoriaByIdPST.setInt(4, id);

            int c = updateKategoriaByIdPST.executeUpdate();

            if(c>0)
            {
                System.out.println("Udało się aktualizować rekord w Kategoria.");
                connection.commit();
            }
            else{
                System.out.println("Nie udało się zaktualizować rekordu w Kategoria. Sprawdż, czy rekord istnieje.");
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

    public void deleteKategoria(int id) {
        try {
            deleteKategoriaByIdPST.setInt(1, id);
            int c = deleteKategoriaByIdPST.executeUpdate();
            if(c>0) {
                System.out.println("Udało się usunąć rekord w Kategoria.");
                connection.commit();
            }
            else
            {
                System.out.println("Nie ma rekordu do usunięcia.");
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

    public ArrayList<Kategoria> getKategorie() {
        ArrayList<Kategoria> result = new ArrayList<Kategoria>();

        try {
            ResultSet rs = selectKategoriaPST.executeQuery();

            while (rs.next()) {
                int idnew = rs.getInt("id");
                String nazwa = rs.getString("nazwa");
                String opis = rs.getString("opis");
                Boolean isAlcoholic = rs.getBoolean("isAlcoholic");

                Kategoria kategoria = new Kategoria(idnew, nazwa, opis, isAlcoholic);
                result.add(kategoria);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

//    public void addKategorias(List<Kategoria> kategorias) {
//
//        try {
//            connection.setAutoCommit(false);
//            for (Kategoria kategoria : kategorias) {
//
//                insertKategoriaPST.setInt(1, kategoria.getId());
//                insertKategoriaPST.setString(2, kategoria.getNazwa());
//                insertKategoriaPST.setString(3, kategoria.getOpis());
//                insertKategoriaPST.setBoolean(4, kategoria.isAlcoholic());
//                int c = insertKategoriaPST.executeUpdate();
//
//                if(c>0) {
//                    System.out.println("Dodałem: " + kategoria);
//                }
//                else
//                {
//                    System.out.println("Nie udało się dodać listy Kategorii.");
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
