package ru.iu3.fclient;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import android.os.Bundle;
import android.view.View;

import java.text.DecimalFormat;

public class PinpadActivity extends AppCompatActivity {
    TextView textViewPin;
    String pin = "";
    final int MAX_KEYS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpad);

        textViewPin = findViewById(R.id.txtPin);

        //ShuffleKeys();

        // Получаем ID-шники кнопки и назначаем на них быстрое событие
        findViewById(R.id.btnOK).setOnClickListener((View) -> {
            Intent intent = new Intent();
            intent.putExtra("pin", pin);
            setResult(RESULT_OK, intent);

            finish();
        });

        findViewById(R.id.btnReset).setOnClickListener((View) -> {
           pin = "";
           textViewPin.setText(pin);
        });

        // Обработка txtAmount
        TextView ta = findViewById(R.id.txtAmount);

        String amt = String.valueOf(getIntent().getStringExtra("amount"));
        Long f = Long.valueOf(amt);
        DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
        String s = df.format(f);

        ta.setText("Summ: " + s);

        // Обработка txtPtc
        TextView tp = findViewById(R.id.txtPtc);
        int pts = getIntent().getIntExtra("ptc", 0);
        if (pts == 2) {
            tp.setText("2 popitki ostalos");
        } else if (pts == 1) {
            tp.setText("1 popitka ostalas");
        }
    }

    // Обработчик события нажатия кнопки
    public void keyClick(View view) {
        // Получаем текущий ключ
        String key = ((TextView) view).getText().toString();

        int size = pin.length();
        if (size < 4) {
            pin += key;
            textViewPin.setText("****".substring(3 - size));
        }
    }

//    protected void ShuffleKeys() {
//        Button[] keys = new Button[] {
//                findViewById(R.id.btnKey0),
//                findViewById(R.id.btnKey1),
//                findViewById(R.id.btnKey2),
//                findViewById(R.id.btnKey3),
//                findViewById(R.id.btnKey4),
//                findViewById(R.id.btnKey5),
//                findViewById(R.id.btnKey6),
//                findViewById(R.id.btnKey7),
//                findViewById(R.id.btnKey8),
//                findViewById(R.id.btnKey9)
//        };
//
//        byte[] rnd = MainActivity.randomBytes(MAX_KEYS);
//        for (int i = 0; i < MAX_KEYS; ++i) {
//            int idx = (rnd[i] & 0xFF) % 10;
//            CharSequence txt = keys[idx].getText();
//
//            keys[idx].setText(keys[i].getText());
//            keys[i].setText(txt);
//        }
//    }
}