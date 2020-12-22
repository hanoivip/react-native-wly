package org.cocos2dx.lib;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.WindowManager;

public class Cocos2dxAccelerometer implements SensorEventListener {
	private static final String TAG = "Cocos2dxAccelerometer";
	private Sensor mAccelerometer;
	private Context mContext;
	private int mNaturalOrientation;
	private SensorManager mSensorManager;

	public Cocos2dxAccelerometer(Context context) {
		this.mContext = context;
		this.mSensorManager = (SensorManager) this.mContext
				.getSystemService("sensor");
		this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
		this.mNaturalOrientation = ((WindowManager) this.mContext
				.getSystemService("window")).getDefaultDisplay()
				.getOrientation();
	}

	private static native void onSensorChanged(float var0, float var1,
			float var2, long var3);

	public void disable() {
		this.mSensorManager.unregisterListener((SensorEventListener) this);
	}

	public void enable() {
		this.mSensorManager.registerListener((SensorEventListener) this,
				this.mAccelerometer, 1);
	}

	public void onAccuracyChanged(Sensor sensor, int n2) {
	}

	/*
	 * Enabled aggressive block sorting
	 */
	public void onSensorChanged(SensorEvent sensorEvent) {
		float f2;
		float f3;
		if (sensorEvent.sensor.getType() != 1) {
			return;
		}
		float f4 = sensorEvent.values[0];
		float f5 = sensorEvent.values[1];
		float f6 = sensorEvent.values[2];
		int n2 = this.mContext.getResources().getConfiguration().orientation;
		if (n2 == 2 && this.mNaturalOrientation != 0) {
			f3 = -f5;
			f2 = f4;
		} else {
			f3 = f4;
			f2 = f5;
			if (n2 == 1) {
				f3 = f4;
				f2 = f5;
				if (this.mNaturalOrientation != 0) {
					f3 = f5;
					f2 = -f4;
				}
			}
		}
		Cocos2dxAccelerometer
				.onSensorChanged(f3, f2, f6, sensorEvent.timestamp);
	}
}
