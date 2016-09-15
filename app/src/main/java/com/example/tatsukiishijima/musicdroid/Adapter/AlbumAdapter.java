package com.example.tatsukiishijima.musicdroid.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/09/06.
 */
public class AlbumAdapter extends CursorTreeAdapter {

    private LayoutInflater mInflater;
    private String mAlbumKey;
    private Context mContext;

    public AlbumAdapter(Cursor cursor, Context context) {
        super(cursor, context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context.getApplicationContext();
    }

    // groupCursorは親のCursor
    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {

        mAlbumKey = groupCursor.getString(groupCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));

        // ここでアルバムに含まれる曲のCursorを取得する
        ContentResolver resolver = mContext.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                                       MediaStore.Audio.Media.ALBUM_KEY + " = ?",
                                       new String[] {mAlbumKey}, MediaStore.Audio.Media.TRACK);
        return cursor;
    }

    @Override
    public View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.album_group_item, null);
        return view;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        // 各種オブジェクトを作成する
        ImageView albumImageView      = (ImageView) view.findViewById(R.id.albumGroupImageView);
        TextView  albumNameTextView   = (TextView)  view.findViewById(R.id.albumGroupAlbumName);
        TextView  albumArtistTextView = (TextView)  view.findViewById(R.id.albumGroupArtistName);

        // 各種アルバム情報を取得する
        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
        String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

        if (albumArt != null && albumArt.length() > 0) {
            // アルバムアートを取得できた場合はImageViewに設定する
            Uri uri = Uri.parse(albumArt);
            albumImageView.setImageURI(uri);
        } else {
            // アルバムアートを取得できなかった場合はデフォルトの画像をImageViewに設定する
            albumImageView.setImageResource(R.drawable.albumart_default_icon);
        }

        // アルバム情報をTextViewに設定する
        albumNameTextView.setText(album);
        albumArtistTextView.setText(artist);
    }

    @Override
    public View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.album_child_item, null);
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        // 各種オブジェクトを作成する
        TextView trackNameTextView = (TextView) view.findViewById(R.id.albumChildTrackName);

        // 各種アルバム情報を取得する
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));

        // アルバム情報をTextViewに設定する
        trackNameTextView.setText(title);
    }
}
