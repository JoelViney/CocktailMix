package com.beachrife.cocktailmix.drinks;

public enum DrinkViewMode 
{
	UNKNOWN,
	VIEW_ALL,
	VIEW_MY_COCKTAILS,
	VIEW_BY_RATINGS,
	VIEW_BY_INGREDIENT,
	VIEW_BY_BASKET;

    public static DrinkViewMode fromInteger(int value) 
    {
        switch(value) 
        {
        case 0: return DrinkViewMode.VIEW_ALL;
        case 1: return DrinkViewMode.VIEW_MY_COCKTAILS;
        case 2: return DrinkViewMode.VIEW_BY_RATINGS;
        case 3: return DrinkViewMode.VIEW_BY_INGREDIENT;
        case 4: return DrinkViewMode.VIEW_BY_BASKET;
        default: return DrinkViewMode.UNKNOWN;
        }
    }
    
    public static int toInt(DrinkViewMode drinkViewMode)
    {
    	switch(drinkViewMode)
    	{
        case VIEW_ALL: return 0;
        case VIEW_MY_COCKTAILS: return 1;
        case VIEW_BY_RATINGS: return 2;
        case VIEW_BY_INGREDIENT: return 3;
        case VIEW_BY_BASKET: return 4;
        default: return 0;
    	}
    }

}
