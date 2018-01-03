package me.juhezi.module.engine.demo;

import android.opengl.GLES20;

import java.nio.FloatBuffer;

import me.juhezi.module.engine.core.FunctionsKt;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/29.
 */

public class Triangle {

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;

    static final int COORD_PER_VERTEX = 3;

    static float triangleCoords[] = {
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    private final int mProgram;

    private int mPositionHandle;

    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORD_PER_VERTEX;

    private final int vertexStride = COORD_PER_VERTEX * 4;

    private int mMVPMatrixHandle;

    public Triangle() {
        vertexBuffer = FunctionsKt.createFloatBuffer(triangleCoords);

        mProgram = FunctionsKt.createProgram(vertexShaderCode,
                fragmentShaderCode);
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORD_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
                "uMVPMatrix");

        //pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        //Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        //Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
