package com.example.aplikacja.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacja.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class LoginFragment extends Fragment {

    TextInputEditText editTextEmail, editTextPassword;
    AppCompatButton buttonLog;


    FirebaseAuth mAuth;


    ProgressBar progressBar;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            getParentFragmentManager().popBackStack();

            UserFragment userFragment = new UserFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, userFragment)
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        buttonLog = view.findViewById(R.id.btn_login);
//
        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.registerNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment lf = new RegisterFragment();
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();


                transaction.replace(R.id.fragment_container, lf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String emailText, passwordText;
                emailText = String.valueOf(editTextEmail.getText());
                passwordText = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(emailText)){
                    Toast.makeText(view.getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordText)){
                    Toast.makeText(view.getContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(view.getContext(), "Login successful",
                                            Toast.LENGTH_SHORT).show();

                                    getParentFragmentManager().popBackStack();

                                    UserFragment userFragment = new UserFragment();
                                    getParentFragmentManager().beginTransaction()
                                            .replace(R.id.fragment_container, userFragment)
                                            .commit();
                                } else {
                                    Toast.makeText(view.getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });

        return view;
    }

}