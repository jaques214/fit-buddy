package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import java.util.List;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MainFragmentsActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads.UpdateLevelThread;

public class ProfileFragment extends Fragment {

    private MainFragmentsActivity main;
    private FirebaseAuth mAuth;
    private SharedViewModel sh;
    private LiveData<User> userX;
    private FusedLocationProviderClient mFusedLocationClient;
    private PermissionsRequestor permissionsRequestor;
    private User myUser;
    private int myUserCalories;
    private int calories;


    public ProfileFragment() {
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null){
            updateUI();
        }

        this.main =(MainFragmentsActivity) context;
        calories = 0;
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mContentView = inflater.inflate(R.layout.profile_layout, container, false);
        TextView name = mContentView.findViewById(R.id.nameTV);
        TextView level = mContentView.findViewById(R.id.lvlTV);
        TextView email = mContentView.findViewById(R.id.emailTV2);
        TextView totalCalories = mContentView.findViewById(R.id.calories);

        sh = new ViewModelProvider(this).get(SharedViewModel.class);
        LiveData<User> temp = sh.getUserByEmail(mAuth.getCurrentUser().getEmail());


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(main);
        TextView location = mContentView.findViewById(R.id.locationTV);
        getLastLocation(location);
        sh = new ViewModelProvider(this).get(SharedViewModel.class);

        temp.observe(main, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                myUser = user;
                name.setText(myUser.name);
                totalCalories.setText(main.getResources().getString(R.string.calories_burned) + " " + myUser.total_calories);


                level.setText(main.getResources().getString(R.string.level) + " " + myUser.level);
                myUserCalories = myUser.total_calories;

            }
        });


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if(myUserCalories != calories) {
                            UpdateLevelThread th = new UpdateLevelThread(getContext(), main, mAuth.getCurrentUser().getEmail(), myUserCalories);
                            th.start();
                            calories = myUser.total_calories;
                            break;
                        }
                    }
                }
            });



        email.setText(email.getText() + " : "+mAuth.getCurrentUser().getEmail());

        return mContentView;
    }



    public void updateUI(){
        Intent intent = new Intent(main.getApplicationContext(), AccessControl.class);
        mAuth.signOut();
        startActivity(intent);
    }

    private void handleAndroidPermissions() {
        permissionsRequestor = new PermissionsRequestor(main);
        permissionsRequestor.request(new PermissionsRequestor.ResultListener() {

            @Override
            public void permissionsGranted() {
                Log.e("TAG", "Permissions accepted by user.");
            }

            @Override
            public void permissionsDenied() {
                Log.e("TAG", "Permissions denied by user.");
            }
        });
    }

    Runnable updateLevel = new Runnable() {
        @Override
        public void run() {

            checkAndUpdate();
        }

        public void checkAndUpdate(){
            UpdateLevelThread th = new UpdateLevelThread(getContext(),main,mAuth.getCurrentUser().getEmail(),calories);
            th.start();
        }
    };

    public void getLastLocation(TextView locationValue) {
        if(ActivityCompat.checkSelfPermission(main, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            handleAndroidPermissions();
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(main, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Geocoder geocoder = new Geocoder(main);
                            List<Address> address = null;
                            try {
                                address = geocoder.getFromLocation(location.getLatitude(),
                                        location.getLongitude(), 1);
                                String message = address.get(0).getLocality() + ", " + address.get(0).getCountryName();
                                locationValue.setText(message);

                            } catch (IOException e) {
                                Log.e("Erro", e.getMessage());
                            }
                        }
                    }
                })
                .addOnFailureListener(main, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Erro", e.getMessage());
                    }
                });
    }
}