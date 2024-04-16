package com.example.aplikacja.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.aplikacja.R;
import com.example.aplikacja.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class GardenFragment extends Fragment {

    Button addFlowersBtn;

    private PopupWindow popupWindow;
    private ViewGroup rootView;
    private View popupView;

    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // we have to check if user is logged in
        View view = inflater.inflate(R.layout.fragment_garden, container, false);



        addFlowersBtn = view.findViewById(R.id.addFlowersBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

//        Find root layout


        addFlowersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowersFragment fragment = new FlowersFragment();
                FragmentManager manager = getParentFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.frameLayout, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }


}