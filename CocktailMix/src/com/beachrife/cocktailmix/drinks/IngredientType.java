package com.beachrife.cocktailmix.drinks;


public enum IngredientType
{
	UNKNOWN,
	SPIRIT,
	LIQUEUR,
	MIXER,
	KITCHEN,
	GARNISH;

	// Could just use below but is processor heavy: return IngredientType.values()[value];

	// Could use this to cache the values, but then is memory heavy:
	// private static IngredientType[] enumValues = IngredientType.values();
	// return enumValues[value];

    public static IngredientType fromInteger(int value) 
    {
        switch(value) 
        {
        case 0: return IngredientType.UNKNOWN;
        case 1: return IngredientType.SPIRIT;
        case 2: return IngredientType.LIQUEUR;
        case 3: return IngredientType.MIXER;
        case 4: return IngredientType.KITCHEN;
        case 5: return IngredientType.GARNISH;
        }
        return IngredientType.UNKNOWN;
    }
}