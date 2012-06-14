package com.bls220.TribalWars;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.bls220.TribalWars.Tile.ETile;
import com.bls220.TribalWars.Tile.Tile;


public class Map
{
	//Map Size Consts
	private final int MAP_SIZE_X = 27;
	private final int MAP_SIZE_Y = 27;
	private final int MAP_SIZE_Z = 54;

	
	private static Resources res; //The Resources
	
	private byte[][][] mMap; //The map
	private static HashMap<Byte,Tile> tileset = null; //<ID,Tile>
	
	Map(Resources resources)
	{
		res = resources;
		mMap = new byte[MAP_SIZE_X][MAP_SIZE_Y][MAP_SIZE_Z]; //[X][Y][Z]
		if(tileset == null) tileset = new HashMap<Byte,Tile>();
	}

	public void loadTile(Tile t)
	{
		byte id = t.ID();
		if( !tileset.containsKey(id))
		{
			t.createTile(res);
			tileset.put( id, t );
		}
	}
	
	public void setTile(Tile tile, int X, int Y, int Z)
	{
		loadTile(tile);
		mMap[X][Y][Z] = tile.ID();
	}
	
	public void drawMap(Canvas canvas)
	{
		//TODO: Draw Map
		Tile tileToDraw = tileset.get(mMap[0][0][0]);
		tileToDraw.draw(canvas);
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
					this.setTile(new Tile(ETile.DIRT),x,y,z);
				}
			}
		}
	}
}
