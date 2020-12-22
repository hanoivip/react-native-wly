package vn.zing.ngoalongmb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oh.wly.R;

import org.cocos2dx.lib.Cocos2dxEditText;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

import vn.zing.ngoalongmb.Wly_Uqee;

public class GameHandler extends Handler {
	public static final int CHANGE_EDIT_INPUT_NUM = 3;
	public static final int CHANGE_EDIT_NUM_ONLY = 4;
	public static final int ENTER_GAME = 1;
	public static final int GO_TO_WEB = 2;
	private AlertDialog alertDialog;
	private Dialog dialog;
	private ProgressDialog progressBar;
	private WebView webView;

	public void handleMessage(Message object) {
		super.handleMessage((Message) object);
		switch (object.arg1) {
		case ENTER_GAME: {
			Wly_Uqee.activity.setContentView(R.layout.game_demo);
			Wly_Uqee.activity.mGLView = (Cocos2dxGLSurfaceView) Wly_Uqee.activity
					.findViewById(R.id.game_gl_surfaceview);
			Wly_Uqee.activity.mGLView
					.setTextField((Cocos2dxEditText) Wly_Uqee.activity
							.findViewById(R.id.textField));
			return;
		}
		case GO_TO_WEB: {
			this.webView = new WebView((Context) Wly_Uqee.activity);
			this.webView.getSettings().setJavaScriptEnabled(true);
			this.webView.getSettings().setBuiltInZoomControls(true);
			this.webView.requestFocus();
			Object data = (String) object.obj;
			this.webView.loadUrl((String) data);
			this.webView.setWebViewClient(new WebViewClient() {

				public void onPageFinished(WebView webView, String string2) {
					if (GameHandler.this.progressBar.isShowing()) {
						GameHandler.this.progressBar.dismiss();
					}
					if (!GameHandler.this.dialog.isShowing()) {
						GameHandler.this.dialog.show();
					}
				}

				public void onReceivedError(WebView webView, int n2,
						String string2, String string3) {
					Toast.makeText(
							(Context) Wly_Uqee.activity,
							(CharSequence) "\u7f51\u9875\u52a0\u8f7d\u51fa\u9519\uff01",
							(int) 1);
					GameHandler.this.alertDialog
							.setTitle((CharSequence) "ERROR");
					GameHandler.this.alertDialog
							.setMessage((CharSequence) string2);
					GameHandler.this.alertDialog.setButton((CharSequence) "OK",
							new DialogInterface.OnClickListener() {

								public void onClick(
										DialogInterface dialogInterface, int n2) {
								}
							});
					GameHandler.this.dialog.dismiss();
					GameHandler.this.alertDialog.show();
				}

				public boolean shouldOverrideUrlLoading(WebView webView,
						String string2) {
					webView.loadUrl(string2);
					return true;
				}

			});
			this.progressBar = ProgressDialog
					.show((Context) Wly_Uqee.activity,
							(CharSequence) null,
							(CharSequence) "Xin ch\u1edd trong gi\u00e2y l\u00e1t\u2026");
			this.dialog = new Dialog((Context) Wly_Uqee.activity, 2131099660);
			Display display = Wly_Uqee.activity.getWindowManager()
					.getDefaultDisplay();
			this.dialog.addContentView((View) this.webView,
					(ViewGroup.LayoutParams) new LinearLayout.LayoutParams(
							display.getWidth() - 40, display.getHeight() - 20));
			this.alertDialog = new AlertDialog.Builder(
					(Context) Wly_Uqee.activity).create();
			return;
		}
		case CHANGE_EDIT_INPUT_NUM: {
			int n2 = (Integer) object.obj;
			((Cocos2dxEditText) Wly_Uqee.activity.mGLView.getTextField())
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							n2) });
			return;
		}
		case CHANGE_EDIT_NUM_ONLY: {
			if (((Boolean) object.obj).booleanValue()) {
				((Cocos2dxEditText) Wly_Uqee.activity.mGLView.getTextField())
						.setInputType(2);
				return;
			}
			((Cocos2dxEditText) Wly_Uqee.activity.mGLView.getTextField())
					.setInputType(1);
		}
		}

	}

}
