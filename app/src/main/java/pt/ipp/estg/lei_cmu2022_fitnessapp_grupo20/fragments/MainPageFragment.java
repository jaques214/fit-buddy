package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.Exercise.Exercise;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviewhistory.RvHistoryFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews.ExerciseAdapter;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews.FavouritesFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews.RvExerciseFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews.UserFavouritesFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads.DbAddExerciseThread;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads.DbAddUserThread;

public class MainPageFragment extends Fragment {
    private Activity main;
    private FirebaseAuth mAuth;
    private SharedViewModel sh;
    private boolean flag;
    public static final String EXTRA_MESSAGE = "pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.EXTRA";

    public MainPageFragment() {
    }

    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.main = (MainFragmentsActivity) context;
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            updateUI();
        }

    }

    public void updateUI(){
        Intent intent = new Intent(main.getApplicationContext(), AccessControl.class);
        String message = "Logout";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag =false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View mContentView = inflater.inflate(R.layout.fragment_main_page, container, false);
        TextView area = mContentView.findViewById(R.id.tvMain);
        Button profile = mContentView.findViewById(R.id.profile);
        Button train = mContentView.findViewById(R.id.train);
        Button history = mContentView.findViewById(R.id.history);
        Button favs = mContentView.findViewById(R.id.favPlaces);
        FloatingActionButton favourite = mContentView.findViewById(R.id.favouritesButton);

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFavouritesFragment f = new UserFavouritesFragment();
                Bundle args = new Bundle();
                f.setArguments(args);
                FragmentManager fg = getParentFragmentManager();
                fg.beginTransaction()
                        .replace(R.id.Frame,f)
                        .addToBackStack(null)
                        .commit();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileFragment profilef = new ProfileFragment();
                Bundle args = new Bundle();
                profilef.setArguments(args);
                FragmentManager fg = getParentFragmentManager();
                fg.beginTransaction()
                        .replace(R.id.Frame, profilef)
                        .addToBackStack(null)
                        .commit();

            }
        });

        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RvExerciseFragment rvExercisef = new RvExerciseFragment();
                Bundle args = new Bundle();
                rvExercisef.setArguments(args);
                FragmentManager fg = getParentFragmentManager();
                fg.beginTransaction()
                        .replace(R.id.Frame, rvExercisef)
                        .addToBackStack(null)
                        .commit();

            }
        });

       history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RvHistoryFragment rvHistoryf = new RvHistoryFragment();
                Bundle args = new Bundle();
                rvHistoryf.setArguments(args);
                FragmentManager fg = getParentFragmentManager();
                fg.beginTransaction()
                        .replace(R.id.Frame, rvHistoryf)
                        .addToBackStack(null)
                        .commit();

            }
        });

        favs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FavouritesFragment rvFavsf = new FavouritesFragment();
                Bundle args = new Bundle();
                rvFavsf.setArguments(args);
                FragmentManager fg = getParentFragmentManager();
                fg.beginTransaction()
                        .replace(R.id.Frame, rvFavsf)
                        .addToBackStack(null)
                        .commit();

            }
        });

        sh = new ViewModelProvider(this).get(SharedViewModel.class);
        LiveData<List<Exercise>> temp = sh.getExercises();
        temp.observe((LifecycleOwner) main, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(List<Exercise> exercises) {
                if(exercises.size()==2){
                    flag = true;
                }
            }
        });

        if(!flag){
            DbAddExerciseThread thread = new DbAddExerciseThread(mContentView.getContext(),main);
            thread.start();
        }

        return mContentView;
    }



}