package com.bls220.TribalWars.Tile;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Tileset {

	public static final int TILE_SIZE = 32;
	int[] textures;
	
	//Create Textures from Bitmap for all Tiles
	public Tileset(Bitmap tileset){
		int numTiles = ETile.values().length;
		textures = new int[numTiles];
		GLES20.glGenTextures(numTiles, textures, 0);
		
		//Bind and config texture
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[1]);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        
        //Crop image
        Bitmap img = Bitmap.createBitmap(tileset, 1*TILE_SIZE, 0*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
        img.recycle();
	}
	
}
