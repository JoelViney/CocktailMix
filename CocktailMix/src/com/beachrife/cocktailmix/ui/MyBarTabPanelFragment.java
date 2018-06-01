package com.beachrife.cocktailmix.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.datalayer.DataException;
import com.beachrife.cocktailmix.datalayer.IngredientFactory;
import com.beachrife.cocktailmix.drinks.DrinkViewMode;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;
import com.beachrife.cocktailmix.drinks.IngredientType;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;

@SuppressLint("ValidFragment")
public class MyBarTabPanelFragment extends Fragment implements OnClickListener 
{
	public static final String INGREDIENT_FILTER_ARGUMENT = "ingredient_filter_type"; // The fragment argument representing the section number for this fragment.
	private ApplicationEx context;

	public MyBarTabPanelFragment() 
	{
		this.context = null;
	}
	public MyBarTabPanelFragment(ApplicationEx context) 
	{
		this.context = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		ConsoleHelper.writeLine("MyBarTabPanelFragment.onCreateView()");
		
		View rootView = inflater.inflate(R.layout.my_bar_tab_panel_fragment, container, false);
		
		// Filter the ingredients...
		int ingredientFilter = getArguments().getInt(INGREDIENT_FILTER_ARGUMENT);
		IngredientType ingredientTypeFilter = IngredientType.fromInteger(ingredientFilter);
		
		ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();
		IngredientList allIngredients = context.getIngredients();
		
		IngredientList ingredients = allIngredients.getByIngredientType(ingredientTypeFilter);
		
		ExpandableHeightGridView gridView = (ExpandableHeightGridView)rootView.findViewById(R.id.gridView);
		
		// Load the adapter...
		MyBarItemAdapter adapter = new MyBarItemAdapter(context, ingredients, this);
		gridView.setAdapter(adapter);

		return rootView;
	}


	
	public void onClick(View view) 
	{
		Object tag = view.getTag(R.string.tag_ingredient);
		
		if (tag != null)
		{
			if (tag instanceof String)
			{
				String name = (String)tag;
				Ingredient ingredient = this.context.getIngredients().findByName(name);
				
				// If the user clicked the ImageView we need to set the ToggleButton...
				if (view instanceof ImageView) 
				{
			    	Intent intent = new Intent(this.getActivity(), IngredientDetailActivity.class);
			    	intent.putExtra(DrinkListFragment.DRINK_VIEW_MODE_ARGUMENT, DrinkViewMode.toInt(DrinkViewMode.VIEW_BY_INGREDIENT));
			    	intent.putExtra(IngredientDetailActivity.INGREDIENT_NAME_ARGUMENT, ingredient.getName());
			    	this.startActivityForResult(intent, 0);
			    	/*
					String tagKey = MyBarItemAdapter.TOGGLE_BUTTON + ingredient.getName();
					ToggleButton toggleButton = (ToggleButton)view.getRootView().findViewWithTag(tagKey);
					ImageView imageView = (ImageView)view;


					toggleButton.setChecked(!toggleButton.isChecked());
					ingredient.setUserHasIngredient(toggleButton.isChecked());
					
					this.saveIngredient(ingredient);
					imageView.setImageResource(ingredient.getImageId());
					*/
				}
				else if (view instanceof ToggleButton) 
				{
					ConsoleHelper.writeLine("ToggleButton Name: %s to %s", ingredient.getName(), ingredient.getBarHasIngredient());
					
					String tagKey = MyBarItemAdapter.IMAGE_VIEW + ingredient.getName();
					ToggleButton toggleButton = (ToggleButton)view;
					ImageView imageView = (ImageView)view.getRootView().findViewWithTag(tagKey);

					ingredient.setBarHasIngredient(toggleButton.isChecked());

					this.saveIngredient(ingredient);
					
					imageView.setImageResource(ingredient.getImageId());
				}
				else if (view instanceof CheckBox) 
				{
					ConsoleHelper.writeLine("CheckBox Name: %s to %s", ingredient.getName(), ingredient.getBarHasIngredient());
					
					String tagKey = MyBarItemAdapter.IMAGE_VIEW + ingredient.getName();
					CheckBox checkBox = (CheckBox)view;
					ImageView imageView = (ImageView)view.getRootView().findViewWithTag(tagKey);

					ingredient.setBarHasIngredient(checkBox.isChecked());

					this.saveIngredient(ingredient);
					
					imageView.setImageResource(ingredient.getImageId());
				}
			}
		}
	}
	
	
	
	private void saveIngredient(Ingredient ingredient)
	{
		ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();
		try 
		{
			IngredientFactory factory = new IngredientFactory(context);
									
			factory.save(context.getIngredients());
		} 
		catch (DataException e) 
		{
			e.printStackTrace();
			Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	
	
	// This is the return result from the DrinkDetailActivity or the SettingsActivity.
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{		
		// ConsoleHelper.writeLine(">>> DrinkListActivity.onActivityResult(%s)", resultCode);
		
		if (resultCode == android.app.Activity.RESULT_OK)
		{
			// We need to refresh
			if (data != null)
			{
				if (data.hasExtra(SettingsActivity.SETTING_CHANGED_ARGUMENT))
				{
					ConsoleHelper.writeLine(">>> SettingsActivity.SETTING_CHANGED_ARGUMENT");
					// The settings have changed.
				}
				else if (data.hasExtra(IngredientDetailActivity.INGREDIENT_NAME_ARGUMENT))
				{
					// Update the image
					View rootView = this.getView();
					ExpandableHeightGridView gridView = (ExpandableHeightGridView)rootView.findViewById(R.id.gridView);
					
					MyBarItemAdapter adapater = (MyBarItemAdapter)gridView.getAdapter();
					
					adapater.notifyDataSetChanged();
				}
			}
		}
		
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	
}
