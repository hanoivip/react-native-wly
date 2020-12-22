package us.vn1.nl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.cocos2dx.lib.Cocos2dxActivity;

import vn.zing.ngoalongmb.Wly_Uqee;
import vn.zing.ngoalongmb.ZaloOAuthen;

public class Wolong extends Cocos2dxActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        Intent intent = new Intent();
        intent.setClass(this.getApplicationContext(), Wly_Uqee.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);

    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }
}
