package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments.LoginFragment;

public class AccessControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_control);

        ActionBar actionBar;
        actionBar = getSupportActionBar();

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#F32D2D"));

        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("FitBuddy");

        LoginFragment loginf = new LoginFragment();
        Bundle args = new Bundle();
        loginf.setArguments(args);
        FragmentManager fg = getSupportFragmentManager();
        fg.beginTransaction()
                .add(R.id.FrameLayout, loginf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
    }
}