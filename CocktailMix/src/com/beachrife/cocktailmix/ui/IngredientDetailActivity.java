package com.beachrife.cocktailmix.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;

public class IngredientDetailActivity extends FragmentActivity implements DrinkListFragment.OnItemSelectedCallback
{
	public static String INGREDIENT_NAME_ARGUMENT = "ingredient_name_argument";

	private Ingredient ingredient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// ConsoleHelper.writeLine(">>> IngredientDetailActivity.onCreate()");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ingredient_detail_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.

		// Load the  argument
		ApplicationEx context = (ApplicationEx)this.getApplicationContext();
		Intent intent = getIntent();
		String name = intent.getStringExtra(INGREDIENT_NAME_ARGUMENT);
		this.ingredient = context.getIngredients().findByName(name);
		
		// Populate the display
		ImageView imageView = (ImageView)this.findViewById(R.id.barItemImage);
		TextView titleTextView = (TextView)this.findViewById(R.id.titleTextView);
		TextView descriptionTextView = (TextView)this.findViewById(R.id.descriptionTextView);
		TextView drinkListTitleTextView = (TextView)this.findViewById(R.id.drinkListTitleTextView);
		CheckBox barHasIngredientCheckBox =  (CheckBox)this.findViewById(R.id.barHasIngredientCheckBox);
		
		
		imageView.setImageResource(this.ingredient.getImageEnabledId());
		titleTextView.setText(ingredient.getName());
		descriptionTextView.setText(ingredient.getDescription());
		String drinkListTitleFormat = getResources().getString(R.string.ingredient_detail_title);
		String drinkListTitle = String.format(drinkListTitleFormat, this.ingredient.getName());
		drinkListTitleTextView.setText(drinkListTitle);
		barHasIngredientCheckBox.setChecked(ingredient.getBarHasIngredient());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.default_menu, menu); // Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch (item.getItemId()) 
		{
		case android.R.id.home: // This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users to navigate up one level in the application structure. 
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_settings:
	    	Intent intent = new Intent(this, SettingsActivity.class);
	    	startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(Drink drink) 
	{
		// In single-pane mode, simply start the detail activity for the selected item ID.
		Intent intent = new Intent(this, DrinkDetailActivity.class);
		intent.putExtra(DrinkDetailFragment.DRINK_NAME_ARGUMENT, drink.getName());
		this.startActivityForResult(intent, 0);
	}
	

	// This is the return result from the DrinkDetailActivity...
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (resultCode == RESULT_OK)
		{
			// We need to refresh
			if (data != null)
			{
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
	
	public void onCheckboxClicked(View view) 
	{
		CheckBox checkBox = (CheckBox)view;
		ingredient.setBarHasIngredient(checkBox.isChecked());
		ConsoleHelper.writeLine("CHECKBOX %s", checkBox.isChecked());
	}

	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// On the back key save the ratings
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {	
			// Here we need to tell our parents that something has changed or not!
			Intent intent = this.getIntent();
			intent.putExtra(INGREDIENT_NAME_ARGUMENT, ingredient.getName());
			this.setResult(RESULT_OK, intent);

			finish(); // If you have no further use for this activity or there is no dependency on this activity
	        return true;
        }
        
        return super.onKeyDown(keyCode, event);
    }

}
