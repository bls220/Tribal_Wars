package com.bls220.TribalWars;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
	private GameView mGame;
	private SurfaceHolder mHolder;
	private boolean mRun = false;
	private long mStartTime;
	private long mElapsedTime;
	private Map mMap;

	GameThread( GameView gameView )
	{
		mGame = gameView;
		mHolder = mGame.getHolder();
		mMap = new Map(mGame.getResources());
	}

	public void setRunning( boolean value )
	{
		mRun = value;
	}
	
	@Override
	public void run()
	{
		Canvas canvas = null;
		mStartTime = System.currentTimeMillis();
		mMap.generateMap();
		while (mRun)
		{
			try
			{
				canvas = mHolder.lockCanvas();
				if ( canvas != null)
				{
					this.doUpdate();
					this.doDraw(mElapsedTime, canvas);
					mElapsedTime = System.currentTimeMillis() - mStartTime;
				}
			}
			finally
			{
				mHolder.unlockCanvasAndPost(canvas);
				mStartTime = System.currentTimeMillis();
			}
		}
	}
	
	public void doDraw(long elapsedTime, Canvas canvas)
	{
		canvas.drawColor(Color.BLACK);
		
		//Draw Terrain
		mMap.drawMap(canvas);
		//TODO: Call draw methods of all Entities
		
		//Draw FPS Counter
		Paint text = new Paint();
		int textSize = 12;
		text.setColor(Color.WHITE);
		text.setTextSize(textSize);
		canvas.drawText("FPS: " + Math.round(1000f / elapsedTime), 10, (int) textSize-1, text);
		canvas.drawText("Screen Size: " + GameView.mWidth + "x" + GameView.mHeight, 10, (int) 2*textSize-1,text);
	}
	
	public void doUpdate()
	{
		//TODO: Update entities
	}

}
