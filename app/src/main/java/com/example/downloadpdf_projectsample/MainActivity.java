package com.example.downloadpdf_projectsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private EditText url;
    private Button download;
    protected long downloadId;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = (EditText)findViewById(R.id.urlLink);
        download = (Button)findViewById(R.id.dowloadBtn);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUrl = url.getText().toString();

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrl));
                String title = URLUtil.guessFileName(getUrl, null, null);
                request.setTitle(title);
                request.setDescription("Downloading File...");
                String cookie = CookieManager.getInstance().getCookie(getUrl);
                request.addRequestHeader("cookie", cookie);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                File filePath = Environment.getDownloadCacheDirectory();
                request.setDestinationInExternalFilesDir(MainActivity.this, filePath.getAbsolutePath() + "/.data", URLUtil.guessFileName(getUrl, null, null));

                DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                downloadId = downloadManager.enqueue(request);

                progressBar = findViewById(R.id.progressBar);
                Timer myTimer = new Timer();
                myTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        DownloadManager.Query q = new DownloadManager.Query();
                        q.setFilterById(downloadId);
                        Cursor cursor = downloadManager.query(q);
                        cursor.moveToFirst();
                        @SuppressLint("Range") long bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        @SuppressLint("Range") long bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        cursor.close();
                        double dl_progress = ((bytes_downloaded * 1f / bytes_total) * 100);
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                progressBar.setProgress((int) dl_progress);
                            }
                        });
                    }
                }, 0, 10);

                Toast.makeText(MainActivity.this, "Downloading Started", Toast.LENGTH_SHORT).show();
            }
        });

//        BroadcastReceiver receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//
//                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
//                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
//                    DownloadManager.Query query = new DownloadManager.Query();
//                    query.setFilterById(enquee);
//                    Cursor c = dm.query(query);
//                    if (c.moveToFirst()){
//                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
//                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)){
//                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//                            Uri a = Uri.parse(uriString);
//                            File d = new File(a.getPath());
//                        }
//                    }
//                }
//            }
//        }
    }

}