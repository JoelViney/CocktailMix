package com.beachrife.cocktailmix.test;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.junit.Test;

import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkIngredientList;
import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;
import com.beachrife.cocktailmix.drinks.IngredientType;
import com.beachrife.cocktailmix.shopping.ShoppingAdvisor;
import com.beachrife.cocktailmix.shopping.ShoppingBasket;

public class ShoppingTest extends TestCase  
{
	// Test when we only have one option.
	@Test
	public void testSingleOption() 
	{
		Ingredient vodka = new Ingredient("Vodka", IngredientType.SPIRIT, new BigDecimal("31.95"), false);
		IngredientList ingredients = new IngredientList(vodka);
		IngredientList.generateIds(ingredients);
		
		Drink vodkaOnTheRocks = new Drink("Vodka on the Rocks", new DrinkIngredientList("30", "Vodka"));
		DrinkList drinks = new DrinkList(vodkaOnTheRocks);
		DrinkList.generateIngredientIds(drinks, ingredients);
		
		// This better suggest Gin and Tonic
		ShoppingAdvisor advisor = new ShoppingAdvisor(drinks, ingredients);
		ShoppingBasket basket = advisor.getBestBasket();
		
		// It's first suggestion should be Tonic
		assertEquals(1, basket.getIngredients().size());
		assertTrue(basket.getIngredients().contains(vodka));
		
		assertEquals(1, basket.getDrinks().size());
		assertTrue(basket.getDrinks().contains(vodkaOnTheRocks));
	} 
	
	// We already have the Gin, so it should select Tonic for us
	@Test
	public void testOneIngredient() 
	{
		Ingredient gin = new Ingredient("Gin", IngredientType.SPIRIT, new BigDecimal("31.95"), true);
		Ingredient vermouth = new Ingredient("Vermouth", IngredientType.SPIRIT, new BigDecimal("16.95"), false);
		Ingredient tonic = new Ingredient("Tonic", IngredientType.SPIRIT, new BigDecimal("1.95"), false);
		IngredientList ingredients = new IngredientList(gin, vermouth, tonic);
		IngredientList.generateIds(ingredients);
		
		Drink martini = new Drink("Martini", new DrinkIngredientList("60", "Gin", "30", "Vermouth"));
		Drink ginAndTonic = new Drink("Gin and Tonic", new DrinkIngredientList("60", "Gin", "90", "Tonic"));
		DrinkList drinks = new DrinkList(martini, ginAndTonic);
		DrinkList.generateIngredientIds(drinks, ingredients);
		
		// This better suggest Gin and Tonic
		ShoppingAdvisor advisor = new ShoppingAdvisor(drinks, ingredients);
		ShoppingBasket basket = advisor.getBestBasket();
		
		// It's first suggestion should be Tonic
		assertEquals(1, basket.getIngredients().size());
		assertTrue(basket.getIngredients().contains(tonic));
		
		assertEquals(1, basket.getDrinks().size());
		assertTrue(basket.getDrinks().contains(ginAndTonic));
	} 

	// This test checks to see that it will return two ingredients.
	@Test
	public void testTwoIngredients() 
	{
		Ingredient gin = new Ingredient("Gin", IngredientType.SPIRIT, new BigDecimal("31.95"), true);
		Ingredient vermouth = new Ingredient("Vermouth", IngredientType.SPIRIT, new BigDecimal("16.95"), false);
		Ingredient tonic = new Ingredient("Tonic", IngredientType.SPIRIT, new BigDecimal("1.95"), false);
		IngredientList ingredients = new IngredientList(gin, vermouth, tonic);
		IngredientList.generateIds(ingredients);

		Drink martini = new Drink("Martini", new DrinkIngredientList("60", "Gin", "30", "Vermouth"));
		Drink ginAndTonic = new Drink("Gin and Tonic", new DrinkIngredientList("60", "Gin", "90", "Tonic"));
		DrinkList drinks = new DrinkList(martini, ginAndTonic);
		DrinkList.generateIngredientIds(drinks, ingredients);
		
		// This better suggest Gin and Tonic
		ShoppingAdvisor advisor = new ShoppingAdvisor(drinks, ingredients);
		ShoppingBasket basket = advisor.getBestBasket();
		
		// It's first suggestion should be Gin and Tonic
		assertEquals(1, basket.getIngredients().size());
		assertTrue(basket.getIngredients().contains(tonic));
		
		assertEquals(1, basket.getDrinks().size());
		assertTrue(basket.getDrinks().contains(ginAndTonic));
	} 
	
	/* Give the advisor 2 possible combinations.
	*  1. Gin & Tonic 
	*  2. Vodka & Cranberry
	*  
	*  Check to see that it goes for the more expensive option because it can return twice as many drinks.
	**/
	@Test
	public void testReturnHigherDrinkCombination()
	{
		Ingredient gin = new Ingredient("Gin", IngredientType.SPIRIT, new BigDecimal("31.95"), false);
		Ingredient tonic = new Ingredient("Tonic", IngredientType.SPIRIT, new BigDecimal("1.95"), false);
		Ingredient vodka = new Ingredient("Vodka", IngredientType.SPIRIT, new BigDecimal("31.95"), false);
		Ingredient cranberryJuice = new Ingredient("Cranberry Juice", IngredientType.MIXER, new BigDecimal("5.95"), false);
		Ingredient limeJuice = new Ingredient("Lime Juice", IngredientType.SPIRIT, new BigDecimal("1.95"), true);
		IngredientList ingredients = new IngredientList(gin, tonic, vodka, cranberryJuice, limeJuice);
		IngredientList.generateIds(ingredients);

		Drink ginAndTonic = new Drink("Gin and Tonic", new DrinkIngredientList("60", "Gin", "90", "Tonic"));
		Drink capeCodder = new Drink("Cape Codder", new DrinkIngredientList("60", "Vodka", "120", "Cranberry Juice"));
		Drink cranberryKick = new Drink("Cranberry Kick", new DrinkIngredientList("30", "Vodka", "120", "Cranberry Juice", "15", "Lime Juice"));
		DrinkList drinks = new DrinkList(ginAndTonic, capeCodder, cranberryKick);
		DrinkList.generateIngredientIds(drinks, ingredients);

		// This better suggest Gin and Tonic
		ShoppingAdvisor advisor = new ShoppingAdvisor(drinks, ingredients);
		ShoppingBasket basket = advisor.getBestBasket();
		
		// It's first suggestion should be Vodka and Cranberry Juice
		assertEquals(2, basket.getIngredients().size());
		assertTrue(basket.getIngredients().contains(vodka));
		assertTrue(basket.getIngredients().contains(cranberryJuice));
		
		assertEquals(2, basket.getDrinks().size());
		assertTrue(basket.getDrinks().contains(capeCodder));
		assertTrue(basket.getDrinks().contains(cranberryKick));
	}
	
	
}
