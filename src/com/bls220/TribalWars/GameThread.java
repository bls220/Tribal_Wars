package com.bls220.TribalWars;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.SurfaceHolder;

public class GameThread extends Thread
{
	private boolean mRun = false;
	private long mStartTime;
	private long mElapsedTime;
	private Map mMap;

	GameThread( GameView gameView )
	{
		Bitmap tileset = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.tileset);
		mMap = new Map(tileset);
	}

	public void setRunning( boolean value )
	{
		mRun = value;
	}
	
	@Override
	public void run()
	{
//		Canvas canvas = null;
		mStartTime = System.currentTimeMillis();
		mMap.generateMap();
		while (mRun)
		{
//			try
//			{
//				canvas = mHolder.lockCanvas();
//				if ( canvas != null)
//				{
					this.doUpdate();
//					this.doDraw(mElapsedTime, canvas);
					mElapsedTime = System.currentTimeMillis() - mStartTime;
//				}
//			}
//			finally
//			{
//				mHolder.unlockCanvasAndPost(canvas);
				mStartTime = System.currentTimeMillis();
//			}
		}
	}
	
//	public void doDraw(long elapsedTime, Canvas canvas)
//	{
//		canvas.drawColor(Color.BLACK);
//		
//		//Draw Terrain
//		mMap.drawMap(canvas, mGame.mCurZ);
//		//TODO: Call draw methods of all Entities
//		
//		//Draw FPS Counter
//		String str = "FPS: " + Math.round(1000f / elapsedTime);//"Screen Size: " + mGame.mWidth + "x" + mGame.mHeight;
//		Rect bounds = new Rect();
//		Paint text = new Paint();
//		int textSize = 16;
//		text.setTypeface(Typeface.DEFAULT_BOLD);
//		text.setTextSize(textSize);
//		text.getTextBounds(str, 0, str.length(), bounds);
//		bounds.set(0, 0, bounds.right+20, textSize+bounds.bottom+2);
//		text.setColor(Color.rgb(127, 127, 255));
//		canvas.drawRect(bounds,text);
//		text.setColor(Color.WHITE);
//		canvas.drawText(str, 0, str.length(), 10, textSize-1, text);
//	}
	
	public void doUpdate()
	{
		//TODO: Update entities
	}

}
