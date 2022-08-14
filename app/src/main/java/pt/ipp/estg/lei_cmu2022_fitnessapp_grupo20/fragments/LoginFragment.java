package pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
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
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.AccessControl;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.MapActivity;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.R;
import pt.ipp.estg.lei_cmu2022_fitnessapp_grupo20.database.SharedViewModel;

public class LoginFragment extends Fragment {

    private AccessControl main;
    private FirebaseAuth mAuth;
    private boolean flag;
    private String email;

    public LoginFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.main = (AccessControl) context;
        mAuth = FirebaseAuth.getInstance();

    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mContentView = inflater.inflate(R.layout.login_layout, container, false);
        EditText txtEmail = mContentView.findViewById(R.id.emailtxt);
        EditText txtPwd = mContentView.findViewById(R.id.pwdtxt);
        Button btnLogin = mContentView.findViewById(R.id.loginbtn);
        Button btnCriar = mContentView.findViewById(R.id.Criar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (txtEmail.getText().toString().equals("") || txtPwd.getText().toString().equals("") ){
                        Toast.makeText(mContentView.getContext(), "Please fill all the fields", Toast.LENGTH_LONG).show();
                    }else{
                       /* if(!flag){
                            String[] parts = txtEmail.getText().toString().split("@");
                            String part1 = parts[0];
                            DbAddUserOnLogin thread = new DbAddUserOnLogin(mContentView.getContext(),main,txtEmail.getText().toString(),txtPwd.getText().toString(),part1);
                            thread.start();
                            doAuth(txtEmail.getText().toString(), txtPwd.getText().toString());
                            updateUI();*/
                       // }else{
                            doAuth(txtEmail.getText().toString(), txtPwd.getText().toString());
                        }

                    }
           // }

        });

        btnCriar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerf = new  RegisterFragment();
                Bundle args = new Bundle();
                registerf.setArguments(args);
                FragmentManager fg = getParentFragmentManager();
                fg.beginTransaction()
                        .replace(R.id.FrameLayout, registerf)
                        .addToBackStack(null)
                        .commit();

            }
        });
        return mContentView;
    }

    public void doAuth(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            updateUI();
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(),"Auth failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void updateUI(){
        Intent intent = new Intent(main, MapActivity.class);
        startActivity(intent);
    }

    Runnable getUser = new Runnable() {
        @Override
        public void run() {
            getUserFromDB();
        }

        protected void getUserFromDB(){
            SharedViewModel sh = new ViewModelProvider(main).get(SharedViewModel.class);
            int temp = sh.getRepository().checkIfExists(email);

            flag = temp != 0;
        }
    };

}