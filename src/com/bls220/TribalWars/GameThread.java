package com.bls220.TribalWars;

public class GameThread extends Thread
{
	private boolean mRun = true;
	//private Map mMap;
	private final GameView mGame;

	GameThread( GameView gameView )
	{
		//mMap = new Map();
		mGame = gameView;
	}

	public void setRunning( boolean value )
	{
		mRun = value;
	}
	
	@Override
	public void run()
	{
		this.setRunning(true);
		//mMap.generateMap();
		while (mRun)
		{ 
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				//We don't care about the interrupting cow...
			}
		}
	}
	
	public void update()
	{
		//TODO: Update entities
	}

}
