package com.bls220.TribalWars.Tile;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Tile {
	
	public static final float TILE_SIZE = 0.0625f; /** 512/32 = 16 tile/row -> 1/16 = 0.0625 row/tile */
	public boolean isPassable = true;
	
	public final FloatBuffer mTextureMapData; /** Texture mapping data */

	static private final int mPositionDataSize = 2; /** Dimension of position */
	static private final int mColorOffset = mPositionDataSize*6; /** Offset to Color info in SquareData */

	public Tile(){ //TODO: Make Abstract
		
		final float[] texMapData = {
				// X, Y
				TILE_SIZE	,0,			//LL
				TILE_SIZE	,TILE_SIZE,	//UL
				2*TILE_SIZE	,0,			//LR
				2*TILE_SIZE	,0,			//LR
				TILE_SIZE	,TILE_SIZE,	//UL
				2*TILE_SIZE	,TILE_SIZE	//UR
		};
				
		// Initialize the buffers.
		mTextureMapData = ByteBuffer.allocateDirect(texMapData.length * 4)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		// Put data into buffer and return to start of buffer
		mTextureMapData.put(texMapData).position(0);
	}
	
	public final void draw(final int mShader){
		//Allow Object specific additions
		onDraw();
		
		//Do constant draw setup
		//	Pass in the position information
		final int mPositionHandle = GLES20.glGetAttribLocation(mShader, "a_Position");
		//mSquareData.position(0);
        //GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, 0, mSquareData);              
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
        
        //	Pass in the color information
        final int mColorHandle = GLES20.glGetUniformLocation(mShader, "u_Color");
        GLES20.glUniform4f(mColorHandle, 1, 1, 1, 1);
        
        // Pass in texture map coords
        final int mTextureCoordinateHandle = GLES20.glGetAttribLocation(mShader, "a_TexCoord");
        mTextureMapData.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,mPositionDataSize,GLES20.GL_FLOAT,false,0,mTextureMapData);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
		//Do Render
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
	};
	
	public final void update(){
		//Do Object specific updates
		onUpdate();
		//Update tile placement data
		for(int i = 0;i<mColorOffset; i+=mPositionDataSize){
			
		}
		
	};
	
	protected void onDraw(){}; //TODO: Make Abstract
	protected void onUpdate(){}; //TODO: Make Abstract
}