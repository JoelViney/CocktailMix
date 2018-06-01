package com.beachrife.cocktailmix.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.beachrife.cocktailmix.R;

/**
 * An activity representing a single Drink detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a DrinkListActivity.
 * 
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link DrinkDetailFragment}.
 */
public class DrinkDetailActivity extends FragmentActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drink_detail_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.

		// savedInstanceState is non-null when there is fragment state saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at: http://developer.android.com/guide/components/fragments.html
		if (savedInstanceState == null)
		{
			// Create the detail fragment and add it to the activity using a fragment transaction.
			Bundle arguments = new Bundle();
			
			Intent intent = getIntent();
			String name = intent.getStringExtra(DrinkDetailFragment.DRINK_NAME_ARGUMENT);
			arguments.putString(DrinkDetailFragment.DRINK_NAME_ARGUMENT, name);
			
			DrinkDetailFragment fragment = new DrinkDetailFragment();
			fragment.setArguments(arguments);
			
			FragmentManager manager = this.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.add(R.id.drink_detail_container, fragment);
			transaction.commit();
		}
	}

    @SuppressLint("NewApi")
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
    {
    	// This will shut-down the activity if we are in landscape mode and the resolution is above

	    super.onConfigurationChanged(newConfig);
		
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE )
	    {
	    	if (android.os.Build.VERSION.SDK_INT >= 13)
	    	{
		    	if (newConfig.screenHeightDp >= 600 | newConfig.screenWidthDp >= 600)
		    	{
			    	finish(); // If we are in 600dp mode...
		    	}
	    	}
	    	else
	    	{
		    	Display display = getWindowManager().getDefaultDisplay();
		    	DisplayMetrics outMetrics = new DisplayMetrics ();
		        display.getMetrics(outMetrics);

		        float density  = getResources().getDisplayMetrics().density;
		        float dpHeight = outMetrics.heightPixels / density;
		        float dpWidth  = outMetrics.widthPixels / density;
		        
		    	if (dpHeight >= 600 | dpWidth >= 600)
		    	{
			    	finish(); // If we are in 600dp mode...
		    	}
	    	}
	    }
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.drink_detail_menu, menu); // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
			case android.R.id.home: // This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users to navigate up one level in the application structure. 
			NavUtils.navigateUpTo(this, new Intent(this, DrinkListActivity.class));
			return true;
	
			case R.id.action_settings:
	    	Intent intent = new Intent(this, SettingsActivity.class);
	    	startActivityForResult(intent, 0);
			return true;
	
			case R.id.action_clear_rating:
        	DrinkDetailFragment fragment = (DrinkDetailFragment)getSupportFragmentManager().findFragmentById(R.id.drink_detail_container);
        	fragment.clearRating();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// On the back key save the ratings
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
        	DrinkDetailFragment fragment = (DrinkDetailFragment)getSupportFragmentManager().findFragmentById(R.id.drink_detail_container);
        	
			boolean dirty = fragment.getDirty();
	
			// Here we need to tell our parents that something has changed or not!
			// I don't think we need to do this any more....
			Intent intent = this.getIntent();
			if (dirty)
			{
				String name = fragment.getArguments().getString(DrinkDetailFragment.DRINK_NAME_ARGUMENT);
				intent.putExtra(DrinkDetailFragment.DRINK_NAME_ARGUMENT, name);
				this.setResult(RESULT_OK, intent);
			}
			else
			{
				this.setResult(RESULT_CANCELED);			
			}
	        
			finish(); // If you have no further use for this activity or there is no dependency on this activity
	        return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// Fired by the setting activity
		if (resultCode == RESULT_OK)
		{
        	DrinkDetailFragment fragment = (DrinkDetailFragment)getSupportFragmentManager().findFragmentById(R.id.drink_detail_container);
        	fragment.updateItem();
		}
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
}
