package com.example.womensafety;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class  Sensors  implements SensorEventListener {
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private onshakeListener mListener;
    private long ShakeTime;
    private int shakeCount;

    public void setOnShakeListener(onshakeListener listener) {
        this.mListener = listener;
    }

    public interface onshakeListener {
        public void onShake(int count);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x/SensorManager.GRAVITY_EARTH;
            float gY = y/SensorManager.GRAVITY_EARTH;
            float gZ = z /SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            Float f = new Float(gX * gX + gY * gY + gZ * gZ);
            Double d = Math.sqrt(f.doubleValue());
            float gForce = d.floatValue();

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (ShakeTime + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (ShakeTime + SHAKE_COUNT_RESET_TIME_MS < now) {
                    shakeCount = 0;
                }

                ShakeTime = now;
                shakeCount++;

                mListener.onShake(shakeCount);
            }
        }
    }
}
