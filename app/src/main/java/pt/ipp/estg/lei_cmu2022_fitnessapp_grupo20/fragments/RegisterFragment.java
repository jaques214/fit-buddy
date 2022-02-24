package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.models.User.User;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.threads.DbAddUserThread;

public class RegisterFragment extends Fragment {

    private AccessControl main;
    private FirebaseAuth mAuth;
    private SharedViewModel sh;
    public static final String EXTRA_MESSAGE = "pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.EXTRA";

    public RegisterFragment() {
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.main =(AccessControl) context;
        mAuth = FirebaseAuth.getInstance();
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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


        View mContentView = inflater.inflate(R.layout.register_layout, container, false);
        EditText txtUser = mContentView.findViewById(R.id.txtName);
        EditText txtPwd = mContentView.findViewById(R.id.txtpwd);
        EditText txtEmail = mContentView.findViewById(R.id.txtEmail);
        Button btnLogin = mContentView.findViewById(R.id.btnLogin);
        Button btnCriar = mContentView.findViewById(R.id.btnCriar);
        LifecycleOwner fds = getViewLifecycleOwner();


        sh = new ViewModelProvider(main).get(SharedViewModel.class);
        LiveData<List<User>> user = sh.getUsers();

        user.observe(main, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                String x = "X : " + user.getValue().size();
            }
        });


        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtEmail.getText().toString().equals("") || txtPwd.getText().toString().equals("") || txtUser.getText().toString().equals("")) {
                    Toast.makeText(mContentView.getContext(), "Please fill all the fields...", Toast.LENGTH_SHORT).show();
                } else {
                        mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPwd.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            DbAddUserThread threadAdd = new DbAddUserThread(mContentView.getContext(),main,txtEmail.getText().toString(),txtPwd.getText().toString(),txtUser.getText().toString());
                                            threadAdd.start();
                                            Toast.makeText(mContentView.getContext(), "you are authenticated successfully...", Toast.LENGTH_SHORT).show();
                                            updateUI();
                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(mContentView.getContext(), "Failed in : " + message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }

        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              LoginFragment loginf = new  LoginFragment();
                Bundle args = new Bundle();
                loginf.setArguments(args);
                FragmentManager fg = getParentFragmentManager();
                fg.beginTransaction()
                        .replace(R.id.FrameLayout, loginf)
                        .addToBackStack(null)
                        .commit();

            }
        });



        return mContentView;
    }


    public void updateUI(){
        Intent intent = new Intent(main, MapActivity.class);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String message = currentUser.getEmail();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}