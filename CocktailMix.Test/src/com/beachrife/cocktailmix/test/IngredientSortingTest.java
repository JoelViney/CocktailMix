package com.beachrife.cocktailmix.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;
import com.beachrife.cocktailmix.drinks.IngredientType;

public class IngredientSortingTest extends TestCase 
{

	@Test
	public void testSortByName() 
	{
		IngredientList ingredients = new IngredientList();
		ingredients.add(new Ingredient("Vodka"));
		ingredients.add(new Ingredient("Gin"));
		ingredients.add(new Ingredient("Vermouth"));
		IngredientList.generateIds(ingredients);
		
		ingredients.sortByName();

		assertEquals("Gin", ingredients.get(0).getName());
		assertEquals("Vermouth", ingredients.get(1).getName());
		assertEquals("Vodka", ingredients.get(2).getName());
	}

	@Test
	public void testGetByType()
	{
		Ingredient vodka = new Ingredient("Vodka", IngredientType.SPIRIT);
		Ingredient vermouth = new Ingredient("Vermouth", IngredientType.LIQUEUR);
		Ingredient gin = new Ingredient("Gin", IngredientType.SPIRIT);
		IngredientList ingredients = new IngredientList(vodka, vermouth, gin);
		IngredientList.generateIds(ingredients);
		
		IngredientList spirits = ingredients.getByIngredientType(IngredientType.SPIRIT);
		
		assertTrue(spirits.contains(vodka));
		assertTrue(spirits.contains(gin));
		assertFalse(spirits.contains(vermouth));
		
	}
}
