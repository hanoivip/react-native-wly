package org.cocos2dx.lib;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.core.view.MotionEventCompat;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import org.cocos2dx.lib.Cocos2dxEditText;
import org.cocos2dx.lib.Cocos2dxRenderer;
import org.cocos2dx.lib.TextInputWraper;

import vn.zing.ngoalongmb.GameHandler;

public class Cocos2dxGLSurfaceView extends GLSurfaceView {
	private static final int HANDLER_CLOSE_IME_KEYBOARD = 3;
	private static final int HANDLER_OPEN_IME_KEYBOARD = 2;
	private static final String TAG = Cocos2dxGLSurfaceView.class
			.getCanonicalName();
	private static final boolean debug = false;
	private static Handler handler;
	private static Cocos2dxGLSurfaceView mainView;
	private static TextInputWraper textInputWraper;
	private Cocos2dxRenderer mRenderer;
	private Cocos2dxEditText mTextField;

	public Cocos2dxGLSurfaceView(Context context) {
		super(context);
		this.initView();
	}

	public Cocos2dxGLSurfaceView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.initView();
	}

	public static void closeIMEKeyboard() {
		Message message = new Message();
		message.what = 3;
		handler.sendMessage(message);
	}

	private void dumpEvent(MotionEvent motionEvent) {
		StringBuilder stringBuilder = new StringBuilder();
		int n2 = motionEvent.getAction();
		int n3 = n2 & 255;
		stringBuilder.append("event ACTION_").append(
				new String[] { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
						"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" }[n3]);
		if (n3 == 5 || n3 == 6) {
			stringBuilder.append("(pid ").append(n2 >> 8);
			stringBuilder.append(")");
		}
		stringBuilder.append("[");
		n2 = 0;
		do {
			if (n2 >= motionEvent.getPointerCount()) {
				stringBuilder.append("]");
				Log.d((String) TAG, (String) stringBuilder.toString());
				return;
			}
			stringBuilder.append("#").append(n2);
			stringBuilder.append("(pid ").append(motionEvent.getPointerId(n2));
			stringBuilder.append(")=").append((int) motionEvent.getX(n2));
			stringBuilder.append(",").append((int) motionEvent.getY(n2));
			if (n2 + 1 < motionEvent.getPointerCount()) {
				stringBuilder.append(";");
			}
			++n2;
		} while (true);
	}

	private String getContentText() {
		return this.mRenderer.getContentText();
		
	}

	public static void openIMEKeyboard() {
		Message message = new Message();
		message.what = 2;
		message.obj = mainView.getContentText();
		handler.sendMessage(message);
	}

	public void deleteBackward() {
		this.queueEvent(new Runnable() {

			@Override
			public void run() {
				Cocos2dxGLSurfaceView.this.mRenderer.handleDeleteBackward();
			}
		});
	}

	public TextView getTextField() {
		System.out.println(mTextField);
		return this.mTextField;
	}

	protected void initView() {
		this.mRenderer = new Cocos2dxRenderer();
		this.setFocusableInTouchMode(true);
		this.setRenderer((GLSurfaceView.Renderer) this.mRenderer);
		textInputWraper = new TextInputWraper(this);
		handler = new Handler() {

			/*
			 * Enabled aggressive block sorting
			 */
			public void handleMessage(Message object) {
				switch (object.what) {
				case 2: {
					if (Cocos2dxGLSurfaceView.this.mTextField == null
							|| !Cocos2dxGLSurfaceView.this.mTextField
									.requestFocus())
						return;
					{
						Cocos2dxGLSurfaceView.this.mTextField
								.removeTextChangedListener((TextWatcher) textInputWraper);
						Cocos2dxGLSurfaceView.this.mTextField
								.setText((CharSequence) "");
						String msg = (String) object.obj;
						Cocos2dxGLSurfaceView.this.mTextField
								.append((CharSequence) msg);
						textInputWraper.setOriginText((String) msg);
						Cocos2dxGLSurfaceView.this.mTextField
								.addTextChangedListener((TextWatcher) textInputWraper);
						((InputMethodManager) mainView.getContext()
								.getSystemService("input_method"))
								.showSoftInput(
										(View) Cocos2dxGLSurfaceView.this.mTextField,
										0);
						Log.d((String) "GLSurfaceView",
								(String) "showSoftInput...");
						return;
					}
				}
				default: {
					return;
				}
				case 3: {
					if (Cocos2dxGLSurfaceView.this.mTextField == null)

						((InputMethodManager) mainView.getContext()
								.getSystemService("input_method"))
								.hideSoftInputFromWindow(
										Cocos2dxGLSurfaceView.this.mTextField
												.getWindowToken(), 0);
					Log.d((String) "GLSurfaceView", (String) "HideSoftInput");

				}
				}
			}
		};
		mainView = this;
	}

	public void insertText(final String string2) {
		this.queueEvent(new Runnable() {

			@Override
			public void run() {
				Cocos2dxGLSurfaceView.this.mRenderer.handleInsertText(string2);
			}
		});
	}

	public boolean onKeyDown(final int n2, KeyEvent keyEvent) {
		//if (n2 == 4 || n2 == 82) {
        //hanoivip
        if (n2 == 82) {
			this.queueEvent(new Runnable() {

				@Override
				public void run() {
					Cocos2dxGLSurfaceView.this.mRenderer.handleKeyDown(n2);
				}
			});
			return true;
		}
		return super.onKeyDown(n2, keyEvent);
	}

	public void onPause() {
		this.queueEvent(new Runnable() {

			@Override
			public void run() {
				Cocos2dxGLSurfaceView.this.mRenderer.handleOnPause();
			}
		});
	}

	public void onResume() {
		super.onResume();
		this.queueEvent(new Runnable() {

			@Override
			public void run() {
				Cocos2dxGLSurfaceView.this.mRenderer.handleOnResume();
			}
		});
	}

	protected void onSizeChanged(int n2, int n3, int n4, int n5) {
		this.mRenderer.setScreenWidthAndHeight(n2, n3);
	}

	public boolean onTouchEvent(final MotionEvent motionEvent) {
		int i;
		int pointerCount = motionEvent.getPointerCount();
		final int[] iArr = new int[pointerCount];
		final float[] fArr = new float[pointerCount];
		final float[] fArr2 = new float[pointerCount];
		for (i = 0; i < pointerCount; i++) {
			iArr[i] = motionEvent.getPointerId(i);
			fArr[i] = motionEvent.getX(i);
			fArr2[i] = motionEvent.getY(i);
		}
		switch (motionEvent.getAction() & MotionEventCompat.ACTION_MASK) {
		case 0:
			queueEvent(new Runnable() {

				@Override
				public void run() {
					Cocos2dxGLSurfaceView.this.mRenderer.handleActionDown(
							motionEvent.getPointerId(0), fArr[0], fArr2[0]);
				}

			});
			break;
		case GameHandler.ENTER_GAME /* 1 */:
			queueEvent(new Runnable() {

				@Override
				public void run() {
					Cocos2dxGLSurfaceView.this.mRenderer.handleActionUp(
							motionEvent.getPointerId(0), fArr[0], fArr2[0]);
				}

			});
			break;
		case HANDLER_OPEN_IME_KEYBOARD /* 2 */:
			queueEvent(new Runnable() {

				@Override
				public void run() {
					Cocos2dxGLSurfaceView.this.mRenderer.handleActionMove(iArr,
							fArr, fArr2);
				}

			});
			break;
		case HANDLER_CLOSE_IME_KEYBOARD /* 3 */:
			queueEvent(new Runnable() {

				@Override
				public void run() {
					Cocos2dxGLSurfaceView.this.mRenderer.handleActionCancel(
							iArr, fArr, fArr2);
				}

			});
			break;
		}
		return true;
	}

	public void setTextField(Cocos2dxEditText cocos2dxEditText) {
		this.mTextField = cocos2dxEditText;
		if (this.mTextField != null && textInputWraper != null) {
			this.mTextField
					.setOnEditorActionListener((TextView.OnEditorActionListener) textInputWraper);
			this.mTextField.setMainView(this);
			this.requestFocus();
		}
	}

}
