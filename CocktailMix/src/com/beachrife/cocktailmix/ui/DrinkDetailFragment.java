package com.beachrife.cocktailmix.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.ApplicationSettings;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.datalayer.DataException;
import com.beachrife.cocktailmix.datalayer.DrinkFactory;
import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkIngredient;
import com.beachrife.cocktailmix.drinks.DrinkList;

/**
 * A fragment representing a single Drink detail screen. This fragment is either
 * contained in a {@link DrinkListActivity} in two-pane mode (on tablets) or a
 * {@link DrinkDetailActivity} on handsets.
 */
public class DrinkDetailFragment extends Fragment implements OnRatingBarChangeListener
{
	public static final String DRINK_NAME_ARGUMENT = "drink_name_argument"; // The fragment argument representing the item ID that this fragment represents.
	public static final String RETURN_RESULT_DIRTY = "dirty";

	private Drink drink; // The content this fragment is presenting.
	private boolean dirty;
	
	public interface RatingCallback  { void onRatingChange(); }
	private static RatingCallback dummyCallback = new RatingCallback() { @Override public void onRatingChange() { } }; 
	RatingCallback ratingCallback = dummyCallback;

	// Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon screen orientation changes).
	public DrinkDetailFragment() 
	{
		this.dirty = false;
	}
	
	public boolean getDirty()
	{
		return this.dirty;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Load the drink argument
		ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();
		String name = this.getArguments().getString(DRINK_NAME_ARGUMENT);		
		this.drink = context.getDrinks().findByName(name);
	}
	
	@Override
	public void onDestroy()
	{	
		// Please note: Fragment.onDestroy is not always called by the framework so don't depend on it.
		super.onDestroy();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.drink_detail_fragment, container, false);
		ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();

		if (this.drink != null) 
		{
			TextView titleTextView = (TextView)rootView.findViewById(R.id.titleTextView);
			TextView methodTextView = (TextView)rootView.findViewById(R.id.methodTextView);
			TextView ingredientsTextView = (TextView)rootView.findViewById(R.id.ingredientsTextView);
			ImageView imageView = (ImageView)rootView.findViewById(R.id.backgroundImageView);
			RatingBar ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
					
			titleTextView.setText(this.drink.getName());	
			methodTextView.setText(this.drink.getMethod());
			ingredientsTextView.setText(this.getIngredientsString(drink, context.getSettings()));
			float ratingFloat = (float)this.drink.getRating();
			ratingBar.setRating(ratingFloat);
			
			ratingBar.setOnRatingBarChangeListener(this);
			
			// Image
			imageView.setImageResource(this.drink.getImageResourceId());
		}

		return rootView;
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
	{
		try
		{
			ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();
			DrinkFactory factory = new DrinkFactory(context);
			DrinkList drinks = context.getDrinks();

			int ratingInt = (int)Math.round(rating);
			
			if (ratingInt != drink.getRating())
			{
				this.dirty = true;
				this.drink.setRating(ratingInt);
				
				if (this.ratingCallback != null)
				{
					// ConsoleHelper.writeLine("::: Triggering Callback...");
					this.ratingCallback.onRatingChange();
				}
			}
			
			factory.save(drinks);			
		} 
		catch (DataException e) 
		{
			e.printStackTrace();
			
			Toast toast = Toast.makeText(this.getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
	public void clearRating()
	{
		View rootView = this.getView();

		RatingBar ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
		if (ratingBar.getRating() != 0)
		{
			ratingBar.setRating(0);
			this.dirty = true;
		}
		
		Toast toast = Toast.makeText(this.getActivity().getApplicationContext(), this.getString(R.string.drink_detail_rating_cleared_message), Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void updateItem()
	{
		// Right now the only think that will be updated is the ingredients.
		ApplicationEx context = (ApplicationEx)this.getActivity().getApplicationContext();
		View rootView = this.getView();

		TextView ingredientsTextView = (TextView)rootView.findViewById(R.id.ingredientsTextView);
		ingredientsTextView.setText(this.getIngredientsString(drink, context.getSettings()));
	}

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof RatingCallback)) 
		{
			this.ratingCallback = dummyCallback; // The container doesn't implement the callback
			// throw new IllegalStateException("Activity must implement the DrinkDetailFragment's RatingCallback.");
		}
		else
		{
			this.ratingCallback = (RatingCallback)activity;
		}
	}
	
	@Override
	public void onDetach() 
	{
		super.onDetach(); // Reset the active callbacks interface to the dummy implementation.
		this.ratingCallback = dummyCallback;
	}

	
	public String getIngredientsString(Drink drink, ApplicationSettings settings)
	{
		StringBuilder builder = new StringBuilder();
		
		for (DrinkIngredient ingredient : drink.getIngredientList())
		{
			if (builder.length() == 0)
			{
				String quantityText =  ingredient.getQuantityText(settings);
				if (quantityText.equals(""))
					builder.append(ingredient.getName());
				else
					builder.append(String.format("\r\n%s %s", quantityText, ingredient.getName()));
			}
			else
			{
				String quantityText =  ingredient.getQuantityText(settings);
				if (quantityText.equals(""))
					builder.append(String.format("\r\n%s", ingredient.getName()));
				else
					builder.append(String.format("\r\n%s %s", quantityText, ingredient.getName()));
			}
		}
		
		return builder.toString();
	}
}
