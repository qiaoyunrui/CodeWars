package me.juhezi.module.engine.core.edit.player;

import android.content.Context;

/**
 * Audio VideoEditKit
 * Created by Juhezi[juhezix@163.com] on 2017/11/30.
 */
public class AudioPlayer extends AbsPlayer {

    /**
     * 懒加载
     *
     * @param context
     */
    AudioPlayer(Context context) {
        super(context, true);
    }

}
