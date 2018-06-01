package com.beachrife.cocktailmix.helpers;

public class ConsoleHelper {

	// Reads an asset file and returns the contents in a string.
	public static void writeLine(String format) 
	{
		System.out.println(format);
	}

	public static void writeLine(String format, Object...args) 
	{
		String str = String.format(format, args);
		System.out.println(str);
	}
}
