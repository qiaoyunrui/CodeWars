package me.juhezi.module.engine.core.until

import android.media.MediaCodec
import android.media.MediaFormat
import java.nio.ByteOrder

/**
 * Functions
 * Created by Juhezi[juhezix@163.com] on 2017/12/19.
 */

/**
 * 获取信道的样本
 * 不明所以
 */
inline fun MediaCodec.getSamplesForChannel(bufferId: Int, channelIx: Int):
        Array<Short>? {
    val outputBuffer = getOutputBuffer(bufferId)
    val format = outputFormat
    val samples = outputBuffer.order(ByteOrder.nativeOrder())
            .asShortBuffer()
    val numChannels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
    if (channelIx < 0 || channelIx >= numChannels) {
        return null
    }
    val res = Array<Short>(samples.remaining() / numChannels, { 0 })
    res.forEachIndexed({ index, _ ->
        res[index] = samples[index * numChannels + channelIx]
    })
    return res
}

inline fun MediaCodec.getVideoFrameSize(): Pair<Int, Int> {
    val format = outputFormat
    var width = format.getInteger(MediaFormat.KEY_WIDTH)
    if (format.containsKey("crop-left") &&
            format.containsKey("crop-right")) {
        width = format.getInteger("crop-right") + 1 -
                format.getInteger("crop-left")
    }
    var height = format.getInteger(MediaFormat.KEY_HEIGHT)
    if (format.containsKey("crop-top") &&
            format.containsKey("crop-bottom")) {
        height = format.getInteger("crop-bottom") + 1 -
                format.getInteger("crop-top")
    }
    return Pair(width, height)
}