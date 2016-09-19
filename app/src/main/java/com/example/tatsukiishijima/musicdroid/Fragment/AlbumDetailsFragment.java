package com.example.tatsukiishijima.musicdroid.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tatsukiishijima.musicdroid.Adapter.SongAdapter;
import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/09/19.
 */
public class AlbumDetailsFragment extends Fragment {

    private Cursor mCursor;

    public AlbumDetailsFragment() {

    }

    public static AlbumDetailsFragment newInstance() {
        AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
        return albumDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.album_details_fragment, container, false);

        // 各種オブジェクトを作成する
        ImageView albumImage = (ImageView) rootView.findViewById(R.id.albumDetails_AlbumImage);
        TextView albumName   = (TextView) rootView.findViewById(R.id.albumDetails_AlbumName);
        TextView artistName  = (TextView) rootView.findViewById(R.id.albumDetails_ArtistName);
        ListView listView    = (ListView) rootView.findViewById(R.id.albumDetails_listView);

        // 値を受け取る
        String albumKey = getArguments().getString("AlbumKey");
        String album    = getArguments().getString("Album");
        String artist   = getArguments().getString("Artist");
        String albumArt = getArguments().getString("AlbumArt");

        if (albumArt != null && albumArt.length() > 0) {
            // アルバムアートを取得できた場合はImageViewに設定する
            Uri uri = Uri.parse(albumArt);
            albumImage.setImageURI(uri);
        } else {
            // アルバムアートを取得できなかった場合はデフォルトの画像をImageViewに設定する
            albumImage.setImageResource(R.drawable.albumart_default_icon);
        }

        albumName.setText(album);
        artistName.setText(artist);

        ContentResolver resolver = getContext().getContentResolver();
        mCursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                 null,
                                 MediaStore.Audio.Media.ALBUM_KEY + " = ?",
                                 new String[] {albumKey},
                                 MediaStore.Audio.Media.TRACK);

        SongAdapter songAdapter = new SongAdapter(getContext(), mCursor);
        listView.setAdapter(songAdapter);

        return rootView;
    }
}
