package com.camelcasing.image.gif;

/**
 * Collection of static method to aid the creation of GIF files. This class can not be instantized.
 * 
 * @author Philip Teclaff
 * @since 1.0
 */
public class GIFUtils{
	
	private GIFUtils(){
		//private constructor as class can not be instantized
	}
	
	/**
	 * extracts bits of a int to the specified size from a specified offset, will add leading zeros
	 * @param value The <code>Integer</code> value to process
	 * @param numberOfBitsRequired The length the returned String need to be (including zeros)
	 * @param offset number of bits to exclude ie will return higher order bits
	 * @return The butchered bits
	 */
	public static String getBitsFromInt(int value, int numberOfBitsRequired, int offset){
		String s = Integer.toBinaryString(value);
		StringBuilder sBuilder = new StringBuilder(6);
			if(s.length() > numberOfBitsRequired + offset){
				sBuilder.append(s.substring((s.length() - offset) - numberOfBitsRequired, s.length() - offset));
			}else{
				if((s.length() - offset) >= 0){
					sBuilder.append(s.substring(0, s.length() - offset));
				}
				int loopFor = numberOfBitsRequired - sBuilder.length();
					for(int i = 0; i < loopFor; i++){
						sBuilder.insert(0, '0');
					}
			}
		return sBuilder.toString();
	}

	/**
	 * extracts bits of a int to the specified size from a offset of 0, will add leading zeros
	 * @param value The <code>Integer</code> value to process
	 * @param numberOfBitsRequired The length the returned String need to be (including zeros)
	 * @return The butchered bits
	 */
	public static String getBitsFromInt(int value, int numberOfBitsRequired){
		return(getBitsFromInt(value, numberOfBitsRequired, 0));
	}
	
	/**
	 * @param colourTableSize Square root of the Size of the <code>Global/Local Colour Table</code>
	 * 		(Same value as entered into {@link com.camelcasing.image.gif.ScreenDescriptorField ScreenDescriptorField}) 
	 * @return Number of bit required to represent the maximum value in the colour table
	 */
	public static int numberOfBitsRequired(int colourTableSize){
		int n = ((int)Math.pow(2, colourTableSize + 1)) - 1;
		return((Integer.toBinaryString(n)).length());
	}
	
	public static int getColourTableSize(int colourCount){
		if(colourCount >= 128) return 7;
		if(colourCount >= 64) return 6;
		if(colourCount >= 32) return 5;
		if(colourCount >= 16) return 4;
		if(colourCount >= 8) return 3;
		return 2;
	}
}
