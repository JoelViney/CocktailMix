package com.beachrife.cocktailmix.datalayer;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.drinks.Ingredient;
import com.beachrife.cocktailmix.drinks.IngredientList;
import com.beachrife.cocktailmix.drinks.IngredientType;
import com.beachrife.cocktailmix.helpers.AssetHelper;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;
import com.beachrife.cocktailmix.ui.MainActivity;

public class IngredientFactory
{
	private final String INGREDIENT_NAME = "INGREDIENT_";
	
	private Context context;

	// Constructor needed to reference the classes static methods. 
	public IngredientFactory()
	{
		this.context = null;
	}
	
	public IngredientFactory(Context context)
	{
		this.context = context;
	}
	
	
	// Returns all of the cocktails from the assets/cocktails.json file.
	public IngredientList load() throws DataException
	{
		IngredientList list = new IngredientList();
		
		// Load the file
		String fileString;
		try 
		{
			fileString = AssetHelper.readFile(this.context, "ingredients.json");
		} catch (IOException e) {
			throw new DataException("Failed to load the cocktails file.", e);
		}

		try 
		{
			// Parse the JSON objects...
			JSONObject jasonRoot = new JSONObject(fileString);
	
			JSONArray jasonList = jasonRoot.getJSONArray("ingredients");
	
			for (int x = 0; x < jasonList.length(); x++) 
			{
				JSONObject jasonItem = jasonList.getJSONObject(x);
				
				Ingredient item = new Ingredient();
				String name = jasonItem.getString("name");
				String ingredientTypeString = jasonItem.getString("ingredientType");
				String costString = jasonItem.getString("cost");
				String description = jasonItem.getString("description");
					
				IngredientType ingredientType = IngredientType.valueOf(ingredientTypeString);
				
				String enabledImageName = name.toLowerCase();
				enabledImageName = enabledImageName.replace(' ', '_');
				enabledImageName = enabledImageName.replace("\'", "");
				enabledImageName = enabledImageName.replace('-', '_');
				enabledImageName = enabledImageName.replace('é', 'e');
				enabledImageName = ingredientTypeString.toLowerCase() + "_" + enabledImageName;
				
				String disabledImageName = enabledImageName + "_gray";

				int enabledImageId = this.context.getResources().getIdentifier(enabledImageName, "drawable", MainActivity.PACKAGE_NAME);
				int disabledImageId = this.context.getResources().getIdentifier(disabledImageName, "drawable", MainActivity.PACKAGE_NAME);

				if (enabledImageId == 0 | disabledImageId == 0)
				{
					ConsoleHelper.writeLine("Failed to locate the image for the Ingredient: %s.", name);
					enabledImageId = R.drawable.ingredient_unknown;
					disabledImageId = R.drawable.ingredient_unknown_gray;
				}
				
				item.setName(name);
				item.setIngredientType(ingredientType);
				item.setDescription(description);
				item.setCost(new BigDecimal(costString));
				item.setImageEnabledId(enabledImageId);
				item.setImageDisabledId(disabledImageId);
	
				list.add(item);
			}
		} 
		catch (JSONException e)
		{
			throw new DataException("Failed to load the ingredients file.", e);
		}

		this.loadUserSettings(this.context, list);
		
		list.sortByName(); // Return the list pre-sorted. 

		// Index the list
		IngredientList.generateIds(list);
		
		return list;
	}
	
	public void save(IngredientList list) throws DataException
	{

		this.saveUserSettings(this.context, list);
	}
	
	private void saveUserSettings(Context context, IngredientList list) throws DataException
	{
		try
		{
			JSONObject jason = new JSONObject();
	
			for (Ingredient item : list)
			{
				String key = INGREDIENT_NAME + item.getName();
				jason.put(key, item.getBarHasIngredient());
			}
			
			String jasonString = jason.toString();
			
			SharedPreferences sharedPreferences = this.context.getSharedPreferences(MainActivity.PACKAGE_NAME, Context.MODE_PRIVATE);
		    SharedPreferences.Editor editor = sharedPreferences.edit();
		    editor.putString("ingredients", jasonString);
		    editor.commit();
		} catch (JSONException e) {
			throw new DataException("Failed to build the user preferences.", e);
		}
	}
	
	private void loadUserSettings(Context context, IngredientList list) throws DataException
	{
		try
		{
			SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.PACKAGE_NAME, Context.MODE_PRIVATE);
			String jasonString = sharedPreferences.getString("ingredients", null);
			
			if (jasonString != null)
			{
				JSONObject jason = new JSONObject(jasonString);
	
				for (Ingredient item : list)
				{
					String key = INGREDIENT_NAME + item.getName();
					if (jason.has(key))
					{
						boolean value = jason.getBoolean(key);
						item.setBarHasIngredient(value);

						// ConsoleHelper.writeLine("Loading Bar Ingredient: %s == %s", item.getName(), item.getUserHasIngredient());
					}
				}
			}
		} catch (JSONException e) {
			throw new DataException("Failed to build the user preferences.", e);
		}
	}
	
}
