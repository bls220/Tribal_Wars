package com.bls220.TribalWars;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.graphics.Rect;
import android.graphics.RectF;

import com.bls220.TribalWars.Tile.ETile;
import com.bls220.TribalWars.Tile.Tile;


public class Map
{
	//public static final int TILE_SIZE = 32;
	
	//Map Size Consts -- Do Not Exceed 180x180xZ
	private final int MAP_SIZE_X = 2;
	private final int MAP_SIZE_Y = 2;
	private final int MAP_SIZE_Z = 54;
	
	private ETile[][][] mMap; //The map
	private final FloatBuffer mPlaneVertBuffer;
	private final ShortBuffer mPlaneIndBuffer;
	static final int mPositionDataSize = 2;
	
	Map()
	{
		//Initialize Map
		mMap = new ETile[MAP_SIZE_X][MAP_SIZE_Y][MAP_SIZE_Z]; //[X][Y][Z]
		//Construct Plane
		final int vertCount = (MAP_SIZE_X+1)*(MAP_SIZE_Y+1);
		final int numSquares = MAP_SIZE_X * MAP_SIZE_Y;
		final int indexCount = numSquares * 6;
		//IBO_Size = indexCount*2BytesPerShort
		//VBO_Size = vertCount*mPositionDataSize*4BytesPerFloat
		mPlaneVertBuffer = ByteBuffer.allocateDirect(vertCount*mPositionDataSize*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mPlaneIndBuffer = ByteBuffer.allocateDirect(indexCount*2).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		//Create Vertices
		mPlaneVertBuffer.position(0);
		for( int y=0; y <= MAP_SIZE_Y; y++){
			for( int x=0; x <= MAP_SIZE_X; x++){
				mPlaneVertBuffer.put(x);
				mPlaneVertBuffer.put(y);
			}
		}
		
		//Build Indices
		mPlaneIndBuffer.position(0);
		for( int i=0,x=0,y=0,w=(MAP_SIZE_X+1); i<numSquares; i++,x++){
			if( x == MAP_SIZE_X ){ y++; x=0;}
			//	0	1	2
			//	3	4	5
			//	6	7	8
			short[] ind = {
				(short) (y*w+x),		//0 1  3 4
				(short) ((y+1)*w+x),	//3 4  6 7
				(short) ((y+1)*w+x+1),	//4 5  7 8
				
				(short) ((y+1)*w+x+1),	//4 5  7 8
				(short) (y*w+x),		//0 1  3 4
				(short) (y*w+x+1)		//1 2  4 5
			};
			mPlaneIndBuffer.put(ind);
		}
		
		
	}
	
	public void setTile(ETile tile, int X, int Y, int Z)
	{
		mMap[X][Y][Z] = tile;
	}
	
//	public void drawMap(Canvas canvas,int z)
//	{
//		Rect src = new Rect(mTileBounds); //tile in tileset
//		Rect dst = new Rect(mTileBounds); //Position on screen
	
//		for( int x=0; x < MAP_SIZE_X; x++)
//		{
//			for( int y=0; y < MAP_SIZE_Y; y++)
//			{
//				dst.offsetTo(x*TILE_SIZE, y*TILE_SIZE);//Select canvas location
//				for( int dz=z-1; dz < z+1; dz++)
//				{
//					if( dz < MAP_SIZE_Z && dz >= 0){
//						//int ind = mMap[x][y][dz].base*TILE_SIZE;
//						//src.offsetTo(ind%512,ind/512);//Set to Tile
//
//						//canvas.drawBitmap(mTileSet,src,dst,mTilePaint); //copy base to tile
//					}
//				}
//			}
//		}
//	}
	
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
