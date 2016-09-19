package com.example.tatsukiishijima.musicdroid.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorWrapper;
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
import android.widget.Toast;

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

        ContentResolver resolver = getActivity().getContentResolver();
        mCursor = resolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,                     // 検索したいデータのURI
                                 null,                                                              // 取得したいカラム
                                 null,                                                              // 検索条件
                                 null,                                                              // 検索条件パラメータ
                                 "ARTIST ASC");                                                     // ソート条件

        final ArtistAdapter adapter = new ArtistAdapter(mCursor, getContext());

        expandableListView.setAdapter(adapter);
        /*
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                // クリックされた場所の情報を取り出す
                CursorWrapper cw = (CursorWrapper) adapter.getChild(groupPosition, childPosition);
                String albumKey = cw.getString(cw.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
                String album    = cw.getString(cw.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String artist   = cw.getString(cw.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                String albumArt = cw.getString(cw.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                // 画面遷移 & 値渡し
                Fragment fragment = new AlbumDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("AlbumKey", albumKey);
                bundle.putString("Album", album);
                bundle.putString("Artist", artist);
                bundle.putString("AlbumArt", albumArt);
                fragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.root_frame, fragment);
                ft.setTransition(ft.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();

                return true;
            }
        });
        */
        return rootView;
    }
}
