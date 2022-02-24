package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;

public class DbAddExerciseThread extends Thread {
    private Context context;
    private Activity main;

    public DbAddExerciseThread(Context c,Activity main){
        this.context = c;
        this.main = main;
    }

    @Override
    public void run() {
        this.addExercises();
    }

    public void addExercises(){
        SharedViewModel sharedViewModel = new ViewModelProvider((MainFragmentsActivity) main).get(SharedViewModel.class);
        Exercise exercise = new Exercise(1,"Stretch","Muscles",false);
        Exercise exercise1 = new Exercise(2,"Jogging","Cardio",true);
        sharedViewModel.getRepository().addExercise(exercise);
        sharedViewModel.getRepository().addExercise(exercise1);
    }
}
