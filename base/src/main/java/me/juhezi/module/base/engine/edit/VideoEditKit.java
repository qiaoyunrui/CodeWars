package me.juhezi.module.base.engine.edit;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.TextureView;

import me.juhezi.module.base.engine.edit.player.AbsPlayer;
import me.juhezi.module.base.engine.edit.player.AudioPlayer;
import me.juhezi.module.base.engine.edit.player.VideoPlayer;

/**
 * Created by Juhezi on 2017/11/30.
 */
public class VideoEditKit {

    private static final String TAG = "VideoEditKit";

    private boolean mAutoPlay = true;  //自动播放，即播放器加载
    private Context mContext;
    private VideoPlayer mVideoPlayer;
    private AudioPlayer mAudioPlayer;

    private float mVideoVolume = 1f;  //视频音量
    private int mCurrentFilterId = 0;

    /**
     * 在构造函数里要把 VideoPlayer 和 AudioPlayer 创建出来
     *
     * @param context
     */
    public VideoEditKit(Context context) {
        this.mContext = context;
        if (mContext == null) {
            throw new NullPointerException("The context can not be null!");
        }
        mVideoPlayer = AbsPlayer.createVideoPlayer(context);
        mVideoPlayer.setLooping(true);
        mVideoPlayer.setCallback(new VideoPlayer.Callback() {
            @Override
            public void onCanPlay(boolean afterPrepared) {
                if (afterPrepared || mAutoPlay) {
                    play();
                }
            }

            /**
             * SurfaceView 销毁的时候调用
             * 每次必调用
             */
            @Override
            public void onShouldPause() {
//                internalPause();
                pause();
            }
        });

        mAudioPlayer = AbsPlayer.createAudioPlayer(context);
        mAudioPlayer.setLooping(true);
    }

    /**
     * 初始化 VideoPlayer，包括检测视频路径是否存在，SurfaceHolder 是否能够可用等等。
     * 真正的逻辑是封装在 VideoPlayer 中实现的
     *
     * @param videoPath
     */
    public void init(String videoPath, SurfaceHolder surfaceHolder) {
        mVideoPlayer.init(videoPath, surfaceHolder);
    }

    public void init(String videoPath, TextureView textureView) {
        mVideoPlayer.init(videoPath, textureView);
    }

    //StoryController 初始化的函数

    /**
     * Double
     */
    public void play() {
        internalPlay();
    }

    /**
     * Double
     */
    public void pause() {
        internalPause();
    }

    private void internalPlay() {
        mVideoPlayer.play();
        mAudioPlayer.play();
    }

    private void internalPause() {
        mAudioPlayer.pause();
        mVideoPlayer.pause();
    }

    /**
     * Double
     */
    public void replay() {
        seekTo(0);
        play();
    }

    public void seekTo(int msec) {
        mAudioPlayer.seekTo(msec);
        mVideoPlayer.seekTo(msec);
    }

    /**
     * Double
     *
     * @param volume
     */
    public void setVideoVolume(float volume) {
        mVideoPlayer.setVolume(volume);
    }

    /**
     * Double
     */
    public void destroy() {
        mVideoPlayer.release();
        mAudioPlayer.release();
    }

    public Context getContext() {
        return mContext;
    }

    public VideoPlayer getVideoPlayer() {
        return mVideoPlayer;
    }

    public AudioPlayer getAudioPlayer() {
        return mAudioPlayer;
    }

    public int getDuration() {
        return mVideoPlayer.getDuration();
    }

    public void addMusic(String path) {
        if (mAudioPlayer != null) {
            mAudioPlayer.setSourcePath(path);
            replay();
        }
    }

    public void removeMusic() {
        if (mAudioPlayer != null) {
            mAudioPlayer.setSourcePath(null);
            replay();
        }
    }

    public void setAudioVolume(float volume) {
        if (mAudioPlayer != null) {
            mAudioPlayer.setVolume(volume);
        }
    }

    public boolean isPlaying() {
        return mVideoPlayer.isPlaying();
    }

    public EditBundle getEditBean() {
        EditBundle mEditBundle = new EditBundle();
        mEditBundle.setAudioPath(mAudioPlayer.getSourcePath())
                .setAudioVolume(mAudioPlayer.getVolume())
                .setFilterId(mCurrentFilterId);
        mEditBundle.setVideoPath(mVideoPlayer.getSourcePath())
                .setVideoVolume(mVideoPlayer.getVolume());
        return mEditBundle;
    }

    public void applyFilter(int id) {
        mCurrentFilterId = id;
    }

}