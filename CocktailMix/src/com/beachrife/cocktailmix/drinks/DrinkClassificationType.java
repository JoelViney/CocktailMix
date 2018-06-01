package com.beachrife.cocktailmix.drinks;

public enum DrinkClassificationType
{
	UNKNOWN,
	AFTER_DINNER_COCKTAIL,
	BEFORE_DINNER_COCKTAIL,
	ALL_DAY_COCKTAIL,
	LONG_DRINK,
	SPARKLING_COCKTAIL,
	HOT_DRINK;


    public static DrinkClassificationType fromInteger(int value) 
    {
        switch(value) 
        {
        case 0: return DrinkClassificationType.UNKNOWN;
        case 1: return DrinkClassificationType.AFTER_DINNER_COCKTAIL;
        case 2: return DrinkClassificationType.BEFORE_DINNER_COCKTAIL;
        case 3: return DrinkClassificationType.ALL_DAY_COCKTAIL;
        case 4: return DrinkClassificationType.LONG_DRINK;
        case 5: return DrinkClassificationType.SPARKLING_COCKTAIL;
        case 6: return DrinkClassificationType.HOT_DRINK;
        default: return DrinkClassificationType.UNKNOWN;
        }
    }
}