package com.bls220.TribalWars;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class Tribal_Wars extends Activity
{
	private GameView mGame;
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
        //LinearLayout mainLayout = new LinearLayout(this);
        //mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        //mainLayout.setGravity(Gravity.FILL);
        //mainLayout.setWeightSum(10.0f);        
        //add views to main view
        	//gameView
        //mGame = new GameView(this);
        //mainLayout.addView(mGame, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 9.0f));
        	//sideMenu
        //LayoutInflater.from(this).inflate(R.layout.side_menu, mainLayout, true);
        setContentView(R.layout.game);
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