package com.bls220.TribalWars;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GameView extends GLSurfaceView
{
	public static int mCurZ;
	
	private GameRenderer mRenderer;
	
	public GameView(Context context)
	{
		super(context);
		init(context.getResources());
	}
	
	public GameView(Context context, AttributeSet attrs)
	{
		super(context,attrs);
		init(context.getResources());
	}
	
	public void init(Resources res)
	{
		// Create an OpenGL ES 2.0 context
		setEGLContextClientVersion(2);
		mRenderer = new GameRenderer(res);
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
