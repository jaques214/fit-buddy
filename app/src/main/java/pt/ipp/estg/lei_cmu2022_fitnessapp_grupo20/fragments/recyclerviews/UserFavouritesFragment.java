package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.RecyclerViewAdapter;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.UserFavouritesAdapter;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.FavouritePlaces.FavouritePlaces;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.UserPlace.UserPlace;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads.DbAddFavouritePlacesThread;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFavouritesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MainFragmentsActivity main;
    private SharedViewModel sh;
    //private LiveData<List<FavouritePlaces>> favorite_places;

    public UserFavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFavouritesFragment newInstance(String param1, String param2) {
        UserFavouritesFragment fragment = new UserFavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        main = (MainFragmentsActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vf = inflater.inflate(R.layout.fragment_favorites, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        sh = new ViewModelProvider(main).get(SharedViewModel.class);
        LiveData<User> user = sh.getUserByEmail(mAuth.getCurrentUser().getEmail());

        user.observe(main, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                LiveData<List<FavouritePlaces>> places = sh.getPlacesByUser(user.name);

                places.observe(main, new Observer<List<FavouritePlaces>>() {
                    @Override
                    public void onChanged(List<FavouritePlaces> places) {
                        RecyclerView mRecyclerView = vf.findViewById(R.id.mRecyclerView);

                        UserFavouritesAdapter adapter = new UserFavouritesAdapter(main, places);
                        mRecyclerView.setAdapter(adapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(main));

                        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(main, DividerItemDecoration.VERTICAL);

                        mRecyclerView.addItemDecoration(itemDecoration);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return vf;
    }
}
