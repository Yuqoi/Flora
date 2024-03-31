package com.example.aplikacja.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacja.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterFragment extends Fragment {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
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
                    .replace(R.id.frameLayout, userFragment)
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        editTextEmail = view.findViewById(R.id.email);
        editTextPassword = view.findViewById(R.id.password);
        buttonReg = view.findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment lf = new LoginFragment();
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.frameLayout, lf);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String emailText, passwordText;
                emailText = String.valueOf(editTextEmail.getText());
                passwordText = String.valueOf(editTextPassword.getText());
                if (TextUtils.isEmpty(emailText)){
                    Toast.makeText(view.getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if (TextUtils.isEmpty(passwordText)){
                    Toast.makeText(view.getContext(), "Enter password", Toast.LENGTH_SHORT).show();
                    return ;
                }

                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {

                                    Toast.makeText(view.getContext(), "Account created",
                                            Toast.LENGTH_SHORT).show();

                                    getParentFragmentManager().popBackStack();

                                    UserFragment userFragment = new UserFragment();
                                    getParentFragmentManager().beginTransaction()
                                            .replace(R.id.frameLayout, userFragment)
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