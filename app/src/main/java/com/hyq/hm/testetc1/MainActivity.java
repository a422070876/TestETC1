package com.hyq.hm.testetc1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.opengl.ETC1;
import android.opengl.ETC1Util;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Handler testHandler;
    private HandlerThread testThread;

    private EGLUtils eglUtils;

    private GLRenderer renderer;
//    private BitmapRenderer bitmapRenderer;


    private TextureView surfaceView;
    private Bitmap bitmap;


    private int index = 0;

    private int screenWidth,screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testThread = new HandlerThread("testThread");
        testThread.start();
        testHandler = new Handler(testThread.getLooper());

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_car);

        eglUtils = new EGLUtils();

//        framebuffer = new GLFramebuffer();

        renderer = new GLRenderer();

//        bitmapRenderer = new BitmapRenderer();



        surfaceView = findViewById(R.id.surface_view);

        surfaceView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
                screenWidth = width;
                screenHeight = height;
                testHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        eglUtils.initEGL(new Surface(surface));
//                        framebuffer.initFramebuffer();
                        renderer.initShader();
//                        bitmapRenderer.initShader();
                        drawFrame();
                    }
                });
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                testHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        eglUtils.release();
                    }
                });
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                if(files != null && files.length != 0){
//                    for (File file : files){
//                        InputStream inputStream = null;
//                        try {
//                            inputStream = new FileInputStream(file);
//                            ETC1Util.ETC1Texture texture = createTexture(inputStream);
//                            list.add(texture);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }finally {
//                            if(inputStream != null){
//                                try {
//                                    inputStream.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                    if(list.size() != 0){
////                        drawFrame();
//                    }
//                }
//            }
//        };
//        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        testThread.quit();
    }
//    private List<ETC1Util.ETC1Texture> list = new ArrayList<>();
    private void drawFrame(){
        testHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(index >= 240){
                    index = 0;
                }
//                bitmapRenderer.drawFrame(bitmap,screenWidth,screenHeight);
                long time = System.currentTimeMillis();
                InputStream inputStream = null;
                ETC1Util.ETC1Texture texture = null;
                try {
                    inputStream = getAssets().open("pkm_test_300/"+index+".pkm");
                    texture = ETC1Util.createTexture(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(inputStream != null){
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(texture != null ){
                    renderer.drawFrame(texture,screenWidth,screenHeight);
                }
                eglUtils.swap();
                Log.d("=============","index = "+ index);
                Log.d("=============","time = "+ (System.currentTimeMillis() - time));
                index++;
                if(texture != null){
                    drawFrame();
                }
            }
        },30);
    }
}
