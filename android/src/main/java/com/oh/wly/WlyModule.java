package com.oh.wly;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.content.Intent;
import vn.zing.ngoalongmb.Wly_Uqee;

public class WlyModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public WlyModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "Wly";
    }

    @ReactMethod
	public void enterGame(String token, int uid, Callback callback) {
		ReactApplicationContext context = getReactApplicationContext();
		Intent intent = new Intent(context, Wly_Uqee.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("token", token);
		intent.putExtra("uid", uid);
		context.startActivity(intent);
	}
}
