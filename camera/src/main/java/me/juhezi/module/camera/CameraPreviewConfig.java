package me.juhezi.module.camera;

import android.graphics.SurfaceTexture;

import com.weibo.story.core.CameraMicCallback;

public class CameraPreviewConfig {

    final CameraMicCallback listener;
    final int width, height;
    final SurfaceTexture surfaceTexture;

    public CameraPreviewConfig(CameraMicCallback listener, int width, int height, SurfaceTexture surfaceTexture) {
        this.width = width;
        this.height = height;
        this.listener = listener;
        this.surfaceTexture = surfaceTexture;
    }
}