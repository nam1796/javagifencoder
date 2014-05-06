package com.camelcasing.image.gif;

/**
 * @author camelCasing
 *
 */
public final class ScreenDescriptorField {
		
		/**
		 * true for the existance of global color table
		 */
		private final boolean colorTableFlag;
		/**
		 * limited to 3 bits (value of 7), is the max number of colours (to calculate raise 2 to the power of this 
		 * value + 1 eg a input 7 means 2 raised to the power of 8 which equals 256
		 */
		private final int colorResolution;
		/**
		 * true if the global colour table is sorted, ie most used colour first
		 */
		private final boolean sortFlag;
		/**
		 * limited to 3 bits (value 7) same calculate as colorResolution and is the maximum amount of different colours
		 * that can be used in an image (if using the global colour table)
		 */
		private final int colorTableSize;
		
	/**
	 * Sets the fields of this class and valitates the input, will throw an {@link java.lang.IllegalArgumentException} if this fails. <br />
	 * Call create to return the byte representation of these settings.
	 * 
	 * @param colorTableFlag true for the existance of global color table
	 * @param colorResolution limited to 3 bits (value of 7), is the max number of colours (to calculate raise 2 to the power of this 
	 * 		value + 1 eg a input 7 means 2 raised to the power of 8 which equals 256)
	 * @param sortFlag true if the global colour table is sorted, ie most used colour first
	 * @param colorTableSize limited to 3 bits (value 7) same calculate as colorResolution and is the maximum amount of different colours
	 * 		that can be used in an image (if using the global colour table)	
	 */
	public ScreenDescriptorField(boolean colorTableFlag, int colorResolution, boolean sortFlag, int colorTableSize){
		this.colorTableFlag = colorTableFlag;
		this.colorResolution = colorResolution;
		this.sortFlag = sortFlag;
		this.colorTableSize = colorTableSize;
		
		validateInput();
	}
	
	/**
	 * @throws IllegalArgumentException
	 */
	private void validateInput() throws IllegalArgumentException{
		if(colorTableSize < 0 || colorTableSize > 7) throw new IllegalArgumentException("colorTableSize must be between 0 and 7");
		if(colorResolution < 0 || colorResolution > 7) throw new IllegalArgumentException("colorTableSize must be between 0 and 7");
	}
	
	/**
	 * Most significant bit contains colorTableFlag<br />
	 * next 3 bits contain the colorResolution<br />
	 * next bit is the sortFlag<br >
	 * final 3 bits represent the colorTableSize
	 * @return a single byte that incorporates all the settings in the values in the constructor
	 */
	public int create(){
		StringBuilder sBuilder = new StringBuilder(6);
		sBuilder.append((colorTableFlag) ? 1 : 0);
		sBuilder.append(GIFUtils.getBitsFromInt(colorResolution, 3));
		sBuilder.append((sortFlag) ? 1 : 0);
		sBuilder.append(GIFUtils.getBitsFromInt(colorTableSize, 3));
		return Integer.valueOf(sBuilder.toString(), 2);
	}
	
	public boolean getColorTableFlag(){
		return colorTableFlag;
	}
	
	public int getColorTableSize(){
		return colorTableSize;
	}
	
	public int getColorResolution(){
		return colorResolution;
	}
}
