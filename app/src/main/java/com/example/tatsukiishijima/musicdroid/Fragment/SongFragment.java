package com.example.tatsukiishijima.musicdroid.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/08/30.
 */
public class SongFragment extends Fragment {

    public SongFragment() {

    }

    public static SongFragment newInstance() {
        SongFragment songFragment = new SongFragment();
        return songFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.song_fragment, container, false);

        return rootView;
    }
}
