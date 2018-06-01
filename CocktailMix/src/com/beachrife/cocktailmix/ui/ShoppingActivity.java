package com.beachrife.cocktailmix.ui;

import java.text.NumberFormat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;
import com.beachrife.cocktailmix.shopping.ShoppingAdvisor;
import com.beachrife.cocktailmix.shopping.ShoppingBasket;

public class ShoppingActivity extends FragmentActivity implements DrinkListFragment.OnItemSelectedCallback, DrinkDetailFragment.RatingCallback
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		ConsoleHelper.writeLine("ShoppingActivity.onCreate()");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping_activity);
		getActionBar().setDisplayHomeAsUpEnabled(true); // Show the Up button in the action bar.
		
		ApplicationEx context = (ApplicationEx)this.getApplicationContext();
		
		ShoppingAdvisor advisor = new ShoppingAdvisor(context.getDrinks(), context.getIngredients());
		ShoppingBasket basket = advisor.getBestBasket();
		
		this.populateControls(basket);
		
		// In two-pane mode, list items should be given the 'activated' state when touched.
		if (findViewById(R.id.drink_list_container) != null) 
		{
			ConsoleHelper.writeLine("ShoppingActivity.onCreate(LOADING FRAGMENT)");
			// The detail container view will be present only in the large-screen layouts
			// If this view is present, then the activity should be in two-pane mode.
			DrinkListFragment fragment = (DrinkListFragment)getSupportFragmentManager().findFragmentById(R.id.drink_list_container);
			
			if (basket == null)
				fragment.filter(new DrinkList());
			else
				fragment.filter(basket.getDrinks());
			
			fragment.setActivateOnItemClick(true);
			ConsoleHelper.writeLine("ShoppingActivity.onCreate(FRAGMENT LOADED)");
		}
	}

	private void populateControls(ShoppingBasket basket)
	{
		IngredientList ingredients;
		DrinkList drinks;
		
		if (basket != null)
		{
			ingredients = basket.getIngredients();
			drinks = basket.getDrinks();
		}
		else
		{
			ingredients = new IngredientList();
			drinks = new DrinkList();
		}
		
		// Set the blurb
		{
			TextView blurbTextView = (TextView)this.findViewById(R.id.blurbTextView);
			String blurbText = null;
			if (drinks.size() == 0)
				blurbText = getResources().getString(R.string.shopping_blurb_nothing_text);
			else if (drinks.size() == 1)
				blurbText = getResources().getString(R.string.shopping_singular_blurb_text);
			else
			{
				String unformattedBlurbText = getResources().getString(R.string.shopping_blurb_text);
				blurbText = String.format(unformattedBlurbText, drinks.size());
			}
			blurbTextView.setText(blurbText);
		}
		
		// Set the total
		TextView ingredientTotalAmountTextView = (TextView)this.findViewById(R.id.ingredientTotalAmountTextView);
		if (basket != null)
		{
			String costString = NumberFormat.getCurrencyInstance().format(basket.getTotalCost());
			ingredientTotalAmountTextView.setText(costString);
		}
		else
		{
			ingredientTotalAmountTextView.setVisibility(View.GONE);
		}
		
		ImageView ingredient1ImageView = (ImageView)this.findViewById(R.id.ingredientImage1);
		TextView ingredient1TextView = (TextView)this.findViewById(R.id.ingredient1TextView);
		TextView ingredient1AmountTextView = (TextView)this.findViewById(R.id.ingredient1AmountTextView);
		
		ImageView ingredient2ImageView = (ImageView)this.findViewById(R.id.ingredientImage2);
		TextView ingredient2TextView = (TextView)this.findViewById(R.id.ingredient2TextView);
		TextView ingredient2AmountTextView = (TextView)this.findViewById(R.id.ingredient2AmountTextView);
		
		ImageView ingredient3ImageView = (ImageView)this.findViewById(R.id.ingredientImage3);
		TextView ingredient3TextView = (TextView)this.findViewById(R.id.ingredient3TextView);
		TextView ingredient3AmountTextView = (TextView)this.findViewById(R.id.ingredient3AmountTextView);

		if (ingredients.size() < 3)
		{
			ingredient3ImageView.setVisibility(View.GONE);
			ingredient3TextView.setVisibility(View.GONE);
			ingredient3AmountTextView.setVisibility(View.GONE);
		}
		if (ingredients.size() < 2)
		{
			ingredient2ImageView.setVisibility(View.GONE);
			ingredient2TextView.setVisibility(View.GONE);
			ingredient2AmountTextView.setVisibility(View.GONE);
		}
		if (ingredients.size() < 1)
		{
			ingredient1ImageView.setVisibility(View.GONE);
			ingredient1TextView.setVisibility(View.GONE);
			ingredient1AmountTextView.setVisibility(View.GONE);
		}
		
		if (ingredients.size() >= 3)
		{
			Ingredient ingredient = ingredients.get(2);
			String costString = NumberFormat.getCurrencyInstance().format(ingredient.getCost());

			ingredient3TextView.setText(ingredient.getName());
			ingredient3AmountTextView.setText(costString);
			ingredient3ImageView.setImageResource(ingredient.getImageEnabledId());
		}

		if (ingredients.size() >= 2)
		{
			Ingredient ingredient = ingredients.get(1);
			String costString = NumberFormat.getCurrencyInstance().format(ingredient.getCost());

			ingredient2TextView.setText(ingredient.getName());
			ingredient2AmountTextView.setText(costString);
			ingredient2ImageView.setImageResource(ingredient.getImageEnabledId());
		}

		if (ingredients.size() >= 1)
		{
			Ingredient ingredient = ingredients.get(0);
			String costString = NumberFormat.getCurrencyInstance().format(ingredient.getCost());

			ingredient1TextView.setText(ingredient.getName());
			ingredient1AmountTextView.setText(costString);
			ingredient1ImageView.setImageResource(ingredient.getImageEnabledId());
		}
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
			NavUtils.navigateUpTo(this, new Intent(this, ShoppingActivity.class));
			return true;

			case R.id.action_settings:
	    	Intent intent = new Intent(this, SettingsActivity.class);
	    	startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	// Callback method from DrinkListFragment indicating that the item was selected.
	@Override
	public void onItemSelected(Drink drink) 
	{
		ApplicationEx context = (ApplicationEx)this.getApplicationContext();
		if (drink != null)
		{
			context.setSelectedDrinkName(drink.getName());
		}
		
		// In single-pane mode, simply start the detail activity for the selected item ID.
		Intent intent = new Intent(this, DrinkDetailActivity.class);
		intent.putExtra(DrinkDetailFragment.DRINK_NAME_ARGUMENT, drink.getName());
		this.startActivityForResult(intent, 0);
	}
	

	// This is the return result from the DrinkDetailActivity or the SettingsActivity.
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
