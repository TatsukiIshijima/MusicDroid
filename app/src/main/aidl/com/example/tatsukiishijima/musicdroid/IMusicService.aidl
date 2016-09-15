// IMusicService.aidl
package com.example.tatsukiishijima.musicdroid;

// Declare any non-default types here with import statements
import com.example.tatsukiishijima.musicdroid.IMusicServiceCallback;

interface IMusicService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void play(int track);                                                                           // 再生する
    void pause();                                                                                   // 一時停止
    void prev();                                                                                    // 前の曲を再生する
    void next();                                                                                    // 次の曲を再生する
    boolean isPlaying();                                                                            // 再生中か問い合わせる
    void open(String key);                                                                          // アルバムをオープンする
    String getTitle();                                                                              // 再生中の曲のタイトルを取得する
    String getArtist();                                                                             // 再生中の曲のアーティスト名を取得する
    String getAlbum();                                                                              // 再生中の曲のアルバム名を取得する
    int getPosition();                                                                              // 現在再生中の曲(mCursorの位置)を取得する

    void registerCallback(IMusicServiceCallback callback);                                          // コールバックメソッドを登録する
    void unregisterCallback(IMusicServiceCallback callback);                                        // コールバックメソッドを登録解除する
}
