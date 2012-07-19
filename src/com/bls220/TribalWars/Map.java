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
	//public static final int TILE_SIZE = 32;

	//Map Size Consts -- Do Not Exceed 180x180xZ
	final int MAP_SIZE_X = 2;
	final int MAP_SIZE_Y = 2;
	final int MAP_SIZE_Z = 5;

	private ETile[][][] mMap; //The map
	private final FloatBuffer mPlaneVertBuffer;
	private final ShortBuffer mPlaneIndBuffer;
	private final FloatBuffer mTextureBuffer;
	private final int[] mBufferHandles = new int[3];
	public static final int mPositionDataSize = 2;

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
		mTextureBuffer = ByteBuffer.allocateDirect(indexCount*mPositionDataSize*4).order(ByteOrder.nativeOrder()).asFloatBuffer();

		//Create Vertices
		mPlaneVertBuffer.position(0);
		for( int y=MAP_SIZE_Y; y >= 0; y--){
			for( int x=0; x <= MAP_SIZE_X; x++){
				mPlaneVertBuffer.put(x);
				mPlaneVertBuffer.put(y);
			}
		}

		//Build Indices
		mPlaneIndBuffer.position(0);
		mTextureBuffer.position(0);
		for( int i=0,x=0,y=0,w=(MAP_SIZE_X+1); i<numSquares; i++,x++){
			if( x == MAP_SIZE_X ){ y++; x=0;}
			//	0	1	2
			//	3	4	5
			//	6	7	8
			final short[] ind = {
					(short) (y*w+x+1),		//UR	1 2  4 5
					(short) (y*w+x),		//UL	0 1  3 4
					(short) ((y+1)*w+x),	//LL	3 4  6 7

					(short) ((y+1)*w+x),	//LL	3 4  6 7
					(short) ((y+1)*w+x+1),	//LR	4 5  7 8
					(short) (y*w+x+1)		//UR	1 2  4 5
			};
			
			//Build Texture Mapping
			float dx = x*0.0625f;
			float dy = y*0.0625f;
			final float[] texMapData = {
					// X,Y
					dx+0.0625f	,dy,			//UR
					dx			,dy,			//UL
					dx			,dy+0.0625f,	//LL
					
					dx			,dy+0.0625f,	//LL
					dx+0.0625f	,dy+0.0625f,	//LR
					dx+0.0625f	,dy				//UR			
			};			
			
			mPlaneIndBuffer.put(ind);
			mTextureBuffer.put(texMapData);
		}

		//Generate handles for the buffers
		GLES20.glGenBuffers(3, mBufferHandles ,0);

		//Bind and Load buffers
			//Texture VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferHandles[2]);
		mTextureBuffer.position(0);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mTextureBuffer.capacity()*4, mTextureBuffer, GLES20.GL_STATIC_DRAW);
			//Plane VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, getVBO());
		mPlaneVertBuffer.position(0);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mPlaneVertBuffer.capacity()*4, mPlaneVertBuffer, GLES20.GL_STATIC_DRAW);
			//Plane IBO
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, getIBO());
		mPlaneIndBuffer.position(0);
		GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, mPlaneIndBuffer.capacity()*2, mPlaneIndBuffer, GLES20.GL_STATIC_DRAW);		
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

		// Bind VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, getVBO());
		// Pass in the position information
		mPlaneVertBuffer.position(0);
		final int mPositionHandle = GLES20.glGetAttribLocation(mShader, "a_Position");
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, 0);              
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		//	Pass in the color information
		final int mColorHandle = GLES20.glGetUniformLocation(mShader, "u_Color");
		GLES20.glUniform4f(mColorHandle,1,0,1,1);

		//Bind Texture VBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mBufferHandles[2]);
		// Pass in texture map position
		mTextureBuffer.position(0);
		final int mTextureCoordinateHandle = GLES20.glGetAttribLocation(mShader, "a_TexCoord");
		GLES20.glVertexAttribPointer(mTextureCoordinateHandle,mPositionDataSize,GLES20.GL_FLOAT,false,0, 0);
		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

		// Bind IBO
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, getIBO());
		// Draw Plane
		mPlaneIndBuffer.position(0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, MAP_SIZE_X*MAP_SIZE_Y*6, GLES20.GL_UNSIGNED_SHORT, 0);

		// Unbind buffers
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public int getVBO(){ return mBufferHandles[0]; }
	public int getIBO(){ return mBufferHandles[1]; }
}
