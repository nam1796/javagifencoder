package com.camelcasing.image.gif;

public class ColorTable{

	/**
	 * Amount of RGB colour values in GlobalColorTable
	 */
	private final int tableSize;
	/**
	 * List containing RGB values,
	 */
	private int[] colorTable; 
	/**
	 * keeps track of where to add next value in table array.
	 */
	private int currentLocation = 0;
	 /**
	  * If the {@link com.camelcasing.image.gif.GlobalColorTable#getColorTable() getColorTable} method is called before 
	  * the table has a definition for all the allocated colours then remaining spaces will be filled with the color BLACK  
	  * @param tableSize
	  * @param colorResolution
	  */
	public ColorTable(int tableSize){
		this.tableSize = tableSize;
		
		calculateTableSize();
	}
	
	/**
	 * Calculate the colorTable size based on the tableSize, making room for RGB values
	 */
	private void calculateTableSize(){
		colorTable = new int[((int)Math.pow(2, tableSize + 1)) * 3];
	}
	
	/**
	 * Adds a RGB colour value to the GlobalColorTable,
	 * @param r Red colour value, must not exceed ColorResolution
	 * @param g Green colour value, must not exceed ColorResolution
	 * @param b Blue colour value, must not exceed ColorResolution
	 */
	public void addColor(int r, int g, int b){
		colorTable[currentLocation] = r;
		currentLocation++;
		colorTable[currentLocation] = g;
		currentLocation++;
		colorTable[currentLocation] = b;
		currentLocation++;
	}
	
	/**
	 * @return and int[] array that holds all the r,g,b values of the ColorTable
	 */
	public int[] getColorTable(){
		return colorTable;
	}

	public int getColorTableSize(){
//			if(tableSize == 1){
//				return 2;
//			}
		return tableSize;
	}
}
