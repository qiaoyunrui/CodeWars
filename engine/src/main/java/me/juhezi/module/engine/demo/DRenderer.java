package me.juhezi.module.engine.demo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import me.juhezi.module.engine.core.FunctionsKt;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/26.
 */

public class DRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "DRenderer";

    private static final String VERTEX_SHADER =
            "attribute vec4 vPosition;\n"
                    + "uniform mat4 uMVPMatrix;\n"
                    + "void main() {\n"
                    + " gl_Position = uMVPMatrix * vPosition;\n"
                    + "}";

    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n"
                    + "void main() {\n"
                    + " gl_FragColor = vec4(0.5, 0, 0, 1);\n"
                    + "}";

    private static final float[] VERTEX = {   // in counterclockwise order:
            0, 1, 0,  // top
            -0.5f, -1, 0,  // bottom left
            1, -1, 0,  // bottom right
    };

    private final FloatBuffer mVertexBuffer;

    private int mProgram;
    private int mPositionHandle;
    private int mMatrixHandle;

    private final float[] mMVPMatrix = new float[16];

    public DRenderer() {
        //这段代码是什么意思？
        mVertexBuffer = FunctionsKt.createFloatBuffer(VERTEX);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        mProgram = FunctionsKt.createProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        GLES20.glUseProgram(mProgram);

        //获取 shader 代码中的变量索引
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mMatrixHandle = GLES20.glGetAttribLocation(mProgram, "uMVPMatrix");

        //启用 vertex
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //绑定 vertex 坐标值
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false,
                12, mVertexBuffer);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        //计算变换矩阵
        Log.i(TAG, "onSurfaceChanged: " + FunctionsKt.toString(mMVPMatrix));
        //透视投影
        Matrix.perspectiveM(mMVPMatrix, 0, 45,
                (float) width / height, 0.f, 100f);
        Log.i(TAG, "透视投影: " + FunctionsKt.toString(mMVPMatrix));
        //位移变换
        Matrix.translateM(mMVPMatrix, 0, 0f, 0f, -2.5f);
        Log.i(TAG, "位移变换投影: " + FunctionsKt.toString(mMVPMatrix));
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //为 uMVPMatrix 赋值
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1,
                false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

}
