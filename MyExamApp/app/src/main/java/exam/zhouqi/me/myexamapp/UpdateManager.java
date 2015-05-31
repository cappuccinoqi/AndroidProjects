package exam.zhouqi.me.myexamapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Tony on 2015/5/31.
 */
public class UpdateManager {
    private Context mContext;
    private String updateMsg = "有新的更新";
    private String apkUrl = "";
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    private int oldVerCode;
    private static final String savePath = "/sdcard/download";
    private static final String saveFileName = savePath + "update.apk";
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_FINISHED = 2;

    private int progress;
    private Thread downloadThread;
    private boolean interceptFlag = false;
    private ContentHandler contentHandler;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_FINISHED:
                    installApk();
                    break;
                default:
                    break;
            }
        }
    };

    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    public void checkUpdateInfo() {
        try {
            oldVerCode = mContext.getPackageManager().getPackageInfo("exam.zhouqi.me.myexamapp", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int versionCode = contentHandler.getVersionCode();
        if (oldVerCode < versionCode) {
            showNoticeDialog();
        } else {
            Toast.makeText(mContext, "NO Update found", Toast.LENGTH_LONG).show();
        }

    }

    private void showNoticeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });

        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress_bar);

        builder.setView(v);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();
        downloadApk();
    }

    private Runnable mdownLoadApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                apkUrl = contentHandler.getApkURL();
                URL url = new URL(apkUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int length = connection.getContentLength();
                InputStream is = connection.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }

                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fileOutputStream = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        mHandler.sendEmptyMessage(DOWN_FINISHED);
                        break;
                    }
                    fileOutputStream.write(buf, 0, numread);
                } while (!interceptFlag);
                fileOutputStream.close();
                is.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    //下载apk
    private void downloadApk() {
        downloadThread = new Thread(mdownLoadApkRunnable);
//        Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                checkUpdateInfo();
//            }
//        });
        downloadThread.start();
    }
}
