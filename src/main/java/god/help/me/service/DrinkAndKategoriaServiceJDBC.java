package god.help.me.service;

import god.help.me.domain.Drink;
import god.help.me.domain.DrinkKat;
import god.help.me.domain.Kategoria;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

public class DrinkAndKategoriaServiceJDBC implements DrinkAndKategoriaService {

    private final String SELECT_DRINKS = "SELECT id, idkategoria, nazwa, cena, opis, isAvailable FROM Drink";
    private final String SELKAT = "SELECT id, nazwa, opis, isAlcoholic FROM Kategoria WHERE id=?";
    private final String DB_URL = "jdbc:hsqldb:hsql://localhost/workdb";
   // private final String SELECT_DRINKS_WHEN_ALC = "SELECT id, idkategoria, nazwa, cena, opis, isAvailable FROM Drink";

    private PreparedStatement selectdrinkspst;
    private PreparedStatement selkatpst;

    private Connection connection;
    private DrinkService ds;
    private KategoriaService ks;

    public DrinkAndKategoriaServiceJDBC(DrinkService ds, KategoriaService ks) {
        try {

            connection = DriverManager.getConnection(DB_URL);

            this.ds = ds;
            this.ks = ks;

            int isolation = connection.getTransactionIsolation();

            connection.setTransactionIsolation(TRANSACTION_READ_UNCOMMITTED);

            selkatpst = connection.prepareStatement(SELKAT);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Drink> DrinkiBezKat() {
        List<Drink> result = new ArrayList<Drink>();

        try {
            int i;
            ArrayList<Drink> drinks = ds.getDrinks();
            int sizelist = drinks.size();
            for (i = 0; i < sizelist; i++) {
                Drink temp = drinks.get(i);
                int id = temp.getKategoriaid();
                selkatpst.setInt(1, id);
                ResultSet rs = selkatpst.executeQuery();
                if (rs.next() == false) {
                    result.add(temp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<DrinkKat> DrinkiPlusKat() {
        List<DrinkKat> result = new ArrayList<DrinkKat>();

        try {
            int i;
            ArrayList<Drink> drinks = ds.getDrinks();
            ArrayList<Kategoria> kategorie = ks.getKategorie();
            int sizelist = drinks.size();
            for (i = 0; i < sizelist; i++) {
                Drink temp = drinks.get(i);
                int id = temp.getKategoriaid();
                selkatpst.setInt(1, id);
                ResultSet rs = selkatpst.executeQuery();
                if (rs.next() != false) {
                    Kategoria ktemp = ks.findKategoria(id);
                    DrinkKat dktemp = new DrinkKat(ktemp, temp);
                    result.add(dktemp);
                }
                else
                {
                  //  DrinkKat dktemp = new DrinkKat(null, temp);
                  //  result.add(dktemp);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<Drink> DrinkiAvail()
    {
        ArrayList<Drink> drinks = new ArrayList<>();
        drinks = ds.getDrinks();
        int size = drinks.size();
        for(int i = 0; i <size; i ++)
        {
            if(!drinks.get(i).isAvailable())
            {
                drinks.remove(i);
            }
        }
        return drinks;
    }

    public List<Drink> DrinkNonAlc()
    {
        ArrayList<Drink> drinks = new ArrayList<>();
        List<DrinkKat> drinkats = DrinkiPlusKat();
        int d = drinkats.size();
        for(int i = 0; i < d; i ++)
        {
            if(!drinkats.get(i).getKat().isAlcoholic())
            {
                drinks.add(drinkats.get(i).getDrink());
            }
        }
        return drinks;
    }



}
