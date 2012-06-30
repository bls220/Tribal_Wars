package com.bls220.TribalWars;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bls220.TribalWars.Tile.Tileset;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;

public class GameRenderer implements Renderer {

	public int mWidth;
	public int mHeight;
	private Resources mRes;
	
	/**
	 * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
	 * of being located at the center of the universe) to world space.
	 */
	private float[] mModelMatrix = new float[16];

	/**
	 * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
	 * it positions things relative to our eye.
	 */
	private float[] mViewMatrix = new float[16];

	/** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
	private float[] mProjectionMatrix = new float[16];
	
	/** Allocate storage for the final combined matrix. This will be passed into the shader program. */
	private float[] mMVPMatrix = new float[16];
	
	/** Store our model data in a float buffer. */
	private final FloatBuffer mSquareVertices;

	/** This will be used to pass in the transformation matrix. */
	private int mMVPMatrixHandle;
	
	/** This will be used to pass in model position information. */
	private int mPositionHandle;
	
	/** This will be used to pass in model color information. */
	private int mColorHandle;

	/** How many bytes per float. */
	private static final int mBytesPerFloat = 4;
	
	/** How many elements per vertex. */
	private static final int mStrideBytes = 3 * mBytesPerFloat;	
	
	/** Offset of the position data. */
	private static final int mPositionOffset = 0;
	
	/** Size of the position data in elements. */
	private static final int mPositionDataSize = 3;
	
	/** Offset of the color data. */
	private static final int mColorOffset = 12;
	
	/** Texture mapping data */
	private final FloatBuffer mTextureMapData;
	
	/** Handle to Texture Data */
	private int[] mTilesetHandle;
	
	/** Used to pass texture to shader */
	private int mTextureUniformHandle;
	
	/** Used to pass texture coord info to shader */
	private int mTextureCoordinateHandle;
	
	/** Size of Texture Coord. data */
	private static final int mTextureCoordinateDataSize = 2;
	
	private static final float TILE_SIZE = 0.0625f;
	
	public GameRenderer(Resources res){
		super();
		// This Square is red, white, blue, and red.
		final float[] squareVerticesData = {
				// X, Y, Z, 
	            0.5f, -0.5f, 0.0f, 	// LR
	            -0.5f, -0.5f, 0.0f,	// LL
	            0.5f, 0.5f, 0.0f,	// UR
	            -0.5f, 0.5f, 0.0f,	// UL
	            // R, G, B, A
	            0.7f, 0.4f, 0.25f, 1.0f //RBGA
	     };
		
		final float[] texMapData = {
				//Lower-Right
				3*TILE_SIZE, 2*TILE_SIZE,
				//Lower-Left
				TILE_SIZE, 2*TILE_SIZE,
				//Upper-Right
				3*TILE_SIZE, 0.0f,
				//Upper-Left
				TILE_SIZE, 0.0f
		};
				
		// Initialize the buffers.
		mSquareVertices = ByteBuffer.allocateDirect(squareVerticesData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		mTextureMapData = ByteBuffer.allocateDirect(texMapData.length * mBytesPerFloat)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		// Put data into buffer and return to start of buffer
		mSquareVertices.put(squareVerticesData).position(0);
		mTextureMapData.put(texMapData).position(0);

		// Setup stuff
		mRes = res;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		// Draw the triangle facing straight on.
        Matrix.setIdentityM(mModelMatrix, 0);
        drawSquare(mSquareVertices,mTextureMapData);
        
	}	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mWidth = width;
		mHeight = height;
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);
		
		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float zoom = 3.0f;
		final float ratio = (float) width / (height*zoom);
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f/zoom;
		final float top = 1.0f/zoom;
		final float near = 1.0f;
		final float far = 2.0f;
		
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//Enable/Disable Features
		//GLES20.glDisable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_TEXTURE_2D);
		GLES20.glEnable(GLES20.GL_BLEND);
		// Set background color to gray
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		// Position the eye behind the origin.
		final float eyeX = 0.0f;
		final float eyeY = 0.0f;
		final float eyeZ = 1.5f;

		// We are looking toward the distance
		final float lookX = 0.0f;
		final float lookY = 0.0f;
		final float lookZ = -5.0f;

		// Set our up vector. This is where our head would be pointing were we holding the camera.
		final float upX = 0.0f;
		final float upY = 1.0f;
		final float upZ = 0.0f;
		
		// Set the view matrix. This matrix can be said to represent the camera position.
		// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
		// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
		Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		// Set Tileset texture
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		mTilesetHandle = new int[1];
		GLES20.glGenTextures(1, mTilesetHandle, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTilesetHandle[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		final Bitmap img = BitmapFactory.decodeResource(mRes, R.drawable.tileset,opts);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
		img.recycle();
		
		final String vertexShader = RawResourceReader.readTextFileFromRawResource(mRes, R.raw.vert_shader);
			
		final String fragmentShader = RawResourceReader.readTextFileFromRawResource(mRes, R.raw.frag_shader);												
			
		// Load in the vertex shader.
		int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

		if (vertexShaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(vertexShaderHandle, vertexShader);

			// Compile the shader.
			GLES20.glCompileShader(vertexShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{				
				GLES20.glDeleteShader(vertexShaderHandle);
				vertexShaderHandle = 0;
			}
		}

		if (vertexShaderHandle == 0)
		{
			throw new RuntimeException("Error creating vertex shader.");
		}
		
		// Load in the fragment shader shader.
		int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

		if (fragmentShaderHandle != 0) 
		{
			// Pass in the shader source.
			GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

			// Compile the shader.
			GLES20.glCompileShader(fragmentShaderHandle);

			// Get the compilation status.
			final int[] compileStatus = new int[1];
			GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

			// If the compilation failed, delete the shader.
			if (compileStatus[0] == 0) 
			{				
				GLES20.glDeleteShader(fragmentShaderHandle);
				fragmentShaderHandle = 0;
			}
		}

		if (fragmentShaderHandle == 0)
		{
			throw new RuntimeException("Error creating fragment shader.");
		}
		
		// Create a program object and store the handle to it.
		int programHandle = GLES20.glCreateProgram();
		
		if (programHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(programHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(programHandle, fragmentShaderHandle);
			
			// Bind attributes
			GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
			GLES20.glBindAttribLocation(programHandle, 1, "a_TexCoord");
			
			// Link the two shaders together into a program.
			GLES20.glLinkProgram(programHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				GLES20.glDeleteProgram(programHandle);
				programHandle = 0;
			}
		}
		
		if (programHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}
        
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");        
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetUniformLocation(programHandle, "u_Color");
      	mTextureUniformHandle = GLES20.glGetUniformLocation(programHandle, "u_Texture");
      	mTextureCoordinateHandle = GLES20.glGetAttribLocation(programHandle, "a_TexCoord");
        
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);
	}

	/**
	 * Draws a Square from the given vertex data.
	 * 
	 * @param aSquareBuffer The buffer containing the vertex data.
	 */
	private void drawSquare(final FloatBuffer aSquareBuffer, final FloatBuffer aTexCoordBuffer)
	{		
		// Pass in the position information
		aSquareBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,
        		mStrideBytes, aSquareBuffer);              
        GLES20.glEnableVertexAttribArray(mPositionHandle);        
        
        // Pass in the color information
        aSquareBuffer.position(mColorOffset);       
        GLES20.glUniform4fv(mColorHandle, 0, aSquareBuffer);
        
        // Pass in texture data and pos
        aTexCoordBuffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle,mTextureCoordinateDataSize,GLES20.GL_FLOAT,false,0,aTexCoordBuffer);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
     
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTilesetHandle[0]);
     
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);                               
	}
}
