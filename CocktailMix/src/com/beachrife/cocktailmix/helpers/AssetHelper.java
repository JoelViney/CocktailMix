package com.beachrife.cocktailmix.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;

public class AssetHelper {

	// Reads an asset file and returns the contents in a string.
	public static String readFile(Context context, String fileName) throws IOException
	{
		AssetManager assetManager = context.getAssets();
		InputStream inputStream = assetManager.open(fileName);

		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			builder.append(line);
		}
		
		String str = builder.toString();
		
		return str;
	}
}
