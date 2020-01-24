package god.help.me;

import god.help.me.domain.*;
import god.help.me.service.*;

import god.help.me.App;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */


public class AppTest{

    private final String DB_URL = "jdbc:hsqldb:hsql://localhost/workdb";
    private Connection connection;

    private final String CREATE_TABLE_DRINK = "CREATE TABLE Drink (id INTEGER IDENTITY PRIMARY KEY, idkategoria int, nazwa varchar(40), cena decimal, opis varchar(225), isAvailable bit)";
    private final String CREATE_TABLE_KATEGORIA = "CREATE TABLE Kategoria (id INTEGER IDENTITY PRIMARY KEY, nazwa varchar(40), opis varchar(225), isAlcoholic bit)";
    private final String DROP_TABLE_DRINK = "DROP TABLE Drink IF EXISTS";
    private final String DROP_TABLE_KATEGORIA = "DROP TABLE Kategoria IF EXISTS";

    @Before @After
    public void DropAll()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            PreparedStatement dropd = connection.prepareStatement(DROP_TABLE_DRINK);
            PreparedStatement dropk = connection.prepareStatement(DROP_TABLE_KATEGORIA);
            dropk.executeUpdate();
            dropd.executeUpdate();
        } catch (SQLException e) {
            fail("Cannot connect to datebase.");
        }
    }


    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void driverFound() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            // Success.
        } catch (ClassNotFoundException e) {
            fail("Driver not found.");
        }
    }

    @Test
    public void connectionEstabilished() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            fail("Cannot connect to datebase.");
        }
    }


    @Test
    public void drinktableisCreatedOrExists() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);

            boolean tableExists = false;

            while (rs.next()) {
                if ("Drink".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }

            if (!tableExists) {
                PreparedStatement createTableDrinkPST = connection.prepareStatement(CREATE_TABLE_DRINK);
                int c = createTableDrinkPST.executeUpdate();
                if (c >= 0) {
                    connection.commit();
                } else {
                    connection.rollback();
                    fail("Creation of table <<DRINK>> failed.");
                }
            }
            rs = connection.getMetaData().getTables(null, null, null, null);


            tableExists = false;

            while (rs.next()) {
                if ("Drink".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }

            assertTrue(tableExists);

        } catch (SQLException e) {
            fail("Creation of table <<DRINK>> failed.");
        }
    }

    @Test
    public void kategoriaisCreatedOrExists() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);


            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);

            boolean tableExists = false;

            while (rs.next()) {
                if ("Kategoria".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                    tableExists = true;
                    break;
                }
            }

            if (!tableExists) {
                PreparedStatement createTableKategoriaPST = connection.prepareStatement(CREATE_TABLE_KATEGORIA);
                int c = createTableKategoriaPST.executeUpdate();
                if(c >= 0)
                {
                    connection.commit();
                }
                else
                {
                    connection.rollback();
                    fail("Creation of table <<KATEGORIA>> failed.");
                }

                rs = connection.getMetaData().getTables(null, null, null, null);

                tableExists = false;

                while (rs.next()) {
                    if ("Kategoria".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
                        tableExists = true;
                        break;
                    }
                }

                assertTrue(tableExists);
            }
        } catch (SQLException e) {
            fail("Creation of table <<KATEGORIA>> failed.");
        }
    }

    @Test(expected = Test.None.class)
    public void addRecordToDrink()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            DrinkService ds = new DrinkServiceJDBC();
            BigDecimal bd = new BigDecimal(0.002103);
            Drink tempdrink = new Drink(1, "nazwa", bd, "opis", true);
            ds.addDrink(tempdrink);
            ArrayList<Drink> tempdrinks =  ds.getDrinks();
            connection.rollback();
            assertFalse(tempdrinks.isEmpty());
        }
        catch(SQLException e)
        {
            fail("Nie udało się stworzyć drinka");
        }
    }

    @Test(expected = Test.None.class)
    public void addRecordToKategoria()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            KategoriaService ks = new KategoriaServiceJDBC();
            Kategoria tempkat = new Kategoria("nazwa", "opis", true);
            ks.addKategoria(tempkat);
            ArrayList<Kategoria> tempkategorie =  ks.getKategorie();
            connection.rollback();
            assertFalse(tempkategorie.isEmpty());
        }
        catch(SQLException e)
        {
            fail("Nie udało się stworzyć kategorii");
        }
    }

    @Test(expected = Test.None.class)
    public void deleteRecordDrink()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            DrinkService ds = new DrinkServiceJDBC();
            BigDecimal bd = new BigDecimal(0.002103);
            Drink tempdrink = new Drink(1, "nazwa", bd, "opis", true);
            ds.addDrink(tempdrink);
            ArrayList<Drink> ald = ds.getDrinks();
            ds.deleteDrink(ald.get(ald.size() - 1).getId());
            ald = ds.getDrinks();
            assertTrue(ald.isEmpty());
            connection.rollback();
        }
        catch(SQLException e)
        {
            fail("Nie udało się usunąć drinka");
        }
    }

    @Test(expected = Test.None.class)
    public void UpdateRecordDrink()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            DrinkService ds = new DrinkServiceJDBC();
            BigDecimal bd = new BigDecimal(0.002103);
            Drink drink = new Drink(1, "NOTUPDATED", bd, "opis", true);
            ds.addDrink(drink);
            Drink tempdrink = new Drink(1, "UPDATED", bd, "opis", true);
            ArrayList<Drink> ald = ds.getDrinks();
            ds.updateDrink(tempdrink, ald.get(ald.size() - 1).getId());
            ald = ds.getDrinks();
            System.out.println(ald);
            System.out.println("*********************************************************");
            assertTrue(ald.get(ald.size()-1).getNazwa().equals("UPDATED"));
            connection.rollback();
        }
        catch(SQLException e)
        {
            fail("Nie udało się edytować drinka");
        }
    }

    @Test(expected = Test.None.class)
    public void ReadRecordDrinkWrongId()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            DrinkService ds = new DrinkServiceJDBC();
            if(!ds.findDrink(-1).isEmpty())
            {
                fail("Zwracana array list powinna byc pusta.");
            }
        }
        catch(SQLException e)
        {
            fail("Wypisywanie rekordu nie działa");
        }
    }

    @Test(expected = Test.None.class)
    public void ReadRecordDrinkGoodId()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            ds.addDrink(new Drink(1,"nazwa", new BigDecimal(32),"opis",false));
            int size = ds.getDrinks().size();
            if(ds.findDrink(ds.getDrinks().get(size-1).getId()).isEmpty())
            {
                fail("Zwracana array list NIE powinna byc pusta.");
                connection.rollback();
            }
        }
        catch(SQLException e)
        {
            fail("Wypisywanie rekordu nie działa");
        }
    }


    @Test(expected = Test.None.class)
    public void deleteRecordKategoria()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            KategoriaService ds = new KategoriaServiceJDBC();
            Kategoria tempKategoria = new Kategoria("nazwa", "opis", true);
            ds.addKategoria(tempKategoria);
            ArrayList<Kategoria> ald = ds.getKategorie();
            ds.deleteKategoria(ald.get(ald.size() - 1).getId());
            ald = ds.getKategorie();
            assertTrue(ald.isEmpty());
            connection.rollback();
        }
        catch(SQLException e)
        {
            fail("Nie udało się usunąć Kategoria");
        }
    }

    @Test(expected = Test.None.class)
    public void UpdateRecordKategoria()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);

            KategoriaService ds = new KategoriaServiceJDBC();
            BigDecimal bd = new BigDecimal(0.002103);
            Kategoria Kategoria = new Kategoria("NOTUPDATED", "opis", true);
            ds.addKategoria(Kategoria);
            Kategoria tempKategoria = new Kategoria("UPDATED", "opis", true);
            ArrayList<Kategoria> ald = ds.getKategorie();
            ds.updateKategoria(tempKategoria, ald.get(ald.size() - 1).getId());
            ald = ds.getKategorie();
            connection.rollback();
            System.out.println();
            assertTrue(ald.get(ald.size()-1).getNazwa().equals("UPDATED"));
        }
        catch(SQLException e)
        {
            fail("Nie udało się edytować Kategoriaa");
        }
    }

    @Test(expected = Test.None.class)
    public void ReadRecordKategoriaWrongId()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            KategoriaService ds = new KategoriaServiceJDBC();
            if(ds.findKategoria(-1)!=null)
            {
                fail("Zwracany obiekt powinien byc pusty.");
            }
        }
        catch(SQLException e)
        {
            fail("Wypisywanie rekordu nie działa");
        }
    }

    @Test(expected = Test.None.class)
    public void ReadRecordKategoriaGoodId()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            KategoriaService ds = new KategoriaServiceJDBC();
            ds.addKategoria(new Kategoria("nazwa","opis",false));
            int size = ds.getKategorie().size();
            if(ds.findKategoria(ds.getKategorie().get(size-1).getId())==null)
            {
                fail("Zwracany obiekt NIE powinien byc pusty.");
                connection.rollback();
            }
        }
        catch(SQLException e)
        {
            fail("Wypisywanie rekordu nie działa");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksNoKatWorksWhenKatDoesntExist()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            //ks.addKategoria(new Kategoria("nazwa","opis",false));
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<Drink> sss= dks.DrinkiBezKat();
            connection.rollback();
            if(sss.isEmpty())
            {
                fail("Lista NIE powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksNoKatWorksWhenKatExists()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            ks.addKategoria(new Kategoria("nazwa","opis",false));
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<Drink> sss= dks.DrinkiBezKat();
            connection.rollback();
            if(!sss.isEmpty())
            {
                fail("Lista powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksNoKatWorksWhenOnlyKatExists()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            ks.addKategoria(new Kategoria("nazwa","opis",false));
            //ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<Drink> sss= dks.DrinkiBezKat();
            connection.rollback();
            if(!sss.isEmpty())
            {
                fail("Lista powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksNoKatWorksWhenNeitherExist()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            //ks.addKategoria(new Kategoria("nazwa","opis",false));
            //ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<Drink> sss= dks.DrinkiBezKat();
            connection.rollback();
            if(!sss.isEmpty())
            {
                fail("Lista powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksWKatWorksWhenKatDoesntExist()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            //ks.addKategoria(new Kategoria("nazwa","opis",false));
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<DrinkKat> sss= dks.DrinkiPlusKat();
            connection.rollback();
            if(!sss.isEmpty())
            {
                fail("Lista powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksWKatWorksWhenKatExists()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            ks.addKategoria(new Kategoria("nazwa","opis",false));
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<DrinkKat> sss= dks.DrinkiPlusKat();
            connection.rollback();
            if(sss.isEmpty())
            {
                fail("Lista NIE powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksWKatWorksWhenNeitherExist()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            //ks.addKategoria(new Kategoria("nazwa","opis",false));
            //ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<DrinkKat> sss = dks.DrinkiPlusKat();
            System.out.println(ds.getDrinks());
            System.out.println(ks.getKategorie());
            if(!sss.isEmpty())
            {
                fail("Lista powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void AssertThatDrinksWKatWorksWhenOnlyKatExists()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, ks);
            ks.addKategoria(new Kategoria("nazwa","opis",false));
            //ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<DrinkKat> sss= dks.DrinkiPlusKat();
            connection.rollback();
            if(!sss.isEmpty())
            {
                fail("Lista powinna byc pusta");
            }
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void NoExceptionWhenArgumentIsTooLongDrink()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            ds.addDrink(new Drink(0,"111111111111111111111111111111111111111" +
                    "111111111111111111111111111111111" +
                    "1111111111111111111111111111111" +
                    "11111111111111111111111111111111111" +
                    "111111111111111111111111111111111111", new BigDecimal(12), "opis", false));
            connection.rollback();
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void NoExceptionWhenArgumentIsTooLongKategoria()
    {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            KategoriaService ks = new KategoriaServiceJDBC();
            ks.addKategoria(new Kategoria("11111111111111111111" +
                    "11111111111111111111" +
                    "11111111111111111111" +
                    "1111111111111111111" +
                    "1111111111111111111","opis",false));
            connection.rollback();
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void ListOfAvailableDrinksIsEmptyWhenItShouldBe()
    {
        try{
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds,ks);
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            List<Drink> temp = dks.DrinkiAvail();
            connection.rollback();
            assertTrue(temp.isEmpty());
    }
        catch(SQLException e)
    {
        fail("Sql error");
    }
    }

    @Test(expected = Test.None.class)
    public void ListOfAvailableDrinksIsNotEmptyWhenItShouldNotBe()
    {
        try{
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds,ks);
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", true));
            List<Drink> temp = dks.DrinkiAvail();
            connection.rollback();
            assertTrue(!temp.isEmpty());
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void ListOfNonAlcoholicDrinksIsEmptyWhenItShouldBe()
    {
        try{
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds,ks);
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            ks.addKategoria(new Kategoria("nazwa", "opis", true));
            List<Drink> temp = dks.DrinkNonAlc();
            connection.rollback();
            assertTrue(temp.isEmpty());
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }

    @Test(expected = Test.None.class)
    public void ListOfNonAlcoholicDrinksIsNotEmptyWhenItShouldNotBe()
    {
        try{
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            DrinkService ds = new DrinkServiceJDBC();
            KategoriaService ks = new KategoriaServiceJDBC();
            DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds,ks);
            ds.addDrink(new Drink(0,"nazwa", new BigDecimal(12), "opis", false));
            ks.addKategoria(new Kategoria("nazwa", "opis", false));
            List<Drink> temp = dks.DrinkNonAlc();
            connection.rollback();
            assertTrue(!temp.isEmpty());
        }
        catch(SQLException e)
        {
            fail("Sql error");
        }
    }


}
