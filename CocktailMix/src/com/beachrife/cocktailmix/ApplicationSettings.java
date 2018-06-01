package com.beachrife.cocktailmix;

public class ApplicationSettings 
{
	private MeasurementType measurementType;
	
	public ApplicationSettings()
	{
		this.measurementType = MeasurementType.MEASUREMENT_CL; // Default to CL		
	}

	public MeasurementType getMeasurementType() { return measurementType; }
	public void setMeasurementType(MeasurementType value) { this.measurementType = value; }
	
	
}
