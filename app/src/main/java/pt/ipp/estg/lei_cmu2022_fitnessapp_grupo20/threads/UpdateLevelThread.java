package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads;

import android.app.Activity;
import android.content.Context;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;

public class UpdateLevelThread extends Thread{
    private Context context;
    private String email;
    private Activity main;
    private int calories;

    public UpdateLevelThread(Context c,Activity main, String email,int calories){
        this.context = c;
        this.main = main;
        this.email = email;
        this.calories = calories;
    }

    @Override
    public void run() {
        this.updateUserLevel();
    }


    public void updateUserLevel(){
        SharedViewModel sharedViewModel = new SharedViewModel(main.getApplication());

        if(calories > 4100){
            sharedViewModel.updateUserLevel(email,"Intermediate");
        }
        if(calories > 10000){
            sharedViewModel.updateUserLevel(email,"Advanced");
        }
        if(calories > 25000){
            sharedViewModel.updateUserLevel(email,"Superstar");
        }
        if(calories > 50000){
            sharedViewModel.updateUserLevel(email,"GodLike");
        }
    }


}
