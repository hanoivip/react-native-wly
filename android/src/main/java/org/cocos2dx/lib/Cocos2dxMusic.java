package org.cocos2dx.lib;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

//import com.google.android.gms.cast.TextTrackStyle;

public class Cocos2dxMusic {
	private static final String TAG = "Cocos2dxMusic";
	private MediaPlayer mBackgroundMediaPlayer;
	private Context mContext;
	private String mCurrentPath;
	private boolean mIsPaused;
	private float mLeftVolume;
	private float mRightVolume;

	public Cocos2dxMusic(Context context) {
		this.mContext = context;
		initData();
	}

	private MediaPlayer createMediaplayerFromAssets(String str) {
		try {
			MediaPlayer mediaPlayer = new MediaPlayer();
			try {
				AssetFileDescriptor openFd = this.mContext.getAssets().openFd(
						str);
				mediaPlayer.setDataSource(openFd.getFileDescriptor(),
						openFd.getStartOffset(), openFd.getLength());
				mediaPlayer.prepare();
				mediaPlayer.setVolume(this.mLeftVolume, this.mRightVolume);
				return mediaPlayer;
			} catch (Exception e2) {
				Log.e(TAG, "error: " + e2.getMessage(), e2);
				return null;
			}
		} catch (Exception e3) {
			Log.e(TAG, "error: " + e3.getMessage(), e3);
			return null;
		}
	}

	private void initData() {
		this.mLeftVolume = 0.5f;
		this.mRightVolume = 0.5f;
		this.mBackgroundMediaPlayer = null;
		this.mIsPaused = false;
		this.mCurrentPath = null;
	}

	public void end() {
		if (this.mBackgroundMediaPlayer != null) {
			this.mBackgroundMediaPlayer.release();
		}
		initData();
	}

	public float getBackgroundVolume() {
		return this.mBackgroundMediaPlayer != null ? (this.mLeftVolume + this.mRightVolume) / 2.0f
				: 0.0f;
	}

	public boolean isBackgroundMusicPlaying() {
		return this.mBackgroundMediaPlayer == null ? false
				: this.mBackgroundMediaPlayer.isPlaying();
	}

	public void pauseBackgroundMusic() {
		if (this.mBackgroundMediaPlayer != null
				&& this.mBackgroundMediaPlayer.isPlaying()) {
			this.mBackgroundMediaPlayer.pause();
			this.mIsPaused = true;
		}
	}

	public void playBackgroundMusic(String str, boolean z) {
		if (this.mCurrentPath == null) {
			this.mBackgroundMediaPlayer = createMediaplayerFromAssets(str);
			this.mCurrentPath = str;
		} else if (!this.mCurrentPath.equals(str)) {
			if (this.mBackgroundMediaPlayer != null) {
				this.mBackgroundMediaPlayer.release();
			}
			this.mBackgroundMediaPlayer = createMediaplayerFromAssets(str);
			this.mCurrentPath = str;
		}
		if (this.mBackgroundMediaPlayer == null) {
			Log.e(TAG, "playBackgroundMusic: background media player is null");
			return;
		}
		try {
			if (this.mIsPaused) {
				this.mBackgroundMediaPlayer.seekTo(0);
				this.mBackgroundMediaPlayer.start();
			} else if (this.mBackgroundMediaPlayer.isPlaying()) {
				this.mBackgroundMediaPlayer.seekTo(0);
			} else {
				this.mBackgroundMediaPlayer.start();
			}
			this.mBackgroundMediaPlayer.setLooping(z);
			this.mIsPaused = false;
		} catch (Exception e) {
			Log.e(TAG, "playBackgroundMusic: error state");
		}
	}

	public void preloadBackgroundMusic(String str) {
		if (this.mCurrentPath == null || !this.mCurrentPath.equals(str)) {
			if (this.mBackgroundMediaPlayer != null) {
				this.mBackgroundMediaPlayer.release();
			}
			this.mBackgroundMediaPlayer = createMediaplayerFromAssets(str);
			this.mCurrentPath = str;
		}
	}

	public void resumeBackgroundMusic() {
		if (this.mBackgroundMediaPlayer != null && this.mIsPaused) {
			this.mBackgroundMediaPlayer.start();
			this.mIsPaused = false;
		}
	}

	public void rewindBackgroundMusic() {
		if (this.mBackgroundMediaPlayer != null) {
			this.mBackgroundMediaPlayer.stop();
			try {
				this.mBackgroundMediaPlayer.prepare();
				this.mBackgroundMediaPlayer.seekTo(0);
				this.mBackgroundMediaPlayer.start();
				this.mIsPaused = false;
			} catch (Exception e) {
				Log.e(TAG, "rewindBackgroundMusic: error state");
			}
		}
	}

	public void setBackgroundVolume(float f) {
		/*
		 * float f2 = TextTrackStyle.DEFAULT_FONT_SCALE; float f3 = 0.0f; if (f
		 * >= 0.0f) { f3 = f; } if (f3 <= TextTrackStyle.DEFAULT_FONT_SCALE) {
		 * f2 = f3; }
		 */
		this.mRightVolume = f;
		this.mLeftVolume = f;
		if (this.mBackgroundMediaPlayer != null) {
			this.mBackgroundMediaPlayer.setVolume(this.mLeftVolume,
					this.mRightVolume);
		}
	}

	public void stopBackgroundMusic() {
		if (this.mBackgroundMediaPlayer != null) {
			this.mBackgroundMediaPlayer.stop();
			this.mIsPaused = false;
		}
	}
}
