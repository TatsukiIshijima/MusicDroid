package com.example.tatsukiishijima.musicdroid.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/09/07.
 */
public class ArtistAdapter extends CursorTreeAdapter{

    private LayoutInflater mInflater;
    private Context mContext;
    private String mArtist;

    public ArtistAdapter(Cursor cursor, Context context) {
        super(cursor, context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context.getApplicationContext();
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {

        mArtist = groupCursor.getString(groupCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));

        String[] FILLED_PROJECTION = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
        };

        // ここでアーティストのアルバムのCursorを取得する
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, FILLED_PROJECTION,
                                       MediaStore.Audio.Media.ARTIST + " = ?",
                                       new String[] {mArtist}, null);
        return cursor;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.artist_group_item, null);
        return view;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        // 各種オブジェクトを作成する
        TextView artistNameTextView = (TextView) view.findViewById(R.id.artistGroupArtistName);

        // 各種アーティスト情報を取得する
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

        // アーティスト名をTextViewに設定する
        artistNameTextView.setText(artist);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.album_child_item, null);
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {

        // 各種オブジェクトを作成する
        ImageView albumImageView      = (ImageView) view.findViewById(R.id.artistChildImageView);
        TextView  albumNameTextView   = (TextView)  view.findViewById(R.id.artistChildAlbumName);

        //String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        /*
        String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

        if (albumArt != null && albumArt.length() > 0) {
            // アルバムアートを取得できた場合はImageViewに設定する
            Uri uri = Uri.parse(albumArt);
            albumImageView.setImageURI(uri);
        } else {
            // アルバムアートを取得できなかった場合はデフォルトの画像をImageViewに設定する
            albumImageView.setImageResource(R.drawable.albumart_default_icon);
        }
        */
        //albumNameTextView.setText(album);

    }
}
