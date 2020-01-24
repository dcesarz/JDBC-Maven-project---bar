package god.help.me;

import god.help.me.domain.Drink;
import god.help.me.domain.Kategoria;
import god.help.me.domain.DrinkKat;
import god.help.me.service.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class App {

    static Scanner scanner = new Scanner(System.in);

    static public void menuchoice(int ch, DrinkService serv, KategoriaService servk, DrinkAndKategoriaService servdk) {

        int tempid = 0;
        Drink tempdrink;
        Kategoria tempkat;

        switch (ch) {
            case 1:
                System.out.println("Wybrałeś dodaj nowy drink.");
                tempdrink = getdrinkargs();
                if (tempdrink != null) {
                    serv.addDrink(tempdrink);

                } else {
                    System.out.println("Dodawanie nie powiodło się.");
                }
                break;
            case 2:
                System.out.println("Wybrałeś usuń drink.");
                tempid = getid();
                if (tempid != -1) {
                    serv.deleteDrink(tempid);
                } else {
                    System.out.println("Usuwanie nie powiodło się.");
                }
                break;
            case 3:
                System.out.println("Wybrałeś edytuj istniejący drink.");
                tempdrink = getdrinkargs();
                tempid = getid();
                if (tempdrink != null && tempid != -1) {
                    serv.updateDrink(tempdrink, tempid);
                } else {
                    System.out.println("Edycja nie powiodła się.");
                }
                break;
            case 4:
                System.out.println("Wybrałeś przeczytaj istniejący drink.");
                List<Drink> templist = serv.findDrink(getid());
                System.out.println(templist);
                break;
            case 5:
                System.out.println("Wybrałeś dodaj nową kategorię.");
                tempkat = getkatargs();
                if (tempkat != null) {
                    servk.addKategoria(tempkat);
                } else {
                    System.out.println("Dodawanie nie powiodło się.");
                }
                break;
            case 6:
                System.out.println("Wybrałeś usuń kategorię.");
                tempid = getid();
                if (tempid != -1) {
                    servk.deleteKategoria(tempid);
                } else {
                    System.out.println("Usuwanie nie powiodło się.");
                }
                break;
            case 7:
                System.out.println("Wybrałeś edytuj istniejącą kategorię.");
                tempkat = getkatargs();
                tempid = getid();
                if (tempkat != null && tempid != -1) {
                    servk.updateKategoria(tempkat, tempid);
                } else {
                    System.out.println("Edycja nie powiodła się.");
                }
                break;
            case 8:
                System.out.println("Wybrałeś przeczytaj istniejącą kategorię.");
                Kategoria tempkk = servk.findKategoria(getid());
                System.out.println(tempkk);
                break;
            case 9:
                System.out.println("Wybrałeś przeczytaj istniejące drinki");
                System.out.println(serv.getDrinks());
                break;
            case 10:
                System.out.println("Wybrałeś przeczytaj istniejące kategorie");
                System.out.println(servk.getKategorie());
                break;
            case 11:
                System.out.println("Wybrałeś przeczytaj drinki, ktorych kategoria nie istnieje");
                System.out.println(servdk.DrinkiBezKat());
                break;
            case 12:
                System.out.println("Wybrałeś przeczytaj drinki wraz z ich kategoriami");
                System.out.println(servdk.DrinkiPlusKat());
                break;
            case 13:
                System.out.println("Wybrałeś przeczytaj drinki, które są dostępne");
                System.out.println(servdk.DrinkiAvail());
                break;
            case 14:
                System.out.println("Wybrałeś przeczytaj drinki, które nie są alkoholowe");
                System.out.println(servdk.DrinkNonAlc());
                break;
            default:
                System.out.println("Wybrałeś złą liczbę!");
                break;
        }
        scanner.nextLine();
    }

    static public int getid() {
        try {
            System.out.println("Podaj id:");
            int choice = scanner.nextInt();
            return choice;
        } catch (InputMismatchException e) {
            System.out.println("Wprowadzono zły argument.");
        }
        return -1;
    }

    static public Drink getdrinkargs() {
        try {
            System.out.println("Podaj ID kategorii:");
            int idk = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Podaj nazwe drinku");
            String nazwa = scanner.nextLine();
            while (nazwa.length() > 40) {
                nazwa = scanner.nextLine();
            }
            System.out.println("Podaj cenę:");
            BigDecimal cena = scanner.nextBigDecimal();
            scanner.nextLine();
            System.out.println("Podaj opis drinku");
            String opis = scanner.nextLine();
            while (opis.length() > 225) {
                opis = scanner.nextLine();
            }
            System.out.println("Czy drink jest dostępny? 0 - NIE, 1 - TAK");
            int av = scanner.nextInt();

            while (av != 0 && av != 1) {
                av = scanner.nextInt();
            }
            boolean bool;
            if (av == 1) {
                bool = true;
            } else {
                bool = false;
            }

            return new Drink(idk, nazwa, cena, opis, bool);
        } catch (InputMismatchException e) {
            System.out.println("Wprowadzono zły argument.");
        }
        return null;
    }

    static public Kategoria getkatargs() {
        try {
            scanner.nextLine();
            System.out.println("Podaj nazwe kategorii");
            String nazwa = scanner.nextLine();
            while (nazwa.length() > 40) {
                nazwa = scanner.nextLine();
            }
            //scanner.nextLine();
            System.out.println("Podaj opis kategorii");
            //scanner.nextLine();
            String opis = scanner.nextLine();
            while (opis.length() > 225) {
                opis = scanner.nextLine();
            }
            System.out.println("Czy drink jest alkoholowy? 0 - NIE, 1 - TAK");
            int av = scanner.nextInt();
            while (av != 0 && av != 1) {
                av = scanner.nextInt();
            }
            boolean bool;
            if (av == 1) {
                bool = true;
            } else {
                bool = false;
            }

            return new Kategoria(nazwa, opis, bool);
        } catch (InputMismatchException e) {
            System.out.println("Wprowadzono zły argument.");
            return null;
        }
    }

    public static void menu() {
        System.out.println("\t\t\t\t1. DODAJ DRINK");
        System.out.println("\t\t\t\t2. USUN DRINK");
        System.out.println("\t\t\t\t3. EDYTUJ DRINK");
        System.out.println("\t\t\t\t4. PRZECZYTAJ DRINK");
        System.out.println();
        System.out.println("\t\t\t\t5. DODAJ KATEGORIE");
        System.out.println("\t\t\t\t6. USUN KATEGORIE");
        System.out.println("\t\t\t\t7. EDYTUJ KATEGORIE");
        System.out.println("\t\t\t\t8. PRZECZYTAJ KATEGORIE");
        System.out.println();
        System.out.println("\t\t\t\t9. WYPISZ DRINKI");
        System.out.println("\t\t\t\t10. WYPISZ KATEGORIE");
        System.out.println();
        System.out.println("\t\t\t\t11. WYPISZ DRINKI, KTORYCH KATEGORIA NIE ISTNIEJE.");
        System.out.println("\t\t\t\t12. WYPISZ DRINKI WRAZ Z NAZWĄ I OPISEM KATEGORII.");
        System.out.println();
        System.out.println("\t\t\t\t13. WYPISZ DOSTEPNE DRINKI");
        System.out.println("\t\t\t\t14. WYPISZ DRINKI BEZALKOHOLOWE");
    }

    public static void main(String[] args) throws SQLException {

        DrinkService ds = new DrinkServiceJDBC();
        KategoriaService dk = new KategoriaServiceJDBC();
        DrinkAndKategoriaService dks = new DrinkAndKategoriaServiceJDBC(ds, dk);
        System.out.println("\t\t\t Witamy w obsłudze bazy danych \" bar \". Proszę wybrać operację do wykonania.");
        menu();
        while (true) {
            try {
                int choice = scanner.nextInt();
                menuchoice(choice, ds, dk, dks);
            } catch (InputMismatchException e) {
                System.out.println("Wprowadzono zły argument.");
                scanner.nextLine();
            }
            menu();
            System.out.println("Wykonaj wyboru ponownie.");
        }
    }

}
