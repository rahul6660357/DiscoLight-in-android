package com.lcoa.discolight;

import android.Manifest;
import android.content.Context;


import android.content.pm.PackageManager;
// 3rd pull request
import android.hardware.camera2.CameraManager;


import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private CameraManager cameraManager;

    SeekBar seek;
int blinkinterval;

    private boolean istorchon=false;
    boolean btnenabled;
Switch Button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
Button1=findViewById(R.id.btn);
        seek = findViewById(R.id.myseek);
        seek.setMax(100);
        getSupportActionBar().hide();//hiding the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//for full screen



        Button1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA},10);
                } else if (Button1.isChecked()) {
                    startblink();
                }
                else if(!Button1.isChecked())
                {
                    flashlightoff();
                    return;
                }
            }
        });
    }
    private void startblink() {
        Timer t1 = new Timer();
        if(!Button1.isChecked()) {
        t1.cancel();
        flashlightoff();
        istorchon=false;
        return;
        }
        blinkinterval=1000/(seek.getProgress()==0?1:seek.getProgress());
        t1.schedule(new TimerTask() {
            @Override
            public void run() {
                if(istorchon) {
                    flashlightoff();
                    istorchon=false;

                }
                else {
                    flashlighton();
                    istorchon=true;
                }
                startblink();
            }
        },blinkinterval);


    }
    void flashlighton() {
        CameraManager cameramanager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            String backcameraid=cameramanager.getCameraIdList()[0];
            cameramanager.setTorchMode(backcameraid,true);
        }
        catch(Exception e)
        {

        }
    }
    void flashlightoff() {
        CameraManager cameramanager=(CameraManager)getSystemService(Context.CAMERA_SERVICE);
        try {
            String backcameraid=cameramanager.getCameraIdList()[0];
            cameramanager.setTorchMode(backcameraid,false);
        }
        catch(Exception e)
        {

        }
    }





}
