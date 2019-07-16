package com.hyq.hm.testetc1;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 海米 on 2018/3/9.
 */

public class FileUtils {
    public static int Version = 18;

    public static String AppPath = null;
    public static String FileNames[] = {"1542359365845","1542363737528","1542618920737","1542618939617","1554258701090"};
    public static String VideoName[] = {"三维演示","多维度演示","平面演示","动画演示","三维序列"};
    public static int AdCounts[] = {2,4,1,1,1};
    public static String AdName[][][] = {
            {
                    {"laobaigan_G.png","niunai_G.png","jiangyou_G.png","kele_G1.png","maotai_G1.png"},
                    {"laobaigan_G.png","niunai_G.png","jiangyou_G.png","kele_G1.png","maotai_G1.png"}
            },
            {
                    {"雀巢海报.jpg"},
                    {"ls.gif","qc.gif"},
                    {"xuebi_G1.png"},
                    {"meiriyanyu_G1.png"}
            },
            {
                    {"雀巢海报.jpg"}
            },
            {
                    {"ls.gif","qc.gif"}
            },
            {
                    {"beizi.png","telunsu.png"}
            }
    };
    public static String SingleName[][] = {
            {"coffee_1.jpg","ht_1.jpg"},
            {"coffee_2.webp","ht_2.webp"},
            {"coffee_3.jpg","ht_3.jpg"},
            {"anjixing.webp"},
            {"coffee_5.jpg","ht_5.jpg"},
            {"coffee_6.webp","ht_6.webp"}
    };

    public static String SingleADFile[][] = {
            {"1/coffee_1.jpg","1/ht_1.jpg"},
            {"4/coffee_2.webp","4/ht_2.webp"},
            {"1/coffee_3.jpg","1/ht_3.jpg"},
            {"6/anjixing.webp","6/anjixing.webp"},
            {"4/coffee_5.jpg","4/ht_5.jpg"},
            {"4/coffee_6.webp","4/ht_6.webp"}
    };
    public static String SingleADURL[][] = {
            {"https://search.jd.com/Search?keyword=%E7%91%9E%E6%96%B0%E5%92%96%E5%95%A1&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E7%91%9E%E6%96%B0&pvid=ea59ac76b07c4928b5398f96e09a5c6e","https://search.jd.com/Search?keyword=%E5%85%AD%E4%B8%AA%E6%A0%B8%E6%A1%83&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E5%85%AD%E4%B8%AA&pvid=bfa4f049fcf4433aa41332f6858d6435"},
            {"https://search.jd.com/Search?keyword=%E7%91%9E%E6%96%B0%E5%92%96%E5%95%A1&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E7%91%9E%E6%96%B0&pvid=ea59ac76b07c4928b5398f96e09a5c6e","https://search.jd.com/Search?keyword=%E5%85%AD%E4%B8%AA%E6%A0%B8%E6%A1%83&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E5%85%AD%E4%B8%AA&pvid=bfa4f049fcf4433aa41332f6858d6435"},
            {"https://search.jd.com/Search?keyword=%E7%91%9E%E6%96%B0%E5%92%96%E5%95%A1&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E7%91%9E%E6%96%B0&pvid=ea59ac76b07c4928b5398f96e09a5c6e","https://search.jd.com/Search?keyword=%E5%85%AD%E4%B8%AA%E6%A0%B8%E6%A1%83&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E5%85%AD%E4%B8%AA&pvid=bfa4f049fcf4433aa41332f6858d6435"},
            {"https://search.jd.com/Search?keyword=%E5%AE%89%E5%90%89%E6%98%9F%E7%B3%BB%E7%BB%9F&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E5%AE%89%E5%90%89%E6%98%9F&pvid=37619dddb8b24b428e0d4cac26690aee","https://search.jd.com/Search?keyword=%E5%AE%89%E5%90%89%E6%98%9F%E7%B3%BB%E7%BB%9F&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E5%AE%89%E5%90%89%E6%98%9F&pvid=37619dddb8b24b428e0d4cac26690aee"},
            {"https://search.jd.com/Search?keyword=%E7%91%9E%E6%96%B0%E5%92%96%E5%95%A1&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E7%91%9E%E6%96%B0&pvid=ea59ac76b07c4928b5398f96e09a5c6e","https://search.jd.com/Search?keyword=%E5%85%AD%E4%B8%AA%E6%A0%B8%E6%A1%83&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E5%85%AD%E4%B8%AA&pvid=bfa4f049fcf4433aa41332f6858d6435"},
            {"https://search.jd.com/Search?keyword=%E7%91%9E%E6%96%B0%E5%92%96%E5%95%A1&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E7%91%9E%E6%96%B0&pvid=ea59ac76b07c4928b5398f96e09a5c6e","https://search.jd.com/Search?keyword=%E5%85%AD%E4%B8%AA%E6%A0%B8%E6%A1%83&enc=utf-8&suggest=1.def.0.V19--12,20,&wq=%E5%85%AD%E4%B8%AA&pvid=bfa4f049fcf4433aa41332f6858d6435"}
    };

    private static FileUtils instance;
    private static final int SUCCESS = 1;
    private static final int FAILED = 0;
    private Context context;
    private FileOperateCallback callback;
    private volatile boolean isSuccess;
    private String errorStr;


    public static FileUtils getInstance(Context context) {
        if (instance == null)
            instance = new FileUtils(context);
        return instance;
    }

    private FileUtils(Context context) {
        this.context = context;

    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (callback != null) {
                if (msg.what == SUCCESS) {
                    callback.onSuccess((List<String>) msg.obj);
                }
                if (msg.what == FAILED) {
                    callback.onFailed(msg.obj.toString());
                }
            }
        }
    };

    public FileUtils copyAssetsToSD(final String srcPath, final String sdPath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> files = new ArrayList<>();
                copyAssetsToDst(context,files, srcPath, sdPath);
                if (isSuccess)
                    handler.obtainMessage(SUCCESS,files).sendToTarget();
                else
                    handler.obtainMessage(FAILED, errorStr).sendToTarget();
            }
        }).start();
        return this;
    }

    public void setFileOperateCallback(FileOperateCallback callback) {
        this.callback = callback;
    }

    private void copyAssetsToDst(Context context, List<String> files, String srcPath, String dstPath) {
        try {
            String fileNames[] = context.getAssets().list(srcPath);
            if (fileNames.length > 0) {
                File file = new File(FileUtils.AppPath, dstPath);
                if (!file.exists()) file.mkdirs();
                for (String fileName : fileNames) {
                    Log.d("=============","fileName = "+fileName);
                    if (!srcPath.equals("")) { // assets 文件夹下的目录
                        copyAssetsToDst(context,files, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
                    } else { // assets 文件夹
                        copyAssetsToDst(context,files, fileName, dstPath + File.separator + fileName);
                    }
                }
            } else {
                File outFile = new File(FileUtils.AppPath, dstPath);
                if(!outFile.exists()){
                    outFile.delete();
                }
                InputStream is = context.getAssets().open(srcPath);
                FileOutputStream fos = new FileOutputStream(outFile);
                byte[] buffer = new byte[1024];
                int byteCount;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
                files.add(outFile.getAbsolutePath());
            }
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            errorStr = e.getMessage();
            isSuccess = false;
        }
    }

    public interface FileOperateCallback {
        void onSuccess(List<String> files);

        void onFailed(String error);
    }


}
