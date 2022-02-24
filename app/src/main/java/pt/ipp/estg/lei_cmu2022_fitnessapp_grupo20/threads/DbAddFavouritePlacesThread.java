package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads;

import android.app.Activity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.UserPlace.UserPlace;

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
        //this.addPlacesByUser();
    }

    public void addPlaces() {
        SharedViewModel sharedViewModel = new SharedViewModel(main.getApplication());
        int id = sharedViewModel.getRepository().getFavouritePlacesSize();
        place.setId(id + 1);
        place.setUsername(user.name);
        sharedViewModel.getRepository().addFavouritePlaces(place);
    }

   /* public void addPlacesByUser() {
        UserPlace user_place = new UserPlace(place.getId(), user.id);
        SharedViewModel mainSharedViewModel = new SharedViewModel(main.getApplication());
        if(user_place.userId == user.id) {
            mainSharedViewModel.getRepository().addUserFavouritePlaces(user_place);
        }
    }*/
}
