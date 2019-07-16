package com.hyq.hm.testetc1;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;


/**
 * Created by 海米 on 2018/6/7.
 */

public class GuideActivity extends BaseActivity {
    @Override
    public String[] getPermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void initView() {
        Log.d("===============","HARDWARE="+ Build.HARDWARE);
        Log.d("===============","MODEL="+Build.MODEL);
        Log.d("===============","SDK_INT="+Build.VERSION.SDK_INT);
        Log.d("===============","RELEASE="+Build.VERSION.RELEASE);
        setContentView(R.layout.activity_guide);

    }


    @Override
    public void init() {
        TextView textView = findViewById(R.id.text_view);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);

//        FileUtils.AppPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                String sp = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sc_alpha";
//                File file = new File(sp);
//                if(file.exists()){
//                    File[] files = file.listFiles();
//                    for (File f : files){
//                        Log.d("================","delete = "+f.getAbsolutePath());
//                        f.delete();
//                    }
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final TextView textView = findViewById(R.id.text_view);
//                        FileUtils.getInstance(GuideActivity.this).copyAssetsToSD("sc_alpha","sc_alpha").setFileOperateCallback(new FileUtils.FileOperateCallback() {
//                            @Override
//                            public void onSuccess(List<String> files) {
//                                textView.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Intent intent = new Intent(GuideActivity.this,MainActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                },1000);
//                            }
//
//                            @Override
//                            public void onFailed(String error) {
//                                // TODO: 文件复制失败时，主线程回调
//                            }
//                        });
//                    }
//                });
//            }
//        };
//        thread.start();
    }
    @Override
    public void notOpenPermissions(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
//                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
//                startActivity(intent);
                Toast.makeText(this,"请在设置内开启储存权限后重新打开应用",Toast.LENGTH_LONG).show();
//                finish();
                break;
            }
        }
        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = getWindow().getDecorView();
            int mHideFlags =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(mHideFlags);
        }else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isTaskRoot()){
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
    }
}
