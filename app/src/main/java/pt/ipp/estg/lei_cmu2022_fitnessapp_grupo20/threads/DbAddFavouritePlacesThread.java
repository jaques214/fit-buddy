package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads;

import android.app.Activity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.favouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.User;

public class DbAddFavouritePlacesThread extends Thread {
    private Activity main;
    private FavouritePlaces place;
    private User user;

    public DbAddFavouritePlacesThread(Activity main, FavouritePlaces place, User user) {
        this.main = main;
        this.place = place;
        this.user = user;
    }

    public void run() {
        this.addPlaces();
    }

    public void addPlaces() {
        SharedViewModel sharedViewModel = new SharedViewModel(main.getApplication());
        int id = sharedViewModel.getRepository().getFavouritePlacesSize();
        place.setId(id + 1);
        place.setUsername(user.name);
        sharedViewModel.getRepository().addFavouritePlaces(place);
    }
}
