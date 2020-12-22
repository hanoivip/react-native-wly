package org.cocos2dx.lib;

import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Cocos2dxRenderer implements Renderer {
	private static final long NANOSECONDSPERMINISECOND = 1000000;
	private static final long NANOSECONDSPERSECOND = 1000000000;
	private static long animationInterval;
	private long last;
	private int screenHeight;
	private int screenWidth;

	static {
		animationInterval = 16666666;
	}

	private static native void nativeDeleteBackward();

	private static native String nativeGetContentText();

	private static native void nativeInit(int i, int i2);

	private static native void nativeInsertText(String str);

	private static native boolean nativeKeyDown(int i);

	private static native void nativeOnPause();

	private static native void nativeOnResume();

	private static native void nativeRender();

	private static native void nativeTouchesBegin(int i, float f, float f2);

	private static native void nativeTouchesCancel(int[] iArr, float[] fArr,
			float[] fArr2);

	private static native void nativeTouchesEnd(int i, float f, float f2);

	private static native void nativeTouchesMove(int[] iArr, float[] fArr,
			float[] fArr2);

	public static void setAnimationInterval(double d) {
		animationInterval = (long) (1.0E9d * d);
	}

	public String getContentText() {
		return nativeGetContentText();
	}

	public void handleActionCancel(int[] iArr, float[] fArr, float[] fArr2) {
		nativeTouchesCancel(iArr, fArr, fArr2);
	}

	public void handleActionDown(int i, float f, float f2) {
		nativeTouchesBegin(i, f, f2);
	}

	public void handleActionMove(int[] iArr, float[] fArr, float[] fArr2) {
		nativeTouchesMove(iArr, fArr, fArr2);
	}

	public void handleActionUp(int i, float f, float f2) {
		nativeTouchesEnd(i, f, f2);
	}

	public void handleDeleteBackward() {
		nativeDeleteBackward();
	}

	public void handleInsertText(String str) {
		nativeInsertText(str);
	}

	public void handleKeyDown(int i) {
		nativeKeyDown(i);
	}

	public void handleOnPause() {
		nativeOnPause();
	}

	public void handleOnResume() {
		nativeOnResume();
	}

	public void onDrawFrame(GL10 gl10) {
		long nanoTime = System.nanoTime();
		long j = nanoTime - this.last;
		nativeRender();
		if (j < animationInterval) {
			try {
				Thread.sleep(((animationInterval - j) * 2)
						/ NANOSECONDSPERMINISECOND);
			} catch (Exception e) {
			}
		}
		this.last = nanoTime;
	}

	public void onSurfaceChanged(GL10 gl10, int i, int i2) {
	}

	public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
		nativeInit(this.screenWidth, this.screenHeight);
		this.last = System.nanoTime();
	}

	public void setScreenWidthAndHeight(int i, int i2) {
		this.screenWidth = i;
		this.screenHeight = i2;
	}
}
