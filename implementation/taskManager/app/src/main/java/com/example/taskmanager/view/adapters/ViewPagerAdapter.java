package com.example.taskmanager.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.taskmanager.view.fragments.CalendarViewFragment;
import com.example.taskmanager.view.fragments.ListViewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    Fragment currentFragment;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new CalendarViewFragment();
            default:
                return new ListViewFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
