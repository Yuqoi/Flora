package com.example.aplikacja.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.aplikacja.fragments.InformacjeFragment;
import com.example.aplikacja.fragments.NotatkiFragment;
import com.example.aplikacja.fragments.PrzypomnieniaFragment;


public class FragmentPageAdapter extends FragmentStateAdapter {


    public FragmentPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch ( position ){
            case 0:
                return new InformacjeFragment();
            case 1:
                return new NotatkiFragment();
            case 2:
                return new PrzypomnieniaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
