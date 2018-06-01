package com.beachrife.cocktailmix.drinks;

import android.annotation.SuppressLint;
import com.beachrife.cocktailmix.ApplicationSettings;

public class DrinkIngredient 
{
	private String quantity;
	private String name;
	private int ingredientId;
	
	public DrinkIngredient() 
	{
		this.quantity = null;
		this.name = null;
		this.ingredientId = Ingredient.NEW_ID;
	}

	public DrinkIngredient(String quantity, String name) 
	{
		this.quantity = quantity;
		this.name = name;
		this.ingredientId = Ingredient.NEW_ID;
	}

	/**
	 * Describes the portion of the ingredient that goes into the drink in a string.
	 * If it is an numeric value then it can be converted between ml, cl and oz.
	 * 
	 * By default in the datastore the integer quantity is stored as ml.
	 */
	public String getQuantity() { return this.quantity; }
	public void setQuantity(String value) { this.quantity = value; }

	/**
	 * The name that is displayed when shown in a list of ingredients for a drink.
	 */
	public String getName() { return this.name; }
	public void setName(String value) { this.name = value; }

	public int getIngredientId() { return this.ingredientId; }
	public void setIngredientId(int value) { this.ingredientId = value; }
	
	private boolean isQuantityNumeric()
	{
		if (this.quantity == null | this.quantity.equals(""))
			return false;
		
		// Checks if the string is numeric. Note: Negative numbers will fail this test.
	    for (char c : this.quantity.toCharArray())
	    {
	        if (!Character.isDigit(c)) 
	        	return false;
	    }
	    
	    return true;
	}
	
	// TODO: This really doesn't belong in the object model.
	@SuppressLint("DefaultLocale")
	public String getQuantityText(ApplicationSettings settings)
	{
		if (this.isQuantityNumeric())
		{
			switch (settings.getMeasurementType())
			{
				case MEASUREMENT_ML: return String.format("%s ml", this.quantity);
				case MEASUREMENT_OZ: 
				{
					int val = Integer.parseInt(this.quantity);
					double dec = ((double)val) / 30;

					switch(val)
					{
					case 5: return "1/6 oz"; 
					case 10: return "1/3 oz"; 
					case 15: return "1/2 oz"; 
					case 20: return "2/3 oz"; 
					case 25: return "5/6 oz"; 
					case 30: return "1 oz"; 
					case 35: return "1 1/6 oz"; 
					case 40: return "1 1/3 oz"; 
					case 45: return "1 1/2 oz"; 
					case 50: return "1 2/3 oz"; 
					case 55: return "1 5/6 oz"; 
					case 60: return "2 oz"; 
					default:
						if (dec == Math.floor(dec))
							return String.format("%.0f oz", dec);
						else
							return String.format("%.2f oz", dec);
					}
				}
				default: // CL
				{
					int val = Integer.parseInt(this.quantity);
					double dec = ((double)val) / 10;
					
					if (dec == Math.floor(dec))
						return String.format("%.0f cl", dec);
					else
						return String.format("%s cl", dec);
				}
			}
		}
		else
		{
			return this.quantity;
		}		
	}
}
