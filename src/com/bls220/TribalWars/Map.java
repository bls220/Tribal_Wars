package com.bls220.TribalWars;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;

import com.bls220.TribalWars.Tile.ETile;
import com.bls220.TribalWars.Tile.Tile;


public class Map
{
	//Map Size Consts -- Do Not Exceed 180x180xZ
	final int MAP_SIZE_X = 60;
	final int MAP_SIZE_Y = 30;
	final int MAP_SIZE_Z = 5;

	private ETile[][][] mMap; //The map
	private final FloatBuffer mPlaneVertBuffer;
	private final FloatBuffer mTexVertBuffer;
	private final int[] mBufferHandles = new int[3];
	public static final int mPositionDataSize = 2;
	private final int indexCount = MAP_SIZE_X * MAP_SIZE_Y * 6;
	Map()
	{
		//Initialize Map
		mMap = new ETile[MAP_SIZE_X][MAP_SIZE_Y][MAP_SIZE_Z]; //[X][Y][Z]
		//Construct Buffers
		mPlaneVertBuffer = ByteBuffer.allocateDirect(indexCount*mPositionDataSize*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTexVertBuffer = ByteBuffer.allocateDirect(indexCount*mPositionDataSize*4).order(ByteOrder.nativeOrder()).asFloatBuffer();

		//Create Vertices
		mTexVertBuffer.position(0);
		mPlaneVertBuffer.position(0);
		for( int y=MAP_SIZE_Y-1; y >= 0; y--){
			for( int x=0; x < MAP_SIZE_X; x++){
				final float[] vertData = {
						// X,Y
						x+1	,y+1,	//UR
						x	,y+1,	//UL
						x	,y,		//LL

						x	,y,		//LL
						x+1	,y,		//LR
						x+1	,y+1	//UR			
				};
				
				//Build Texture Mapping
				float tx = 1*Tile.TILE_SIZE;
				float ty = 0*Tile.TILE_SIZE;
				final float[] texMapData = {
						// X,Y
						tx+Tile.TILE_SIZE	,ty,			//UR
						tx			,ty,			//UL
						tx			,ty+Tile.TILE_SIZE,	//LL

						tx			,ty+Tile.TILE_SIZE,	//LL
						tx+Tile.TILE_SIZE	,ty+Tile.TILE_SIZE,	//LR
						tx+Tile.TILE_SIZE	,ty				//UR			
				};
				
				mPlaneVertBuffer.put(vertData);
				mTexVertBuffer.put(texMapData);
			}
		}

		//Generate handles for the buffers
		GLES20.glGenBuffers(2, mBufferHandles ,0);

		//Bind and Load buffers
		//Texture VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, getTexVBO());
		mTexVertBuffer.position(0);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mTexVertBuffer.capacity()*4, mTexVertBuffer, GLES20.GL_STATIC_DRAW);
		//Plane VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, getPlaneVBO());
		mPlaneVertBuffer.position(0);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mPlaneVertBuffer.capacity()*4, mPlaneVertBuffer, GLES20.GL_STATIC_DRAW);	
		//Unbind Buffers
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
	}

	public void setTile(ETile tile, int X, int Y, int Z)
	{
		mMap[X][Y][Z] = tile;
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

	public void draw(final int mShader){

		//Bind Texture VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, getTexVBO());
		// Pass in texture map position
		mTexVertBuffer.position(0);
		final int mTextureCoordinateHandle = GLES20.glGetAttribLocation(mShader, "a_TexCoord");
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle,mPositionDataSize,GLES20.GL_FLOAT,false,0, 0);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		
		// Bind VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, getPlaneVBO());
		// Pass in the position information
		mPlaneVertBuffer.position(0);
		final int mPositionHandle = GLES20.glGetAttribLocation(mShader, "a_Position");
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, 0);              
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		//	Pass in the color information
		final int mColorHandle = GLES20.glGetUniformLocation(mShader, "u_Color");
		GLES20.glUniform4f(mColorHandle,0,1,1,1);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0, indexCount);

		// Unbind buffers
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public int getPlaneVBO(){ return mBufferHandles[0]; }
	public int getTexVBO(){ return mBufferHandles[1]; }
}
