package me.juhezi.module.engine.core.edit.player;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import java.io.IOException;

/**
 * Audio VideoEditKit
 * Created by Juhezi[juhezix@163.com] on 2017/11/30.
 */
public class VideoPlayer extends AbsPlayer {

    private static final String TAG = "VideoPlayer";

    private SurfaceHolder mHolder;
    private TextureView mTextureView;
    private Callback mCallback;

    private int mVideoHeight;
    private int mVideoWidth;

    private Surface mSurface;
    private int mSeekWhenPrepared;
    private int mCurrentBufferPercent;
    private MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener;
    private MediaPlayer.OnPreparedListener mPreparedListener;
    private MediaPlayer.OnErrorListener mErrorListener;
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener;

    private boolean first = true;

    /**
     * VideoPlayer 中不采用懒加载的方式创建 MediaPlayer
     *
     * @param context
     */
    VideoPlayer(Context context) {
        super(context, false);
        mVideoHeight = 0;
        mVideoWidth = 0;
        mSizeChangedListener = (mp, width, height) -> {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            if (mVideoWidth != 0 && mVideoHeight != 0 && mHolder != null) {
                mHolder.setFixedSize(mVideoWidth, mVideoHeight);
            }
        };
        mPreparedListener = mp -> {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            int seekToPosition = mSeekWhenPrepared;
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if (mVideoWidth != 0 && mVideoHeight != 0 && mHolder != null) {
                mHolder.setFixedSize(mVideoWidth, mVideoHeight);
            }
            if (mCallback != null) {
                mCallback.onCanPlay(true);
            }
        };
        mErrorListener = (mp, what, extra) -> {
            if (mOnCustomErrorListener != null) {
                mOnCustomErrorListener.onError(ERROR_CODE_BASE);
            }
            return true;
        };
        mBufferingUpdateListener = (mp, percent) -> mCurrentBufferPercent = percent;
    }

    /**
     * 1. 检测视频路径是否可用 [OK]
     * 2. 检测 SurfaceHolder 是否可用
     * 设置 SurfaceHolder 只有这一种方式
     * 但是修改 VideoPath 还有另外的途径
     */
    public void init(String videoPath, SurfaceHolder holder) {
        mSourcePath = videoPath;
        first = true;
        configSurfaceHolder(holder);
    }

    public void init(String videoPath, TextureView textureView) {
        mSourcePath = videoPath;
        first = true;
        configTextureView(textureView);
    }

    /**
     * 这个函数只需要执行一次
     */
    private void openVideo() {
        if (!checkPathValid() && mOnCustomErrorListener != null) {
            mOnCustomErrorListener.onError(ERROR_CODE_VALID_PATH);
            return;
        }
        if (mHolder != null && mOnCustomErrorListener != null) {
            mOnCustomErrorListener.onError(ERROR_CODE_NULL_POINTER);
            return;
        }
        release();
        try {
            lazyMediaPlayer().setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMediaPlayer.setDataSource(mSourcePath);
            mMediaPlayer.setDisplay(mHolder);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            if (mOnCustomErrorListener != null) {
                mOnCustomErrorListener.onError(ERROR_CODE_VALID_PATH);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            if (mOnCustomErrorListener != null) {
                mOnCustomErrorListener.onError(ERROR_CODE_ILLEGAL_ARGUMENT);
            }
        }
    }

    private void initMediaPlayer() {
        if (!checkPathValid() && mOnCustomErrorListener != null) {
            mOnCustomErrorListener.onError(ERROR_CODE_VALID_PATH);
            return;
        }
        if (mTextureView != null && mOnCustomErrorListener != null) {
            mOnCustomErrorListener.onError(ERROR_CODE_NULL_POINTER);
            return;
        }
        release();
        try {
            lazyMediaPlayer().setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mMediaPlayer.setDataSource(mSourcePath);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
            if (mOnCustomErrorListener != null) {
                mOnCustomErrorListener.onError(ERROR_CODE_VALID_PATH);
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            if (mOnCustomErrorListener != null) {
                mOnCustomErrorListener.onError(ERROR_CODE_ILLEGAL_ARGUMENT);
            }
        }
    }

    /**
     * 使用 SurfaceHoler 进行显示
     *
     * @param holder
     */
    private void configSurfaceHolder(SurfaceHolder holder) {
        holder.setKeepScreenOn(true);  //保持屏幕常亮
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mHolder = holder;
                if (first) {
                    openVideo();
                    first = false;
                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.setDisplay(mHolder);
                        if (mCallback != null) {
                            mCallback.onCanPlay(false);
                        }
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mHolder = null;
                if (mCallback != null) {
                    mCallback.onShouldPause();
                }
            }
        });
    }

    /**
     * 使用 TextureView 进行显示
     *
     * @param textureView
     */
    private void configTextureView(TextureView textureView) {
        if (textureView == null) {
            return;
        }
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mSurface = new Surface(surface);
                initMediaPlayer();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                mTextureView = null;
                mSurface = null;
                if (mCallback != null) {
                    mCallback.onShouldPause();
                }
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });
    }

    /**
     * 重新创建 MediaPlayer，即调用 {@link #openVideo()}
     *
     * @param path
     */
    @Override
    public void setSourcePath(String path) {
        mSourcePath = path;
        mSeekWhenPrepared = 0;
        openVideo();
    }

    public int getBufferPercentage() {
        if (mMediaPlayer != null) {
            return mCurrentBufferPercent;
        }
        return 0;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {

        void onCanPlay(boolean afterPrepared);

        void onShouldPause();

    }
}
