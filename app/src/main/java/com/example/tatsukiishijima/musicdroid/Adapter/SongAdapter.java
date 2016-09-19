package com.example.tatsukiishijima.musicdroid.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.tatsukiishijima.musicdroid.R;

import java.util.Formatter;

/**
 * Created by TatsukiIshijima on 2016/09/19.
 */
public class SongAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    private Context mContext;

    public SongAdapter(Context context, Cursor c) {
        super(context, c);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context.getApplicationContext();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.song_item, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // 各種オブジェクトを作成する
        TextView songTitleTextView  = (TextView) view.findViewById(R.id.songTitle);
        TextView songArtistTextView = (TextView) view.findViewById(R.id.songArtistName);
        TextView songDurTextView   = (TextView) view.findViewById(R.id.songDuration);

        // 曲の詳細情報を取得する
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        Long duration = Long.parseLong(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));

        // 曲のトラック時間を算出する
        Formatter fmt = new Formatter();
        long sec = duration / 1000;
        String dur = fmt.format("%1$2d:%2$02d", new Object[] {sec / 60, sec % 60}).toString();

        songTitleTextView.setText(title);
        songArtistTextView.setText(artist);
        songDurTextView.setText(dur);
    }
}
