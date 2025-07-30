package prinsberwa.com.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import prinsberwa.com.R;
import prinsberwa.com.base.BaseActivity;

public class ExpiryActivity extends BaseActivity {

    private static final String APK_DIRECT_URL = "https://prinsberwa.com/download/app-release.apk";
    private static final String APK_FILE_NAME = "prinsberwa-update.apk";

    private LinearLayout playStoreContainer;
    private LinearLayout indusStoreContainer;
    private Button btnDownloadUpdate;
    private ProgressBar downloadProgress;
    private TextView downloadStatus;

    private long downloadID;
    private Timer timer;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry);

        // Initialize UI components
        playStoreContainer = findViewById(R.id.play_store_container);
        indusStoreContainer = findViewById(R.id.indus_store_container);
        btnDownloadUpdate = findViewById(R.id.btn_download_update);
        downloadProgress = findViewById(R.id.download_progress);
        downloadStatus = findViewById(R.id.download_status);

        // Initialize DownloadManager
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        // Set click listeners
        playStoreContainer.setOnClickListener(v -> openUrl("https://play.google.com/store/apps/details?id=" + getPackageName()));
        indusStoreContainer.setOnClickListener(v -> openUrl("https://www.indusappstore.com/"));
        btnDownloadUpdate.setOnClickListener(v -> startDownloadUpdate());

        // Register download complete receiver with proper flag for Android 13+
        IntentFilter downloadCompleteFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(onDownloadComplete, downloadCompleteFilter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(onDownloadComplete, downloadCompleteFilter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister receiver and cancel timer
        try {
            unregisterReceiver(onDownloadComplete);
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void startDownloadUpdate() {
        btnDownloadUpdate.setEnabled(false);
        downloadProgress.setVisibility(View.VISIBLE);
        downloadStatus.setVisibility(View.VISIBLE);
        downloadStatus.setText(R.string.starting_download);

        // Create download request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APK_DIRECT_URL));
        request.setTitle(getString(R.string.app_name) + " Update");
        request.setDescription(getString(R.string.downloading_update));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, APK_FILE_NAME);

        // Start download
        downloadID = downloadManager.enqueue(request);

        // Start progress tracking
        startProgressTracking();
    }

    private void startProgressTracking() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateDownloadProgress());
            }
        }, 0, 500);
    }

    private void updateDownloadProgress() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadID);

        try (Cursor cursor = downloadManager.query(query)) {
            if (cursor != null && cursor.moveToFirst()) {
                int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                int downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);

                if (statusIndex != -1 && downloadedIndex != -1 && totalSizeIndex != -1) {
                    int status = cursor.getInt(statusIndex);
                    long downloadedBytes = cursor.getLong(downloadedIndex);
                    long totalBytes = cursor.getLong(totalSizeIndex);

                    switch (status) {
                        case DownloadManager.STATUS_RUNNING:
                            if (totalBytes > 0) {
                                int progress = (int) ((downloadedBytes * 100) / totalBytes);
                                downloadProgress.setProgress(progress);
                                downloadStatus.setText(getString(R.string.downloading_percentage, progress));
                            }
                            break;
                        case DownloadManager.STATUS_FAILED:
                            stopProgressTracking();
                            downloadStatus.setText(R.string.download_failed);
                            btnDownloadUpdate.setEnabled(true);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopProgressTracking() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    // BroadcastReceiver to handle download completion
    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                stopProgressTracking();
                downloadStatus.setText(R.string.download_complete);
                downloadProgress.setProgress(100);

                // Show install prompt
                showInstallPrompt();
            }
        }
    };

    private void showInstallPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_downloaded);
        builder.setMessage(R.string.install_update_now);
        builder.setPositiveButton(R.string.install, (dialog, which) -> installUpdate());
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
            btnDownloadUpdate.setEnabled(true);
            btnDownloadUpdate.setText(R.string.install_update);
            btnDownloadUpdate.setOnClickListener(v -> installUpdate());
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void installUpdate() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME);
        if (file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri apkUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                apkUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                apkUri = Uri.fromFile(file);
            }

            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, R.string.update_file_not_found, Toast.LENGTH_LONG).show();
            btnDownloadUpdate.setEnabled(true);
        }
    }
}