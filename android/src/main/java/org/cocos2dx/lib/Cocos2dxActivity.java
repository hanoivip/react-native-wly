package org.cocos2dx.lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import java.util.Locale;
import org.cocos2dx.lib.Cocos2dxAccelerometer;
import org.cocos2dx.lib.Cocos2dxBitmap;
import org.cocos2dx.lib.Cocos2dxMusic;
import org.cocos2dx.lib.Cocos2dxSound;
import org.cocos2dx.lib.DialogMessage;

public class Cocos2dxActivity extends Activity {
	private static final int HANDLER_SHOW_DIALOG = 1;
	private static Cocos2dxAccelerometer accelerometer;
	private static boolean accelerometerEnabled;
	private static Cocos2dxMusic backgroundMusicPlayer;
	private static Handler handler;
	protected static Cocos2dxActivity ma;
	private static String packageName;
	private static Cocos2dxSound soundPlayer;

	static {
		accelerometerEnabled = false;
	}

	public static void disableAccelerometer() {
		accelerometerEnabled = false;
		accelerometer.disable();
	}

	public static void enableAccelerometer() {
		accelerometerEnabled = true;
		accelerometer.enable();
	}

	public static void end() {
		backgroundMusicPlayer.end();
		soundPlayer.end();
	}

	public static float getBackgroundMusicVolume() {
		return backgroundMusicPlayer.getBackgroundVolume();
	}

	public static String getCocos2dxPackageName() {
		return packageName;
	}

	public static String getCurrentLanguage() {
		return Locale.getDefault().getLanguage();
	}

	public static float getEffectsVolume() {
		return soundPlayer.getEffectsVolume();
	}

	public static boolean isBackgroundMusicPlaying() {
		return backgroundMusicPlayer.isBackgroundMusicPlaying();
	}

	private static native void nativeSetPaths(String var0);

	public static void pauseAllEffects() {
		soundPlayer.pauseAllEffects();
	}

	public static void pauseBackgroundMusic() {
		backgroundMusicPlayer.pauseBackgroundMusic();
	}

	public static void pauseEffect(int n2) {
		soundPlayer.pauseEffect(n2);
	}

	public static void playBackgroundMusic(String string2, boolean bl) {
		backgroundMusicPlayer.playBackgroundMusic(string2, bl);
	}

	public static int playEffect(String string2, boolean bl) {
		return soundPlayer.playEffect(string2, bl);
	}

	public static void preloadBackgroundMusic(String string2) {
		backgroundMusicPlayer.preloadBackgroundMusic(string2);
	}

	public static void preloadEffect(String string2) {
		soundPlayer.preloadEffect(string2);
	}

	public static void resumeAllEffects() {
		soundPlayer.resumeAllEffects();
	}

	public static void resumeBackgroundMusic() {
		backgroundMusicPlayer.resumeBackgroundMusic();
	}

	public static void resumeEffect(int n2) {
		soundPlayer.resumeEffect(n2);
	}

	public static void rewindBackgroundMusic() {
		backgroundMusicPlayer.rewindBackgroundMusic();
	}

	public static void setBackgroundMusicVolume(float f2) {
		backgroundMusicPlayer.setBackgroundVolume(f2);
	}

	public static void setEffectsVolume(float f2) {
		soundPlayer.setEffectsVolume(f2);
	}

	private void showDialog(String string2, String string3) {
		new AlertDialog.Builder((Context) this)
				.setTitle((CharSequence) string2)
				.setMessage((CharSequence) string3)
				.setPositiveButton((CharSequence) "Ok",
						new DialogInterface.OnClickListener() {

							public void onClick(
									DialogInterface dialogInterface, int n2) {
							}
						}).create().show();
	}

	public static void showMessageBox(String string2, String string3) {
		Message message = new Message();
		message.what = 1;
		message.obj = new DialogMessage(string2, string3);
		handler.sendMessage(message);
	}

	public static void stopAllEffects() {
		soundPlayer.stopAllEffects();
	}

	public static void stopBackgroundMusic() {
		backgroundMusicPlayer.stopBackgroundMusic();
	}

	public static void stopEffect(int n2) {
		soundPlayer.stopEffect(n2);
	}

	public static void terminateProcess() {
		Process.killProcess((int) Process.myPid());
	}

	public static void unloadEffect(String string2) {
		soundPlayer.unloadEffect(string2);
	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		ma = this;
		DisplayMetrics metrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		accelerometer = new Cocos2dxAccelerometer((Context) this);
		backgroundMusicPlayer = new Cocos2dxMusic((Context) this);
		soundPlayer = new Cocos2dxSound((Context) this);
		Cocos2dxBitmap.setContext((Context) this);
		handler = new Handler() {

			public void handleMessage(Message message) {
				switch (message.what) {
				default: {
					return;
				}
				case 1:
				}
				Cocos2dxActivity.this.showDialog(
						((DialogMessage) message.obj).title,
						((DialogMessage) message.obj).message);
			}
		};
	}

	protected void onPause() {
		super.onPause();
		if (accelerometerEnabled) {
			accelerometer.disable();
		}
	}

	protected void onResume() {
		super.onResume();
		if (accelerometerEnabled) {
			accelerometer.enable();
		}
	}

	protected void setPackageName(String str) {
		packageName = str;
		try {
			String str2 = getApplication().getPackageManager()
					.getApplicationInfo(str, 0).sourceDir;
			Log.w("apk path", str2);
			nativeSetPaths(str2);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to locate assets, aborting...");
		}
	}

}
