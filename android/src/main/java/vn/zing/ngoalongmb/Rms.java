package vn.zing.ngoalongmb;

import android.app.Activity;
import android.content.SharedPreferences;

public class Rms {
	private Activity aty;
	private String rsname;

	public Rms(Activity activity, String string2) {
		this.aty = activity;
		this.rsname = string2;
	}

	public void clear() {
		this.aty.getSharedPreferences(this.rsname, 0).edit().clear();
	}

	public String get(String string2) {
		return this.aty.getSharedPreferences(this.rsname, 0).getString(string2,
				null);
	}

	public void put(String string2, String string3) {
		SharedPreferences.Editor editor = this.aty.getSharedPreferences(
				this.rsname, 0).edit();
		editor.putString(string2, string3);
		editor.commit();
	}
}
