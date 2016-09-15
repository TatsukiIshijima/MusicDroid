package com.example.tatsukiishijima.musicdroid.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/09/15.
 */
public class RootFragment extends Fragment {

    private static final String TAG = "RootFragment";

    public RootFragment() {

    }

    public static RootFragment newInstance() {
        RootFragment rootFragment = new RootFragment();
        return rootFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.root_fragment, container, false);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.root_frame, new AlbumFragment());
        ft.commit();
        return rootView;
    }
}
