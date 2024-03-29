package com.beachrife.cocktailmix.datalayer;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.beachrife.cocktailmix.R;
import com.beachrife.cocktailmix.drinks.Drink;
import com.beachrife.cocktailmix.drinks.DrinkClassificationType;
import com.beachrife.cocktailmix.drinks.DrinkIngredient;
import com.beachrife.cocktailmix.drinks.DrinkList;
import com.beachrife.cocktailmix.drinks.IngredientList;
import com.beachrife.cocktailmix.helpers.AssetHelper;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;
import com.beachrife.cocktailmix.ui.MainActivity;

public class DrinkFactory 
{
	private final String DRINK_NAME = "DRINK_";
	private Context context;

	// Constructor needed to reference the classes static methods. 
	public DrinkFactory()
	{
		this.context = null;
	}
	
	
	public DrinkFactory(Context context)
	{
		this.context = context;		
	}
	
	
	// Returns all of the cocktails from the assets/cocktails.json file.
	public DrinkList load(IngredientList ingredients) throws DataException
	{
		DrinkList list = new DrinkList();
		String action = "";
		int index = 0;
		
		// Load the file
		String fileString;
		try 
		{
			fileString = AssetHelper.readFile(this.context, "drinks.json");
		} 
		catch (IOException e) 
		{
			throw new DataException("Failed to load the cocktails file.", e);
		}

		String drinkName = null;
		try 
		{
			// Parse the JSON objects...			
			action = "Getting the root JSON object";
			JSONObject jasonRoot = new JSONObject(fileString);
			action = "Getting the JSON drinks array";
			JSONArray jasonList = jasonRoot.getJSONArray("drinks");
			
			for (index = 0; index < jasonList.length(); index++) 
			{
				drinkName = null;
				
				action = "Getting the JSON drink item";
				JSONObject jasonItem = jasonList.getJSONObject(index);
				
				Drink item = new Drink();
				{
					action = "Getting the JSON drink.name";
					String name = jasonItem.getString("name");
					drinkName = name;

					// Set the image name based on the drink.name
					String imageName = name.toLowerCase();
					imageName = imageName.replace(' ', '_');
					imageName = imageName.replace("\'", "");
					imageName = imageName.replace('-', '_');
					imageName = imageName.replace('�', 'e');
					imageName = "drink_" + imageName;
					
					int imageResourceId = this.context.getResources().getIdentifier(imageName, "drawable", MainActivity.PACKAGE_NAME);
					
					if (imageResourceId == 0)
					{
						// We have a failure to load the applications...
						ConsoleHelper.writeLine("Failed to load the image for the Drink: %s", name);
						imageResourceId = R.drawable.drink_martini; // The image that is returned if we can't resolve the image
					}
					
					action = "Getting the JSON drink.classification";
					String classificationString = jasonItem.getString("classification");
					DrinkClassificationType classification = DrinkClassificationType.valueOf(classificationString);
										
					action = "Getting the JSON drink.method";
					String method = jasonItem.getString("method");
					
					item.setName(name);
					item.setClassification(classification);
					item.setImageResourceId(imageResourceId);
					item.setMethod(method);
				}
				
				action = "Getting the JSON drink.ingredients array";
				JSONArray jasonDrinkIngredientlist = jasonItem.getJSONArray("ingredients");
				for (int i = 0; i < jasonDrinkIngredientlist.length(); i++) 
				{
					action = "Getting the JSON ingredients array";
					JSONObject jasonIngredientItem = jasonDrinkIngredientlist.getJSONObject(i);

					action = "Getting the JSON ingredients.quantity";
					String quantity = jasonIngredientItem.getString("quantity");
					
					action = "Getting the JSON ingredients.name";
					String name = jasonIngredientItem.getString("name");
					
					DrinkIngredient drinkIngredient = new DrinkIngredient(quantity, name);
					
					item.getIngredientList().add(drinkIngredient);
				}

				list.add(item);
			}
		} 
		catch (JSONException e) 
		{
			String message = "";
			if (drinkName != null)
				message = String.format("Failed to parse the cocktails file. Index=%s DrinkName=%s Action=%s", index, drinkName, action);
			else
				message = String.format("Failed to parse the cocktails file. Index=%s Action=%s", index, action);
				
			throw new DataException(message, e);
		}

		this.loadUserSettings(this.context, list);
		
		// Assign all of the drink.Ingredients.IngredientId to the Ingredient.Id
		DrinkList.generateIngredientIds(list, ingredients);
		
		return list;
	}
	
	public void save(DrinkList list) throws DataException
	{
		this.saveUserSettings(this.context, list);
	}
	

	private void saveUserSettings(Context context, DrinkList list) throws DataException
	{
		try
		{
			JSONObject jason = new JSONObject();
	
			for (Drink item : list)
			{
				String key = DRINK_NAME + item.getName();
				jason.put(key, item.getRating());
			}
			
			String jasonString = jason.toString();
			
			SharedPreferences sharedPreferences = this.context.getSharedPreferences(MainActivity.PACKAGE_NAME, Context.MODE_PRIVATE);
		    SharedPreferences.Editor editor = sharedPreferences.edit();
		    editor.putString("drinks", jasonString);
		    editor.commit();
		} catch (JSONException e) {
			throw new DataException("Failed to build the user preferences.", e);
		}
	}
	
	private void loadUserSettings(Context context, DrinkList list) throws DataException
	{
		try
		{
			SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.PACKAGE_NAME, Context.MODE_PRIVATE);
			String jasonString = sharedPreferences.getString("drinks", null);
			
			if (jasonString != null)
			{
				JSONObject jason = new JSONObject(jasonString);
	
				for (Drink item : list)
				{
					String key = DRINK_NAME + item.getName();
					if (jason.has(key))
					{
						int value = jason.getInt(key);
						item.setRating(value);

						// ConsoleHelper.writeLine("Loading Drink: %s Rating=%s", item.getName(), item.getRating());
					}
				}
			}
		} catch (JSONException e) {
			throw new DataException("Failed to build the user preferences.", e);
		}
	}
}
