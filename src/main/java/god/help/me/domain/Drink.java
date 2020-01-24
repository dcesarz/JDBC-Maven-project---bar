package god.help.me.domain;

import java.math.BigDecimal;

public class Drink {

    private int id;
    private int kategoriaid;
    private String nazwa;
    private BigDecimal cena;
    private String opis;
    private boolean isAvailable;

    public Drink(int id, int kategoriaid, String nazwa, BigDecimal cena, String opis, boolean isAvailable) {
        nazwa = nazwa.substring(0, Math.min(nazwa.length(), 40));
        opis = nazwa.substring(0, Math.min(nazwa.length(), 225));
        this.id = id;
        this.kategoriaid = kategoriaid;
        this.nazwa = nazwa;
        this.cena = cena;
        this.opis = opis;
        this.isAvailable = isAvailable;
    }

    public Drink(int kategoriaid, String nazwa, BigDecimal cena, String opis, boolean isAvailable) {
        nazwa = nazwa.substring(0, Math.min(nazwa.length(), 40));
        opis = nazwa.substring(0, Math.min(nazwa.length(), 225));
        this.kategoriaid = kategoriaid;
        this.nazwa = nazwa;
        this.cena = cena;
        this.opis = opis;
        this.isAvailable = isAvailable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKategoriaid() {
        return kategoriaid;
    }

    public void setKategoriaid(int kategoriaid) {
        this.kategoriaid = kategoriaid;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {

        nazwa = nazwa.substring(0, Math.min(nazwa.length(), 40));
        this.nazwa = nazwa;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public void setCena(BigDecimal cena) {
        this.cena = cena;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {

        opis = nazwa.substring(0, Math.min(nazwa.length(), 225));
        this.opis = opis;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Drink{" +
                "id=" + id +
                ", kategoriaid=" + kategoriaid +
                ", nazwa='" + nazwa + '\'' +
                ", cena=" + cena +
                ", opis='" + opis + '\'' +
                ", isAvailable=" + isAvailable +
                "}\n";
    }
}

