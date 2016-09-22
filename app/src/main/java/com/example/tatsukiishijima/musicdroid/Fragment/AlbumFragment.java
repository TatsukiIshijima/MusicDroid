package com.example.tatsukiishijima.musicdroid.Fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.tatsukiishijima.musicdroid.Adapter.AlbumAdapter;
import com.example.tatsukiishijima.musicdroid.IMusicService;
import com.example.tatsukiishijima.musicdroid.R;

/**
 * Created by TatsukiIshijima on 2016/08/30.
 */
public class AlbumFragment extends Fragment{

    private Cursor mCursor;
    private String mAlbumKey;
    private OnMusicSelectedListener mMusicSelectedListener;

    public AlbumFragment() {

    }

    public static AlbumFragment newInstance() {
        AlbumFragment albumFragment = new AlbumFragment();
        return albumFragment;
    }

    /***
     * 曲が選択されたときに呼ばれる
     */
    public interface OnMusicSelectedListener {
        public void onSelectMusic(String albumKey, int track);
    }

    /***
     * OnMusicSelectedListenerを登録する
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mMusicSelectedListener = (OnMusicSelectedListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.album_fragment, container, false);

        // オブジェクトの作成
        ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.AlbumExpandListView);

        ContentResolver resolver = getActivity().getContentResolver();
        // アルバム一覧を取得するためのCursorの作成
        mCursor = resolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,                      // 検索したいデータのURI
                                 null,                                                              // 取得したいカラム
                                 null,                                                              // 検索条件
                                 null,                                                              // 検索条件パラメータ
                                 "ALBUM ASC");                                                      // ソート条件
        // アダプターの作成
        final AlbumAdapter albumAdapter = new AlbumAdapter(mCursor, getContext());

        expandableListView.setAdapter(albumAdapter);

        // 子リストのクリックイベント
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // クリックされた場所の情報を取り出す
                CursorWrapper cw = (CursorWrapper) albumAdapter.getGroup(groupPosition);
                mAlbumKey = cw.getString(cw.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY));
                mMusicSelectedListener.onSelectMusic(mAlbumKey, childPosition);
                return true;
            }
        });

        return rootView;
    }
}
