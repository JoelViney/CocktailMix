package com.beachrife.cocktailmix.drinks;


// Represents a single drink/cocktail which is either a cocktail or a shooter etc...
public class Drink
{	
	public static final int NULL_IMAGE = -1;
	private static final int NOT_RATED = 0;
	
	private String 					name;
	private DrinkClassificationType classification;
	private String 					method;
	private int    					imageResourceId;	
	private DrinkIngredientList 	ingredientList;
	
	private int 					rating;
	

	
	public Drink() 
	{
		this.name = null;
		this.classification = DrinkClassificationType.UNKNOWN;
		this.method = null;
		this.imageResourceId = NULL_IMAGE;		
		this.ingredientList = new DrinkIngredientList();
		
		this.rating = NOT_RATED;
	}

	public Drink(String name)
	{
		this.name = name;
		this.classification = DrinkClassificationType.UNKNOWN;
		this.method = null;
		this.imageResourceId = NULL_IMAGE;		
		this.ingredientList = new DrinkIngredientList();
		
		this.rating = NOT_RATED;
	}
	
	public Drink(String name, int rating)
	{
		this.name = name;
		this.classification = DrinkClassificationType.UNKNOWN;
		this.method = null;
		this.imageResourceId = NULL_IMAGE;		
		this.ingredientList = new DrinkIngredientList();
		
		this.rating = rating;
	}

	public Drink(String name, DrinkIngredientList ingredients)
	{
		this.name = name;
		this.classification = DrinkClassificationType.UNKNOWN;
		this.method = null;
		this.imageResourceId = NULL_IMAGE;		
		this.ingredientList = ingredients;
		
		this.rating = NOT_RATED;
	}
	
	
	
	public String getName() { return this.name;	}
	public void setName(String value) { this.name = value; }

	public DrinkClassificationType getClassification() { return this.classification;	}
	public void setClassification(DrinkClassificationType value) { this.classification = value; }
	
	public String getMethod() { return method; }
	public void setMethod(String value) { this.method = value; }

	public int getImageResourceId() { return imageResourceId; }
	public void setImageResourceId(int value) { this.imageResourceId = value; }

    public int getRating() { return rating; }
	public void setRating(int value) { this.rating = value; }
	
	public DrinkIngredientList getIngredientList() { return this.ingredientList; }
	
	
	// This one is a bit funky.
	public boolean barHasIngredients(IngredientList ingredients)
	{				
		for(DrinkIngredient part : this.getIngredientList())
		{
			if (!ingredients.hasIngredient(part))
			{
				return false;
			}
		}
		return true;
	}
	
	
}
