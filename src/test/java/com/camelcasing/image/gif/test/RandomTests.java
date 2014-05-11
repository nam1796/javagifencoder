package com.camelcasing.image.gif.test;

import org.junit.Test;

import com.camelcasing.image.gif.GIFUtils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class RandomTests {
	
	
	@Test
	public void IntegerDivision(){
		int i = 1020 / 255;
		System.out.println(i);
	}
	
	@Test
	public void logTest(){
		assertEquals(7, (int)((Math.log(256))/Math.log(2)) - 1);
	}
}
