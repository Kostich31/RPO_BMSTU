package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.lab.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int res = initRng();
        byte[] rnd = randomBytes(16);

        Random random = new Random();

        byte[] data = new byte[16];
        for (int i = 0; i < data.length; ++i) {
            data[i] = (byte) ((byte) random.nextInt() % 255);
        }
        Log.i("data", Arrays.toString(data));
        byte[] encrypt_data = encrypt(rnd, data);

        byte[] decrypt_data = decrypt(rnd, encrypt_data);

        Log.i("enc_data", Arrays.toString(encrypt_data));
        Log.i("dec_data", Arrays.toString(decrypt_data));

        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
    }

    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}
