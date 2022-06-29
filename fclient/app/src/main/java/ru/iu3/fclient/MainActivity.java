package ru.iu3.fclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.iu3.fclient.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    // Used to load the 'RPO2022' library on application startup.
//    static {
//        System.loadLibrary("fclient");
//        System.loadLibrary("mbedcrypto");
//    }

    private ActivityMainBinding binding;

    // Метод, который возвращает название говносайта
    protected String getPageTitle(String html) {
        Pattern pattern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);

        String p;
        if (matcher.find()) {
            p = matcher.group(1);
        } else {
            p = "Not found";
        }

        return p;
    }

    // Метод, который тестирует работу http клиента
    protected void testHttpClient() {
        new Thread(() -> {
            try {
                HttpURLConnection uc = (HttpURLConnection) (new URL("http://127.0.0.1:8081/api/v1/title").openConnection());
                InputStream inputStream = uc.getInputStream();

                String html = IOUtils.toString(inputStream);
                String title = getPageTitle(html);

                runOnUiThread(() -> {
                    Toast.makeText(this, title, Toast.LENGTH_LONG).show();
                });
            } catch (Exception exception) {
                Log.e("fapptag", "Http client fails", exception);
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button myButton = (Button) findViewById(R.id.sample_button);
    }

    // Альтернативный метод нажатия кнопки
    public void onButtonClick(View view) {
        testHttpClient();
    }
}
