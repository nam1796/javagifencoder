package com.camelcasing.image.gif;

public class ImageDescriptorFields{

		/**
		 * True if image contains a Local Color Table
		 */
		private final boolean localColorTableFlag;
		
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
		private final int sizeOfLocalColorTable;
	
	/**
	 * @param localColorTableFlag True if image contains a Local Color Table
	 * @param interlaceFlag True if image is interlaced (uses four-pass interlace pattern)
	 * @param sortFlag True if image is interlaced (uses four-pass interlace pattern)
	 * @param sizeOfLocalColorTable limited to 3 bits (value of 7), is the max number of colours (to calculate raise 2 to the power of this 
	 * 		value + 1 eg a input 7 means 2 raised to the power of 8 which equals 256)
	 */
	public ImageDescriptorFields(boolean localColorTableFlag, boolean interlaceFlag, boolean sortFlag, int sizeOfLocalColorTable){
		this.localColorTableFlag = localColorTableFlag;
		this.interlaceFlag = interlaceFlag;
		this.sortFlag = sortFlag;
		this.sizeOfLocalColorTable = sizeOfLocalColorTable;
		validate();
	}
	
	/**
	 * @return
	 * @throws IllegalArgumentException
	 */
	private boolean validate() throws IllegalArgumentException{
		if(sizeOfLocalColorTable < 0 || sizeOfLocalColorTable > 7) throw new IllegalArgumentException("colorTableSize must be between 0 and 7");
		return true;
	}
	
	/**
	 * @return
	 */
	public int create(){
		StringBuilder sBuilder = new StringBuilder(6);
		sBuilder.append((localColorTableFlag ? '1' : '0'));
		sBuilder.append((interlaceFlag ? '1' : '0'));
		sBuilder.append((sortFlag ? '1' : '0'));
		sBuilder.append("00");
		sBuilder.append(GIFUtils.getBitsFromInt(sizeOfLocalColorTable, 3));
		return Integer.valueOf(sBuilder.toString(), 2);
	}
}
