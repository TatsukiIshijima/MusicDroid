package com.example.tatsukiishijima.musicdroid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.io.IOException;

/**
 * Created by TatsukiIshijima on 2016/09/04.
 */
public class MusicService extends Service implements MediaPlayer.OnCompletionListener{

    private final RemoteCallbackList<IMusicServiceCallback> mCallbackList = new RemoteCallbackList<IMusicServiceCallback>();
    private Cursor mCursor;
    private String mAlbumKey = null;
    private MediaPlayer mPlayer;

    /***
     * Notificationに渡すPendingIntentを作成する
     */
    PendingIntent getDialogPendingIntent(String action) {
        return PendingIntent.getActivity(this, 0, new Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), 0);
    }

    /***
     * Notificationを表示する
     */
    public void makeNotify() {

        final Resources res = getResources();
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Bitmap largeIconTemp;

        // 既に表示されているNotificationを非表示にする
        notificationManager.cancel(Consts.NOTIFICATION_DEFAULT);

        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setAutoCancel(true).setTicker(mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)))
                .setContentIntent(getDialogPendingIntent(Consts.ACTION_PLAY));

        RemoteViews layout = new RemoteViews(getPackageName(), R.layout.notification);
        layout.setTextViewText(R.id.notification_title, mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));

        // ボタンにPendingIntentを設定する
        layout.setOnClickPendingIntent(R.id.play_button_notification, getDialogPendingIntent(Consts.ACTION_PLAY));
        layout.setOnClickPendingIntent(R.id.prev_button_notification, getDialogPendingIntent(Consts.ACTION_PREV));
        layout.setOnClickPendingIntent(R.id.next_button_notification, getDialogPendingIntent(Consts.ACTION_NEXT));

        builder.setContent(layout);

        // アルバムアートを設定する
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String id = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media._ID));
        Uri uri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

        mmr.setDataSource(this, uri);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) {
            largeIconTemp = BitmapFactory.decodeByteArray(data, 0, data.length);
        } else {
            largeIconTemp = BitmapFactory.decodeResource(res, R.drawable.notification_default_largeicon);
        }

        Bitmap largeIcon = Bitmap.createScaledBitmap(largeIconTemp,
                res.getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                res.getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                false);
        largeIconTemp.recycle();
        builder.setLargeIcon(largeIcon);

        // Notificationを表示する
        notificationManager.notify(Consts.NOTIFICATION_DEFAULT, builder.getNotification());
    }

    /***
     * アプリケーションにUI更新を通知する
     */
    public void uiUpdate() {
        int n = mCallbackList.beginBroadcast();

        for (int i=0; i<n; i++) {
            try {
                mCallbackList.getBroadcastItem(i).update();
            } catch (RemoteException e) {
                //
            }
        }

        mCallbackList.finishBroadcast();
    }

    /***
     * 現在設定されている曲を再生する
     */
    void mpPlay() {
        // MediaPlayerが初期化されていれば再生を開始する
        if (mPlayer != null) {
            mPlayer.start();
        }
        // 音楽情報の表示を更新する
        uiUpdate();

        // Notificationを表示する
        makeNotify();
    }

    /***
     * 現在再生中の曲を一時停止する
     */
    void mpPause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
        uiUpdate();
    }

    /***
     * MediaPlayerをリセットする
     */
    void mpReset() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.reset();
        }
    }

    /***
     * 1つ前の曲を再生する
     */
    void mpPrev() {
        if (mPlayer != null && mPlayer.getCurrentPosition() > 5000) {
            mpReset();
            setTrack();
            mpPlay();
        } else if (mCursor != null && mCursor.moveToPrevious()) {
            mpReset();
            setTrack();
            mpPlay();
        } else {
            mCursor.moveToNext();
        }
    }

    /***
     * 1つ後の曲を再生する
     */
    void mpNext() {
        if (mCursor.moveToNext()) {
            mpReset();
            setTrack();
            mpPlay();
        } else {
            mCursor.moveToPrevious();
        }
    }

    /***
     * MediaPlayerに曲を設定する
     */
    void setTrack() {
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
            mPlayer.setOnCompletionListener(this);
            mPlayer.setLooping(false);
        }

        String id = mCursor.getString(mCursor
                .getColumnIndex(MediaStore.Audio.Media._ID));

        Uri uri = Uri.withAppendedPath(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

        try {
            mPlayer.setDataSource(this, uri);
            mPlayer.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * クライアントからのリクエストを受け付けるインタフェース
     */
    private IMusicService.Stub mBinder = new IMusicService.Stub() {

        // 再生する
        @Override
        public void play(int track) throws RemoteException {
            // リクエストされたトラック番号と再生中のトラック番号が異なれば再設定する
            if (track != mCursor.getPosition()) {
                mpReset();
                mCursor.moveToPosition(track);
                setTrack();
            }
            mpPlay();
        }

        // 一時停止する
        @Override
        public void pause() throws RemoteException {
            mpPause();
        }

        // 前の曲を再生する
        @Override
        public void prev() throws RemoteException {
            mpPrev();
        }

        // 次の曲を再生する
        @Override
        public void next() throws RemoteException {
            mpNext();
        }

        // 現在再生中か問い合わせる
        @Override
        public boolean isPlaying() throws RemoteException {
            if (mPlayer != null) {
                return mPlayer.isPlaying();
            }
            return false;
        }

        // アルバムをオープンする
        @Override
        public void open(String key) throws RemoteException {
            mAlbumKey = key;

            mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                                 null,
                                                 MediaStore.Audio.Media.ALBUM_KEY + " = ?",
                                                 new String[] { mAlbumKey },
                                                 MediaStore.Audio.Media.TRACK); // TRACKをキーに昇順にソートする

            mCursor.moveToPosition(-1);
        }

        // 曲のタイトル情報を取得する
        @Override
        public String getTitle() throws RemoteException {
            return mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        }

        // 曲のアーティスト情報を取得する
        @Override
        public String getArtist() throws RemoteException {
            return mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        }

        // 曲のアルバム情報を取得する
        @Override
        public String getAlbum() throws RemoteException {
            return mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        }

        // 現在再生中の曲(mCursorの位置)を取得する
        @Override
        public int getPosition() throws RemoteException {
            return mCursor.getPosition();
        }

        // アプリケーションのUI更新メソッドを登録する
        @Override
        public void registerCallback(IMusicServiceCallback callback) throws RemoteException {
            mCallbackList.register(callback);
        }

        // アプリケーションのUI更新メソッドを登録解除する
        @Override
        public void unregisterCallback(IMusicServiceCallback callback) throws RemoteException {
            mCallbackList.unregister(callback);
        }
    };


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
    }

    // 1曲再生し終えたら呼ばれる
    @Override
    public void onCompletion(MediaPlayer mp) {
        // 次の曲へ移動する
        mpNext();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
