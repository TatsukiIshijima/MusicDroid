package com.example.tatsukiishijima.musicdroid.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.tatsukiishijima.musicdroid.Adapter.AlbumAdapter;
import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/08/30.
 */
public class AlbumFragment extends Fragment{

    private Cursor mCursor;

    public AlbumFragment() {

    }

    public static AlbumFragment newInstance() {
        AlbumFragment albumFragment = new AlbumFragment();
        return albumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.album_fragment, container, false);

        // オブジェクトの作成
        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.AlbumExpandListView);

        String[] FILLED_PROJECTION = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
        };
        ContentResolver resolver = getActivity().getContentResolver();
        // アルバム一覧を取得するためのCursorの作成
        mCursor = resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                FILLED_PROJECTION, null, null, "ALBUM ASC");
        // アダプターの作成
        AlbumAdapter albumAdapter = new AlbumAdapter(mCursor, getContext());

        expandableListView.setAdapter(albumAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(),String.valueOf(groupPosition) + String.valueOf(childPosition), Toast.LENGTH_SHORT).show();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.root_frame, new ArtistFragment());
                ft.setTransition(ft.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            }
        });

        return rootView;
    }
}
