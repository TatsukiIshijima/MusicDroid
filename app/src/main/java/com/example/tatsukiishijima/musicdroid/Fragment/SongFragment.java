package com.example.tatsukiishijima.musicdroid.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.tatsukiishijima.musicdroid.Adapter.SongAdapter;
import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/08/30.
 */
public class SongFragment extends Fragment {

    private Cursor mCursor;

    public SongFragment() {

    }

    public static SongFragment newInstance() {
        SongFragment songFragment = new SongFragment();
        return songFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.song_fragment, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.songListView);

        ContentResolver resolver = getActivity().getContentResolver();
        // 曲一覧を取得するためのCursorの作成
        mCursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                 null,
                                 MediaStore.Audio.Media.DURATION + " > 5000",                       // 5秒より短い曲を除く
                                 null,
                                 "ARTIST ASC");

        // アダプターの生成
        SongAdapter songAdapter = new SongAdapter(getContext(), mCursor);

        listView.setAdapter(songAdapter);

        return rootView;
    }
}
