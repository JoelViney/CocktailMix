package com.beachrife.cocktailmix.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.beachrife.cocktailmix.ApplicationEx;
import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;

public class MyBarItemAdapter extends ArrayAdapter<Ingredient>
{
	public static String IMAGE_VIEW = "IMAGE_VIEW";
	public static String TOGGLE_BUTTON = "TOGGLE_BUTTON";
	public static String CHECK_BOX = "CHECK_BOX";
	
	private final ApplicationEx context;
	private final IngredientList ingredients;
	private final OnClickListener parentListener;
	
	public MyBarItemAdapter(ApplicationEx context, IngredientList ingredients, OnClickListener parentListener) 
	{
		super(context, R.layout.my_bar_item_layout, ingredients);
		this.context = context;
		this.ingredients = ingredients;
		this.parentListener = parentListener;
		
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Ingredient item = this.ingredients.get(position);

		View parentView = inflater.inflate(R.layout.my_bar_item_layout, parent, false);

		ImageView imageView = (ImageView)parentView.findViewById(R.id.barItemImage);
		imageView.setTag(IMAGE_VIEW + item.getName());
		imageView.setTag(R.string.tag_ingredient, item.getName());
		imageView.setImageResource(item.getImageId());
		imageView.setOnClickListener(this.parentListener);

		/*
		CheckBox checkBox = (CheckBox)parentView.findViewById(R.id.barHasIngredientCheckBox);
		checkBox.setTag(CHECK_BOX + item.getName());
		checkBox.setTag(R.string.tag_ingredient, item.getName());
		checkBox.setChecked(item.getBarHasIngredient());
		checkBox.setOnClickListener(this.parentListener);
		checkBox.setText(item.getName());
		*/
		
		ToggleButton toggleButton = (ToggleButton)parentView.findViewById(R.id.barItemToggleButton);
		toggleButton.setTag(TOGGLE_BUTTON + item.getName());
		toggleButton.setTag(R.string.tag_ingredient, item.getName());
		toggleButton.setTextOn(item.getName());
		toggleButton.setTextOff(item.getName());
		toggleButton.setChecked(item.getBarHasIngredient());
		toggleButton.setOnClickListener(this.parentListener);

		return parentView;
	}
	
	
}