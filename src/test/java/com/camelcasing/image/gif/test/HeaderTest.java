package com.camelcasing.image.gif.test;

import org.junit.*;

import com.camelcasing.image.gif.Headers;

import static org.junit.Assert.assertEquals;

public class HeaderTest{

	@Test
	public void testHeaderBytes(){
		
		
		int[] bytes = Headers.getHeader89a();
		
		assertEquals("1000111", Integer.toBinaryString(bytes[0])); 
		assertEquals("1001001", Integer.toBinaryString(bytes[1]));
		assertEquals("1000110", Integer.toBinaryString(bytes[2]));
		assertEquals("111000", Integer.toBinaryString(bytes[3]));
		assertEquals("111001", Integer.toBinaryString(bytes[4]));
		assertEquals("1100001", Integer.toBinaryString(bytes[5]));
		
	}
}
