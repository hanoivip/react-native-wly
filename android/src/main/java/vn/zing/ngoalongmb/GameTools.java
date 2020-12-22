package vn.zing.ngoalongmb;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
//import vn.zing.ngoalongmb.MD5FileUtil;
//import vn.zing.ngoalongmb.MyBase64;
import vn.zing.ngoalongmb.Rms;
import vn.zing.ngoalongmb.Wly_Uqee;

public class GameTools {
	static String filenameTemp = null;
	private static String tempAccName;
	private static ArrayList<String> unPassedWords = new ArrayList();

	public static void CopyAssets(String dir) {
		Exception e;
		AssetManager assetManager = Wly_Uqee.activity.getAssets();
		System.out.println("copy dir=" + dir);
		String[] files = null;
		try {
			files = assetManager.list(dir);
		} catch (IOException e2) {
			Log.e("tag", e2.getMessage());
			e2.printStackTrace();
		}
		for (String filename : files) {
			String filename2 = new StringBuilder(String.valueOf(dir))
					.append("/").append(filename).toString();
			System.out.println("copy filename=" + filename2);
			try {
				InputStream in = assetManager.open(filename);
				OutputStream out = new FileOutputStream(Wly_Uqee.sdCardRoot
						+ filename2);
				try {
					copyFile(in, out);
					in.close();
					out.flush();
					out.close();
				} catch (Exception e3) {
					e = e3;
					OutputStream outputStream = out;
					Log.e("tag", e.getMessage());
					e.printStackTrace();
				}
			} catch (Exception e4) {
				e = e4;
				Log.e("tag", e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public static void CopySingleFile(String filename) {
		Exception e;
		AssetManager assetManager = Wly_Uqee.activity.getAssets();
		System.out.println("copy filename=" + filename);
		try {
			InputStream in = assetManager.open(filename);
			OutputStream out = new FileOutputStream(Wly_Uqee.sdCardRoot
					+ filename);
			try {
				copyFile(in, out);
				in.close();
				out.flush();
				out.close();
			} catch (Exception e2) {
				e = e2;
				OutputStream outputStream = out;
				Log.e("tag", e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e3) {
			e = e3;
			Log.e("tag", e.getMessage());
			e.printStackTrace();
		}

	}

	private static void copyFile(InputStream inputStream,
			OutputStream outputStream) throws IOException {
		byte[] arrby = new byte[102400];
		int n2;
		while ((n2 = inputStream.read(arrby)) != -1) {
			outputStream.write(arrby, 0, n2);
		}
		return;
	}

	public static void createDir(String path) {
		File object = new File(String.valueOf(Wly_Uqee.sdCardRoot)
				+ (String) path);
		if (!object.exists()) {
			object.mkdirs();
			System.out.println("Create file: " + object.toString());
		}
	}

	public static void delete(File file) {
		File[] arrfile;
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (!file.isDirectory())
			return;
		{
			arrfile = file.listFiles();
			if (arrfile == null || arrfile.length == 0) {
				if (!file.delete())
					return;
				{
					System.out.println("Delete " + file.toString() + " Suc");
					return;
				}
			}
		}
		int n2 = 0;
		do {
			if (n2 >= arrfile.length) {
				file.delete();
				return;
			}
			GameTools.delete(arrfile[n2]);
			++n2;
		} while (true);
	}

	public static String getAccname() {
		String accId = Wly_Uqee.rms.get("accId");
		if (accId == null) {
			System.out.println("Get account id:" + ZaloOAuthen.ZaloID);
			return String.valueOf(ZaloOAuthen.ZaloID);
		}
		System.out.println("Get account id:" + accId);
		return accId;
		/*
		 * arrc = arrc.toCharArray(); int n2 = 0; while (n2 < arrc.length) {
		 * arrc[n2] = (char)(arrc[n2] - n2); ++n2; } return new
		 * String(MyBase64.decode(new String(arrc)));
		 */
	}

	public static String getAccpwd() {
		String accpw = Wly_Uqee.rms.get("accpwd");
		if (accpw == null) {
			return "";
		}
		return accpw;
		/*
		 * arrc = arrc.toCharArray(); int n2 = 0; while (n2 < arrc.length) {
		 * arrc[n2] = (char)(arrc[n2] - n2); ++n2; } return new
		 * String(MyBase64.decode(new String(arrc)));
		 */
	}

	public static String getDeveceName() {
		new Build();
		String string2 = Build.MODEL;
		System.out.println("model=" + string2);
		String string3 = string2;
		if (string2 == null) {
			string3 = "unKowned Device";
		}
		return string3;
	}

	public static String getFileMd5(String object) {
		return "";
	}

	public static String getNetWork() {
		switch (((TelephonyManager) Wly_Uqee.activity.getSystemService("phone"))
				.getNetworkType()) {
		default: {
			return "unKown";
		}
		case 0: {
			return "unKown";
		}
		case 1: {
			return "gprs";
		}
		case 2: {
			return "edge";
		}
		case 3: {
			return "umts";
		}
		case 4: {
			return "cdma";
		}
		case 5: {
			return "EVDO";
		}
		case 6: {
			return "EVDO";
		}
		case 7: {
			return "1xRTT";
		}
		case 8: {
			return "hsdpa";
		}
		case 9: {
			return "hsupa";
		}
		case 10:
		}
		return "hspa";
	}

	public static String getSystemVersion() {
		switch (Integer.parseInt(Build.VERSION.SDK)) {
		default: {
			return "unKownVersion";
		}
		case 1: {
			return "1.0";
		}
		case 2: {
			return "1.1";
		}
		case 3: {
			return "1.5";
		}
		case 4: {
			return "1.6";
		}
		case 5: {
			return "2.0";
		}
		case 6: {
			return "2.0.1";
		}
		case 7: {
			return "2.1";
		}
		case 8: {
			return "2.2";
		}
		case 9: {
			return "2.3";
		}
		case 10: {
			return "2.3.3";
		}
		case 11: {
			return "3.0";
		}
		case 12: {
			return "3.1";
		}
		case 13: {
			return "3.2";
		}
		case 14: {
			return "4.0";
		}
		case 15: {
			return "4.0.3";
		}
		case 16: {
			return "4.1";
		}
		case 17: {
			return "4.2.2";
		}
		case 18: {
			return "4.3";
		}
		}

	}

	public static String getTempAccToken() {
		String string2;
		String string3 = string2 = Wly_Uqee.rms.get(tempAccName);
		System.out.println("Get tmp acc token:" + string2);
		if (string2 == null) {
			string3 = "";
		}
		return string3;
	}

	public static String getWordsIsIn(String string2) {
		Iterator<String> iterator = unPassedWords.iterator();
		do {
			if (iterator.hasNext())
				continue;
			return "false";
		} while (!string2.contains(iterator.next()));
		return "true";
	}

	public static String getWordsPassed(String string2) {
		Iterator<String> iterator = unPassedWords.iterator();
		while (iterator.hasNext()) {
			String string3 = iterator.next();
			if (!string2.contains(string3))
				continue;
			string2 = string2.replaceAll(string3, "**");
		}
		return string2;
	}

	// debug mode = true
	// vng = true
	// can use to detail message from 1.6.6.6
	public static String isDebugAble() {
		return "true";
	}

	public static String isFileNeedCheck() {
		if (filenameTemp != null) {
			String string2;
			block3: {
				String string3 = Wly_Uqee.rms.get(filenameTemp);
				filenameTemp = null;
				if (string3 != null) {
					string2 = string3;
					if (string3.equals(Wly_Uqee.versionName))
						break block3;
				}
				string2 = "true";
			}
			return string2;
		}
		return "false";
	}

	public static String isFirstIn() {
		return "true";
	}

	public static String isFirstIn1() {
		String string2 = Wly_Uqee.rms.get("firstin");
		if (string2 == null || string2.equals("")) {
			Wly_Uqee.rms.put("firstin", "false");
			System.out.println("firstin = true");
			return "true";
		}
		System.out.println("firstin = false");
		return "false";
	}

	public static String isNeedCheck() {
		String string2;
		String string3 = string2 = Wly_Uqee.rms.get("isneedcheck");
		if (string2 == null) {
			string3 = "true";
		}
		return string3;
	}

	// debug mode = ?
	public static String isSmall() {
		// return "true"; crash: missing image make app crash
		return "false";
	}

	public static void loadUnPassWords() {
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(
							GameTools.class.getResourceAsStream("/words")));
			unPassedWords = new ArrayList();
			do {
				String string2;
				if ((string2 = bufferedReader.readLine()) == null) {
					bufferedReader.close();
					return;
				}
				unPassedWords.add(string2);
			} while (true);
		} catch (FileNotFoundException var0_1) {
			var0_1.printStackTrace();
			return;
		} catch (IOException var0_2) {
			var0_2.printStackTrace();
			return;
		}
	}

	public static void setAccname(String accname) {
		System.out.println("Tools:setAccname=" + accname);
		Wly_Uqee.rms.put("accId", accname);
		/*
		 * object = MyBase64.encode(object.getBytes()).toCharArray(); int n2 =
		 * 0; do { if (n2 >= object.length) { object = new
		 * String((char[])object); Wly_Uqee.rms.put("accId", (String)object);
		 * return; } object[n2] = (char)(object[n2] + n2); ++n2; } while (true);
		 */
	}

	public static void setAccpwd(String passwd) {
		System.out.println("setAccpwd=" + passwd);
		Wly_Uqee.rms.put("accpw", passwd);
		/*
		 * object = MyBase64.encode(object.getBytes()).toCharArray(); int n2 =
		 * 0; do { if (n2 >= object.length) { object = new
		 * String((char[])object); Wly_Uqee.rms.put("accpwd", (String)object);
		 * return; } object[n2] = (char)(object[n2] + n2); ++n2; } while (true);
		 */
	}

	private static void setBoolean(final String string2, final boolean bl) {
		Wly_Uqee.activity.runOnUiThread(new Runnable() {

			/*
			 * Enabled aggressive block sorting
			 */
			@Override
			public void run() {
				ContentResolver contentResolver = Wly_Uqee.activity
						.getContentResolver();
				int n2 = bl ? 1 : 0;
				Settings.Secure.putInt((ContentResolver) contentResolver,
						(String) string2, (int) n2);
			}
		});
	}

	public static void setIsFileNeedCheck(String string2) {
		Wly_Uqee.rms.put(string2, Wly_Uqee.versionName);
	}

	public static void setIsFileNeedCheckName(String string2) {
		filenameTemp = string2;
	}

	public static void setIsNeedCheck(String string2) {
		Wly_Uqee.rms.put("isneedcheck", string2);
	}

	public static void setLockPatternEnabled(String string2) {
		if (string2.equals("true")) {
			GameTools.setBoolean("lock_pattern_autolock", true);
			return;
		}
		GameTools.setBoolean("lock_pattern_autolock", false);
	}

	public static void setTempAccToken(String string2) {
		Wly_Uqee.rms.put(tempAccName, string2);
	}

	public static void setTempAccname(String string2) {
		tempAccName = string2;
	}

	public static void showMsg(final String string2) {
		Wly_Uqee.activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText((Context) Wly_Uqee.activity,
						(CharSequence) string2, Toast.LENGTH_LONG).show();
			}
		});
	}

	// 1.6.6.6
	public static String getVNGFlag() {
		//return AppEventsConstants.EVENT_PARAM_VALUE_NO;
		return "0";
	}

	// 1.7.0.1
	public static String SetIsShowVNG(String flag) {
		return "0";
	}
}
