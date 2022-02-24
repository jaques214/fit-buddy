package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.RecyclerViewAdapter;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces.FavouritePlaces;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment {

    private Activity main;
    private SharedViewModel sh;
    private FirebaseAuth mAuth;
    public static final String EXTRA_MESSAGE = "pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.EXTRA";

    public FavouritesFragment() {
    }


    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mAuth = FirebaseAuth.getInstance();

        main = (MainFragmentsActivity) context;
        if(mAuth.getCurrentUser() == null){
            updateUI();
        }
        main = (MainFragmentsActivity) context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vf = inflater.inflate(R.layout.fragment_favorites, container, false);

        sh = new ViewModelProvider((MainFragmentsActivity)main).get(SharedViewModel.class);
        LiveData<List<FavouritePlaces>> places = sh.getFavouritePlaces();;

        places.observe((MainFragmentsActivity)main, new Observer<List<FavouritePlaces>>() {
            @Override
            public void onChanged(List<FavouritePlaces> places) {
                RecyclerView mRecyclerView = vf.findViewById(R.id.mRecyclerView);

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(main, places);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(main));

                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(main, DividerItemDecoration.VERTICAL);

                mRecyclerView.addItemDecoration(itemDecoration);
                adapter.notifyDataSetChanged();
            }
        });


        return vf;
    }

    public void updateUI(){
        Intent intent = new Intent(main, AccessControl.class);
        String message = "Logout";
        intent.putExtra(EXTRA_MESSAGE, message);
        mAuth.signOut();
        startActivity(intent);
    }
}
