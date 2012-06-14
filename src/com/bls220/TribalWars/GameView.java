package com.bls220.TribalWars;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	public static int mWidth;
	public static int mHeight;
	public static GameThread mThread;
	
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
	
	GameView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context,attrs,defStyle);
		init();
	}
	
	public void init()
	{
		getHolder().addCallback(this);
		//Create Game Thread
		mThread = new GameThread(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{
		mWidth = width;
		mHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if ( !mThread.isAlive() )
		{
			//Create and Start Game Thread
			mThread = new GameThread(this);
			mThread.setRunning(true);
			mThread.start();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		if ( mThread.isAlive() )
		{
			boolean retry = true;
			//Stop Game Thread
			mThread.setRunning(false);
			while (retry)
			{
				try{
					//Wait for thread to stop
					mThread.join();
					retry = false;
				}catch (InterruptedException e){ /*Keep Retrying*/}
			}
		}
	}

}
