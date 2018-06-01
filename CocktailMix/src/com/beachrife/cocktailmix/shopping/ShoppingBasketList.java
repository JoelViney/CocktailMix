package com.beachrife.cocktailmix.shopping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ShoppingBasketList extends ArrayList<ShoppingBasket>  
{
	private static final long serialVersionUID = 3696944232895184711L;

	// Default constructor.
	public ShoppingBasketList()
	{

	}

	public void sort()
	{
		Collections.sort(this, new Comparator<ShoppingBasket>() 
			{
	    		public int compare(ShoppingBasket item1, ShoppingBasket item2) 
	    		{
	    			return item1.getCostPerDrink().compareTo(item2.getCostPerDrink());
	    		}
			}); 
	}
}
