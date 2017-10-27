package me.juhezi.module.camera;


public class CameraRecordConfig {

    public final String path;
    public final OnEncoderFinishListener listener;

    public CameraRecordConfig(String path, OnEncoderFinishListener listener) {
        this.path = path;
        this.listener = listener;
    }
}
