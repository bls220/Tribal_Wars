package com.bls220.TribalWars;

import com.bls220.TribalWars.Tile.Tile;

import android.opengl.GLES20;
import android.util.Log;

public class GameThread extends Thread
{
	private boolean mRun = true;
	private int count = 0;
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
		long startTime = System.nanoTime();
		//mMap.generateMap();
		while (mRun)
		{ 
			update();
			count += 1;
			count %=2;
			long now = System.nanoTime();
			double elapsedMs = (now - startTime) / 1.0e6;
			Log.i("Tribal_Wars-Logic Thread", "ms: " + elapsedMs + " \t- fps: " + (1000 / elapsedMs));
			startTime = now;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				//We don't care about the interrupting cow...
			}
		}
	}

	public void update()
	{
		final Map map = mGame.mRenderer.test_map;
		float tx = 1*Tile.TILE_SIZE;
		float ty = count*Tile.TILE_SIZE;
		final float[] tileData = {
				// X,Y
				tx+Tile.TILE_SIZE	,ty,			//UR
				tx			,ty,			//UL
				tx			,ty+Tile.TILE_SIZE,	//LL

				tx			,ty+Tile.TILE_SIZE,	//LL
				tx+Tile.TILE_SIZE	,ty+Tile.TILE_SIZE,	//LR
				tx+Tile.TILE_SIZE	,ty				//UR			
		};
		float[] texMapData = new float[map.MAP_SIZE_X*map.MAP_SIZE_Y*12];
		for( int y=0; y < map.MAP_SIZE_Y; y++){
			for( int x=0; x < map.MAP_SIZE_X; x++){
				int ind = x+y*map.MAP_SIZE_Y*12;
				for( int i = 0; i<12; i++){
						texMapData[ind+i] = tileData[i];
				}
			}
		}
		map.mTexVertBuffer.position(0);
		map.mTexVertBuffer.put(texMapData);
//		mGame.queueEvent( new Runnable(){
//			public void run(){
//				
//			}
//		});
		//TODO: Update entities
	}

}
