package com.bls220.TribalWars.Tile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class Tile {
	
	protected static final float TILE_SIZE = 0.0625f; /** 512/32 = 16 tile/row -> 1/16 = 0.0625 row/tile */
	public boolean isPassable = true;
	
	private final FloatBuffer mSquareData; /** Store our model data in a float buffer. */
	private final FloatBuffer mTextureMapData; /** Texture mapping data */
	static private final int mColorOffset = 12; /** Offset to Color info in SquareData */
	static private final int mPositionDataSize = 3; /** Dimension of position */
	
	static private final float[] defaultSquareData = {
		// X, Y, Z, 
		0.5f, 0.5f, 0.0f,	// UR
        -0.5f, 0.5f, 0.0f,	// UL
        0.5f, -0.5f, 0.0f, 	// LR
        -0.5f, -0.5f, 0.0f,	// LL
        
        // R, G, B, A
        1.0f, 1.0f, 1.0f, 1.0f //Solid White
	};

	public Tile(){
		
		final float[] texMapData = {
				// X, Y
				3*TILE_SIZE, 0.0f,			// UR
				TILE_SIZE, 0.0f,			// UL
				3*TILE_SIZE, 2*TILE_SIZE,	// LR
				TILE_SIZE, 2*TILE_SIZE,		// LL
		};
				
		// Initialize the buffers.
		mSquareData = ByteBuffer.allocateDirect(defaultSquareData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTextureMapData = ByteBuffer.allocateDirect(texMapData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		// Put data into buffer and return to start of buffer
		mSquareData.put(defaultSquareData).position(0);
		mTextureMapData.put(texMapData).position(0);
	}
	
	public final void draw(final int mShader){
		//Allow Object specific additions
		//onDraw();
		
		//Do constant draw setup
		//	Pass in the position information
		final int mPositionHandle = GLES20.glGetAttribLocation(mShader, "a_Position");
		mSquareData.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mSquareData);              
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
        
        //	Pass in the color information
        final int mColorHandle = GLES20.glGetUniformLocation(mShader, "u_Color");
        mSquareData.position(mColorOffset);
        GLES20.glUniform4fv(mColorHandle, 0, mSquareData);
        
        // Pass in texture map position
        final int mTextureCoordinateHandle = GLES20.glGetAttribLocation(mShader, "a_TexCoord");
        mTextureMapData.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,2,GLES20.GL_FLOAT,false,0,mTextureMapData);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
		//Do Render
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	};
	
	//protected abstract void onDraw();
}