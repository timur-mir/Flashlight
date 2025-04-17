package com.example.flashlight;

import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private TextView textFlashlight;
    private ToggleButton toggleButton;
    private CameraManager mCameraManager;
    private String mCameraId;
    MediaPlayer mp;
    RelativeLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleButton = findViewById(R.id.flashlightButton);
        textFlashlight = findViewById(R.id.textFlashlight);
        currentLayout =
                (RelativeLayout) findViewById(R.id.rl);
        textFlashlight.setText("Включить");

        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            flashNotFoundError();
        }


        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchFlashLight(isChecked);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void switchFlashLight(boolean status) {

        try {

            if (status == true) {
                textFlashlight.setText("Выключить");
                currentLayout.setBackgroundColor(Color.BLACK);
                playSound(true);
            } else {
                textFlashlight.setText("Включить");
                currentLayout.setBackgroundColor(Color.parseColor("#AF4444"));
                playSound(true);
            }

            mCameraManager.setTorchMode(mCameraId, status);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void flashNotFoundError() {
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Возникла проблема!");
        alert.setMessage("У Вас на устройстве не поддерживается возможность светодионого освещения.");
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.show();
    }

    private void playSound(Boolean isFlashOn) {
        if (isFlashOn) {
            mp = MediaPlayer.create(MainActivity.this, R.raw.sound);
        } else {
            mp = MediaPlayer.create(MainActivity.this, R.raw.sound);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }


}
