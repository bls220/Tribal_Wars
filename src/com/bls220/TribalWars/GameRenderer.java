package com.bls220.TribalWars;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.widget.TextView;

import com.bls220.TribalWars.Tile.Tile;

public class GameRenderer implements Renderer {

	public int mWidth;
	public int mHeight;
	private Resources mRes;
	
	private int mTextureUniformHandle; /** Used to pass texture to shader */
	private int[] mTilesetHandle; /** Handle to Texture Data */
	
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

	/** This will be used to pass in the transformation matrix. */
	private int mMVPMatrixHandle;
	
	private int mProgramHandle; /** Handle to compiled shader */
	
	private long mTimeSinceLastDraw = 0;
	public int mFps = 0;
	
	private Tile test_tile;
	
	public GameRenderer(Resources res){
		super();

		// Setup stuff
		test_tile = new Tile();
		mRes = res;
		mTimeSinceLastDraw = System.currentTimeMillis();
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Redraw background color
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
		// Reset Model Matrix
        Matrix.setIdentityM(mModelMatrix, 0);
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
     
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTilesetHandle[0]);
     
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        
        //Try to move camera
        Matrix.translateM(mProjectionMatrix, 0, mProjectionMatrix, 0, 0, 0, -0.01f);
        
		// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // multiply MVP Matrix (which currently contains model * view) by the projection Matrix then store in MVP
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //Pass in MVP
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        
        test_tile.draw(mProgramHandle);
        long now = System.currentTimeMillis();
        mFps = (int) (1000f/(now - mTimeSinceLastDraw));
        mTimeSinceLastDraw = now;
	}	

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		mWidth = width;
		mHeight = height;
		// Set the OpenGL viewport to the same size as the surface.
		GLES20.glViewport(0, 0, width, height);
		
		// Create a new perspective projection matrix. The height will stay the same
		// while the width will vary as per aspect ratio.
		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 8.0f;
		
		Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		//Enable/Disable Features
		GLES20.glDisable(GLES20.GL_DEPTH_TEST);
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
		
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		// Set Tileset texture
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
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
		mProgramHandle = GLES20.glCreateProgram();
		
		if (mProgramHandle != 0) 
		{
			// Bind the vertex shader to the program.
			GLES20.glAttachShader(mProgramHandle, vertexShaderHandle);			

			// Bind the fragment shader to the program.
			GLES20.glAttachShader(mProgramHandle, fragmentShaderHandle);
			
			// Bind attributes
			GLES20.glBindAttribLocation(mProgramHandle, 0, "a_Position");
			GLES20.glBindAttribLocation(mProgramHandle, 1, "a_TexCoord");
			
			// Link the two shaders together into a program.
			GLES20.glLinkProgram(mProgramHandle);

			// Get the link status.
			final int[] linkStatus = new int[1];
			GLES20.glGetProgramiv(mProgramHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

			// If the link failed, delete the program.
			if (linkStatus[0] == 0) 
			{				
				GLES20.glDeleteProgram(mProgramHandle);
				mProgramHandle = 0;
			}
		}
		
		if (mProgramHandle == 0)
		{
			throw new RuntimeException("Error creating program.");
		}
        
        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");        
      	mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(mProgramHandle);
	}
}
