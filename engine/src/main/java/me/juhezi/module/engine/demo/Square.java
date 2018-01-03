package me.juhezi.module.engine.demo;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import me.juhezi.module.engine.core.FunctionsKt;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/29.
 */

public class Square {

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    static final int COORD_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f}; // top right

    private short drawOrder[] = {0, 1, 2, 0, 2, 3};

    public Square() {
        vertexBuffer = FunctionsKt.createFloatBuffer(squareCoords);
        drawListBuffer = FunctionsKt.createShortBuffer(drawOrder);
    }

}
