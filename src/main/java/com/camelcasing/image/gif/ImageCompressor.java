package com.camelcasing.image.gif;

import java.util.ArrayList;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public interface ImageCompressor {
	public void compress();
	public ArrayList<Integer> getPackagedBytes();
} 
