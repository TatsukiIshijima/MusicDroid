package com.example.tatsukiishijima.musicdroid;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tatsukiishijima.musicdroid.Adapter.AlbumAdapter;
import com.example.tatsukiishijima.musicdroid.Fragment.AlbumFragment;
import com.example.tatsukiishijima.musicdroid.Fragment.ArtistFragment;
import com.example.tatsukiishijima.musicdroid.Fragment.RootFragment;
import com.example.tatsukiishijima.musicdroid.Fragment.SongFragment;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, AlbumFragment.OnMusicSelectedListener, View.OnClickListener{

    private IMusicService mMusicPlayer;
    private Handler mHandler = new Handler();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // MusicServiceからのコールバックメソッドを実装する
    private IMusicServiceCallback.Stub mCallback = new IMusicServiceCallback.Stub() {
        @Override
        public void update() throws RemoteException {
            mHandler.post(new Runnable() {
                public void run() {
                    TextView trackTextView = (TextView) findViewById(R.id.playTrack);
                    TextView artistTextView = (TextView) findViewById(R.id.playArtist);
                    TextView albumTextView = (TextView) findViewById(R.id.playAlbum);
                    ImageButton playButton = (ImageButton) findViewById(R.id.playButton);

                    try {
                        // タイトル、アーティストの表示を更新する
                        trackTextView.setText(mMusicPlayer.getTitle());
                        artistTextView.setText(mMusicPlayer.getArtist());
                        albumTextView.setText(mMusicPlayer.getAlbum());

                        // 再生中だった場合はボタンのキャプションをPauseにする
                        if (mMusicPlayer.isPlaying()) {
                            playButton.setImageResource(R.drawable.pause_button);
                        } else {
                            playButton.setImageResource(R.drawable.play_button);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicPlayer = IMusicService.Stub.asInterface(service);

            try {
                mMusicPlayer.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicPlayer = null;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // MusicServiceに接続する
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMusicPlayer != null) {
            try {
                mMusicPlayer.unregisterCallback(mCallback);
                mMusicPlayer = null;

                // ノーティフィケーション操作のバッファをクリアする
                //mRequestNotificationAction = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = SongFragment.newInstance();
                        break;
                    case 1:
                        fragment = AlbumFragment.newInstance();
                        break;
                    case 2:
                        fragment = RootFragment.newInstance();
                        break;
                    default:
                        break;

                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.music_white).setText("MUSIC");
        tabLayout.getTabAt(1).setIcon(R.drawable.album_white).setText("ALBUM");
        tabLayout.getTabAt(2).setIcon(R.drawable.artist_white).setText("ARTIST");

        ImageButton mPlayButton = (ImageButton) findViewById(R.id.playButton);
        ImageButton mRewindButton = (ImageButton) findViewById(R.id.rewindButton);
        ImageButton mForwardButton = (ImageButton) findViewById(R.id.forwardButton);

        mPlayButton.setOnClickListener(this);
        mRewindButton.setOnClickListener(this);
        mForwardButton.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    // 曲一覧で選択された曲を再生する
    public void onSelectMusic(String albumKey, int track) {
        try {
            mMusicPlayer.open(albumKey);
            mMusicPlayer.play(track);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // 1つ前の曲を再生する
        if (v.getId() == R.id.rewindButton) {
            try {
                mMusicPlayer.prev();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // 現在選択されている曲を再生する
        } else if (v.getId() == R.id.playButton) {
            try {
                if (mMusicPlayer.isPlaying()) {
                    mMusicPlayer.pause();
                } else {
                    mMusicPlayer.play(mMusicPlayer.getPosition());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            // 次の曲を再生する
        } else if (v.getId() == R.id.forwardButton) {
            try {
                mMusicPlayer.next();

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
