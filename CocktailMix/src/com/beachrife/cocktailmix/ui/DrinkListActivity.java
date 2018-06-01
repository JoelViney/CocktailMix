package com.beachrife.cocktailmix.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkViewMode;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;

// Used to display a list of drinks and if on a large enough device also the details of the selected drink by displaying it side-by-side using two panes.
public class DrinkListActivity extends FragmentActivity implements ActionBar.OnNavigationListener, DrinkListFragment.OnItemSelectedCallback, DrinkDetailFragment.RatingCallback
{
	private boolean inTwoPaneMode; // Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// ConsoleHelper.writeLine(">>> DrinkListActivity.onCreate()");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drink_list_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.
		
		// Get the arguments
		Intent intent = getIntent();
		int drinkViewModeInt = intent.getIntExtra(DrinkListFragment.DRINK_VIEW_MODE_ARGUMENT, DrinkViewMode.toInt(DrinkViewMode.UNKNOWN));
		DrinkViewMode drinkViewMode = DrinkViewMode.fromInteger(drinkViewModeInt);
		this.populateActionBar(drinkViewMode);
		
		// In two-pane mode, list items should be given the 'activated' state when touched.
		if (findViewById(R.id.drink_detail_container) != null) 
		{
			// The detail container view will be present only in the large-screen layouts
			// If this view is present, then the activity should be in two-pane mode.
			inTwoPaneMode = true;
			DrinkListFragment fragment = (DrinkListFragment)getSupportFragmentManager().findFragmentById(R.id.drink_list_container);
			fragment.setActivateOnItemClick(true);
		}
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.drink_detail_menu, menu); 
		return true;
	}

	// Populates the dropdown list in the ActionBar.
	private void populateActionBar(DrinkViewMode drinkViewMode)
	{
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		Context themedContext = this.getActionBarThemedContextCompat(); 
		String[] dropDownTitles = new String[] {
				getString(R.string.drink_list_page_title_1),
				getString(R.string.drink_list_page_title_2),
				getString(R.string.drink_list_page_title_3), 
				};
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(themedContext, android.R.layout.simple_list_item_1, android.R.id.text1, dropDownTitles);
		actionBar.setListNavigationCallbacks(arrayAdapter, this);
		actionBar.setSelectedNavigationItem(DrinkViewMode.toInt(drinkViewMode));
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		ConsoleHelper.writeLine(">>> DrinkListActivity.onOptionsItemSelected() ");
		
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
	        	if (fragment != null)
	        	{
	        		fragment.clearRating();
	        	}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Callback method from DrinkListFragment indicating that the item was selected.
	@Override
	public void onItemSelected(Drink drink) 
	{
		// if (drink == null)
		// 	ConsoleHelper.writeLine(">>> DrinkListActivity.onItemSelected(NULL) ");
		// else
		// 	ConsoleHelper.writeLine(">>> DrinkListActivity.onItemSelected(%s) ", drink.getName());
		
		
		ApplicationEx context = (ApplicationEx)this.getApplicationContext();
		if (drink != null)
		{
			context.setSelectedDrinkName(drink.getName());
		}
		
		if (this.inTwoPaneMode) 
		{
			// In two-pane mode, show the detail view in this activity by adding or replacing the detail fragment using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(DrinkDetailFragment.DRINK_NAME_ARGUMENT, drink.getName());
			DrinkDetailFragment fragment = new DrinkDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().replace(R.id.drink_detail_container, fragment).commit();
		} 
		else 
		{
			// In single-pane mode, simply start the detail activity for the selected item ID.
			Intent intent = new Intent(this, DrinkDetailActivity.class);
			intent.putExtra(DrinkDetailFragment.DRINK_NAME_ARGUMENT, drink.getName());
			this.startActivityForResult(intent, 0);
		}
	}
	
	// ActionbarStuff. Backward-compatible version of getThemedContext() that simply returns the Activity if getThemedContext is unavailable.
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() 
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) 
			return getActionBar().getThemedContext();
		else 
			return this;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		// ConsoleHelper.writeLine(">>> DrinkListActivity.onSaveInstanceState() ");
		
		outState.putInt(DrinkListFragment.DRINK_VIEW_MODE_ARGUMENT, getActionBar().getSelectedNavigationIndex()); // Serialise the current dropdown position.
		
		ApplicationEx context = (ApplicationEx)this.getApplicationContext();
		DrinkListFragment fragment = (DrinkListFragment)getSupportFragmentManager().findFragmentById(R.id.drink_list_container);
		Drink drink = fragment.getSelectedItem();
		
		if (drink != null)
		{
			ConsoleHelper.writeLine(">>> SAVING (%s)", drink.getName());
			context.setSelectedDrinkName(drink.getName());
		}
		else
		{
			ConsoleHelper.writeLine(">>> SAVING (NULL)");			
		}
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		//ConsoleHelper.writeLine(">>> DrinkListActivity.onRestoreInstanceState() ");
		
		// Restore the previously serialised current dropdown position.
		if (savedInstanceState.containsKey(DrinkListFragment.DRINK_VIEW_MODE_ARGUMENT)) 
		{
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(DrinkListFragment.DRINK_VIEW_MODE_ARGUMENT));
		}
		

		ApplicationEx context = (ApplicationEx)this.getApplicationContext();
		if (context.getSelectedDrinkName() != null && this.inTwoPaneMode)
		{
			ConsoleHelper.writeLine(">>> RESTORE (%s)", context.getSelectedDrinkName());
			
			String drinkName = context.getSelectedDrinkName();
			DrinkListFragment fragment = (DrinkListFragment)getSupportFragmentManager().findFragmentById(R.id.drink_list_container);
			
			if (fragment.getList().hasDrink(drinkName))
			{
				Drink drink = context.getDrinks().findByName(drinkName);
				this.onItemSelected(drink);
			}
		}
	}

	
	// This is triggered when the user selects an ActionBar dropdown item.
	@Override
	public boolean onNavigationItemSelected(int position, long id) 
	{
		//ConsoleHelper.writeLine(">>> DrinkListActivity.onNavigationItemSelected() ");
		
		DrinkViewMode viewMode = DrinkViewMode.fromInteger(position);
		Fragment fragment = (Fragment)getSupportFragmentManager().findFragmentById(R.id.drink_list_container);
		
		if (fragment != null)
		{
			DrinkListFragment drinkListFragment = (DrinkListFragment)fragment;
			drinkListFragment.setDrinkViewMode(viewMode);

			if (inTwoPaneMode)
			{
				// If something is selected that is no longer in the list. Make sure it's de-selected.
				ApplicationEx context = (ApplicationEx)this.getApplicationContext();
				
				if (context.getSelectedDrinkName() != null)
				{
					String name = context.getSelectedDrinkName();
					
					if (!drinkListFragment.getList().hasDrink(name))
					{
						// Select the first item
						if (drinkListFragment.getList().size() > 0)
						{
							Drink drink = drinkListFragment.getDrinkByPosition(0);
							this.onItemSelected(drink);
						}
					}
				}
				
				// If we are in landscape mode select the first cocktail.
				if (context.getSelectedDrinkName() == null)
				{
					// Select the first item
					if (drinkListFragment.getItemCount() > 0)
					{
						Drink drink = drinkListFragment.getDrinkByPosition(0);
						this.onItemSelected(drink);
					}
				}
			}
		}
		
		return true;
	}
	
	// This is the return result from the DrinkDetailActivity or the SettingsActivity.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{		
		//ConsoleHelper.writeLine(">>> DrinkListActivity.onActivityResult(%s)", resultCode);
		
		if (resultCode == RESULT_OK)
		{
			// We need to refresh
			if (data != null)
			{
				if (data.hasExtra(SettingsActivity.SETTING_CHANGED_ARGUMENT))
				{
					ConsoleHelper.writeLine(">>> SettingsActivity.SETTING_CHANGED_ARGUMENT");
					// The settings have changed.
					// Refresh the fragment... After the settings are changed... 
					// I should really check a setting as this could cause some fun bugs!
					DrinkDetailFragment fragment = (DrinkDetailFragment)getSupportFragmentManager().findFragmentById(R.id.drink_detail_container);

					if (fragment != null)
					{
						fragment.updateItem();
					}
				}
				
				// Note: We don't actually use the argument. Should really just return a state.
				if (data.hasExtra(DrinkDetailFragment.DRINK_NAME_ARGUMENT))
				{
					DrinkListFragment fragment = (DrinkListFragment)getSupportFragmentManager().findFragmentById(R.id.drink_list_container);
					if (fragment != null)
					{
						DrinkListFragment drinkListFragment = (DrinkListFragment)fragment;
						drinkListFragment.notifyDataSetChanged();
					}
				}
			}
		}
		
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	// This is returned from the DrinkDetailActivity when a setting has changed.
	// We use it to update the rating value in the list.
	@Override
	public void onRatingChange() 
	{
		Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.drink_list_container);
		if (fragment != null)
		{
			DrinkListFragment drinkListFragment = (DrinkListFragment)fragment;
			drinkListFragment.notifyDataSetChanged();
		}
	}
	
	
}
