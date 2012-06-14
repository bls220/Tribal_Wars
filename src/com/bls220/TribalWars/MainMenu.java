package com.bls220.TribalWars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainMenu extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	Log.d(getLocalClassName(), "onCreate - MainMenu");
        super.onCreate(savedInstanceState);
        //Request the the window have no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set Main view
        setContentView(R.layout.main_menu);
    }
    
    @Override
    public void onStart()
    {
    	Log.d(getLocalClassName(), "onStart - MainMenu");
    	super.onStart();
    }
    
    @Override
    public void onResume()
    {
    	Log.d(getLocalClassName(), "onResume - MainMenu");
    	super.onResume();
    }

    @Override
    public void onPause()
    {
    	Log.d(getLocalClassName(), "onPause - MainMenu");
    	super.onPause();
    }
    
    @Override
    public void onStop()
    {
    	Log.d(getLocalClassName(), "onStop - MainMenu");
    	super.onStop();
    }
    
    @Override
    public void onRestart()
    {
    	Log.d(getLocalClassName(), "onRestart - MainMenu");
    	super.onRestart();
    }
    
    @Override
    public void onDestroy()
    {
    	Log.d(getLocalClassName(), "onDestroy - MainMenu");
    	super.onDestroy();
    }
    
    public void onTextViewClick(View v)
    {
    	switch ( v.getId() )
    	{
    	case R.id.NewGameTextView:
    		Log.d(getLocalClassName(),"Click: New Game");
    		//Continue on to starting the game....
    	case R.id.ContinueTextView:
    		Log.d(getLocalClassName(),"Click: Continue");
    		startActivity( new Intent(this,Tribal_Wars.class) );
    		break;
    	case R.id.OptionsTextView:
    		Log.d(getLocalClassName(),"Click: Options");
    		break;
    	case R.id.QuitTextView:
    		Log.d(getLocalClassName(),"Click: Quit");
    		this.finish();
    		break;
    	}
    }
}
