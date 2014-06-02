package com.camelcasing.image.gif.test;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.camelcasing.image.gif.CreateGIF;
import com.camelcasing.image.gif.GIFOptions;
import com.camelcasing.image.gif.InputImage;
import com.camelcasing.image.gif.InputBufferedImage;
import com.camelcasing.image.gif.GIFOptions.MemeLocation;

public class AniGifTest {

		private static Logger logger = Logger.getLogger(AniGifTest.class);
	
	
	public void createAniGIF(){
		InputImage i = new InputBufferedImage("/media/camelcasing/ExtraDrive/image1.png");
		InputImage i1 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image2.png");
		InputImage i2 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image3.png");
		InputImage i3 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image4.png");
		InputImage i4 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image5.png");
		InputImage i5 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image6.png");
		InputImage i6 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image7.png");
		InputImage i7 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image8.png");
		InputImage i8 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image9.png");
		InputImage i9 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image10.png");
		InputImage i10 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image11.png");
		InputImage i11 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image12.png");
		InputImage i12 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image13.png");
			
		new CreateGIF("/media/camelcasing/ExtraDrive/helloworld.gif",i, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12)
		.addGIFOptions(new GIFOptions().setTimeDelay(300).setMemeText("MarsBar anyone?").setMemeLocation(MemeLocation.BOTTOM), 12)
		.setMaxColours(100)
		.create();			
	}
	
	

	public void createAniScaledGIF(){
		InputImage i = new InputBufferedImage("/media/camelcasing/ExtraDrive/image1l.png");
		InputImage i1 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image2l.png");
		InputImage i2 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image3l.png");
		InputImage i3 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image4l.png");
		InputImage i4 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image5l.png");
		InputImage i5 = new InputBufferedImage("/media/camelcasing/ExtraDrive/image6l.png");
			
		new CreateGIF("/media/camelcasing/ExtraDrive/helloworld_scaled.gif", 650, 550, i, i1, i2, i3, i4, i5)
		.addGIFOptions(new GIFOptions().setTimeDelay(300).setMemeText("Resized Image!").setMemeLocation(MemeLocation.BOTTOM), 5)
		.create();			
	}
	
	public void createAniGIF2(){
		try{
			InputImage i = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/traffic1.png")));
			InputImage i1 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/traffic2.png")));
			InputImage i2 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/traffic3.png")));
			
			new CreateGIF("/media/camelcasing/ExtraDrive/traffic.gif", 25, 50, i, i1, i2)
			.addGIFOptions(new GIFOptions().setTimeDelay(120), 2)
			.create();			
		}catch(IOException e){
			logger.debug(e.getMessage());
		}
	}

	@Test
	public void singleImageTest(){
		try{
			InputImage i = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image13.png")));
			new CreateGIF("/media/camelcasing/ExtraDrive/simgleImage.gif", 650, 550, i)
			.addGIFOptions(new GIFOptions().setMemeText("Text"), 0)
			//.setMaxColours(10)
			.create();
		}catch(IOException e){
			logger.debug(e.getMessage());
		}
	}
}
