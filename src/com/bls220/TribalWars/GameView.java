package com.bls220.TribalWars;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GameView extends GLSurfaceView implements GLSurfaceView.Renderer
{
	public static int mCurZ;
	
	private GameRenderer mRenderer;
	
	GameView(Context context)
	{
		super(context);
		init();
	}
	
	GameView(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		init();
	}
	
	public void init()
	{
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		mRenderer = new GameRenderer();
		this.setRenderer(mRenderer);
	}

//	@Override
//	public void surfaceDestroyed(SurfaceHolder holder)
//	{
//		if ( mThread.isAlive() )
//		{
//			boolean retry = true;
//			//Stop Game Thread
//			mThread.setRunning(false);
//			while (retry)
//			{
//				try{
//					//Wait for thread to stop
//					mThread.join();
//					retry = false;
//				}catch (InterruptedException e){ /*Keep Retrying*/}
//			}
//		}
//	}

}
