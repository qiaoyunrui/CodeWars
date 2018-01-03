package me.juhezi.module.engine.demo

/**
 * Created by Juhezi[juhezix@163.com] on 2017/12/27.
 */
//顶点着色器
val VERTEX_SHADER = """
uniform mat4 uMVPMatrix;    //标准化设备坐标点（NDC）MVP 变换矩阵
uniform mat4 uTexMatrix;    //纹理坐标变换矩阵
attribute vec4 aPosition;   //NDK坐标点
attribute vec4 aTextureCoord;   //纹理坐标点
varying vec2 vTextureCoord;     //纹理坐标点变换后输出
void main() {
    gl_Position = uMVPMatrix * aPosition;   //对NDK坐标点进行变换，赋值给gl_Position
    vTextureCoord = (uTexMatrix * aTextureCoord).xy;    //对纹理片段进行变换，赋值后作为片段着色器输出
}
"""

//片段着色器
val FRAGMENT_SHADER = """
#extension GL_OES_EGL_image_external : require  //声明OES纹理作为使用扩展
precision mediump float;    //精度声明
varying vec2 vTextureCoord; //顶点着色器输出经图元装配和栅格化后的纹理坐标点序列
uniform samplerExternalOES sTexture;    //OES纹理，接受相机纹理作为输入
void main() {
    gl_FragColor = texture2D(sTexture,vTextureCoord);   //像素点赋值为相机纹理像素
}
"""