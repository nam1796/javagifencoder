package com.camelcasing.image.gif;

/**
 * Collection of static method to aid the creation of GIF files. This class can not be instantized.
 * @version 0.2
 * @since 0.2
 * @author camelCasing
 *
 */
public class GIFUtils{
	
	private GIFUtils(){
		//private constructor as class can not be instantized
	}
	
	/**
	 * extracts bits of a int to the specified size from a specified offset, will add leading zeros
	 * @param value
	 * @param numberOfBitsRequired
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
	 * @param value
	 * @param numberOfBitsRequired
	 * @return The butchered bits
	 */
	public static String getBitsFromInt(int value, int numberOfBitsRequired){
		return(getBitsFromInt(value, numberOfBitsRequired, 0));
	}
	
	/**
	 * Combines 2 integers into a {@link com.camelcasing.image.gif.InputColor InputColor}. Will create an array int[a, b].
	 * @param a value to be added to index 0.
	 * @param b value to be added to index 1.
	 * @return
	 */
	public static InputColor simpleColorArray(int a, int b){
		return new InputColor(new int[] {a, b});
	}
	
	/**
	 * see {@link com.camelcasing.image.gif.GIFUtils#simpleColorArray(int, int) simpleColorArray(int, int)}
	 * @param a first input values.
	 * @param b value to be appended to a.
	 * @return
	 */
	public static InputColor simpleColorArray(int[] a, int b){
		int[] c = new int[a.length + 1];
		System.arraycopy(a, 0, c, 0, a.length);
		c[c.length - 1] = b;
		return new InputColor(c);
	}
	
	/**
	 * @param colorTableSize Square root of the Size of the <code>Global Color Table</code>, minus one 
	 * 		(Same value as entered into {@link com.camelcasing.image.gif.ScreenDescriptorField ScreenDescriptorField}) 
	 * @return Number of bit required to represent the maximum size of the input parameter 
	 */
	public static int numberOfBitsRequired(int colorTableSize){
		int max = (int)((Math.pow(2, colorTableSize + 1)) - 1);
		return((Integer.toBinaryString(max)).length());
	}
}
