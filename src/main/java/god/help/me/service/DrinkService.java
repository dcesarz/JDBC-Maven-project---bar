package god.help.me.service;

import god.help.me.domain.Drink;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface DrinkService {

    public void addDrink(Drink drink);

    public List<Drink> findDrink(int id);

    public void deleteDrink(int id);

    public void updateDrink(Drink drink, int id);

    public ArrayList<Drink> getDrinks();

//    public void addDrinks(List<Drink> drinks);

}
