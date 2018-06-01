package com.beachrife.cocktailmix.drinks;

import java.math.BigDecimal;



public class Ingredient 
{
	public static int NEW_ID = -1;
	private int id;
	private String name;
	private IngredientType ingredientType;
	private String description;
	private BigDecimal cost; 
	private int imageEnabledId;
	private int imageDisabledId;

	private boolean barHasIngredient;
	
	public Ingredient()
	{
		this.id = NEW_ID;
		this.name = null;
		this.ingredientType = IngredientType.UNKNOWN;
		this.description = null;
		this.cost = new BigDecimal("0.00");
		this.imageEnabledId = 0;
		this.imageDisabledId = 0;
		this.barHasIngredient = false;
	}

	public Ingredient(String name)
	{
		this.id = NEW_ID;
		this.name = name;
		this.ingredientType = IngredientType.UNKNOWN;
		this.description = null;
		this.cost = new BigDecimal("0.00");
		this.imageEnabledId = 0;
		this.imageDisabledId = 0;
		this.barHasIngredient = false;
	}	
	
	
	public Ingredient(String name, boolean barHasIngredient)
	{
		this.id = NEW_ID;
		this.name = name;
		this.ingredientType = IngredientType.UNKNOWN;
		this.description = null;
		this.cost = new BigDecimal("0.00");
		this.imageEnabledId = 0;
		this.imageDisabledId = 0;
		this.barHasIngredient = barHasIngredient;
	}	
	
	public Ingredient(String name, IngredientType ingredientType)
	{
		this.id = NEW_ID;
		this.name = name;
		this.ingredientType = ingredientType;
		this.description = null;
		this.cost = new BigDecimal("0.00");
		this.imageEnabledId = 0;
		this.imageDisabledId = 0;
		this.barHasIngredient = false;
	}

	public Ingredient(String name, IngredientType ingredientType, BigDecimal cost, boolean barHasIngredient)
	{
		this.id = NEW_ID;
		this.name = name;
		this.ingredientType = ingredientType;
		this.description = null;
		this.cost = cost;
		this.imageEnabledId = 0;
		this.imageDisabledId = 0;
		this.barHasIngredient = barHasIngredient;
	}

	public int getId() { return this.id; }
	public void setId(int value) { this.id = value; }
		
	public String getName() { return this.name; }
	public void setName(String value) { this.name = value; }

	public IngredientType getIngredientType() { return this.ingredientType; }
	public void  setIngredientType(IngredientType value) { this.ingredientType = value; }
	
	public String getDescription() { return this.description; }
	public void setDescription(String value) { this.description = value; }

	public BigDecimal getCost() { return this.cost; }
	public void setCost(BigDecimal value) { this.cost = value; }
	
	public int getImageEnabledId() { return this.imageEnabledId; }
	public void setImageEnabledId(int value) { this.imageEnabledId = value; }

	public int getImageDisabledId() { return imageDisabledId; }
	public void setImageDisabledId(int value) { this.imageDisabledId = value; }

	public boolean getBarHasIngredient() { return this.barHasIngredient; }
	public void setBarHasIngredient(boolean value) { this.barHasIngredient = value; }
		
	public int getImageId()
	{
		if (this.barHasIngredient)
			return this.imageEnabledId;
		else
			return this.imageDisabledId;		
	}
	

	// WARNING: This created disconnected Ingredient objects..
	// e.g. ones not cached in the application.
	public Ingredient DeepCopy()
	{
		Ingredient item = new Ingredient();
		
		item.id = id;
		item.name = name;
		item.ingredientType = this.ingredientType;
		item.description = this.description;
		item.cost = new BigDecimal(this.cost.toString());
		item.imageEnabledId = this.imageEnabledId;
		item.imageDisabledId = this.imageDisabledId;
		item.barHasIngredient = this.barHasIngredient;
		
		return item;
	}
}
