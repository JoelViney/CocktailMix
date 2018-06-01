package com.beachrife.cocktailmix.drinks;

import java.util.ArrayList;

public class DrinkIngredientList extends ArrayList<DrinkIngredient>
{
	private static final long serialVersionUID = 4345328471894647981L;

	public DrinkIngredientList()
	{
		
	}

	// Constructor used by the unit tests.
	public DrinkIngredientList(String...args)
	{
		for (int i = 0; i < args.length; i = i + 2)
		{
			String quantity = args[i];
			String name = args[i + 1];
			DrinkIngredient item = new DrinkIngredient(quantity, name);
			this.add(item);
		}	
	}

	public boolean existsByName(String ingredientName)
	{
		for(DrinkIngredient item : this)
		{
			if (item.getName().equals(ingredientName))
				return true;			
		}
		return false;
	}

}
