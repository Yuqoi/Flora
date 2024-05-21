package com.example.aplikacja.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplikacja.R;
import com.example.aplikacja.activities.InformacjeAplikacjaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;


public class UserFragment extends Fragment {


    ConstraintLayout aboutApplication;

    AppCompatButton logout;
    AppCompatButton usunKonto;
    AppCompatButton zapisz;
    TextView username;
    TextView userEmail;

    EditText changeUsername;
    EditText oldPassword;
    EditText changePassword1;
    EditText changePassword2;

    FirebaseAuth auth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    String userID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        username = view.findViewById(R.id.profile_username);
        userEmail = view.findViewById(R.id.profile_email);
        logout = view.findViewById(R.id.profile_logout);
        zapisz = view.findViewById(R.id.profile_zapisz);
        changeUsername = view.findViewById(R.id.profile_change_username);
        changePassword1 = view.findViewById(R.id.profile_change_password1);
        changePassword2 = view.findViewById(R.id.profile_change_password2);
        aboutApplication = view.findViewById(R.id.profile_aplication_information);
        oldPassword = view.findViewById(R.id.profile_old_password);
        usunKonto = view.findViewById(R.id.user_usun_konto);
//        database
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();


        if (user == null){
            getParentFragmentManager().popBackStack();

            LoginFragment loginFragment = new LoginFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, loginFragment)
                    .commit();
        }else{
            userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
            if (!userID.isEmpty()) {
                DocumentReference documentReference = fStore.collection("users").document(userID);

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null && value.exists()){
                            username.setText(value.getString("username"));
                            userEmail.setText(value.getString("email"));

                        }
                    }
                });
            }
        }

        aboutApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InformacjeAplikacjaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        zapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changePassword1.getText().toString().isEmpty() && !changePassword2.getText().toString().isEmpty()){
                    changePassword(user, user.getEmail(), oldPassword.getText().toString());
                }
                if(changeUsername.getText().toString().length() <= 20 && !username.getText().toString().isEmpty()){
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    documentReference.update("username", changeUsername.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "zmieniono nazwe", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        usunKonto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Czy na pewno chcesz usunąć konto");
                builder.setTitle("Usuń konto");
                builder.setCancelable(false);


                builder.setNegativeButton("Nie", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                builder.setPositiveButton("Tak", (DialogInterface.OnClickListener) (dialog, which) -> {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Pomyślnie usunięto konto", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Nie udało się usunąć konta", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getParentFragmentManager().popBackStack();
                LoginFragment loginFragment = new LoginFragment();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, loginFragment)
                        .commit();
            }
        });
        return view;
    }

    private void changePassword(FirebaseUser user, String email, String oldPass){
        String firstPass = changePassword1.getText().toString();
        String secondPass = changePassword2.getText().toString();
        if (firstPass.equals(secondPass)) {
            if (user != null) {
                AuthCredential credential = EmailAuthProvider.getCredential(email,oldPass);
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            user.updatePassword(secondPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(!task.isSuccessful()){
                                        Toast.makeText(getContext(), "Nie udało się zmienić", Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getContext(), "Zmieniono pozytywnie", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(getContext(),  "Nie udało się rozpoznanie", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(),  "Nie pasują do siebie hasła", Toast.LENGTH_LONG).show();
            }
        }
    }

}