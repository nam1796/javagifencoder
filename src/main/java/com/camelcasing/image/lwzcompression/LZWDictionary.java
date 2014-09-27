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
		private int maxSize;
		private ArrayList<String> dictionary = new ArrayList<String>();
		private int offset;
		
	public LZWDictionary(int offset){
		this.offset = offset;
	}
	
	public void clear(){
		dictionary.clear();
	}
	
	public void setMaxSize(int newSize){
		maxSize = newSize;
	}
	
	public void add(String s){
		dictionary.add(s);
	}
	
	public boolean contains(String s){
		return dictionary.contains(s);
	}
	
	public int get(String s){
		return (dictionary.indexOf(s)) + offset;
	}
	
	public int getSize(){
		return dictionary.size();
	}
}
