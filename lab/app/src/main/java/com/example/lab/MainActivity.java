package com.example.lab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.app.Activity;
import android.content.Intent;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements TransactionEvents {

    ActivityResultLauncher activityResultLauncher;

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("mbedcrypto");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        pin = data.getStringExtra("pin");
                        synchronized (MainActivity.this) {
                            MainActivity.this.notifyAll();
                        }
                    }
                });

        int res = initRng();
        byte[] rnd = randomBytes(10);
    }

    public void onButtonClick(View view) {
        new Thread(() -> {
            try {
                byte[] trd = stringToHex("9F0206000000000100");
                transaction(trd);
            } catch (Exception exception) {
                Log.println(Log.ERROR, "MtLog", Arrays.toString(exception.getStackTrace()));
            }
        }).start();
    }

    public static byte[] stringToHex(String s) {
        byte[] hex;
        try {
            hex = Hex.decodeHex(s.toCharArray());
        } catch (DecoderException ex) {
            hex = null;
        }
        return hex;
    }

    public static native int initRng();

    public static native byte[] randomBytes(int no);

    public static native byte[] encrypt(byte[] key, byte[] data);

    public static native byte[] decrypt(byte[] key, byte[] data);

    public native boolean transaction(byte[] trd);


    private String pin;

    @Override
    public String enterPin(int ptc, String amount) {
        pin = new String();

        Intent intent = new Intent(MainActivity.this, PinpadActivity.class);
        intent.putExtra("ptc", ptc);
        intent.putExtra("amount", amount);

        synchronized (MainActivity.this) {
            activityResultLauncher.launch(intent);
            try {
                MainActivity.this.wait();
            } catch (Exception exception) {
                Log.println(Log.ERROR, "MtLog", exception.getMessage());
            }
        }

        return pin;
    }
    @Override
    public void transactionResult(boolean result) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, result ? "ok" : "failed", Toast.LENGTH_SHORT).show();
        });
    }
}
