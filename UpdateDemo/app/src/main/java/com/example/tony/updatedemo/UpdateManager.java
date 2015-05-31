package com.example.tony.updatedemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * Created by Tony on 2015/5/31.
 */
public class UpdateManager {
    private String _package = "com.example.tony.updatedemo";
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISHED = 2;
    HashMap<String,String> mHashMap;
    private String mSavePath;
    private int progress;
    private boolean cancleUpdate = false;
    private Context mContext;
    private ProgressBar mProgressBar;
    private Dialog mDownLoadDialog;
    private String URLAddress = "http://hongyan.cqupt.edu.cn/app/cyxbsAppUpdate.xml";

    public UpdateManager(Context context){
        this.mContext = context;
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            //下载
            switch (msg.what) {
                case DOWNLOAD:
                    mProgressBar.setProgress(progress);
                    break;
                case DOWNLOAD_FINISHED:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(mSavePath,mHashMap.get("versionName"));
        if(!apkfile.exists()){
            return;
        }
        //通过intent安装apk
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://"+apkfile.toString()),"application/vnd.android.pacakage-archiever");
        mContext.startActivity(intent);
    }
    //检查更新
    public void checkUpdate(){
        if(isUpdate()){
            showNoticeDialog();
        }else{
            Toast.makeText(mContext,"No update found ",Toast.LENGTH_LONG).show();
        }
    }

    private void showNoticeDialog() {
        //构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.update_title);
        builder.setMessage(R.string.update_info);
        //更新
        builder.setPositiveButton(R.string.update_updatebtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //显示下载对话框
                showDownloadDialog();
            }
        });
        //稍后更新
        builder.setNegativeButton(R.string.update_later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 显示下载对话框
     */
    private void showDownloadDialog() {
        //构造下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.updating);
        //增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.update_progress,null);
        mProgressBar = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        //取消更新
        builder.setNegativeButton(R.string.update_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置取消状态
                cancleUpdate = true;
            }
        });
        mDownLoadDialog = builder.create();
        mDownLoadDialog.show();
    }

    /**
     * 下载apk文件
     * @return
     */
    public void downloadApk(){
        //启动新线程下载文件
        new downloadApkThread().start();
    }

    /**
     * 下载文件线程
     */
    private class downloadApkThread extends Thread{
        @Override
        public void run() {

                try {
                    //判断SD卡是否存在
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        //获取存储路径
                        String sdPath = Environment.getExternalStorageDirectory() + "/";
                        mSavePath = sdPath + "download";
                        URL url = new URL(mHashMap.get("apkURL"));
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.connect();
                        //获取文件大小
                        int length = connection.getContentLength();
                        //创建输入流
                        InputStream is = connection.getInputStream();
                        File file = new File(mSavePath);
                        //
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        File apkFile = new File(mSavePath, mHashMap.get("versionName"));
                        FileOutputStream fileOutputStream = new FileOutputStream(apkFile);
                        int count = 0;
                        //写入到缓存
                        byte buf[] = new byte[1024];
                        //写入到文件
                        do {
                            int numread = is.read(buf);
                            count = count + numread;
                            //计算进度条位置
                            progress = (int) (((float) count / length) * 100);
                            //更新进度
                            mHandler.sendEmptyMessage(DOWNLOAD);
                            if (numread <= 0) {
                                //下载完成
                                mHandler.sendEmptyMessage(DOWNLOAD_FINISHED);
                                break;
                            }
                            //写入文件
                            fileOutputStream.write(buf, 0, numread);
                        } while (!cancleUpdate);
                        fileOutputStream.close();
                        is.close();
                    }
            } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            mDownLoadDialog.dismiss();
        }
    }
    private boolean isUpdate() {
        //获取当前版本号
        int versionCode =getVersionCode(mContext);

        //获取xml文件信息
        InputStream inStream  = ParseXmlService.class.getClassLoader().getResourceAsStream("cyxbsAppUpdate.xml");
        //解析XML文件
        ParseXmlService service = new ParseXmlService();
        try {
            mHashMap = service.parseXml(inStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mHashMap != null){
            int serviceCode = Integer.valueOf(mHashMap.get("versionCode"));
            //版本判断
            System.out.println("service"+serviceCode);
            if(serviceCode>versionCode){
                return true;
            }
        }
        return false;
    }

    public int getVersionCode(Context context)
    {
        int versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(_package,0).versionCode;
            System.out.println(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
