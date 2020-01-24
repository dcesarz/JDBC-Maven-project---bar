package god.help.me.domain;

public class DrinkKat {
    private Kategoria kat;
    private Drink drink;

    public DrinkKat(Kategoria kat, Drink drink) {
        this.kat = kat;
        this.drink = drink;
    }

    public Kategoria getKat() {
        return kat;
    }

    public void setKat(Kategoria kat) {
        this.kat = kat;
    }

    public Drink getDrink() {
        return drink;
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }

    @Override
    public String toString() {
        return "DrinkKat{" +
                "kat=" + kat +
                ", drink=" + drink +
                "}\n";
    }
}
