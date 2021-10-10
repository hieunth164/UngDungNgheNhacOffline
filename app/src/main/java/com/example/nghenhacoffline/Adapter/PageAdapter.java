package com.example.nghenhacoffline.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.nghenhacoffline.Fragment.FavoriteFragment;
import com.example.nghenhacoffline.Fragment.HistoryFragment;
import com.example.nghenhacoffline.Fragment.PlayListFragment;
import com.example.nghenhacoffline.Fragment.SongFragment;

public class PageAdapter extends FragmentPagerAdapter
{
    int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {
            case 0 : return new SongFragment();
            case 1 : return new FavoriteFragment();
            case 2 : return new HistoryFragment();
            case 3 : return new PlayListFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
