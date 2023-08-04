package com.padyak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class frmSplash extends AppCompatActivity {
    ProgressBar spinner;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_splash);

        spinner = findViewById(R.id.progressBar);
        spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                runOnUiThread(() -> {
                            intent = new Intent(frmSplash.this, frmMain.class);
                            startActivity(intent);
                            finish();
                        }
                );
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }
}