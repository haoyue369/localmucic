package com.dyql.media.test.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;

public class MusicService extends BaseService{
    private static final String TAG = MusicService.class.getSimpleName();
    private final IBinder binder = new MyMusicBinder();
    //音频媒体，音频焦点，音量管理初始化,wifi锁
    private MediaPlayer mMediaPlayer;
    private AudioAttributes mAudioAttributes;
    private AudioFocusRequest mFocusRequest;
    private AudioManager mAudioManager ;
    private WifiManager.WifiLock mWifiLock;
    private OnAudioFocusChangeListener mAudioFocusChangeListener;
    //音乐播放
    private boolean isFirstPlay = true;
    private int mCurrentPosition = 0;
    private OnErrorListener mOnErrorListener;
//    private OnCompletionListener mOnCompletionListener;
//    private OnPreparedListener mOnPreparedListener;


    @Nullable
    @Override
    /*
    acivity绑定服务
     */
    public IBinder onBind(Intent intent) {
        System.out.println("MusicService onBind!");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("MusicService onCreate!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("MusicService onStartCommand!");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        System.out.println("MusicService onTaskRemoved!");
        //保存信息
    }

    @Override
    public void onDestroy() {
        System.out.println("MusicService onDestroy!");
        super.onDestroy();
        //释放资源
        releaseMediaPlayer();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("MusicService onUnbind!");
        return super.onUnbind(intent);
    }
    public class MyMusicBinder extends Binder{
        public MusicService getMusicService(){
            return MusicService.this;
        }
    }

    
    @Override
    protected void init() {
        initMediaplayer();

    }

    private void releaseMediaPlayer() {
        //1.释放音频焦点,必须申请焦点
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAudioManager.abandonAudioFocusRequest(mFocusRequest);
        }
        //2.释放WIFI锁
        if (mWifiLock.isHeld()) mWifiLock.release();
        if (mWifiLock!=null) mWifiLock = null;
        //3.清空音频焦点方法
        if (mAudioFocusChangeListener!=null) mAudioFocusChangeListener = null;
        if (mFocusRequest!=null) mFocusRequest = null;
        if (mAudioAttributes!=null) mAudioAttributes = null;
        if (mAudioManager!=null) mAudioManager = null;
        if (mMediaPlayer!=null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }


    private void initMediaplayer() {
        mMediaPlayer = new MediaPlayer();

        mMediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mWifiLock = ((WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL,"dyqlLock");
        //唤醒锁定模式，关闭屏幕时，CPU不休眠
        //音频焦点
        mAudioFocusChangeListener = new OnAudioFocusChangeListener();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAudioAttributes = new AudioAttributes.Builder().
                    setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setWillPauseWhenDucked(true)
                    .setOnAudioFocusChangeListener(mAudioFocusChangeListener)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaPlayer.setAudioAttributes(mAudioAttributes);
        }

    }
    private class OnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener{
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN://获得长时间播放焦点，短暂失去焦点后出发此回调
                    Log.d(TAG, "onAudioFocusChange: 获得长时间播发焦点");
                    break;

                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    Log.d(TAG, "onAudioFocusChange: 短暂失去焦点");
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK://失去焦点,但可以共同使用,需要主动降低音量
                    Log.d(TAG, "onAudioFocusChange: 失去焦点,但可以共同使用");
                    break;

                case AudioManager.AUDIOFOCUS_LOSS://长时间失去焦点
                    Log.d(TAG, "onAudioFocusChange: 长时间失去焦点");
                    break;
            }
        }
    }
    private void StopMediaPlayer(){
        if (mMediaPlayer!=null){
            if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
            if (!isFirstPlay()) {
                mCurrentPosition = 0;
                mMediaPlayer.seekTo(0);
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                if (mWifiLock.isHeld()) mWifiLock.release();//解除wifi锁
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mAudioManager.abandonAudioFocusRequest(mFocusRequest);//释放音频焦点
                }

            }
        }else Log.i(TAG, "StopMediaPlayer: MediaPlayer==null");
    }
    private void SetMediaPlayerResource(String Path,boolean isNetMsuic){
        StopMediaPlayer();
        if (mMediaPlayer!=null) {
            try {
                mMediaPlayer.setDataSource(Path);
                PlayMediaPlayer(isNetMsuic);
            } catch (IOException e) {
//                e.printStackTrace();
                mOnErrorListener.onError(mMediaPlayer,1,2);
            }
        }else Log.i(TAG, "StopMediaPlayer: MediaPlayer==null");


    }
    private void PlayMediaPlayer(boolean isNetMsuic){
        if (mMediaPlayer!=null) {
            if (mCurrentPosition == 0) {
                    try {
                        if (isNetMsuic) mMediaPlayer.prepareAsync();
                        else mMediaPlayer.prepare();
                    } catch (IOException e) {
//                        e.printStackTrace();自定义得报错信息
                        mOnErrorListener.onError(mMediaPlayer,2,2);
                    }

            }else {//1.接着上次播放。2.暂停后播放
                if (isFirstPlay()) {

                }
            }
        }else Log.i(TAG, "StopMediaPlayer: MediaPlayer==null");
        if (isFirstPlay()) {//只执行一次
            mOnErrorListener = new OnErrorListener();
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
            mMediaPlayer.setOnCompletionListener(new OnCompletionListener());
            mMediaPlayer.setOnPreparedListener(new OnPreparedListener());
        }
    }
    private class OnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.d(TAG, "onError: what:"+what+", extra = "+extra);
            switch (what){
                case 1:
                    if (extra ==1)
                        Toast.makeText(MusicService.this,"播放错误，歌曲地址为空，请播放其他歌曲",Toast.LENGTH_SHORT).show();
                    else if (extra == 2)
                        Toast.makeText(MusicService.this,"该音乐文件已损坏，请播放其他歌曲",Toast.LENGTH_SHORT).show();
                    else if (extra == -2147483648)
                        Toast.makeText(MusicService.this,"音乐文件解码失败，请删除该文件，尝试播放网络版本",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MusicService.this,"播放错误，请播放其他歌曲",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    if (extra ==2)
                        Toast.makeText(MusicService.this,"音乐文件解码失败，请播放其他歌曲",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MusicService.this,"播放错误，请播放其他歌曲",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Error Prepare ");
                    break;
            }
            /*
            onError返回false会触发OnCompletionListener
            所以返回false,一般意味着会退出当前歌曲播放
            如果不想退出当前歌曲播放则应该返回true

             */
            return true;
        }
    }
    private class OnCompletionListener implements MediaPlayer.OnCompletionListener{


        @Override
        public void onCompletion(MediaPlayer mp) {//自动下一曲播放
            Log.d(TAG, "onCompletion: ");

        }
    }
    private class  OnPreparedListener implements MediaPlayer.OnPreparedListener{

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (isFirstPlay()) {
                isFirstPlay = false;
            }
            if (mCurrentPosition>0) mMediaPlayer.seekTo(mCurrentPosition);
            //启动wifi锁
            mWifiLock.acquire();
            //申请长时间播放的焦点
            mAudioManager.requestAudioFocus(mFocusRequest);
            //所有都准备好了，开始播放
            mp.start();

        }
    }
    public boolean isFirstPlay() {
        return isFirstPlay;
    }
    public void OnPause(){

    }
    public void OnContinuePlay(){

    }
    public void OnPlay(String Path){
        SetMediaPlayerResource(Path,false);

    }
}
