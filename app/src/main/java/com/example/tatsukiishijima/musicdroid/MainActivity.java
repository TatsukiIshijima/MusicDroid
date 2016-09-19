package com.example.tatsukiishijima.musicdroid;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tatsukiishijima.musicdroid.Fragment.AlbumFragment;
import com.example.tatsukiishijima.musicdroid.Fragment.ArtistFragment;
import com.example.tatsukiishijima.musicdroid.Fragment.RootFragment;
import com.example.tatsukiishijima.musicdroid.Fragment.SongFragment;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = SongFragment.newInstance();
                        break;
                    case 1:
                        fragment = AlbumFragment.newInstance();
                        break;
                    case 2:
                        fragment = RootFragment.newInstance();
                        break;
                    default:
                        break;

                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.music_white).setText("MUSIC");
        tabLayout.getTabAt(1).setIcon(R.drawable.album_white).setText("ALBUM");
        tabLayout.getTabAt(2).setIcon(R.drawable.artist_white).setText("ARTIST");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
