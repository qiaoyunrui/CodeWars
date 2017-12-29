package me.juhezi.module.engine.core

import android.media.MediaCodec
import android.media.MediaFormat
import android.opengl.GLES20
import android.opengl.GLES30
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

/**
 * Functions
 * Created by Juhezi[juhezix@163.com] on 2017/12/19.
 */

val TAG = "Functions TAG"

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

fun MediaCodec.getVideoFrameSize(): Pair<Int, Int> {
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

/**
 * 创建并加载着色器代码
 */
fun loadShader(shaderType: Int, vertexShaderSrc: String): Int {
    val shader = GLES30.glCreateShader(shaderType)  //创建指定类型的着色器
    GLES30.glShaderSource(shader, vertexShaderSrc)  //加载着色器代码
    GLES30.glCompileShader(shader)  //编译着色器
    return shader
}

fun checkGlError(op: String) {
    val error = GLES20.glGetError()
    while (error != GLES20.GL_NO_ERROR) {
        Log.i(TAG, op + ": glError " + error)
        throw RuntimeException(op + ": glError " + error)
    }
}

fun createProgram(vertexSource: String, fragmentSource: String): Int {
    val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
    if (vertexShader == 0) return 0
    val pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
    if (pixelShader == 0) return 0

    var program = GLES20.glCreateProgram()  //创建一个 OpenGL ES 程序
    checkGlError("glCreateProgram")
    if (program == 0)
        Log.e(TAG, "Could create program")
    GLES20.glAttachShader(program, vertexShader) //绑定顶点着色器
    checkGlError("glAttachShader")
    GLES20.glAttachShader(program, pixelShader)  //绑定片段着色器
    checkGlError("glAttachShader")

    GLES20.glLinkProgram(program)   //链接 OpenGL 程序
    val linkStatus = IntArray(1)
    GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS,
            linkStatus, 0)  //检测链接状态
    if (linkStatus[0] != GLES20.GL_TRUE) {
        Log.e(TAG, "Could not link program: ")
        Log.e(TAG, GLES20.glGetProgramInfoLog(program))
        GLES20.glDeleteProgram(program)
        program = 0
    }
    return program
}

fun createFloatBuffer(array: FloatArray): FloatBuffer {
    //先初始化 buffer，数组的长度 * 4，因为一个 float 占4个字节
    val byteBuffer = ByteBuffer.allocateDirect(array.size * 4)
    //数组排列用 nativeOrder
    byteBuffer.order(ByteOrder.nativeOrder())
    val floatBuffer = byteBuffer.asFloatBuffer()
    floatBuffer.put(array)
    floatBuffer.position(0)
    return floatBuffer
}

fun createShortBuffer(array: ShortArray): ShortBuffer {
    //2 bytes per short
    val byteBuffer = ByteBuffer.allocateDirect(array.size * 2)
    byteBuffer.order(ByteOrder.nativeOrder())
    val shortBuffer = byteBuffer.asShortBuffer()
    shortBuffer.put(array)
    shortBuffer.position(0)
    return shortBuffer
}

fun toString(array: FloatArray) = buildString {
    array.forEach { append("$it ") }
}