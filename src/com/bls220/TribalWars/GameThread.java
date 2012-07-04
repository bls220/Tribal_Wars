package com.bls220.TribalWars;

import android.widget.TextView;

public class GameThread extends Thread
{
	private boolean mRun = true;
	//private Map mMap;
	private GameView mGame;
	private TextView mFpsView;

	GameThread( GameView gameView )
	{
		//mMap = new Map();
		mGame = gameView;
		mFpsView = (TextView) gameView.findViewById(R.id.fpsView);
	}

	public void setRunning( boolean value )
	{
		mRun = value;
	}
	
	@Override
	public void run()
	{
		//this.setRunning(true);
		//mMap.generateMap();
		while (mRun)
		{ 
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void doUpdate()
	{
		//TODO: Update entities
	}

}
