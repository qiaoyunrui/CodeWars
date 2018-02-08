package me.juhezi.module.base.engine.edit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/11.
 */

public class EditBundle {

    private static final String TAG = "EditBundle";

    private Map<String, Object> mExtraMap = new HashMap<>();  //额外的属性
    private String mVideoPath;
    private String mAudioPath;
    private String mTargetPath;

    private double mVideoVolume;
    private double mAudioVolume;

    private int mFilterId = 0;

    //------ Other props -----
    /*
     * FilterId
     * Title
     * as so on
     */

    public EditBundle() {
    }

    public EditBundle(String videoPath, String targetPath) {
        this.mVideoPath = videoPath;
        this.mTargetPath = targetPath;
    }

    public String getVideoPath() {
        return mVideoPath;
    }

    public EditBundle setVideoPath(String videoPath) {
        this.mVideoPath = videoPath;
        return this;
    }

    public String getAudioPath() {
        return mAudioPath;
    }

    public EditBundle setAudioPath(String audioPath) {
        this.mAudioPath = audioPath;
        return this;
    }

    public String getTargetPath() {
        return mTargetPath;
    }

    public EditBundle setTargetPath(String targetPath) {
        this.mTargetPath = targetPath;
        return this;
    }

    public double getVideoVolume() {
        return mVideoVolume;
    }

    public EditBundle setVideoVolume(double videoVolume) {
        this.mVideoVolume = videoVolume;
        return this;
    }

    public double getAudioVolume() {
        return mAudioVolume;
    }

    public EditBundle setAudioVolume(double audioVolume) {
        this.mAudioVolume = audioVolume;
        return this;
    }

    public EditBundle putExtra(String key, Object value) {
        mExtraMap.put(key, value);
        return this;
    }

    public Object getExtra(String key) {
        return mExtraMap.get(key);
    }

    public int getFilterId() {
        return mFilterId;
    }

    public EditBundle setFilterId(int filterId) {
        mFilterId = filterId;
        return this;
    }
    
    @Override
    public String toString() {
        return "EditBundle{" +
                "mExtraMap=" + mExtraMap +
                ",\n mVideoPath='" + mVideoPath + '\'' +
                ",\n mAudioPath='" + mAudioPath + '\'' +
                ",\n mTargetPath='" + mTargetPath + '\'' +
                ",\n mVideoVolume=" + mVideoVolume +
                ",\n mAudioVolume=" + mAudioVolume +
                ",\n mFilterId=" + mFilterId +
                '}';
    }

}
