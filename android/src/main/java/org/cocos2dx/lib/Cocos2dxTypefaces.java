package org.cocos2dx.lib;

import android.content.Context;
import android.graphics.Typeface;
import java.util.Hashtable;

public class Cocos2dxTypefaces {
	private static final Hashtable<String, Typeface> cache;

	static {
		cache = new Hashtable();
	}

	public static Typeface get(Context context, String str) {
		Typeface typeface;
		synchronized (cache) {
			if (!cache.containsKey(str)) {
				cache.put(str,
						Typeface.createFromAsset(context.getAssets(), str));
			}
			typeface = (Typeface) cache.get(str);
		}
		return typeface;
	}
}
