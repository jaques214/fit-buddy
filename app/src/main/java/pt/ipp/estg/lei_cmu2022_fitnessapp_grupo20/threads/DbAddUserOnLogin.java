package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads;

import android.app.Activity;
import android.content.Context;
import androidx.lifecycle.ViewModelProvider;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.user.User;

public class DbAddUserOnLogin extends Thread {
    private Context context;
    private String email;
    private String username;
    private String password;
    private int id;
    private Activity main;

    public DbAddUserOnLogin(Context c,Activity main, String email, String password, String username){
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
        this.id++;
        User userToAdd = new User(this.id,this.username,this.email,this.password);
        sharedViewModel.getRepository().addUser(userToAdd);
    }

    protected int getLastId(){
        SharedViewModel sharedViewModel = new ViewModelProvider((AccessControl) main).get(SharedViewModel.class);

        return sharedViewModel.getSize();

    }
}
