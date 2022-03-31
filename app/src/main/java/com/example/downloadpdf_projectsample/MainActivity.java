package com.example.downloadpdf_projectsample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
//    private EditText url;
//    private Button download;

    protected long downloadId;
    private ProgressBar progressBar;
    private PDFView pdfView;

//    String getUrl = url.getText().toString();
//
//    String title = URLUtil.guessFileName(url.getText().toString(), null, null);
//
//    File filePath = getCacheDir();
//
//    File file = new File(filePath.getAbsolutePath() + "/.temp", title);
//
//    Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//            BuildConfig.APPLICATION_ID + ".provider", file);


//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        url = findViewById(R.id.urlLink);
//        download = findViewById(R.id.dowloadBtn);

        performDowload("https://www.learningcontainer.com/wp-content/uploads/2019/09/sample-pdf-download-10-mb.pdf");
//        performDowload("https://dev-kelaspintar.extramarks.com/content_data/e-book/2020/08/05/2502992/Ebook_Gr11_Geografi_Ch6_Keberagaman-Budaya-Bangsa-Sebagai-Identitas-Nasional-Berdasarkan-Keunikan_dan_Sebarannya.pdf");

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

//        unregisterReceiver(receiver);


//        download.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String getUrl = url.getText().toString();
//
//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrl));
//                String title = URLUtil.guessFileName(getUrl, null, null);
//                request.setTitle(title);
//                request.setDescription("Downloading File...");
//                String cookie = CookieManager.getInstance().getCookie(getUrl);
//                request.addRequestHeader("cookie", cookie);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
//                File filePath = getCacheDir();
//                request.setDestinationInExternalFilesDir(MainActivity.this, filePath.getAbsolutePath() + "/.temp", URLUtil.guessFileName(getUrl, null, null));
//
//                DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
//                downloadId = downloadManager.enqueue(request);
//
//                progressBar = findViewById(R.id.progressBar);
//                Timer myTimer = new Timer();
//                myTimer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        DownloadManager.Query q = new DownloadManager.Query();
//                        q.setFilterById(downloadId);
//                        Cursor cursor = downloadManager.query(q);
//                        cursor.moveToFirst();
//                        @SuppressLint("Range") long bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
//                        @SuppressLint("Range") long bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
//                        cursor.close();
//                        double dl_progress = ((bytes_downloaded * 1f / bytes_total) * 100);
//                        runOnUiThread(new Runnable(){
//                            @Override
//                            public void run(){
//                                progressBar.setProgress((int) dl_progress);
//                            }
//                        });
//                    }
//                }, 0, 10);




//
//                File file = new File(filePath.getAbsolutePath() + "/.temp", title);
//
////                Uri uri = FileProvider.getUriForFile(MainActivity.this, "com.example.downloadpdf_projectsample", file);
//                Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                        BuildConfig.APPLICATION_ID + ".provider", file);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(uri, "application/pdf");
//                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                startActivity(intent);
//            }
//        });
    }

    private void performDowload(String url){
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        String title = URLUtil.guessFileName(uri.toString(), null, null);
        request.setTitle(title);
        request.setDescription("Downloading File...");
        String cookie = CookieManager.getInstance().getCookie(uri.toString());
        request.addRequestHeader("cookie", cookie);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        File filePath = getCacheDir();
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
        request.setDestinationInExternalFilesDir(MainActivity.this, filePath.getAbsolutePath() + "/.temp", "test.pdf");

        DownloadManager dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        
        downloadId = dm.enqueue(request);
        

        progressBar = findViewById(R.id.progressBar);
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query q = new DownloadManager.Query();
                q.setFilterById(downloadId);
                Cursor cursor = dm.query(q);
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


    private BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
//                            @SuppressLint("Range") String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            //TODO : Use this local uri and launch intent to open file
//                            pdfView = findViewById(R.id.pdfView);
//
//                            pdfView.fromUri(Uri.parse(uriString))
//                                    .enableSwipe(true)
//                                    .swipeHorizontal(false)
//                                    .enableAnnotationRendering(true)
//                                    .load();
//                            String title = URLUtil.guessFileName(uri.toString(), null, null);
//                            File filePath = getCacheDir();
                            File file = new File(getCacheDir().getAbsolutePath() + "/.temp" + "test.pdf");

                            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                                    BuildConfig.APPLICATION_ID + ".provider", file);
//                            pdfView = findViewById(R.id.pdfView);

//                            pdfView.fromUri(Uri.parse(uriString))
//                                    .enableSwipe(true)
//                                    .swipeHorizontal(false)
//                                    .enableAnnotationRendering(true)
//                                    .load();
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            startActivity(intent);
                        }
                    }
                }
            }
        };

    protected void onDestroy() {
         super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
    }
//    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//
//            if (downloadId == id) {
////                pdfView.fromUri(uri)
////                        .enableSwipe(true)
////                        .swipeHorizontal(false)
////                        .enableAnnotationRendering(true)
////                        .load();
//
//                Toast.makeText(MainActivity.this, "Download Completed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    };

//    private Intent openPdf() {
//        String getUrl = url.getText().toString();
//        String title = URLUtil.guessFileName(getUrl, null, null);
//        File filePath = getCacheDir();
//        File file = new File(filePath.getAbsolutePath() + "/.temp", title);
//        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                BuildConfig.APPLICATION_ID + ".provider", file);
//        pdfView.fromUri(uri)
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .enableAnnotationRendering(true)
//                .load();
//
//        return openPdf();
//    }
}