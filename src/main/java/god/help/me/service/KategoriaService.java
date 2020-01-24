package god.help.me.service;
import god.help.me.domain.Kategoria;

import java.util.ArrayList;
import java.util.List;

public interface KategoriaService {
    public void addKategoria(Kategoria kategoria);
    public Kategoria findKategoria(int id);
    public void deleteKategoria(int id);
    public void updateKategoria(Kategoria kategoria, int id);

    public ArrayList<Kategoria> getKategorie();

//    public void addKategorias(List<Kategoria> kategorie);

}
