package com.example.aplikacja.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.aplikacja.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserFragment extends Fragment {


    FirebaseAuth auth;
    Button logout;
    TextView username_details;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        auth = FirebaseAuth.getInstance();
        logout = view.findViewById(R.id.logout);
        username_details = view.findViewById(R.id.display_username);

        user = auth.getCurrentUser();
        if (user == null){
            getParentFragmentManager().popBackStack();

            LoginFragment loginFragment = new LoginFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, loginFragment)
                    .commit();
        }else{
            username_details.setText(user.getEmail());
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getParentFragmentManager().popBackStack();

                LoginFragment loginFragment = new LoginFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, loginFragment)
                        .commit();
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        // Get the FragmentManager
        FragmentManager fragmentManager = getParentFragmentManager();

        // Start a FragmentTransaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the new fragment
        transaction.replace(R.id.frameLayout, fragment);

        // Add the transaction to the back stack
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
    public View getViewFromFragment(Fragment fragment) {
        return fragment.getView();
    }
}