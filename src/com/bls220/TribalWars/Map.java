package com.bls220.TribalWars;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.AvoidXfermode;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;

import com.bls220.TribalWars.Tile.ETile;


public class Map
{
	public static final int TILE_SIZE = 32;
	
	//Map Size Consts
	private final int MAP_SIZE_X = 27;
	private final int MAP_SIZE_Y = 27;
	private final int MAP_SIZE_Z = 54;
	
	private ETile[][][] mMap; //The map
	private static Bitmap mTileSet = Bitmap.createBitmap(TILE_SIZE*16, TILE_SIZE*16, Config.ARGB_8888);
	private static Bitmap mColorMask = Bitmap.createBitmap(TILE_SIZE, TILE_SIZE, Config.ARGB_8888);
	private static final Paint mTilePaint = new Paint();
	private static final Rect mTileBounds = new Rect(0,0,TILE_SIZE-1,TILE_SIZE-1);
	
	private float[] matrix = {	.25f, 0, 0, 0, 0,
								0, .25f, 0, 0, 0,
								0, 0, .25f, 0, 0,
								0, 0, 0, .25f, 0 };
	
	Map(Bitmap tileset)
	{
		//Initialize Map
		mMap = new ETile[MAP_SIZE_X][MAP_SIZE_Y][MAP_SIZE_Z]; //[X][Y][Z]
		//Set Tileset
		if(tileset != null) mTileSet = tileset;
		//Configure Paint
		//mColorMask.eraseColor(ETile.DIRT.overlay);
		//mTilePaint.setXfermode(new AvoidXfermode(Color.argb(255, 127, 127, 127),255, AvoidXfermode.Mode.TARGET));
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
						int ind = mMap[x][y][dz].base*TILE_SIZE;
						src.offsetTo(ind%512,ind/512);//Set to Tile
						
						//mTilePaint.setColorFilter(new PorterDuffColorFilter(mMap[x][y][dz].overlay,Mode.SCREEN));
						mTilePaint.setColorFilter(new ColorMatrixColorFilter(matrix));
						canvas.drawBitmap(mTileSet,src,dst,mTilePaint); //copy base to tile
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
