package com.beachrife.cocktailmix.ui;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkIngredient;
import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.DrinkViewMode;
import com.beachrife.cocktailmix.drinks.IngredientList;

// Extends the default Drink adapter used by the DrinkListFragment
public class DrinkListAdapter extends ArrayAdapter<Drink> 
{
	private final ApplicationEx context;
	private DrinkList drinks;
	
	public DrinkListAdapter(Application context, DrinkViewMode drinkViewMode, String ingredientName) 
	{
		super(context, R.layout.drink_list_item_layout);

		this.context = (ApplicationEx)context;
	}
	
	// Returns a list of Drinks in the list.
	public DrinkList getList()
	{
		return this.drinks;		
	}
	
	public void setList(DrinkList drinks)
	{
		this.drinks = drinks;

		super.clear();
		super.addAll(drinks);

		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// Displays the a drink in the drink list based on the provided position.
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// TODO: For some reason this totally spat chunks while testing once...
		View rowView = inflater.inflate(R.layout.drink_list_item_layout, parent, false);
		
		if (position >= this.drinks.size())
			return rowView;
		
		Drink drink = this.drinks.get(position);

		// Image
		{
			ImageView imageView = (ImageView)rowView.findViewById(R.id.drink_image_view);
			imageView.setImageResource(drink.getImageResourceId());
		}
		
		// Text
		{
			IngredientList allIngredients = context.getIngredients();
			
			TextView textView = (TextView)rowView.findViewById(R.id.drink_name_text_view);
			textView.setText(drink.getName());

			if (!drink.barHasIngredients(allIngredients))
				textView.setTextColor(Color.GRAY);
		}
		
		// This should possibly be one image or a read-only bar.
		{
			ImageView ratingImageView1 = (ImageView)rowView.findViewById(R.id.ratingImageView1);
			ImageView ratingImageView2 = (ImageView)rowView.findViewById(R.id.ratingImageView2);
			ImageView ratingImageView3 = (ImageView)rowView.findViewById(R.id.ratingImageView3);
			ImageView ratingImageView4 = (ImageView)rowView.findViewById(R.id.ratingImageView4);
			ImageView ratingImageView5 = (ImageView)rowView.findViewById(R.id.ratingImageView5);
			if (drink.getRating() < 5) ratingImageView5.setVisibility(View.INVISIBLE);
			if (drink.getRating() < 4) ratingImageView4.setVisibility(View.INVISIBLE);
			if (drink.getRating() < 3) ratingImageView3.setVisibility(View.INVISIBLE);
			if (drink.getRating() < 2) ratingImageView2.setVisibility(View.INVISIBLE);
			if (drink.getRating() < 1) ratingImageView1.setVisibility(View.INVISIBLE);
		}
		
		// Blurb
		{
			TextView textView = (TextView)rowView.findViewById(R.id.drink_blerb_text_view);
			
			String text = "";
			
			IngredientList allIngredients = context.getIngredients();
			
			for(DrinkIngredient part : drink.getIngredientList())
			{
				String segment = null;
				if (allIngredients.hasIngredient(part))
					segment = String.format("%s", part.getName());
				else
					segment = String.format("<font color='grey'>%s</font>", part.getName());
				
				if (text == "")
					text = segment;
				else
					text += ", " + segment;
				
			}
			textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
		}
		
		return rowView;
	}
	
}
