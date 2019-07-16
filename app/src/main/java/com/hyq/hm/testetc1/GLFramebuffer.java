package com.hyq.hm.testetc1;

import android.graphics.SurfaceTexture;
import android.opengl.ETC1;
import android.opengl.ETC1Util;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by 海米 on 2017/8/16.
 */

public class GLFramebuffer {

    private FloatBuffer vertexBuffer;
    private FloatBuffer textureVertexBuffer;
    private int programId = -1;
    private int aPositionHandle;
    private int uTextureSamplerHandle;
    private int aTextureCoordHandle;
    private String fragmentShader = "varying highp vec2 vTexCoord;\n" +
            "uniform sampler2D sTexture;\n" +
            "varying highp vec2 vAlphaCoord;\n" +
            "void main() {\n" +
            "    highp vec4 color = texture2D(sTexture, vTexCoord);\n"+
            "    color.a = texture2D(sTexture,vAlphaCoord).r;\n" +
            "    gl_FragColor = color;\n" +
            "}";
    private  String vertexShader = "attribute vec4 aPosition;\n" +
            "attribute vec2 aTexCoord;\n" +
            "varying vec2 vTexCoord;\n" +
            "varying vec2 vAlphaCoord;\n" +
            "void main() {\n" +
            "  vTexCoord = aTexCoord * vec2(1.0, 0.5);\n" +
            "  vAlphaCoord = vTexCoord + vec2(0.0, 0.5);\n" +
            "  gl_Position = aPosition;\n" +
            "}";

    private int[] textures;
    private int[] framebuffers;
    public GLFramebuffer(){
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


    public void initFramebuffer(){
        programId = ShaderUtils.createProgram(vertexShader, fragmentShader);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        uTextureSamplerHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");

        textures = new int[2];
        GLES20.glGenTextures(textures.length, textures, 0);
        for (int texture : textures){
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        framebuffers = new int[1];
        GLES20.glGenFramebuffers(framebuffers.length, framebuffers,0);
    }
    public void bindETC1Texture(ETC1Util.ETC1Texture texture){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0]);
        ETC1Util.loadTexture(GLES20.GL_TEXTURE_2D,0,0,GLES20.GL_RGB,GLES20
                .GL_UNSIGNED_SHORT_5_6_5,texture);
        GLES20.glUniform1i(uTextureSamplerHandle,0);
    }

    public int drawFrame(ETC1Util.ETC1Texture texture,int width,int height){
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffers[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textures[1], 0);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glViewport(0,0,width,height);
        GLES20.glUseProgram(programId);
        bindETC1Texture(texture);
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false,
                12, vertexBuffer);

        textureVertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureVertexBuffer);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        return textures[1];
    }

}
