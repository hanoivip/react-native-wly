package vn.zing.ngoalongmb;

import java.io.PrintStream;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;

import vn.zing.ngoalongmb.Wly_Uqee;

public class WlyUqeeJniTool {
	private static native void CppVnLoginGame();

	public static void vnLoginGame() {
		Wly_Uqee.activity.mGLView.queueEvent(new Runnable() {

			@Override
			public void run() {
				System.out.println("wjf:CppVnLoginGame");
				WlyUqeeJniTool.CppVnLoginGame();
			}
		});
	}

}
