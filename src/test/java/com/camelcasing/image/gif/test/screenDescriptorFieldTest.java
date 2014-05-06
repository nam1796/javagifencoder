package com.camelcasing.image.gif.test;

import org.junit.Test;

import com.camelcasing.image.gif.ScreenDescriptorField;

import static org.junit.Assert.assertEquals;

public class screenDescriptorFieldTest{
		
	@Test (expected=IllegalArgumentException.class)
	public void testScreenDescriptorField(){
		new ScreenDescriptorField(false, 15, false, (byte)5);
	}
		
	@Test (expected=IllegalArgumentException.class)
	public void testScreenDescriptorField2(){
		new ScreenDescriptorField(false, 7, false, (byte)8);
	}
		
	@Test
	public void testScreenDescriptorField3(){
		new ScreenDescriptorField(false, 7, false, (byte)0);
	}
	
	@Test
	public void resultTest(){	
		assertEquals(255, new ScreenDescriptorField(true, (byte)7, true, (byte)7).create());
	}
	
	@Test
	public void resultTest1(){	
		assertEquals(45, new ScreenDescriptorField(false, (byte)2, true, (byte)5).create());
	}
	
	@Test
	public void resultTest2(){	
		assertEquals(119, new ScreenDescriptorField(false, (byte)7, false, (byte)7).create());
	}
	
	@Test
	public void resultTest3(){	
		assertEquals(51, new ScreenDescriptorField(false, (byte)3, false, (byte)3).create());
	}
	
	@Test
	public void resultTest4(){	
		assertEquals(0, new ScreenDescriptorField(false, (byte)0, false, (byte)0).create());
	}
}
