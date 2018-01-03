package me.juhezi.module.engine.demo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/29.
 */

public class TRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;

    //mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mTriangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(mProjectionMatrix, 0,
                -ratio, ratio,
                -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float[] scratch = new float[16];

        //set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix,
                0, 0, 0, -3,
                0f, 0f, 0f, 0f,
                1.0f, 0.0f);

        //calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0,
                mProjectionMatrix, 0,
                mViewMatrix, 0);

        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix,
                0, angle,
                0, 0, -1.0f);

        Matrix.multiplyMM(scratch, 0,
                mMVPMatrix, 0,
                mRotationMatrix, 0);

        //draw shape
        mTriangle.draw(scratch);

    }

}
