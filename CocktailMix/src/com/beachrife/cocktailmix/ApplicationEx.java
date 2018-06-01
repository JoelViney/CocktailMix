package com.beachrife.cocktailmix;

import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.IngredientList;

import android.app.Application;

// This is a subclass of android.app.Application and specified in the application tag in the manifest.
// Android automatically creates an instance of that class and makes it available for the entire application.
public class ApplicationEx extends Application 
{
	private DrinkList drinks;
	private IngredientList ingredients;
	private ApplicationSettings settings;
	
	private String selectedDrinkName;
	
	public ApplicationEx()
	{
		this.drinks = null;
		this.ingredients = null;
		this.settings = null;
		this.selectedDrinkName = null;
	}

	
	public DrinkList getDrinks() { return drinks; }
	public void setDrinks(DrinkList value) { this.drinks = value; }

	public IngredientList getIngredients() { return ingredients; }
	public void setIngredients(IngredientList value) { this.ingredients = value; }

	public ApplicationSettings getSettings() { return settings; }
	public void setSettings(ApplicationSettings value) { this.settings = value; }
	
	public String getSelectedDrinkName() { return selectedDrinkName; }
	public void setSelectedDrinkName(String value) { this.selectedDrinkName = value; }
	
}
