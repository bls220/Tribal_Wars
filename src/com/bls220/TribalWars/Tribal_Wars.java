package com.bls220.TribalWars;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Tribal_Wars extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	Log.d(getLocalClassName(), "onCreate - TribalWars");
        super.onCreate(savedInstanceState);
        //Request the the window have no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set Main view
        setContentView(R.layout.game);
        
        final TextView mFpsView = (TextView) findViewById(R.id.fpsView);
        final GameView mGame = (GameView) findViewById(R.id.gameView);
        Timer timer = new Timer();
        TimerTask updatefps = new TimerTask(){
        	public void run(){
	        	mFpsView.post(new Runnable(){
	        		public void run(){
	        			mFpsView.setText("FPS: " + mGame.mRenderer.mFps); 
	        		}
	        	});
        	}
		};
		timer.schedule(updatefps, 1000, 1000);
    }
    
    @Override
    public void onStart()
    {
    	Log.d(getLocalClassName(), "onStart - TribalWars");
    	super.onStart();
    }
    
    @Override
    public void onResume()
    {
    	Log.d(getLocalClassName(), "onResume - TribalWars");
    	super.onResume();
    }

    @Override
    public void onPause()
    {
    	Log.d(getLocalClassName(), "onPause - TribalWars");
    	super.onPause();
    }
    
    @Override
    public void onStop()
    {
    	Log.d(getLocalClassName(), "onStop - TribalWars");
    	super.onStop();
    }
    
    @Override
    public void onRestart()
    {
    	Log.d(getLocalClassName(), "onRestart - TribalWars");
    	super.onRestart();
    }
    
    @Override
    public void onDestroy()
    {
    	Log.d(getLocalClassName(), "onDestroy - TribalWars");
    	super.onDestroy();
    }
    
	public void onSideMenuClick( View v )
	{
		//TODO: Button Actions
		this.findViewById(R.id.menu_container).setVisibility(View.GONE);
	}
    
}