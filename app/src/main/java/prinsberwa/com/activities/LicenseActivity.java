package prinsberwa.com.activities;

import android.os.Bundle;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import prinsberwa.com.R;
import prinsberwa.com.base.BaseActivity;

public class LicenseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        WebView licensesWebView = findViewById(R.id.licensesWebView);
        String licensesHtml = readLicensesFromFile();
        licensesWebView.loadDataWithBaseURL(null, licensesHtml, "text/html", "utf-8", null);
    }

    private String readLicensesFromFile() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.licenses);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "<html><body><h2>Error reading licenses</h2></body></html>";
        }
    }
}
