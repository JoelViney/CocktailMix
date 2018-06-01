package com.beachrife.cocktailmix;


public enum MeasurementType
{
	UNKNOWN,
	MEASUREMENT_CL,
	MEASUREMENT_ML,
	MEASUREMENT_OZ;
	

    public static MeasurementType fromInteger(int value) 
    {
        switch(value) 
        {
        case 0: return UNKNOWN;
        case 1: return MEASUREMENT_CL;
        case 2: return MEASUREMENT_ML;
        case 3: return MEASUREMENT_OZ;
        }
        return MeasurementType.UNKNOWN;
    }

    public static int toInt(MeasurementType value)
    {
    	switch(value)
    	{
        case UNKNOWN: return 0;
        case MEASUREMENT_CL: return 1;
        case MEASUREMENT_ML: return 2;
        case MEASUREMENT_OZ: return 3;
        default: return 0;
    	}
    }
}