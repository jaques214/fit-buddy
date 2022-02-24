package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.mapview.MapError;
import com.here.sdk.mapview.MapScene;
import com.here.sdk.mapview.MapScheme;
import com.here.sdk.mapview.MapView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.MapItems;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.MapObjects;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.PermissionsRequestor;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.WeatherFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit.ObjectWrapper;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit.POI;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit.RetrofitGeoapifyInstance;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit.Weather;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.retrofit.WeatherDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements WeatherFragment.FragmentCommunication {
    public static final String EXTRA_MESSAGE = "pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.EXTRA";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest locationRequest;
    private PermissionsRequestor permissionsRequestor;
    private MapItems mapItems;
    private MapObjects mapObjects;
    private String circleStr;
    private String biasStr;
    private FirebaseAuth mAuth;
    private WeatherFragment fragment = new WeatherFragment();
    private Location lastLocation;
    private MapView mapView;
    private AutoCompleteTextView textView;
    private List<String> items = new ArrayList<>();
    private Button submit;
    private GeoCoordinates geoCoordinates;
    private List<GeoCoordinates> coordinates_list = new ArrayList<>();
    private FloatingActionButton refreshMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        mAuth = FirebaseAuth.getInstance();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F32D2D"));

        if (mAuth.getCurrentUser() == null) {
            updateUI();
        }

        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("FitBuddy");

        submit = findViewById(R.id.submit_button);
        refreshMap = findViewById(R.id.floating_button);
        textView = findViewById(R.id.autocomplete_location);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mapView = findViewById(R.id.mapView);
            mapView.onCreate(savedInstanceState);
            mapView.setFrameRate(24);
            getLastLocation();
        } else {
            Toast.makeText(MapActivity.this, "Sem internet", Toast.LENGTH_LONG).show();
        }
    }

    private void retrofitCall() {
        Geocoder geocoder = new Geocoder(this);
        Call<ObjectWrapper> apiInterface = RetrofitGeoapifyInstance.getApi("https://api.geoapify.com/v2/").getGymsByRadius("sport.fitness",
                circleStr, biasStr, 20, "95cdb77126424791bf392856023bf040");

        apiInterface.enqueue(new Callback<ObjectWrapper>() {
            @Override
            public void onResponse(@NonNull Call<ObjectWrapper> call, @NonNull Response<ObjectWrapper> response) {
                Log.d("Url", response.toString());
                ObjectWrapper poi = null;
                if (response.body() != null) {
                    poi = response.body();
                }
                if (poi.getFeatures().length == 0) {
                    Toast.makeText(MapActivity.this, "No gyms in the vicinity", Toast.LENGTH_SHORT).show();

                }

                int totalValue = poi.getFeatures().length;
                for (POI feature : poi.getFeatures()) {
                    double[] coordinates = feature.getGeometry().getCoordinates();
                    geoCoordinates = new GeoCoordinates(coordinates[1], coordinates[0]);

                    coordinates_list.add(geoCoordinates);
                    try {
                        List<Address> addr = geocoder.getFromLocation(coordinates[1], coordinates[0], 1);
                        items.add(addr.get(0).getThoroughfare());

                    } catch (IOException e) {
                        Log.e("Erro", e.getMessage());
                    }

                    if (mapItems != null) {
                        mapItems.addCircleMapMarker(geoCoordinates);
                        mapItems.addPOIMapMarker(geoCoordinates);
                    } else {
                        Toast.makeText(MapActivity.this, "Please refresh the map to show places", Toast.LENGTH_SHORT).show();
                    }
                }

                String msg = totalValue + " " + getResources().getString(R.string.toast_msg);
                Toast.makeText(MapActivity.this, msg, Toast.LENGTH_LONG).show();

                refreshMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mapItems.clearMap();
                        for (int i = 0; i < items.size(); i++) {
                            mapItems.addCircleMapMarker(coordinates_list.get(i));
                            mapItems.addPOIMapMarker(coordinates_list.get(i));
                        }
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mapItems.clearMap();
                        for (int i = 0; i < items.size(); i++) {
                            if(items.get(i).equals(textView.getText().toString())) {
                                mapItems.addRoute(new GeoCoordinates(lastLocation.getLatitude(), lastLocation.getLongitude()), coordinates_list.get(i));
                            }
                        }
                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
                textView.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<ObjectWrapper> call, @NonNull Throwable t) {
                Toast.makeText(MapActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void updateUI(Weather poi) {
        WeatherDetails[] weathers = poi.getWeather();
        String full_location = poi.getName() + ", " + poi.getSys().getCountry() + " - " + weathers[0].getDescription();
        String url = "http://openweathermap.org/img/w/" + weathers[0].getIcon() + ".png";
        double degree = poi.getMain().getTemp();
        String text = degree + "ºC";
        double min = poi.getMain().getTemp_min();
        double max = poi.getMain().getTemp_max();
        String interval = min + "ºC / " + max + "ºC";

        FragmentManager fg = getSupportFragmentManager();
        Bundle args = new Bundle();
        args.putString("location", full_location);
        args.putString("weather_icon", url);
        args.putString("temp", text);
        args.putString("temp_interval", interval);
        fragment.setArguments(args);
        fg.beginTransaction()
                .add(R.id.weatherFragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void weatherApi(double lat, double lon) {
        Call<Weather> apiInterface = RetrofitGeoapifyInstance.getApi("https://api.openweathermap.org/data/2.5/").getCurrentWeather(lat, lon, "metric", "db0a03df023db06846a3e0d37f423eaa");

        apiInterface.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(@NonNull Call<Weather> call, @NonNull Response<Weather> response) {
                Log.d("Url", response.toString());
                Weather poi;
                if (response.body() != null) {
                    poi = response.body();
                    updateUI(poi);
                }

            }

            @Override
            public void onFailure(@NonNull Call<Weather> call, Throwable t) {
                Log.d("Erro", t.getMessage());
                Toast.makeText(MapActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateUI() {
        Intent intent = new Intent(getApplicationContext(), AccessControl.class);
        String message = "Logout";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            handleAndroidPermissions();
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lastLocation = location;
                            weatherApi(lastLocation.getLatitude(), lastLocation.getLongitude());
                            circleStr = "circle:" + lastLocation.getLongitude() + "," + lastLocation.getLatitude() + ",10000";
                            biasStr = "proximity:" + lastLocation.getLongitude() + "," + lastLocation.getLatitude();
                            loadMapScene();
                            retrofitCall();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Erro", e.getMessage());
                    }
                });
    }

    public void startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            handleAndroidPermissions();
            return;
        }

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    circleStr = "circle:" + location.getLongitude() + "," + location.getLatitude() + ",10000";
                    biasStr = "proximity:" + location.getLongitude() + "," + location.getLatitude();
                }
            }
        };

        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void handleAndroidPermissions() {
        permissionsRequestor = new PermissionsRequestor(this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsRequestor.onRequestPermissionsResult(requestCode, grantResults);
    }

    public void loadMapScene() {
        // Load a scene from the HERE SDK to render the map with a map scheme.
        mapView.getMapScene().loadScene(MapScheme.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapError mapError) {
                if (mapError == null) {
                    mapObjects = new MapObjects(mapView);
                    mapItems = new MapItems(new GeoCoordinates(lastLocation.getLatitude(), lastLocation.getLongitude()), MapActivity.this, mapView);



                    mapObjects.showMapCircle(new GeoCoordinates(lastLocation.getLatitude(), lastLocation.getLongitude()));
                    mapItems.addLocationMapMarker(new GeoCoordinates(lastLocation.getLatitude(), lastLocation.getLongitude()));
                } else {
                    Log.d("TAG", "Loading map failed: mapError: " + mapError.name());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings: {
                mAuth.signOut();
                updateUI();
                return true;
            }
            case R.id.action_profile: {
                Intent intent = new Intent(getApplicationContext(), MainFragmentsActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.action_home: {
                return true;
            }
            default: {
                return super.onOptionsItemSelected(menuItem);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mapView.onDestroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mapView.onResume();
        }
    }
}