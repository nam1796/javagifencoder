package com.camelcasing.image.gif;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class ColourTable{

	/**
	 * Amount of RGB colour values in ColourTable
	 */
	protected final int tableSize;
	/**
	 * List containing RGB values,
	 */
	protected int[] colourTable; 
	/**
	 * keeps track of where to add next value in table array.
	 */
	protected int currentLocation = 0;
	 /**
	  * If the {@link com.camelcasing.image.gif.ColourTable#getColourTable() getColourTable} method is called before 
	  * the table has a definition for all the allocated colours then remaining spaces will be filled with the colour BLACK  
	  * @param tableSize value between 2 and 7 are acceptable.
	  */
	public ColourTable(int tableSize){
		this.tableSize = tableSize;
		calculateTableSize();
	}
	
	/**
	 * Calculate the colourTable size based on the tableSize, making room for RGB values
	 */
	protected void calculateTableSize(){
		colourTable = new int[((int)Math.pow(2, tableSize + 1)) * 3];
	}
	
	/**
	 * Adds a RGB colour value to the GlobalColourTable,
	 * @param r Red colour value, must not exceed ColourResolution
	 * @param g Green colour value, must not exceed ColourResolution
	 * @param b Blue colour value, must not exceed ColourResolution
	 */
	public void addColour(int r, int g, int b){
		colourTable[currentLocation] = r;
		currentLocation++;
		colourTable[currentLocation] = g;
		currentLocation++;
		colourTable[currentLocation] = b;
		currentLocation++;
	}
	
	public int getCurrentLocation(){
		return currentLocation;
	}
	
	/**
	 * @return and int[] array that holds all the r,g,b values of the ColourTable
	 */
	public int[] getColourTable(){
		return colourTable;
	}

	/**
	 * @return size of the colour table 
	 */
	public int getColourTableSize(){
		return tableSize;
	}
}
