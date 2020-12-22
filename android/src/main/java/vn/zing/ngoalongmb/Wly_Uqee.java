package vn.zing.ngoalongmb;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.UUID;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StatFs;
import android.util.Log;
import android.widget.Toast;

import com.oh.wly.R;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

@SuppressLint(value = { "NewApi" })
public class Wly_Uqee extends Cocos2dxActivity {
	private static final String CAMPAIGN_SOURCE_PARAM = "utm_source";
	private static final String GA_PROPERTY_ID = "UA-43961878-1";
	private static final String SCREEN_LABEL = "Home Screen";
	private static String TAG;
	private static String accountInfo;
	public static Wly_Uqee activity;
	private static String chanel_platform;
	private static String chanel_platform_loginstr;
	private static String chanel_platform_modify_pwd;
	private static String chanel_platform_pay;
	private static String chanel_platform_pay_method;
	private static String chanel_platform_regstr;
	public static ClipboardManager cmm;
	public static ConnectivityManager conMan;
	public static String exOrderNo;
	public static GameHandler gameHandler;
	private static String isSessionValid;
	public static final boolean isUsedSdcard = false;
	private static String isWalled;
	private static String keyContent;
	private static String keyMyPackage;
	private static String packageHotro;
	public static Rms rms;
	public static String sdCardRoot;
	private static String serverId;
	static String serverIdString;
	private static String serverIp;
	private static String serverName;
	private static String serverPort;
	private static String server_flag;
	private static String uqeeId;
	public static ProgressDialog uqeeProgress;
	private static String uqeePw;
	static String usernameString;
	public static String versionName;
	private static String wlyGoldenCoins;
	private static String wlyLoginName;
	private static String wlyPayVnd;
	private static String wlyRoleName;
	private static String wlyServerId;
	public static String zingme_displayname;
	public static long zingme_uid;
	public Cocos2dxGLSurfaceView mGLView;
	public Intent msgPushIntent = null;

	static {
		packageHotro = "vng.cs.td.hotro";
		keyContent = "NLMB";
		keyMyPackage = "vn.zalo.ngoalongmb";
		exOrderNo = "ex1101";
		TAG = "Wly_Uqee";

		System.loadLibrary("cocosdenshion");
		System.loadLibrary("lua");
		System.loadLibrary("game");
	}

	/*
	 * Enabled aggressive block sorting
	 */
	public static void checkAddTime(long l2) {
		System.out.println("\ufffd?\ufffd\ufffd\u672c\u5730APK\u6587\u4ef6");
		Object object = rms.get("addTime");
		boolean bl = false;
		if (object == null || object.equals("")) {
			bl = true;
			rms.put("addTime", String.valueOf(l2));
		} else if ((long) Integer.parseInt((String) object) < l2) {
			bl = true;
			rms.put("addTime", String.valueOf(l2));
		}
		if (bl) {
			if (sdCardRoot != null) {
				File file = new File(String.valueOf(sdCardRoot)
						+ "apk/wly_uqee.apk");
				if (file.exists()) {
					if (file.delete()) {
						System.out.println("Delete Old Apk Success");
					} else {
						System.out.println("Delete Old Apk Failed");
					}
				}
			} else {
				System.out.println("sdCardRoot==NULL");
			}
		}
		System.out
				.println("\ufffd?\ufffd\ufffd\u672c\u5730APK\u6587\u4ef6\u7ed3\u675f");
	}

	public static void gameBegin(String string2) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if (Wly_Uqee.uqeeProgress != null) {
					Wly_Uqee.uqeeProgress.dismiss();
					Wly_Uqee.uqeeProgress = null;
				}
			}
		});
	}

	public static String getAccountInfo() {
		accountInfo = rms.get("accountInfo");
		System.out.println("Crruent account info: " + accountInfo);
		if (accountInfo == null || accountInfo.equals("")) {
			System.out.println("\u751f\u6210UUID");
			accountInfo = UUID.randomUUID().toString();
			rms.put("accountInfo", accountInfo);
		}
		return accountInfo;
	}

	public static String getBackSound() {
		String string2 = rms.get("backSoundVoice");
		if (string2 != null) {
			String string3 = string2;
			if (!string2.equals(""))
				return string3;
		}
		rms.put("backSoundVoice", "0.5");
		return "0.5";
	}

	public static String getChanelPlatForm() {
		if (chanel_platform == null) {
			return "";
		}
		return chanel_platform;
	}

	public static String getChanelPlatFormLoginStr() {
		if (chanel_platform_loginstr == null) {
			return "";
		}
		return chanel_platform_loginstr;
	}

	public static String getChanelPlatFormPay() {
		if (chanel_platform_pay == null) {
			return "";
		}
		return chanel_platform_pay;
	}

	public static String getChanelPlatFormPwdUi() {
		if (chanel_platform_modify_pwd == null) {
			return "";
		}
		return chanel_platform_modify_pwd;
	}

	public static String getChanelPlatFormRegStr() {
		if (chanel_platform_regstr == null) {
			return "";
		}
		return chanel_platform_regstr;
	}

	public static String getClipInfo() {
		if (Integer.parseInt(Build.VERSION.SDK) < 11) {
			android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) activity
					.getSystemService("clipboard");
			if (clipboardManager.getText() == null
					|| clipboardManager.getText().equals("")) {
				return "";
			}
			return clipboardManager.getText().toString();
		}
		if (cmm != null) {
			if (cmm.getText() == null || cmm.getText().equals("")) {
				return "";
			}
			return cmm.getText().toString();
		}
		return "";
	}

	/*
	 * Enabled force condition propagation Lifted jumps to return sites
	 */
	public static String getEffectSound() {
		String string2 = rms.get("effectSoundVoice");
		if (string2 != null) {
			String string3 = string2;
			if (!string2.equals(""))
				return string3;
		}
		rms.put("effectSoundVoice", "0.5");
		return "0.5";
	}

	public static String getGameVersionName() {
		System.out.println("versionName=" + versionName);
		if (versionName == null) {
			return "";
		}
		return versionName;
	}

	public static String getIsSessionValid() {
		return "true";
	}

	public static String getIsWalled() {
		isWalled = rms.get("isWalled");
		if (isWalled == null) {
			return "";
		}
		return isWalled;
	}

	public static String getNewAccountInfo() {
		System.out.println("\u751f\u6210UUID");
		accountInfo = UUID.randomUUID().toString();
		rms.put("accountInfo", accountInfo);
		return accountInfo;
	}

	public static String getSdCardPath() {
		if (sdCardRoot == null) {
			return "";
		}
		return sdCardRoot;
	}

	public static String getServerFlag() {
		if (server_flag == null) {
			return "";
		}
		return server_flag;
	}

	public static String getServerId() {
		serverId = rms.get("serverId");
		if (serverId == null) {
			return "";
		}
		return serverId;
	}

	public static String getServerIp() {
		serverIp = rms.get("serverIp");
		if (serverIp == null) {
			return "";
		}
		return serverIp;
	}

	public static String getServerName() {
		serverName = rms.get("servername");
		if (serverName == null) {
			return "";
		}
		return serverName;
	}

	public static String getServerPort() {
		serverPort = rms.get("serverPort");
		System.out.print("getServerPort=" + serverPort);
		if (serverPort == null) {
			return "";
		}
		return serverPort;
	}

	public static String getSubStr(String string2, int n2, int n3) {
		String string3 = string2;
		if (string2 == null)
			return string3;
		string3 = string2;
		if (n3 <= 0)
			return string3;
		string3 = string2;
		if (n2 < 0)
			return string3;
		if (n2 > string2.length()) {
			return "";
		}
		if (string2.length() >= n2 + n3)
			return string2.substring(n2, n2 + n3);
		return string2.substring(n2);
	}

	public static String getUqeeId() {
		uqeeId = rms.get("uqeeId");
		if (uqeeId == null) {
			return "";
		}
		return uqeeId;
	}

	public static String getUqeePw() {
		uqeePw = rms.get("uqeePw");
		if (uqeePw == null) {
			return "";
		}
		return uqeePw;
	}

	/*
	 * Enabled aggressive block sorting
	 */
	public static int getValuePreferences(String string2) {
		if (usernameString == null || serverIdString == null) {
			if (string2.equals("guideIding"))
				return 1;
			return 0;
		}
		String string3 = rms.get(String.valueOf(usernameString)
				+ serverIdString + string2);
		if (string3 != null) {
			return Integer.parseInt(string3);
		}
		if (usernameString == null && string2.equals("guideIding")) {
			return 1;
		}
		rms.put(String.valueOf(usernameString) + serverIdString + string2, "0");
		return 0;
	}

	public static String getWlyGoldenCoins() {
		return wlyGoldenCoins;
	}

	public static String getWlyLoginName() {
		return wlyLoginName;
	}

	public static String getWlyPayVnd() {
		return wlyPayVnd;
	}

	public static String getWlyRoleName() {
		return wlyRoleName;
	}

	public static String getWlyServerId() {
		return wlyServerId;
	}

	public static String get_surpport_tourist() {
		return activity.getString(2131165348);
	}

	public static void goToWifi() {
		activity.startActivity(new Intent("android.settings.WIFI_SETTINGS"));
	}

	public static void initPreferences(String string2, String string3) {
		usernameString = string2;
		serverIdString = string3;
		if (rms.get(String.valueOf(usernameString) + serverIdString
				+ "guideIding") == null) {
			rms.put(String.valueOf(usernameString) + serverIdString
					+ "guideIding", "1");
		}
	}

	public static void installGameAPK() {
		if (sdCardRoot != null) {
			Uri uri = Uri.fromFile((File) new File(String.valueOf(sdCardRoot)
					+ "apk/wly_uqee.apk"));
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.setDataAndType(uri,
					"application/vnd.android.package-archive");
			activity.startActivity(intent);
		}
	}

	public static boolean isConnectWifi() {
		return true;
	}

	public static boolean isInWifiStaus() {
		if (conMan.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			return true;
		}
		return false;
	}

	/*
	 * Enabled force condition propagation Lifted jumps to return sites
	 */
	private boolean isNetworkAvailable() {
		ConnectivityManager connectivity = (ConnectivityManager) this
				.getApplicationContext().getSystemService("connectivity");
		if (connectivity == null)
			return false;
		NetworkInfo[] arrnetworkInfo = connectivity.getAllNetworkInfo();
		int n2 = 0;
		while (n2 < arrnetworkInfo.length) {
			if (arrnetworkInfo[n2].getState() == NetworkInfo.State.CONNECTED) {
				return true;
			}
			++n2;
		}
		return false;
	}

	public static boolean isPassWordRight(String string2) {
		return string2.matches("[a-zA-Z0-9_.;,?:~!@#$%^&*()-=+`]*");
	}

	public static boolean isUserNameRight(String string2) {
		if (chanel_platform != null && chanel_platform.equals("lewan")) {
			return true;
		}
		return string2.matches("[a-zA-Z0-9_.;?:~!@#$%^&*()-=+`]*");
	}

	private static void launchComponent(String string2, String string3,
			String string4) {
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		intent.setComponent(new ComponentName(string2, string3));
		intent.setFlags(268435456);
		intent.putExtra(keyContent, string4);
		intent.putExtra(keyMyPackage, activity.getPackageName());
		activity.startActivity(intent);
	}

	public static void removePreferences(String string2) {
	}

	/*
	 * Enabled aggressive block sorting
	 */
	public static void saveClipInfo(String string2) {
		int n2 = string2.lastIndexOf(":");
		if (Integer.parseInt(Build.VERSION.SDK) < 11) {
			android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) activity
					.getSystemService("clipboard");
			if (n2 == -1) {
				clipboardManager.setText((CharSequence) string2);
				return;
			}
			clipboardManager.setText((CharSequence) string2.substring(n2 + 1));
			return;
		}
		if (cmm == null)
			return;
		{
			if (n2 != -1) {
				cmm.setText((CharSequence) string2.substring(n2 + 1));
				return;
			}
		}
		cmm.setText((CharSequence) string2);
	}

	public static void setAccountInfo(String string2) {
		accountInfo = string2;
		rms.put("accountInfo", accountInfo);
	}

	public static void setBackSound(String string2) {
		rms.put("backSoundVoice", string2);
	}

	public static void setEditNum(int n2) {
		Message message = new Message();
		message.arg1 = 3;
		message.obj = n2;
		gameHandler.sendMessage(message);
	}

	public static void setEffectSound(String string2) {
		rms.put("effectSoundVoice", string2);
	}

	public static void setIsNumOnly(boolean bl) {
		Message message = new Message();
		message.arg1 = 4;
		message.obj = bl;
		gameHandler.sendMessage(message);
	}

	public static void setIsWalled(String string2) {
		isWalled = string2;
		rms.put("isWalled", isWalled);
	}

	public static void setServerId(String string2) {
		System.out.println("Set serverid=" + string2);
		serverId = string2;
		rms.put("serverId", serverId);
	}

	public static void setServerIp(String string2) {
		serverIp = string2;
		rms.put("serverIp", serverIp);
	}

	public static void setServerName(String string2) {
		serverName = string2;
		rms.put("servername", serverName);
	}

	public static void setServerPort(String string2) {
		serverPort = string2;
		rms.put("serverPort", serverPort);
	}

	public static void setUqeeId(String string2) {
		uqeeId = string2;
		rms.put("uqeeId", uqeeId);
	}

	public static void setUqeePw(String string2) {
		uqeePw = string2;
		rms.put("uqeePw", uqeePw);
	}

	public static void setValuePreferences(String string2, int n2) {
		rms.put(String.valueOf(usernameString) + serverIdString + string2,
				String.valueOf(n2));
	}

	public static void setWlyGoldenCoins(String string2) {
		wlyGoldenCoins = string2;
	}

	public static void setWlyLoginName(String string2) {
		wlyLoginName = string2;
	}

	public static void setWlyPayVnd(String string2) {
		wlyPayVnd = string2;
	}

	public static void setWlyRoleName(String string2) {
		wlyRoleName = string2;
	}

	public static void setWlyServerId(String string2) {
		wlyServerId = string2;
	}

	private static void showInMarket(String string2) {
		Intent intent = new Intent("android.intent.action.VIEW",
				Uri.parse((String) ("market://details?id=" + string2)));
		intent.setFlags(268435456);
		activity.startActivity(intent);
	}

	public static void startApplication() {
		try {
			Intent object = new Intent("android.intent.action.MAIN");
			object.addCategory("android.intent.category.LAUNCHER");
			object.addFlags(65536);

			Iterator<ResolveInfo> i = activity.getPackageManager()
					.queryIntentActivities(object, 0).iterator();
			ResolveInfo resolveInfo = null;
			while (true) {
				if (!i.hasNext()) {
					Wly_Uqee.showInMarket(packageHotro);
					return;
				}
				resolveInfo = i.next();
				if (resolveInfo.activityInfo.packageName
						.equalsIgnoreCase(packageHotro))
					break;
			}
			/*
			 * do { if (!object.hasNext()) {
			 * Wly_Uqee.showInMarket(packageHotro); return; } ResolveInfo
			 * resolveInfo = (ResolveInfo)object.next(); } while
			 * (!(resolveInfo.activityInfo
			 * .packageName.equalsIgnoreCase(packageHotro));
			 */
			Wly_Uqee.launchComponent(resolveInfo.activityInfo.packageName,
					resolveInfo.activityInfo.name, "NLMB");
			return;
		} catch (Exception var0_1) {
			Wly_Uqee.showInMarket(packageHotro);
			return;
		}
	}

	public static void toPay(String string2) {
		System.out.println("toPay:" + string2);
		Message message = new Message();
		message.arg1 = 2;
		message.obj = string2;
		gameHandler.sendMessage(message);
	}

	public static void toPayYuenan(String string2) {
		System.out.println("wjf check value getUqeeId=" + Wly_Uqee.getUqeeId());
		System.out.println("wjf check value getAccountInfo="
				+ Wly_Uqee.getAccountInfo());
		System.out.println("wjf check value getServerPort="
				+ Wly_Uqee.getServerPort());
		System.out.println("wjf check value getServerName="
				+ Wly_Uqee.getServerName());
		System.out.println("wjf check value getServerId="
				+ Wly_Uqee.getServerId());
		System.out.println("wjf check value getAccname="
				+ GameTools.getAccname());
		System.out.println("wjf check value getWlyLoginName()="
				+ Wly_Uqee.getWlyLoginName());
		System.out.println("wjf check value getWlyRoleName()="
				+ Wly_Uqee.getWlyRoleName());
		System.out.println("wjf check value getWlyServerId()="
				+ Wly_Uqee.getWlyServerId());
		System.out.println("wjf check value getWlyGoldenCoins()="
				+ Wly_Uqee.getWlyGoldenCoins());
		System.out.println("wjf check value getWlyPayVnd()="
				+ Wly_Uqee.getWlyPayVnd());

	}

	public static void useOtherPaySDK(String string2, String string3,
			String string4) {
		System.out.println("chanel_platform=" + chanel_platform);
		System.out.println("chanel_platform_pay_method="
				+ chanel_platform_pay_method);
		System.out.println("username=" + string2 + ",serverid=" + string3
				+ ",loginname=" + string4);
		try {
			Wly_Uqee.class.getMethod(chanel_platform_pay_method, String.class,
					String.class, String.class).invoke(Wly_Uqee.class, string2,
					string3, string4);
			return;
		} catch (NoSuchMethodException var0_1) {
			var0_1.printStackTrace();
			return;
		} catch (IllegalArgumentException var0_2) {
			var0_2.printStackTrace();
			return;
		} catch (IllegalAccessException var0_3) {
			var0_3.printStackTrace();
			return;
		} catch (InvocationTargetException var0_4) {
			var0_4.printStackTrace();
			return;
		}
	}

	public static void vnLogout() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Log.d((String) TAG, (String) "vnLogout");
				// ZaloOAuth.Instance.unauthenticate();
			}
		});
	}

	public static void vnRequestZingMeLogin() {
		Log.d((String) TAG, (String) ("uid=" + zingme_uid));
		System.out.println("wjf:vnRequestZingMeLogin");
		Wly_Uqee.setAccountInfo("" + zingme_uid);
		WlyUqeeJniTool.vnLoginGame();
	}

	protected void onActivityResult(int n2, int n3, Intent intent) {
		super.onActivityResult(n2, n3, intent);
	}
    
    boolean storageGranted = false;
    private boolean checkPermissions()
	{
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {

			// Permission is not granted
			// Should we show an explanation?
			/*if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.

			} else {
				// No explanation needed; request the permission
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						1);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}*/
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					1);
			Log.d("DGame", "Permission: write ext storage not granted");
		} else {
			// Permission has already been granted
			storageGranted = true;
			Log.d("DGame", "Permission: write ext storage granted");
		}

		if (!storageGranted)
        {
			Toast.makeText(this, "Cần cho phép ứng dụng các quyền để tiếp tục!", Toast.LENGTH_LONG);
            return false;
        }
        return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 1: {//write storage code
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission was granted, yay! Do the
					// contacts-related task you need to do.
					storageGranted = true;
				} else {
					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

		}
		if (!storageGranted)
			Toast.makeText(this, "Cần cho phép ứng dụng các quyền để tiếp tục!", Toast.LENGTH_LONG);
	}

	@Override
	protected void onCreate(Bundle object) {
		super.onCreate((Bundle) object);
        if (!this.checkPermissions())
        {
            this.finish();
            return;
        }
        
        //get intent parameter
		Intent intent = getIntent();
		int uid = intent.getIntExtra("uid", 0);
		String token = intent.getStringExtra("token");
        
        //continue..
		System.out.println("wjf:Wly_UqeeActivity onCreate");
		activity = this;
		// MobclickAgent.onError((Context)this);
		super.setPackageName(this.getApplication().getPackageName());
		activity = this;

		if (!this.isNetworkAvailable()) {
			this.finish();
			Toast.makeText((Context) this,
					(CharSequence) "Kh\u00f4ng c\u00f3 m\u1ea1ng", (int) 1)
					.show();
		}
		conMan = (ConnectivityManager) this.getSystemService("connectivity");
		if (!Environment.getExternalStorageState().equals("mounted")) {
			this.finish();
			Toast.makeText(
					(Context) this,
					(CharSequence) "\u6ca1\u6709\u53d1\u73b0SD\u5361\uff0c\u65e0\u6cd5\u542f\u52a8\u5e94\u7528",
					(int) 1).show();
			return;
		}
		File dir = Environment.getExternalStorageDirectory();
		sdCardRoot = dir.getAbsolutePath();
		System.out.println("getExternalStorageDirectory(): " + sdCardRoot);
		gameHandler = new GameHandler();
		if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
			cmm = (ClipboardManager) activity.getSystemService("clipboard");
		}
		Wly_Uqee.rms = new Rms(this, "wly");
		Wly_Uqee.rms.put("serverId", "");// fuckfuck
		if ((Wly_Uqee.chanel_platform = rms.get("chanel_platform")) == null) {
			chanel_platform = getString(R.string.chanel_platform);
			rms.put("chanel_platform", chanel_platform);
			System.out
					.println("\u9996\u6b21\u5b89\u88c5\u5e94\u7528\uff0c\u6e20\u9053\u5e73\u53f0\u662f"
							+ chanel_platform);
		} else {
			System.out
					.println("\u66f4\u65b0\u5e94\u7528\uff0c\u6e20\u9053\u5e73\u53f0\u662f"
							+ chanel_platform);
		}
		if ((Wly_Uqee.chanel_platform_modify_pwd = rms
				.get("chanel_platform_modify_pwd")) == null) {
			chanel_platform_modify_pwd = getString(R.string.chanel_platform_modify_pwd);
			rms.put("chanel_platform_modify_pwd", chanel_platform_modify_pwd);
			System.out
					.println("\u9996\u6b21\u5b89\u88c5\u5e94\u7528\uff0c\u6e20\u9053\u4fee\u6539\u5bc6\u7801URI\u662f\uff1a"
							+ chanel_platform_modify_pwd);
		} else {
			System.out
					.println("\u66f4\u65b0\u5e94\u7528\uff0c\u6e20\u9053\u4fee\u6539\u5bc6\u7801URI\u662f\uff1a"
							+ chanel_platform_modify_pwd);
		}
		if ((Wly_Uqee.chanel_platform_pay = "uqee") == null
				|| !chanel_platform_pay.equals("null")) {
			chanel_platform_pay = rms.get("chanel_platform_pay");
			if (chanel_platform_pay == null) {
				chanel_platform_pay = getString(R.string.chanel_platform_pay);
				if (chanel_platform_pay.equals("default")) {
					chanel_platform_pay = "uqee";
				}
				rms.put("chanel_platform_pay", chanel_platform_pay);
				System.out
						.println("\u9996\u6b21\u5b89\u88c5\u5e94\u7528\uff0c\u6e20\u9053\u652f\u4ed8\u65b9\u5f0f\u96c6\u5408\uff1a"
								+ chanel_platform_pay);
			} else {
				System.out
						.println("\u66f4\u65b0\u5e94\u7528\uff0c\u6e20\u9053\u652f\u4ed8\u65b9\u5f0f\u96c6\u5408\uff1a"
								+ chanel_platform_pay);
			}
		}
		if ((Wly_Uqee.chanel_platform_pay_method = rms
				.get("chanel_platform_pay_method")) == null) {
			chanel_platform_pay_method = getString(R.string.chanel_platform_pay_method);
			rms.put("chanel_platform_pay_method", chanel_platform_pay_method);
			System.out
					.println("\u9996\u6b21\u5b89\u88c5\u5e94\u7528\uff0c\u6e20\u9053\u652f\u4ed8\u5165\u53e3\u51fd\u6570\uff1a"
							+ chanel_platform_pay_method);
		} else {
			System.out
					.println("\u66f4\u65b0\u5e94\u7528\uff0c\u6e20\u9053\u652f\u4ed8\u5165\u53e3\u51fd\u6570\uff1a"
							+ chanel_platform_pay_method);
		}
		if ((Wly_Uqee.chanel_platform_regstr = rms
				.get("chanel_platform_regstr")) == null) {
			chanel_platform_regstr = getString(R.string.chanel_platform_regstr);
			rms.put("chanel_platform_regstr", chanel_platform_regstr);
			System.out
					.println("\u9996\u6b21\u5b89\u88c5\u5e94\u7528\uff0c\u6ce8\u518c\u8bf4\u7684\u8bdd"
							+ chanel_platform_regstr);
		} else {
			System.out
					.println("\u66f4\u65b0\u5e94\u7528\uff0c\u6ce8\u518c\u8bf4\u7684\u8bdd"
							+ chanel_platform_regstr);
		}
		if ((Wly_Uqee.chanel_platform_loginstr = rms
				.get("chanel_platform_loginstr")) == null) {
			chanel_platform_loginstr = getString(R.string.chanel_platform_loginstr);
			rms.put("chanel_platform_loginstr", chanel_platform_loginstr);
			System.out
					.println("\u9996\u6b21\u5b89\u88c5\u5e94\u7528\uff0c\u767b\u5f55\u8bf4\u7684\u8bdd"
							+ chanel_platform_loginstr);
		} else {
			System.out
					.println("\u66f4\u65b0\u5e94\u7528\uff0c\u767b\u5f55\u8bf4\u7684\u8bdd"
							+ chanel_platform_loginstr);
		}
		server_flag = getString(R.string.server_flag);
		System.out.println("\u5f53\u524d\u670d\u52a1\u5668\u6807\u793a\uff1a"
				+ server_flag);
		// AppsFlyerLib.getInstance().setCustomerUserId("" +
		// ZaloOAuth.Instance.getZaloId());

		sdCardRoot = String.valueOf(sdCardRoot)
				+ getString(R.string.sdcard_resource_root);

		//new ResourceMoveToSDCardThread().start();

		// test another account: set another account info, reinstall => fail
		// this.setAccountInfo("1111");

		// login form => success
		// GameTools.setAccname("1111");
		// server list form
		// setAccountInfo("8e647c47-a42a-40fe-927e-dbffa35ae0ba");

		// vnRequestZingMeLogin
		// zingme_uid = 2;
		// zingme_displayname = "tester2";

		// Fake Zalo
		zingme_uid = uid;//ZaloOAuthen.ZaloID;
		zingme_displayname = "xxx";//ZaloOAuthen.ZaloDisplayName;
		// this might helps us to build list server
		// this.setServerId(ServerSelector.ServerId);

		// 2020/12/03: tao react native module
		// khong hieu sao ko can goi vnRequestZingMeLogin van co the tu dong vao
		// bat new ResourceMoveToSDCardThread().start(); vao game bi loi
		enterGame();
	}

	private void enterGame() {
		Message message = new Message();
		message.arg1 = 1;
		gameHandler.sendMessage(message);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.mGLView != null) {
			this.mGLView.onPause();
		}
		// MobclickAgent.onPause((Context)this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (this.mGLView != null) {
			this.mGLView.onResume();
		}
		// MobclickAgent.onResume((Context)this);
	}

	public void onStart() {
		super.onStart();
		this.getIntent().getData();
	}

	public void onStop() {
		super.onStop();
	}

}
