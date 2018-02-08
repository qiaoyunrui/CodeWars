package me.juhezi.module.base.engine.edit.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * MediaPlayer 要做到懒加载
 * Created by Juhezi[juhezix@163.com] on 2017/11/30.
 */

public abstract class AbsPlayer {

    public static final int ERROR_CODE_BASE = 0x00;
    public static final int ERROR_CODE_VALID_PATH = 0x01;    //路径不可用
    public static final int ERROR_CODE_NULL_POINTER = 0x02;
    public static final int ERROR_CODE_ILLEGAL_ARGUMENT = 0x03;

    private static final String TAG = "AbsPlayer";

    private float mVolume;

    public static AudioPlayer createAudioPlayer(Context context) {
        return new AudioPlayer(context);
    }

    public static VideoPlayer createVideoPlayer(Context context) {
        return new VideoPlayer(context);
    }

    protected MediaPlayer mMediaPlayer;
    protected String mSourcePath;
    protected Context mContext;
    protected OnErrorListener mOnCustomErrorListener;
    //------Config-----
    private boolean first;  //初次创建
    protected boolean looping;  //循环
    protected boolean mLazyLoadMediaPlayer;     //是否对 MediaPlayer 进行懒加载

    protected AbsPlayer(Context context, boolean lazyLoadMediaPlayer) {
        mContext = context;
        mLazyLoadMediaPlayer = lazyLoadMediaPlayer;
        if (!mLazyLoadMediaPlayer) {    //如果不是懒加载，则立刻创建 MediaPlayer
            mMediaPlayer = new MediaPlayer();
            config();
        }
    }

    /**
     * Action
     * MediaPlayer 应该已经实例化了，如果有资源，那么就播放，没有资源就不进行播放
     */
    public void play() {
        Log.i(TAG, "play: 播放");
        if (checkPathValid()) {
            lazyMediaPlayer().start();
            lazyMediaPlayer().setLooping(looping);  //设置循环必须放在 start() 之后才会生效
        }
    }

    /**
     * Action
     */
    public void pause() {
        if (checkPathValid()) {
            Log.i(TAG, "pause: 视频暂停");
            lazyMediaPlayer().pause();
        }
    }

    /**
     * 0.0 - 1.0
     * Action
     *
     * @param volume 音量
     */
    public void setVolume(float volume) {
        if (checkPathValid()) {
            mVolume = volume;
            lazyMediaPlayer().setVolume(volume, volume);
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * setSourcePath 需要调用 reset() 方法使 MediaPlayer 对象进入 Idle 状态。
     *
     * @param path
     */
    public void setSourcePath(String path) {
        mSourcePath = path;
        //如果资源路径合适，才会进行setDataSource()操作
        if (checkPathValid()) {
            try {
                if (!first) {
                    lazyMediaPlayer().reset();
                }
                lazyMediaPlayer().setDataSource(mSourcePath);
                lazyMediaPlayer().prepare();
                first = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
            }
        }
    }

    /**
     * Action
     *
     * @param msec
     */
    public void seekTo(int msec) {
        if (checkPathValid()) {
            lazyMediaPlayer().seekTo(msec);
        }
    }

    /**
     * start() 之后调用
     *
     * @param looping
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    /**
     * 检查资源路径是否有效
     */
    public boolean checkPathValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(mSourcePath)) {
            isValid = false;
        } else {
            File source = new File(mSourcePath);
            if (!source.exists() || !source.isFile()) {
                isValid = false;
            }
        }
        if (isValid && mOnCustomErrorListener != null) {
            mOnCustomErrorListener.onError(ERROR_CODE_VALID_PATH);
        }
        return isValid;
    }

    protected MediaPlayer lazyMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            config();
        }
        return mMediaPlayer;
    }

    /**
     * 创建 MediaPlayer 之后进行对应的配置
     */
    protected void config() {
        first = true;
        if (mMediaPlayer == null) return;
    }

    /**
     * Temporarily
     * get Audio | Video duration
     */
    public int getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        }
        return -1;
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public String getSourcePath() {
        return mSourcePath;
    }

    public double getVolume() {
        return mVolume;
    }

    public interface OnErrorListener {
        void onError(int errorCode);
    }

}
