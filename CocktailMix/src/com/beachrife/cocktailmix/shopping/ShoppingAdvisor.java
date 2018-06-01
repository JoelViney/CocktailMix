package com.beachrife.cocktailmix.shopping;

import java.math.BigDecimal;

import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkIngredient;
import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;
import com.beachrife.cocktailmix.drinks.IngredientType;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;

/**
 * Returns advise on what the user should purchase next so that they can make the most drinks.
 * 
 * It needs to balance out the advise based on:
 * * Cost per drink
 * * Total number of drinks created.
 * 
 * */
public class ShoppingAdvisor 
{
	private DrinkList availableDrinks;
	private DrinkList drinks;
	private IngredientList allIngredients;
	
	public ShoppingAdvisor(DrinkList drinks, IngredientList ingredients) 
	{
		this.drinks = drinks;
		this.allIngredients = ingredients;
		
		this.availableDrinks = this.drinks.getBarMenu(ingredients);
	}
	
	
	public ShoppingBasket getBestBasket()
	{
		long start = System.currentTimeMillis();
		
		// 
		// Multiplier. 
		// 
		// Select a spirit
		// Find best single Spirit
		// Find best single Liqueur
		// Find best single Mixer
		// Find best single Kitchen item... modifier +
		
		// 
		// 
		// 
		
		ShoppingBasket basket = this.getBestBasketInternal(IngredientType.UNKNOWN);

		long end = System.currentTimeMillis();
		double time = ((double)(end - start)) / 1000;
		ConsoleHelper.writeLine("Execution Time: %f", time);
		
		return basket;
	}
	
	public ShoppingBasket getBestBasketInternal(IngredientType ingredientType)
	{
		IngredientList unstocked = this.allIngredients.getUnstockedIngredients();
		Ingredient bestIngredient = null;
		int bestDrinkCount = 0;
		BigDecimal bestDrinkCost = new BigDecimal("999.00");

		if (unstocked.size() == 0)
			return null;
		
		for(Ingredient ingredient : unstocked)
		{
			if (ingredientType == IngredientType.UNKNOWN || ingredient.getIngredientType() == ingredientType)
			{
				int newDrinkCount = 0;
				
				for(Drink drink : drinks)
				{
					boolean partMatches = false;
					boolean hasIngredients = true;
					for(DrinkIngredient part : drink.getIngredientList())
					{
						if (part.getIngredientId() == ingredient.getId())
						{
							partMatches = true;
						}
						else if (!this.allIngredients.getBarHasIngredient(part))
						{
							hasIngredients = false;
							continue;
						}
					}
					
					if (partMatches && hasIngredients)
					{
						newDrinkCount++;
					}
				}
				
				ConsoleHelper.writeLine("Ingredient[%s] Drinks[%d] Ingredient Cost[%s]", ingredient.getName(), newDrinkCount, ingredient.getCost());

				if (newDrinkCount > 0 && newDrinkCount >= bestDrinkCount)
				{
					if (ingredient.getCost().compareTo(bestDrinkCost) < 0)
					{
						bestIngredient = ingredient;
						bestDrinkCount = newDrinkCount;
						bestDrinkCost = ingredient.getCost();
					}
				}
			}
			
		}
		
		// Build the best basket...
		ShoppingBasket bestBasket = null;
		
		if (bestIngredient != null)
		{
			bestBasket = new ShoppingBasket();
			bestBasket.getIngredients().add(bestIngredient);

			DrinkList tempList = this.drinks.getBarMenu(this.allIngredients, bestIngredient);	
			DrinkList newDrinks = tempList.NonIntersect(this.availableDrinks);
			
			bestBasket.setDrinks(newDrinks);
		}

		return bestBasket;
	}

	public ShoppingBasket getBestBasketInternal2()
	{
		ShoppingBasket bestBasket = null;
		IngredientList list = this.allIngredients.DeepCopy();
		IngredientList unstocked = list.getUnstockedIngredients();
		
		if (unstocked.size() == 0)
			return null;
		
		// Check for single / double combinations
		for(int a = 0; a < unstocked.size() ; a = a + 1) 
		{
			for(int b = a ; b < unstocked.size(); b = b + 1)
			{
				Ingredient item1 = unstocked.get(a);
				Ingredient item2 = null;
				
				if (item1.getIngredientType() == IngredientType.SPIRIT)
				{
					item1.setBarHasIngredient(true);
	
					//ConsoleHelper.writeLine("x))) %d %d", a, b);
					
					if (a != b)
					{	
						item2 = unstocked.get(b);
						item2.setBarHasIngredient(true);
					}
	
					// Get the drink list & remove any that the bar already had
					DrinkList tempList = this.drinks.getBarMenu(list);	
					DrinkList newDrinks = tempList.NonIntersect(this.availableDrinks);
					
					if (newDrinks.size() > 0)
					{
						ShoppingBasket basket = new ShoppingBasket(newDrinks, item1, item2);
						
						//ConsoleHelper.writeLine("Ingredients[%d] Drinks[%d] Cost Per Drink[%s]", basket.getIngredients().size(), basket.getDrinks().size(), NumberFormat.getCurrencyInstance().format(basket.getCostPerDrink()));
						
						if (bestBasket == null)
						{
							bestBasket = basket;					
						}
						else
						{
							if (bestBasket.getCostPerDrink().compareTo(basket.getCostPerDrink()) > 0)
							{
								bestBasket = basket;
							}
						}
					}
					
					// Reset...
					item1.setBarHasIngredient(false);
					
					if (item2 != null)
					{
						item2.setBarHasIngredient(false);
					}
				}
			} // b
		} // a
		
		// This returns the cached item, otherwise we would be using a DeepCopy item.
		if (bestBasket != null)
		{
			IngredientList newList = new IngredientList();
			for(Ingredient item : bestBasket.getIngredients())
			{
				Ingredient newItem = this.allIngredients.findById(item.getId());
				newList.add(newItem);				
			}
			bestBasket.setIngredients(newList);
		}
		
		return bestBasket;
	}

	public ShoppingBasket getBestBasketInternal()
	{
		ShoppingBasketList baskets = new ShoppingBasketList();
		
		IngredientList list = this.allIngredients.DeepCopy();
		IngredientList unstocked = list.getUnstockedIngredients();
		
		if (unstocked.size() == 0)
			return null;
		
		// Get the 10 best single combinations...
		for(int i = 0; i < unstocked.size() ; i = i + 1) 
		{
			Ingredient item = unstocked.get(i);
			// First item needs to be a spirit or a liqueur?
			// if (item.getIngredientType() == IngredientType.SPIRIT | item.getIngredientType() == IngredientType.LIQUEUR)
			{
			item.setBarHasIngredient(true);

			// Get the drink list & remove any that the bar already had
			DrinkList tempList = this.drinks.getBarMenu(list);	
			DrinkList newDrinks = tempList.NonIntersect(this.availableDrinks);
			
			if (newDrinks.size() > 0)
			{
				ShoppingBasket basket = new ShoppingBasket(newDrinks, item);
				baskets.add(basket);
			}
			
			// Reset...
			item.setBarHasIngredient(false);
			}
		} // a
		
		baskets.sort();
		
		while(baskets.size() > 10)
		{
			baskets.remove(1);
		}
		
		// This returns the cached item, otherwise we would be using a DeepCopy item.
		for(ShoppingBasket basket : baskets)
		{
			if (basket != null)
			{
				IngredientList newList = new IngredientList();
				for(Ingredient item : basket.getIngredients())
				{
					Ingredient newItem = this.allIngredients.findById(item.getId());
					newList.add(newItem);				
				}
				basket.setIngredients(newList);
			}
		}
		
		return baskets.get(0);
	}
	

}
