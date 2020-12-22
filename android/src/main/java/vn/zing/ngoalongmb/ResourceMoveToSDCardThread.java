package vn.zing.ngoalongmb;

import android.content.res.AssetManager;
import android.os.Message;
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceMoveToSDCardThread extends Thread {
	private String[] dirPath;

	public ResourceMoveToSDCardThread() {
		this.dirPath = new String[] { "apk", "fonts", "", "Image",
				"Image/Activity", "Image/Anim", "Image/Anim/hunting",
				"Image/Anim/npc_anim", "Image/Appoint", "Image/AreaMap",
				"Image/AreaMap2", "Image/BattleUI", "Image/Campaign", "Image/ChildTrain",
				"Image/Dialog", "Image/General", "Image/GroupBattle",
				"Image/Item", "Image/PlunderWar", "Image/MapBg",
				"Image/MapBg/CamEnter", "Image/MapBg2", "Image/Patrol",
				"Image/Patrol/Area", "Image/Patrol/Location",
				"Image/Patrol/Peason", "Image/PersonalBattle", "Image/ResType",
				"Image/SkillEffect", "Image/Soldier", "Image/Tech",
				"Image/WarBg", "Image/WarBg2", "maincity", "UI", "UI/a",
				"UI/f", "Sound", "Map", "Map/Map_zh_CN", "Map/Map_zh_CN/Area",
				"Map/Map_zh_CN/Campaign", "Map/Map_zh_CN/Tower",
				"Map/Map_zh_CN/War", "StaticData/StaticData_zh_CN", "Xml" };
	}

	public void run() {
		super.run();
		// GameTools.loadUnPassWords();
		Message msg = new Message();
		msg.arg1 = 1;
		Wly_Uqee.gameHandler.sendMessage(msg);
	}

	private void CopySingleFile(String filename) {
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

	private void CopyAssets(String dir) {
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

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[10240];
		while (true) {
			int read = in.read(buffer);
			if (read != -1) {
				out.write(buffer, 0, read);
			} else {
				return;
			}
		}
	}
}
