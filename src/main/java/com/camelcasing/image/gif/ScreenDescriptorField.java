package com.camelcasing.image.gif;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public final class ScreenDescriptorField {
		
		/**
		 * true for the existance of global colour table
		 */
		private final boolean colourTableFlag;
		/**
		 * limited to 3 bits (value of 7), is the max number of colours (to calculate raise 2 to the power of this 
		 * value + 1 eg a input 7 means 2 raised to the power of 8 which equals 256
		 */
		private final int colourResolution;
		/**
		 * true if the global colour table is sorted, ie most used colour first
		 */
		private final boolean sortFlag;
		/**
		 * limited to 3 bits (value 7) same calculate as colourResolution and is the maximum amount of different colours
		 * that can be used in an image (if using the global colour table)
		 */
		private final int colourTableSize;
		
	/**
	 * Sets the fields of this class and valitates the input, will throw an {@link java.lang.IllegalArgumentException} if this fails.
	 * Call create to return the byte representation of these settings.
	 * 
	 * @param colourTableFlag true for the existance of global colour table
	 * @param colourResolution limited to 3 bits (value of 7), is the max number of colours (to calculate raise 2 to the power of this 
	 * 		value + 1 eg a input 7 means 2 raised to the power of 8 which equals 256)
	 * @param sortFlag true if the global colour table is sorted, ie most used colour first
	 * @param colourTableSize limited to 3 bits (value 7) same calculate as colourResolution and is the maximum amount of different colours
	 * 		that can be used in an image (if using the global colour table)	
	 */
	public ScreenDescriptorField(boolean colourTableFlag, int colourResolution, boolean sortFlag, int colourTableSize){
		this.colourTableFlag = colourTableFlag;
		this.colourResolution = colourResolution;
		this.sortFlag = sortFlag;
		this.colourTableSize = colourTableSize;
		
		validateInput();
	}
	
	/**
	 * @throws IllegalArgumentException
	 */
	private void validateInput() throws IllegalArgumentException{
		if(colourTableSize < 0 || colourTableSize > 7) throw new IllegalArgumentException("colourTableSize must be between 0 and 7");
		if(colourResolution < 0 || colourResolution > 7) throw new IllegalArgumentException("colourTableSize must be between 0 and 7");
	}
	
	/**
	 * Most significant bit contains colourTableFlag
	 * next 3 bits contain the colourResolution
	 * next bit is the sortFlag
	 * final 3 bits represent the colourTableSize
	 * @return a single byte that incorporates all the settings in the values in the constructor
	 */
	public int create(){
		StringBuilder sBuilder = new StringBuilder(6);
		sBuilder.append((colourTableFlag) ? 1 : 0);
		sBuilder.append(GIFUtils.getBitsFromInt(colourResolution, 3));
		sBuilder.append((sortFlag) ? 1 : 0);
		sBuilder.append(GIFUtils.getBitsFromInt(colourTableSize, 3));
		return Integer.valueOf(sBuilder.toString(), 2);
	}
	
	public boolean getColourTableFlag(){
		return colourTableFlag;
	}
	
	public int getColourTableSize(){
		return colourTableSize;
	}
	
	public int getColourResolution(){
		return colourResolution;
	}
}
