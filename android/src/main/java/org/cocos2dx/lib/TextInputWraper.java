package org.cocos2dx.lib;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.util.regex.*;

class TextInputWraper implements TextWatcher, OnEditorActionListener {
	private static final Boolean debug;
	private Cocos2dxGLSurfaceView mMainView;
	private String mOriginText;
	private String mText;

	static {
		debug = Boolean.valueOf(false);
	}

	public TextInputWraper(Cocos2dxGLSurfaceView cocos2dxGLSurfaceView) {
		this.mMainView = cocos2dxGLSurfaceView;
		
	}

	private void LogD(String str) {
		if (debug.booleanValue()) {
			Log.d("TextInputFilter", str);
		}
	}

	private boolean containsEmoji(String str) {
		int length = str.length();
		for (int i = 0; i < length; i++) {
			if (isEmojiCharacter(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private String filterEmoji(String str) {
		if (!containsEmoji(str)) {
			return str;
		}
		int length = str.length();
		StringBuilder stringBuilder = null;
		for (int i = 0; i < length; i++) {
			char charAt = str.charAt(i);
			if (isEmojiCharacter(charAt)) {
				if (stringBuilder == null) {
					stringBuilder = new StringBuilder(str.length());
				}
				stringBuilder.append(charAt);
			}
		}
		return (stringBuilder == null || stringBuilder.length() == length) ? str
				: stringBuilder.toString();
	}

	private boolean isEmojiCharacter(char c) {
		return c == '\u0000'
				|| c == '\t'
				|| c == '\n'
				|| c == '\r'
				|| ((c >= ' ' && c <= '\ud7ff') || ((c >= '\ue000' && c <= '\ufffd') || (c >= '\u0000' && c <= '\uffff')));
	}

	private Boolean isFullScreenEdit() {
		return Boolean.valueOf(((InputMethodManager) this.mMainView
				.getTextField().getContext().getSystemService("input_method"))
				.isFullscreenMode());
	}

	public void afterTextChanged(Editable editable) {
		if (!isFullScreenEdit().booleanValue()) {
			LogD("afterTextChanged: " + editable);
			int length = editable.length() - this.mText.length();
			if (length > 0) {
				String charSequence = editable.subSequence(this.mText.length(),
						editable.length()).toString();
				this.mMainView.insertText(charSequence);
				LogD("insertText(" + charSequence + ")");
			} else {
				while (length < 0) {
					this.mMainView.deleteBackward();
					LogD("deleteBackward");
					length++;
				}
			}
			this.mText = editable.toString();

			System.out.println(this.mText);
		}
	}

	public void beforeTextChanged(CharSequence charSequence, int i, int i2,
			int i3) {
		LogD("beforeTextChanged(" + charSequence + ")start: " + i + ",count: "
				+ i2 + ",after: " + i3);
		this.mText = charSequence.toString();
	}

	public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
		if (this.mMainView.getTextField() == textView
				&& isFullScreenEdit().booleanValue()) {
			for (int length = this.mOriginText.length(); length > 0; length--) {
				this.mMainView.deleteBackward();
				LogD("deleteBackward");
			}
			String charSequence = textView.getText().toString();
			if (charSequence.compareTo("") == 0) {
				charSequence = "\n";
			}
			if ('\n' != charSequence.charAt(charSequence.length() - 1)) {
				charSequence = new StringBuilder(String.valueOf(charSequence))
						.append('\n').toString();
			}
			charSequence = filterEmoji(charSequence);
			 //Pattern pattern = Pattern.compile("[0-9a-zA-Z]{1,6}");
	        // See if this String matches.
	        //Matcher m = pattern.matcher(charSequence);
	        if (charSequence.matches("^\\p{ASCII}*$")) {
	        	System.out.println("resultText = " + charSequence);
				this.mMainView.insertText(charSequence);
				LogD("insertText(" + charSequence + ")");
	        }else{
	        	System.out.println("resultText = false");
	        }
			
		}
		return false;
	}

	public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
	}

	public void setOriginText(String str) {
		System.out.println(str);
		this.mOriginText = str;
		
	}
}
