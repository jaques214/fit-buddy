package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.DB;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.PermissionsRequestor;

public class DbAddUserThread extends Thread{
    private Context context;
    private String email;
    private String username;
    private String password;
    private int id;
    private Activity main;

    public DbAddUserThread(Context c,Activity main, String email, String password, String username){
        this.context = c;
        this.main = main;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    @Override
    public void run() {
        this.addDbUser();
    }

    public void addDbUser(){
        SharedViewModel sharedViewModel = new SharedViewModel(main.getApplication());
        this.id = getLastId();
        User userToAdd = new User(this.id+1,this.username,this.email,this.password);
        sharedViewModel.getRepository().addUser(userToAdd);
    }

    protected int getLastId(){
        SharedViewModel sharedViewModel = new ViewModelProvider((AccessControl) main).get(SharedViewModel.class);
        return sharedViewModel.getSize();

    }

}
