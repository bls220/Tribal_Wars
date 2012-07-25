package com.bls220.TribalWars;

import com.bls220.TribalWars.Tile.Tile;

import android.opengl.GLES20;

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
		//mMap.generateMap();
		while (mRun)
		{ 
			update();
			count += 1;
			count %=2;
			try {
				Thread.sleep(250);
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
		final float[] texMapData = {
				// X,Y
				tx+Tile.TILE_SIZE	,ty,			//UR
				tx			,ty,			//UL
				tx			,ty+Tile.TILE_SIZE,	//LL

				tx			,ty+Tile.TILE_SIZE,	//LL
				tx+Tile.TILE_SIZE	,ty+Tile.TILE_SIZE,	//LR
				tx+Tile.TILE_SIZE	,ty				//UR			
		};
		map.mTexVertBuffer.position(0);
		for( int y=map.MAP_SIZE_Y-1; y >= map.MAP_SIZE_Y/2; y--){
			for( int x=0; x < map.MAP_SIZE_X/4; x++){
				map.mTexVertBuffer.put(texMapData);
			}
		}
		mGame.queueEvent( new Runnable(){
			public void run(){
				map.mTexVertBuffer.position(0);
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, map.getTexVBO());
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, map.mTexVertBuffer.capacity()*4, map.mTexVertBuffer, GLES20.GL_DYNAMIC_DRAW);
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			}
		});
		//TODO: Update entities
	}

}
