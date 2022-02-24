package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.AutomaticTrackingFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.MainPageFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.ManualTrackingFragment;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.recyclerviews.ExerciseAdapter;

public class MainFragmentsActivity extends AppCompatActivity implements ExerciseAdapter.TrackingOnClick {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragments);

        mAuth = FirebaseAuth.getInstance();
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        if (mAuth.getCurrentUser() == null) {
            updateUI();
        }

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F32D2D"));

        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("FitBuddy");

        MainPageFragment mainf = new MainPageFragment();
        Bundle args = new Bundle();
        mainf.setArguments(args);
        FragmentManager fg = getSupportFragmentManager();
        fg.beginTransaction()
                .add(R.id.Frame, mainf)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onBackPressed() {
    }

    public void updateUI() {
        Intent intent = new Intent(getApplicationContext(), AccessControl.class);
        startActivity(intent);
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
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
            }
            default: {
                return super.onOptionsItemSelected(menuItem);
            }
        }
    }

    @Override
    public void onClick(int position) {
        if (position == 0) {
            ManualTrackingFragment mtf = new ManualTrackingFragment();
            FragmentManager fg = getSupportFragmentManager();
            fg.beginTransaction()
                    .replace(R.id.Frame, mtf, "mtf")
                    .addToBackStack(null)
                    .commit();
        } else {
            AutomaticTrackingFragment mtf = new AutomaticTrackingFragment();
            FragmentManager fg = getSupportFragmentManager();
            fg.beginTransaction()
                    .replace(R.id.Frame, mtf)
                    .addToBackStack(null)
                    .commit();
        }

    }

}