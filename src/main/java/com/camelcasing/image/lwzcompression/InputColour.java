package com.camelcasing.image.lwzcompression;

import java.util.Arrays;

import com.camelcasing.image.gif.GIFUtils;

/**
 * Basically a wapper Class for an int[] with <code>HashCode</code> and <code>Equals</code> overridden 
 * to allow arrays to be added to a <code>HashMap</code> and exploit the <code>Contains</code> method. 
 * For use in the {@link com.camelcasing.image.lwzcompression.LZWCompressor LZWCompressor} class
 * 
 * @author Philip Teclaff
 * @since 1.0
 */
public class InputColour{

		private int[] colour;
	
	/**
	 * @param colour generated by {@link com.camelcasing.image.gif.GIFUtils GIFUtils} to help the {@link com.camelcasing.image.lwzcompression.LZWCompressor LZWCompressor}
	 */
	public InputColour(int[] colour){
		this.colour = colour;
	}
	
	public InputColour(int i){
		colour = new int[1];
		colour[0] = i;
	}

	public int[] getColour(){
		return colour;
	}

	public int getSize(){
		return colour.length;
	}
	
	public int getFirst(){
		return colour[0];
	}
	
	public int getLength(){
		return colour.length;
	}

	@Override
	public int hashCode(){
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(colour);
		return result;
	}

	@Override
	public boolean equals(Object obj){
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		InputColour other = (InputColour)obj;
		if(!Arrays.equals(colour, other.colour))
			return false;
		return true;
	}
}
