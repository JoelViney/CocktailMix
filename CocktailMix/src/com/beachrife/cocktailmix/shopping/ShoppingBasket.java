package com.beachrife.cocktailmix.shopping;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;

//
// This class is used to hold items that can be purchased which is used for comparison and display.
//
public class ShoppingBasket 
{
	private IngredientList ingredients;
	private DrinkList drinks;
	
	public ShoppingBasket()
	{
		this.ingredients = new IngredientList();
		this.drinks = new DrinkList();
	}

	public ShoppingBasket(DrinkList drinks, Ingredient ingredient1)
	{
		this.drinks = drinks;
		this.ingredients = new IngredientList(ingredient1);
	}
	
	public ShoppingBasket(DrinkList drinks, Ingredient...args)
	{
		this.drinks = drinks;
		this.ingredients = new IngredientList(args);
	}
	
	public IngredientList getIngredients() { return this.ingredients;	}
	public void setIngredients(IngredientList value) { this.ingredients = value; }

	public DrinkList getDrinks() { return this.drinks;	}
	public void setDrinks(DrinkList value) { this.drinks = value; }

	public BigDecimal getTotalCost() 
	{
		BigDecimal cost = new BigDecimal(0);
		
		for(Ingredient item : ingredients)
		{
			cost = cost.add(item.getCost());
		}
		
		return cost;	
	}
	
	public BigDecimal getCostPerDrink()
	{
		BigDecimal cost = this.getTotalCost();
		BigDecimal quantity = new BigDecimal(this.drinks.size());
		
		// int costCents = cost.intValue();
		// int quantityCents = quantity.intValue();
		
		if (cost.equals(0))
			return new BigDecimal(0);
		
		if (quantity.equals(0))
			return new BigDecimal(0);

		//if (quantity.equals(1))
		//	return new BigDecimal(cost.toString());
		
		BigDecimal result = null;
		
		try
		{
			result = cost.divide(quantity, 2, RoundingMode.HALF_UP);
		}
		catch(Exception ex)
		{
			return new BigDecimal(0); 
		}
		
		return result;
	}
}
