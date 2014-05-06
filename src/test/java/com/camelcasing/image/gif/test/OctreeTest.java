package com.camelcasing.image.gif.test;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import com.camelcasing.image.gif.OctreeColorQuantilizer;
import org.junit.*;
import static org.junit.Assert.assertArrayEquals;

public class OctreeTest{
		
		@Test
		public void treeCreation(){
			try{
				BufferedImage image = ImageIO.read(new File("/media/camelcasing/ExtraDrive/Backgrounds/orange_eye.jpg"));
				new OctreeColorQuantilizer(image, 256);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
}
