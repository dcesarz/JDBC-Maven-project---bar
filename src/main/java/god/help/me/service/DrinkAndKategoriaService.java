package god.help.me.service;

import god.help.me.domain.*;

import java.util.ArrayList;
import java.util.List;

public interface DrinkAndKategoriaService {

    public List<Drink> DrinkiBezKat();
    public List<DrinkKat> DrinkiPlusKat();
    public List<Drink> DrinkiAvail();
    public List<Drink> DrinkNonAlc();

}
