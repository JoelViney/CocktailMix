package com.beachrife.cocktailmix.test;


import junit.framework.TestCase;

import org.junit.Test;

import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkIngredientList;
import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;

public class DrinkTest extends TestCase 
{

	@Test
	public void testSortByName() 
	{
		DrinkList list = new DrinkList();
		list.add(new Drink("Martini"));
		list.add(new Drink("Caipiroska"));
		list.add(new Drink("Gin and Tonic"));
		
		list.sortByName();
		
		assertEquals("Caipiroska", list.get(0).getName());
		assertEquals("Gin and Tonic", list.get(1).getName());
		assertEquals("Martini", list.get(2).getName());
	}
	
	@Test
	public void testSortByRating()
	{
		DrinkList list = new DrinkList();
		list.add(new Drink("Caipiroska", 0));
		list.add(new Drink("Martini", 2));
		list.add(new Drink("Gin and Tonic", 2));
		
		list.sortByRating();

		assertEquals("Gin and Tonic", list.get(0).getName()); // G&T comes first as it has the same rating but beats it in Alphabetical.
		assertEquals("Martini", list.get(1).getName());
		assertEquals("Caipiroska", list.get(2).getName());
	}
	
	@Test 
	public void testGetMenu()
	{
		IngredientList ingredients = new IngredientList();
		ingredients.add(new Ingredient("Vermouth", true));
		ingredients.add(new Ingredient("Gin", true));
		ingredients.add(new Ingredient("Tonic", false));
		IngredientList.generateIds(ingredients);
		
		DrinkList drinks = new DrinkList();
		drinks.add(new Drink("Caipiroska", new DrinkIngredientList("60", "Vodka", "1/2 of a", "Lime", "1 teaspoon", "White Sugar", "1 teaspoon", "Raw Sugar")));
		drinks.add(new Drink("Martini", new DrinkIngredientList("60", "Gin", "30", "Vermouth")));
		drinks.add(new Drink("Gin and Tonic", new DrinkIngredientList("60", "Gin", "90", "Tonic")));
		DrinkList.generateIngredientIds(drinks, ingredients);
		
		DrinkList menu = drinks.getBarMenu(ingredients);
		
		assertEquals(1, menu.size());
		assertEquals("Martini", menu.get(0).getName()); // This should also assert that the getMenu doesn't do anything fancy like re-sort the Drinks.
		// assertEquals("Gin and Tonic", menu.get(1).getName());
		
	}
	
	@Test
	public void testGetDrinksWithIngredient()
	{
		DrinkList drinks = new DrinkList();
		drinks.add(new Drink("Caipiroska", new DrinkIngredientList("60", "Vodka", "1/2 of a", "Lime", "1 teaspoon", "White Sugar", "1 teaspoon", "Raw Sugar")));
		drinks.add(new Drink("Martini", new DrinkIngredientList("60", "Gin", "30", "Vermouth")));
		drinks.add(new Drink("Gin and Tonic", new DrinkIngredientList("60", "Gin", "90", "Tonic")));
		
		DrinkList drinksWithGin = drinks.getDrinksWith("Gin");

		assertEquals(2, drinksWithGin.size());
		assertEquals("Martini", drinksWithGin.get(0).getName()); // This should also assert that the getMenu doesn't do anything fancy like re-sort the Drinks.
		assertEquals("Gin and Tonic", drinksWithGin.get(1).getName());
	}
	
}
