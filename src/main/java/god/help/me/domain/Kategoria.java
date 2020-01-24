package god.help.me.domain;


public class Kategoria{

    private int id;
    private String nazwa;
    private String opis;
    private boolean isAlcoholic;

    public Kategoria(int id, String nazwa, String opis, boolean isAlcoholic) {
        this.id = id;
        this.nazwa = nazwa;
        this.opis = opis;
        this.isAlcoholic = isAlcoholic;
    }

    public Kategoria(String nazwa, String opis, boolean isAlcoholic) {
        nazwa = nazwa.substring(0, Math.min(nazwa.length(), 40));
        opis = nazwa.substring(0, Math.min(nazwa.length(), 225));
        this.nazwa = nazwa;
        this.opis = opis;
        this.isAlcoholic = isAlcoholic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        nazwa = nazwa.substring(0, Math.min(nazwa.length(), 40));
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        opis = nazwa.substring(0, Math.min(nazwa.length(), 225));
        this.opis = opis;
    }

    public boolean isAlcoholic() {
        return isAlcoholic;
    }

    public void setAlcoholic(boolean alcoholic) {
        isAlcoholic = alcoholic;
    }

    @Override
    public String toString() {
            return "Kategoria{" +
                    "id=" + id +
                    ", nazwa='" + nazwa + '\'' +
                    ", opis='" + opis + '\'' +
                    ", isAlcoholic=" + isAlcoholic +
                    "}\n";
        }
}
