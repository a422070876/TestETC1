package com.hyq.hm.testetc1;

import android.graphics.Bitmap;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by 海米 on 2017/8/16.
 */

public class BitmapRenderer {
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureVertexBuffer;
    private int programId = -1;
    private int aPositionHandle;
    private int uTextureSamplerHandle;
    private int aTextureCoordHandle;
    private String fragmentShader = "varying highp vec2 vTexCoord;\n" +
            "uniform sampler2D sTexture;\n" +
            "void main() {\n" +
            "    highp vec4 color = texture2D(sTexture, vec2(vTexCoord.x,1.0-vTexCoord.y));\n"+
//            "    highp vec4 color = vec4(0.0);\n"+
//            "    color.a = 1.0;\n"+
            "    gl_FragColor = color;\n" +
            "}";
    private  String vertexShader = "attribute vec4 aPosition;\n" +
            "attribute vec2 aTexCoord;\n" +
            "varying vec2 vTexCoord;\n" +
            "void main() {\n" +
            "  vTexCoord = aTexCoord;\n" +
            "  gl_Position = aPosition;\n" +
            "}";
    private int[] textures;

    public BitmapRenderer(){
        final float[] vertexData = {
                1f, -1f, 0f,
                -1f, -1f, 0f,
                1f, 1f, 0f,
                -1f, 1f, 0f
        };


        final float[] textureVertexData = {
                1f, 0f,
                0f, 0f,
                1f, 1f,
                0f, 1f
        };
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);

        textureVertexBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureVertexBuffer.position(0);
    }

    public void initShader(){
        programId = ShaderUtils.createProgram(vertexShader, fragmentShader);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");

        textures = new int[1];
        GLES20.glGenTextures(textures.length, textures, 0);
        for (int texture : textures){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }
    public void bindTexture(Bitmap bitmap){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap,0);
        GLES20.glUniform1i(uTextureSamplerHandle,0);
    }
    public void viewportSize(int screenWidth,int screenHeight,int videoWidth,int videoHeight) {
        int left, top, viewWidth, viewHeight;
        float sh = screenWidth * 1.0f / screenHeight;
        float vh = videoWidth * 1.0f / videoHeight;
        if (sh < vh) {
            left = 0;
            viewWidth = screenWidth;
            viewHeight = (int) (videoHeight * 1.0f / videoWidth * viewWidth);
            top = (screenHeight - viewHeight) / 2;
        } else {
            top = 0;
            viewHeight = screenHeight;
            viewWidth = (int) (videoWidth * 1.0f / videoHeight * viewHeight);
            left = (screenWidth - viewWidth) / 2;
        }
        GLES20.glViewport(left, top, viewWidth, viewHeight);
    }
    public void drawFrame(Bitmap bitmap,int screenWidth,int screenHeight){
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        viewportSize(screenWidth,screenHeight,bitmap.getWidth(),bitmap.getHeight());
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(programId);
        bindTexture(bitmap);
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false,
                12, vertexBuffer);

        textureVertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureVertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }

}
