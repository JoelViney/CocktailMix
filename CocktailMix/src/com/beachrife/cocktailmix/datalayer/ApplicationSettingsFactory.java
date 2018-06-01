package com.beachrife.cocktailmix.datalayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.beachrife.cocktailmix.ApplicationSettings;
import com.beachrife.cocktailmix.MeasurementType;
import com.beachrife.cocktailmix.helpers.ConsoleHelper;

public class ApplicationSettingsFactory
{
	private Context context;
	
	public ApplicationSettingsFactory(Context context)
	{
		this.context = context;
	}
	
	public ApplicationSettings load()
	{
		ApplicationSettings settings = new ApplicationSettings();
		
		SharedPreferences preferences = context.getSharedPreferences("UserInfo", 0);

		int value = preferences.getInt("MeasurementType", MeasurementType.toInt(MeasurementType.MEASUREMENT_CL));
		MeasurementType measurementType = MeasurementType.fromInteger(value);

		settings.setMeasurementType(measurementType);

		ConsoleHelper.writeLine("Loading setting [MeasurementType]: %s (%s)", value, settings.getMeasurementType().toString());
		
		return settings;		
	}
	
	public void save(ApplicationSettings settings)
	{
		SharedPreferences preferences = context.getSharedPreferences("UserInfo", 0);
		SharedPreferences.Editor editor = preferences.edit();
		
		int i = MeasurementType.toInt(settings.getMeasurementType());
		editor.putInt("MeasurementType", i);
		
		ConsoleHelper.writeLine("Saving setting [MeasurementType] %s (%s)", settings.getMeasurementType(), i);
		
		editor.commit();
	}

}
