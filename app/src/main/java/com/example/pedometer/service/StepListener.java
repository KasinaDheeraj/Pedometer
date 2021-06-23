package com.example.pedometer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.pedometer.MainActivity;

public class StepListener extends Service implements SensorEventListener {
    public static float steps=0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        SensorManager sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor stepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepCounter != null) {
            Toast.makeText(this, "Started Counting Steps", Toast.LENGTH_LONG).show();
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Device not Compatible!", Toast.LENGTH_LONG).show();
            this.stopSelf();
        }

        //implementing batch mode
        if(stepCounter!=null) {
            final boolean batchMode = sensorManager.registerListener(
                    this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL, 2000000);
            if (!batchMode) {
                Log.e("BATCH_MODE", "~~~~~~~~~~~Could not register batch mode for sensor~~~~~~~~~~~");
            }
        }

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR){
            steps++;
            //Toast.makeText(getApplicationContext(),"STEPS : "+steps,Toast.LENGTH_SHORT).show();
            MainActivity.STEPS=steps;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
