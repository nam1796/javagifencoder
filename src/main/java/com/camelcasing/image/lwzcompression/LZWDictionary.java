package com.camelcasing.image.lwzcompression;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * Class to store the LZW compression code and handle clearing
 * @author camelcasing
 * @since 1.0
 */
public class LZWDictionary {

		private Logger logger = Logger.getLogger(getClass().getName());
		private ArrayList<InputColour> dictionary = new ArrayList<InputColour>();
		private int offset;
		
	public LZWDictionary(int offset){
		this.offset = offset;
	}
	
	public void clear(){
		//logger.debug("dictionary cleared");
		dictionary.clear();
	}
		
	public void add(InputColour s){
		dictionary.add(s);
	}
		
	public boolean contains(InputColour s){
		return dictionary.contains(s);
	}
		
	public int get(InputColour s){
		return (dictionary.indexOf(s)) + offset;
	}
		
//	public int getSize(){
//		return dictionary.size();
//	}
}
