package com.beachrife.cocktailmix.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.DrinkViewMode;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;

// A list fragment representing a list of Drinks. 
// This fragment also supports tablet devices by allowing list items to be given an 'activated' state upon selection. 
// This helps indicate which item is currently being viewed in a DrinkDetailFragment.
// Activities containing this fragment MUST implement the Callbacks interface.
public class DrinkListFragment extends ListFragment 
{
	public static final String DRINK_VIEW_MODE_ARGUMENT = "drink_view_mode_argument";
	public static final String DRINK_INGREDIENT_ARGUMENT = "drink_ingredient_argument";
	
	private static final String STATE_ACTIVATED_POSITION = "activated_position"; // The serialisation (saved instance state) Bundle key representing the activated item position. Only used on tablets.
	private int activatedPosition = ListView.INVALID_POSITION; 				 // The current activated item position. Only used on tablets.
	
	private DrinkViewMode drinkViewMode;
	
	// A callback interface that all activities containing this fragment must implement. 
	// This mechanism allows activities to be notified of item selections.
	// Callback for when an item has been selected.
	public interface OnItemSelectedCallback { public void onItemSelected(Drink item); }
	private static OnItemSelectedCallback dummyCallbacks = new OnItemSelectedCallback() { @Override public void onItemSelected(Drink item) { } }; // A dummy implementation of the Callbacks interface that does nothing. Used only when this fragment is not attached to an activity.
	private OnItemSelectedCallback onItemClickCallback = dummyCallbacks; 							 // The fragment's current callback object, which is notified of list item clicks.
	
	
	
	// Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
	public DrinkListFragment() 
	{
		
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		ConsoleHelper.writeLine("DrinkListFragment.onCreate()");
		super.onCreate(savedInstanceState);

		// Load the mode from the intent passed in.
		Intent intent = this.getActivity().getIntent();
		DrinkViewMode drinkViewMode = DrinkViewMode.UNKNOWN;
		String ingredientName = null;
		
		if (intent != null)
		{
			if (intent.hasExtra(DrinkListFragment.DRINK_VIEW_MODE_ARGUMENT))
			{
				int viewModeArg = intent.getIntExtra(DrinkListFragment.DRINK_VIEW_MODE_ARGUMENT, DrinkViewMode.toInt(DrinkViewMode.VIEW_ALL));
				drinkViewMode = DrinkViewMode.fromInteger(viewModeArg);
			}
			
			if (intent.hasExtra(IngredientDetailActivity.INGREDIENT_NAME_ARGUMENT))
			{
				ingredientName = intent.getStringExtra(IngredientDetailActivity.INGREDIENT_NAME_ARGUMENT);
			}
		}

		this.drinkViewMode = drinkViewMode;
		
		// Populate the Adaptor.
		ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();
		DrinkListAdapter listAdapter = new DrinkListAdapter(context, this.drinkViewMode, ingredientName);

		this.setListAdapter(listAdapter);
		
		this.filter(drinkViewMode, ingredientName);
	}
	
	public void notifyDataSetChanged()
	{
		DrinkListAdapter adapter = (DrinkListAdapter)this.getListAdapter();
		adapter.notifyDataSetChanged();
	}
	
	public void setDrinkViewMode(DrinkViewMode drinkViewMode)
	{
		if (drinkViewMode != this.drinkViewMode)
		{
			this.drinkViewMode = drinkViewMode;

			this.filter(this.drinkViewMode, null);
		}
	}

	public void filter(DrinkList drinks)
	{
		ConsoleHelper.writeLine("DrinkListFragment.filter(DrinkList)");
		
		DrinkListAdapter adapter = (DrinkListAdapter)this.getListAdapter();

		adapter.setList(drinks);

		this.notifyDataSetChanged();
	}
	
	private void filter(DrinkViewMode drinkViewMode, String ingredientName)
	{
		ConsoleHelper.writeLine("DrinkListFragment.filter(%s)", drinkViewMode);
		ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();
		
		DrinkListAdapter adapter = (DrinkListAdapter)this.getListAdapter();

		DrinkList allDrinks = context.getDrinks();

		DrinkList drinks = allDrinks.getFilteredList(drinkViewMode, context, ingredientName);
		
		adapter.setList(drinks);

		this.notifyDataSetChanged();
	}
	
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) 
	{
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialised activated item position.
		if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) 
		{
			setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof OnItemSelectedCallback)) 
		{
			throw new IllegalStateException("Activity must implement the DrinkListFragment Callbacks.");
		}

		this.onItemClickCallback = (OnItemSelectedCallback)activity;
	}

	@Override
	public void onDetach() 
	{
		super.onDetach(); // Reset the active callbacks interface to the dummy implementation.
		this.onItemClickCallback = dummyCallbacks;
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) 
	{
		// ConsoleHelper.writeLine(">### DrinkListFragment.onListItemClick(%s)", position);
		
		super.onListItemClick(listView, view, position, id);

		this.activatedPosition = position;
		
		Drink cocktail = this.getDrinkByPosition(position);
		
		// Notify the active callbacks interface (the activity, if the fragment is attached to one) that an item has been selected.
		onItemClickCallback.onItemSelected(cocktail);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		if (activatedPosition != ListView.INVALID_POSITION) 
		{
			// Serialise and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, activatedPosition);
		}
	}
	
	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) 
	{
		// When setting CHOICE_MODE_SINGLE, ListView will automatically give items the 'activated' state when touched.
		getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
	}
	
	private void setActivatedPosition(int position) 
	{
		if (position == ListView.INVALID_POSITION) 
		{
			getListView().setItemChecked(activatedPosition, false);
		} 
		else 
		{
			getListView().setItemChecked(position, true);
		}

		activatedPosition = position;
	}
	
	public int getSelectedIndex()
	{
		return this.activatedPosition;
	}
	
	public DrinkList getList()
	{
		DrinkListAdapter adapter = (DrinkListAdapter)this.getListAdapter();
		return adapter.getList();
	}
	
	public int getItemCount()
	{
		DrinkListAdapter adapter = (DrinkListAdapter)this.getListAdapter();
		return adapter.getCount();
	}
	public Drink getSelectedItem()
	{
		return this.getDrinkByPosition(this.activatedPosition);		
	}
	
	public Drink getDrinkByPosition(int position)
	{
		if (position == -1)
			return null;
		if (this.getListAdapter() == null)
			return null;
		
		return (Drink)this.getListAdapter().getItem(position);		
	}
}
