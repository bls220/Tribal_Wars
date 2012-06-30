package com.bls220.TribalWars;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.bls220.TribalWars.Tile.ETile;


public class Map
{
	public static final int TILE_SIZE = 32;
	
	//Map Size Consts
	private final int MAP_SIZE_X = 27;
	private final int MAP_SIZE_Y = 27;
	private final int MAP_SIZE_Z = 54;
	
	private ETile[][][] mMap; //The map
	private static final Rect mTileBounds = new Rect(0,0,TILE_SIZE-1,TILE_SIZE-1);
	
	Map(Bitmap tileset)
	{
		//Initialize Map
		mMap = new ETile[MAP_SIZE_X][MAP_SIZE_Y][MAP_SIZE_Z]; //[X][Y][Z]
		//Set Tileset
		//if(tileset != null) mTileSet = tileset;
	}
	
	public void setTile(ETile tile, int X, int Y, int Z)
	{
		mMap[X][Y][Z] = tile;
	}
	
	public void drawMap(Canvas canvas,int z)
	{
		Rect src = new Rect(mTileBounds); //tile in tileset
		Rect dst = new Rect(mTileBounds); //Position on screen
		//TODO: Draw Map
		for( int x=0; x < MAP_SIZE_X; x++)
		{
			for( int y=0; y < MAP_SIZE_Y; y++)
			{
				dst.offsetTo(x*TILE_SIZE, y*TILE_SIZE);//Select canvas location
				for( int dz=z-1; dz < z+1; dz++)
				{
					if( dz < MAP_SIZE_Z && dz >= 0){
						//int ind = mMap[x][y][dz].base*TILE_SIZE;
						//src.offsetTo(ind%512,ind/512);//Set to Tile

						//canvas.drawBitmap(mTileSet,src,dst,mTilePaint); //copy base to tile
					}
				}
			}
		}
	}
	
	public void generateMap()
	{
		//TODO: Terrain generation code
		
		for( int x=0; x < MAP_SIZE_X; x++)
		{
			for( int y=0; y < MAP_SIZE_Y; y++)
			{
				for( int z=0; z < MAP_SIZE_Z; z++)
				{
					this.setTile(ETile.DIRT,x,y,z);
				}
			}
		}
	}
}
