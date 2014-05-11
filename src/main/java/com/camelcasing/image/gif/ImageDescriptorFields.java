package com.camelcasing.image.gif;

public class ImageDescriptorFields{

		/**
		 * True if image contains a Local Colour Table
		 */
		private final boolean localColourTableFlag;
		
		/**
		 * True if image is interlaced (uses four-pass interlace pattern)
		 */
		private final boolean interlaceFlag;
		
		/**
		 * True if image is interlaced (uses four-pass interlace pattern)
		 */
		private final boolean sortFlag;
		
		/**
		 * limited to 3 bits (value of 7), is the max number of colours (to calculate raise 2 to the power of this 
		 * value + 1 eg a input 7 means 2 raised to the power of 8 which equals 256)
		 */
		private final int sizeOfLocalColourTable;
	
	/**
	 * @param localColourTableFlag True if image contains a Local Colour Table
	 * @param interlaceFlag True if image is interlaced (uses four-pass interlace pattern)
	 * @param sortFlag True if image is interlaced (uses four-pass interlace pattern)
	 * @param sizeOfLocalColourTable limited to 3 bits (value of 7), is the max number of colours (to calculate raise 2 to the power of this 
	 * 		value + 1 eg a input 7 means 2 raised to the power of 8 which equals 256)
	 */
	public ImageDescriptorFields(boolean localColourTableFlag, boolean interlaceFlag, boolean sortFlag, int sizeOfLocalColourTable){
		this.localColourTableFlag = localColourTableFlag;
		this.interlaceFlag = interlaceFlag;
		this.sortFlag = sortFlag;
		this.sizeOfLocalColourTable = sizeOfLocalColourTable;
		validate();
	}
	
	/**
	 * @return True/False if input was validated, checks the size of the colourTable is between 0 and 7
	 * @throws IllegalArgumentException
	 */
	private boolean validate() throws IllegalArgumentException{
		if(sizeOfLocalColourTable < 0 || sizeOfLocalColourTable > 7) throw new IllegalArgumentException("colourTableSize must be between 0 and 7");
		return true;
	}
	
	/**
	 * @return The <code>Integer</code> values of these fields
	 */
	public int create(){
		StringBuilder sBuilder = new StringBuilder(6);
		sBuilder.append((localColourTableFlag ? '1' : '0'));
		sBuilder.append((interlaceFlag ? '1' : '0'));
		sBuilder.append((sortFlag ? '1' : '0'));
		sBuilder.append("00");
		sBuilder.append(GIFUtils.getBitsFromInt(sizeOfLocalColourTable, 3));
		return Integer.valueOf(sBuilder.toString(), 2);
	}
}
