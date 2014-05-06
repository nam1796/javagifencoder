package com.camelcasing.image.gif;

import java.util.Arrays;

/**
 * Basically a wapper Class for an int[] with <code>HashCode</code> and <code>Equals</code> overridden 
 * to allow arrays to be added to a <code>HashMap</code> and exploit the <code>Contains</code> method
 * 
 * @author camelCasing
 * @version 0.2
 * @since 0.2
 */
public class InputColor{

		private int[] color;
	
	/**
	 * @param color
	 */
	public InputColor(int[] color){
		this.color = color;
	}

	public int[] getColor(){
		return color;
	}

	public int getSize(){
		return color.length;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(color);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputColor other = (InputColor) obj;
		if (!Arrays.equals(color, other.color))
			return false;
		return true;
	}
}
