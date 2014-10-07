package com.camelcasing.image.gif.test;

import org.junit.*;

import com.camelcasing.image.gif.GraphicControlExtension;
import com.camelcasing.image.gif.NetscapeApplicationExtension;
import com.camelcasing.image.lwzcompression.InputColour;

import static org.junit.Assert.assertTrue;
	
public class GraphicControlTest{

	@Test
	public void test1(){
		GraphicControlExtension gce = new GraphicControlExtension(0, false, false, 0, 0);
		InputColour gcResult = new InputColour(gce.getGraphicControlExtension());	
		InputColour shouldBe = new InputColour(new int[]{0x21, 0xF9, 0x04, 0, 0, 0, 0, 0});
		assertTrue(gcResult.equals(shouldBe));
	}
	
	@Test
	public void test2(){
		NetscapeApplicationExtension ae = new NetscapeApplicationExtension(0); 
		InputColour gcResult = new InputColour(ae.create());
		InputColour shouldBe = new InputColour(new int[]{0x21, 0xFF, 0x0B, 0x4E, 0x45, 0x54, 0x53, 0x43, 0x41, 0x50, 0x45, 0x32, 0x2E, 0x30, 0x03, 0x01, 0x00, 0x00, 0x00});
		assertTrue(gcResult.equals(shouldBe));
	}
}
