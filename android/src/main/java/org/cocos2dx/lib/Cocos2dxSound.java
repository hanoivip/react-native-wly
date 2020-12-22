package org.cocos2dx.lib;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;
import java.util.HashMap;
import java.util.Map.Entry;

public class Cocos2dxSound {
	private static final int MAX_SIMULTANEOUS_STREAMS_DEFAULT = 5;
	private static final int SOUND_PRIORITY = 1;
	private static final int SOUND_QUALITY = 5;
	private static final float SOUND_RATE = 1.0f;
	private static final String TAG = "Cocos2dxSound";
	private final int INVALID_SOUND_ID;
	private final int INVALID_STREAM_ID;
	private Context mContext;
	private float mLeftVolume;
	private HashMap<String, Integer> mPathSoundIDMap;
	private float mRightVolume;
	private HashMap<Integer, Integer> mSoundIdStreamIdMap;
	private SoundPool mSoundPool;

	public Cocos2dxSound(Context context) {
		this.INVALID_SOUND_ID = -1;
		this.INVALID_STREAM_ID = -1;
		this.mContext = context;
		initData();
	}

	private void initData() {
		this.mSoundIdStreamIdMap = new HashMap();
		this.mSoundPool = new SoundPool(SOUND_QUALITY, 3, SOUND_QUALITY);
		this.mPathSoundIDMap = new HashMap();
		this.mLeftVolume = 0.5f;
		this.mRightVolume = 0.5f;
	}

	private void pauseOrResumeAllEffects(boolean z) {
		for (Entry key : this.mSoundIdStreamIdMap.entrySet()) {
			int intValue = ((Integer) key.getKey()).intValue();
			if (z) {
				pauseEffect(intValue);
			} else {
				resumeEffect(intValue);
			}
		}
	}

	public int createSoundIdFromAsset(String str) {
		try {
			return this.mSoundPool.load(this.mContext.getAssets().openFd(str),
					0);
		} catch (Throwable e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
			return -1;
		}
	}

	public void end() {
		this.mSoundPool.release();
		this.mPathSoundIDMap.clear();
		this.mSoundIdStreamIdMap.clear();
		initData();
	}

	public float getEffectsVolume() {
		return (this.mLeftVolume + this.mRightVolume) / 2.0f;
	}

	public void pauseAllEffects() {
		pauseOrResumeAllEffects(true);
	}

	public void pauseEffect(int i) {
		Integer num = (Integer) this.mSoundIdStreamIdMap
				.get(Integer.valueOf(i));
		if (num != null && num.intValue() != -1) {
			this.mSoundPool.pause(num.intValue());
			this.mPathSoundIDMap.remove(Integer.valueOf(i));
		}
	}

	public int playEffect(String str, boolean z) {
		int i = -1;
		Integer num = (Integer) this.mPathSoundIDMap.get(str);
		if (num != null) {
			this.mSoundPool.stop(num.intValue());
			SoundPool soundPool = this.mSoundPool;
			int intValue = num.intValue();
			float f = this.mLeftVolume;
			float f2 = this.mRightVolume;
			if (!z) {
				i = 0;
			}
			this.mSoundIdStreamIdMap.put(num, Integer.valueOf(soundPool.play(
					intValue, f, f2, SOUND_PRIORITY, i, SOUND_RATE)));
		} else {
			num = Integer.valueOf(preloadEffect(str));
			if (num.intValue() == -1) {
				return -1;
			}
			playEffect(str, z);
		}
		return num.intValue();
	}

	public int preloadEffect(String str) {
		if (this.mPathSoundIDMap.get(str) != null) {
			return ((Integer) this.mPathSoundIDMap.get(str)).intValue();
		}
		int createSoundIdFromAsset = createSoundIdFromAsset(str);
		if (createSoundIdFromAsset == -1) {
			return createSoundIdFromAsset;
		}
		this.mSoundIdStreamIdMap.put(Integer.valueOf(createSoundIdFromAsset),
				Integer.valueOf(-1));
		this.mPathSoundIDMap.put(str, Integer.valueOf(createSoundIdFromAsset));
		return createSoundIdFromAsset;
	}

	public void resumeAllEffects() {
		pauseOrResumeAllEffects(false);
	}

	public void resumeEffect(int i) {
		Integer num = (Integer) this.mSoundIdStreamIdMap
				.get(Integer.valueOf(i));
		if (num != null && num.intValue() != -1) {
			this.mSoundPool.resume(num.intValue());
			this.mPathSoundIDMap.remove(Integer.valueOf(i));
		}
	}

	public void setEffectsVolume(float f) {
		float f2 = SOUND_RATE;
		float f3 = 0.0f;
		if (f >= 0.0f) {
			f3 = f;
		}
		if (f3 <= SOUND_RATE) {
			f2 = f3;
		}
		this.mRightVolume = f2;
		this.mLeftVolume = f2;
		for (Entry value : this.mSoundIdStreamIdMap.entrySet()) {
			this.mSoundPool.setVolume(((Integer) value.getValue()).intValue(),
					this.mLeftVolume, this.mRightVolume);
		}
	}

	public void stopAllEffects() {
		for (Entry key : this.mSoundIdStreamIdMap.entrySet()) {
			stopEffect(((Integer) key.getKey()).intValue());
		}
	}

	public void stopEffect(int i) {
		Integer num = (Integer) this.mSoundIdStreamIdMap
				.get(Integer.valueOf(i));
		if (num != null && num.intValue() != -1) {
			this.mSoundPool.stop(num.intValue());
			this.mPathSoundIDMap.remove(Integer.valueOf(i));
		}
	}

	public void unloadEffect(String str) {
		Integer num = (Integer) this.mPathSoundIDMap.remove(str);
		if (num != null) {
			this.mSoundPool.unload(num.intValue());
			this.mSoundIdStreamIdMap.remove(num);
		}
	}
}
