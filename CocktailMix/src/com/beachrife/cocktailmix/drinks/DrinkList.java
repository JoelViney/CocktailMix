package com.beachrife.cocktailmix.drinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.beachrife.cocktailmix.ApplicationEx;

public class DrinkList extends ArrayList<Drink> 
{
	private static final long serialVersionUID = 6044956380157401828L;

	public DrinkList()
	{

	}
	
	public DrinkList(DrinkList drinks)
	{
		for(Drink drink : drinks)
		{
			this.add(drink);
		}
	}

	public DrinkList(Drink...args)
	{
		for(Drink drink : args)
		{
			this.add(drink);
		}
	}
	
	
	public static void generateIngredientIds(DrinkList drinks, IngredientList ingredients)
	{
		for(Drink drink : drinks)
		{
			for(DrinkIngredient part : drink.getIngredientList())
			{
				Ingredient ingredient = ingredients.findByName(part.getName());
				if (ingredient != null)
				{
					part.setIngredientId(ingredient.getId());
				}
			}
		}
	}
	
	
	
	// Returns all of the non intersecting elements from this array.
	public DrinkList NonIntersect(DrinkList list)
	{		
		DrinkList result = new DrinkList();
		
		for(Drink drink : this)
		{					
			if (!list.contains(drink))
			{
				result.add(drink);
			}
		}
		
		return result;
	}
	
	public Drink[] toArray()
	{
		Drink[] result = this.toArray(new Drink[this.size()]);
		return result;
	}

	
	public Drink findByName(String name)
	{
		for (Drink item : this)
	        if (item.getName().equals(name))
	        	return item;
		
		return null;
	}
	
	public boolean hasDrink(String name)
	{
		Drink item = this.findByName(name);
		
		return (item != null);
	}

	public DrinkList getFilteredList(DrinkViewMode drinkViewMode, ApplicationEx context, String ingredientName)
	{
		DrinkList list = null;
		IngredientList ingredients = context.getIngredients();
		switch (drinkViewMode)
		{
			case VIEW_ALL: 			
			{
				// ConsoleHelper.writeLine("Apply Filter ALL");
				list = new DrinkList(this); 
			}
			break;
			case VIEW_MY_COCKTAILS: 
			{
				list = this.getBarMenu(ingredients);
			}
			break;
			case VIEW_BY_RATINGS:
			{
				// ConsoleHelper.writeLine("Apply Filter RATING");
				list = new DrinkList(this); 
				list.sortByRating();
			}
			break;
			case VIEW_BY_INGREDIENT:
			{	
				list = new DrinkList(); 
				if (ingredientName != null)
				{
					list = this.getDrinksWith(ingredientName);
				}
			}
			break;
			case VIEW_BY_BASKET:
			{
				list = new DrinkList();
			}
			break;
			default:
			{
				list = new DrinkList(this); 
			}
			break;
		}
		
		return list;
	}
	
	
	
	public void sortByName()
	{
		Collections.sort(this, new Comparator<Drink>() 
			{
	    		public int compare(Drink drink1, Drink drink2) 
	    		{
	    			return drink1.getName().compareTo(drink2.getName());
	    		}
			}); 
	}

	public void sortByRating()
	{
		// Sort by Rating and then name.
		Collections.sort(this, new Comparator<Drink>() 
			{
	    		public int compare(Drink drink1, Drink drink2) 
	    		{
	    			if (drink1.getRating() > drink2.getRating())
	    				return -1;
	    			else if (drink1.getRating() == drink2.getRating())
		    			return drink1.getName().compareTo(drink2.getName());
	    			else
	    				return 1;
	    		}
			}); 
	}
	

	public DrinkList getBarMenu(IngredientList ingredients) 
	{
		DrinkList menu = new DrinkList();
		
		for(Drink item : this)
		{
			boolean hasIngredients = true;
			for(DrinkIngredient part : item.getIngredientList())
			{
				if (!ingredients.getBarHasIngredient(part))
				{
					hasIngredients = false;
					continue;
				}
			}
			
			if (hasIngredients)
			{
				menu.add(item);
			}
		}
		
		return menu;
	}

	public DrinkList getBarMenu(IngredientList ingredients, Ingredient newIngredient) 
	{
		DrinkList menu = new DrinkList();
		
		for(Drink item : this)
		{
			boolean hasIngredients = true;
			for(DrinkIngredient part : item.getIngredientList())
			{
				if (!ingredients.getBarHasIngredient(part))
				{
					if (newIngredient == null || newIngredient.getId() != part.getIngredientId())
					{
						hasIngredients = false;
						continue;
					}
				}
			}
			
			if (hasIngredients)
			{
				menu.add(item);
			}
		}
		
		return menu;
	}
	
	public DrinkList getDrinksWith(String name) 
	{
		DrinkList list = new DrinkList();
		
		for(Drink item : this)
		{
			boolean hasIngredients = false;
			for(DrinkIngredient ingredient : item.getIngredientList())
			{
				if (ingredient.getName().equals(name))
				{
					hasIngredients = true;
				}
			}
			
			if (hasIngredients)
			{
				list.add(item);
			}
		}
		return list;
	}
}
