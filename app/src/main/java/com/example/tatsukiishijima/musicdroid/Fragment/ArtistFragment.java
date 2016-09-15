package com.example.tatsukiishijima.musicdroid.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.tatsukiishijima.musicdroid.Adapter.ArtistAdapter;
import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/08/30.
 */
public class ArtistFragment extends Fragment {

    private Cursor mCursor;

    public static ArtistFragment newInstance() {
        ArtistFragment artistFragment = new ArtistFragment();
        return artistFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.artist_fragment, container, false);

        // オブジェクトの作成
        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.ArtistExpandListView);

        String[] FILLED_PROJECTION = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        };

        ContentResolver resolver = getActivity().getContentResolver();
        mCursor = resolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                                       FILLED_PROJECTION, null, null,"ARTIST  ASC");

        ArtistAdapter adapter = new ArtistAdapter(mCursor, getContext());

        expandableListView.setAdapter(adapter);

        /*
        ListView listView = (ListView) rootView.findViewById(R.id.artistListView);

        String[] FILLED_PROJECTION = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        };

        ContentResolver resolver = getActivity().getContentResolver();
        mCursor = resolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,                // 検索したいデータのURI(基本はEXTERNALでOK)
                FILLED_PROJECTION,                                                                   // 取得するカラム（情報）
                null,                                                                                // 以下条件指定
                null,
                "ARTIST  ASC");

        ArtistAdapter adapter = new ArtistAdapter(getContext(), mCursor);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumFragment fragment = AlbumFragment.newInstance();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                ft.replace(R.id.contents, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        */
        return rootView;
    }
}
