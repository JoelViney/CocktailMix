package com.beachrife.cocktailmix.drinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class IngredientList extends ArrayList<Ingredient> 
{
	private static final long serialVersionUID = 6044956380157401828L;

	// Default constructor.
	public IngredientList()
	{

	}

	// Constructor used by unit tests.
	public IngredientList(Ingredient...args)
	{
		for(Ingredient item : args)
		{
			if (item != null)
			{
				this.add(item);
			}
		}
	}
	
	

	// Generates the identifiers for the ingredients.
	public static void generateIds(IngredientList list)
	{
		for(int i = 0 ; i < list.size() ; i++)
		{
			list.get(i).setId(i);
		}		
	}
	
	
	
	public void sortByName()
	{
		Collections.sort(this, new Comparator<Ingredient>() 
			{
	    		public int compare(Ingredient item1, Ingredient item2) 
	    		{
	    			return item1.getName().compareTo(item2.getName());
	    		}
			}); 
	}
	
	

	public Ingredient findById(int id)
	{
		if (id == Ingredient.NEW_ID)
			return null;
		
		Ingredient ingredient = this.get(id);
		
		if (ingredient.getId() == id)
			return ingredient;

		for (Ingredient item : this)
			if (item.getId() == id)
	        	return item;
		
		return null;
	}
	// Only used when resolving names from parameters.
	public Ingredient findByName(String name)
	{
		for (Ingredient item : this)
	        if (item.getName().equals(name))
	        	return item;
		
		return null;
	}

	public boolean getBarHasIngredient(int id)
	{
		Ingredient ingredient = this.findById(id);
		
		if (ingredient == null)
			return false;
		
		return ingredient.getBarHasIngredient();
	}
	public boolean getBarHasIngredient(DrinkIngredient part)
	{
		Ingredient ingredient = this.findById(part.getIngredientId());
		
		if (ingredient == null)
			return false;
		
		return ingredient.getBarHasIngredient();
	}
	
	public boolean hasIngredient(DrinkIngredient part) 
	{
		for (Ingredient item : this)
		{
			if (item.getId() == part.getIngredientId())
				return true;
		}
		
		return false;
	}
	
	public IngredientList getByIngredientType(IngredientType ingredientType)
	{
		IngredientList list = new IngredientList();
		
		for(Ingredient item : this)
		{
			if (item.getIngredientType() == ingredientType)
			{
				list.add(item);
			}			
		}
		
		return list;
	}

	// Returns a list of all ingredients on flagged as not in stock.
	public IngredientList getUnstockedIngredients()
	{
		IngredientList list = new IngredientList();
		
		for(Ingredient item : this)
		{
			if (!item.getBarHasIngredient())
			{
				list.add(item);
			}			
		}
		
		return list;
	}
	
	// WARNING: This created disconnected Ingredient objects..
	// e.g. ones not cached in the application.
	public IngredientList DeepCopy()
	{
		IngredientList list = new IngredientList();
		
		for(Ingredient item : this)
		{
			list.add(item.DeepCopy());
		}
		
		return list;
	}

}
