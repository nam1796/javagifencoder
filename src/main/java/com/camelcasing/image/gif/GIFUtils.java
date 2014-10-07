package com.camelcasing.image.gif;

import com.camelcasing.image.lwzcompression.InputColour;

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
	 * Combines 2 integers into a {@link com.camelcasing.image.lwzcompression.InputColour InputColour}. Will create an array int[a, b].
	 * @param a value to be added to index 0.
	 * @param b value to be added to index 1.
	 * @return InputColour with the two values
	 */
	public static InputColour simpleColourArray(int a, int b){
		return new InputColour(new int[] {a, b});
	}
	
	/**
	 * see {@link com.camelcasing.image.gif.GIFUtils#simpleColourArray(int, int) simpleColourArray(int, int)}
	 * @param a first input values.
	 * @param b value to be appended to a.
	 * @return InputColour with <code>Integer</code> appended to the array
	 */
	public static InputColour simpleColourArray(int[] a, int b){
		int[] c = new int[a.length + 1];
		System.arraycopy(a, 0, c, 0, a.length);
		c[c.length - 1] = b;
		return new InputColour(c);
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
}
