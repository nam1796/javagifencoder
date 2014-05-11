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

public class AniGifTest {

		private static Logger logger = Logger.getLogger(AniGifTest.class);
	
	@Test
	public void createAniGIF(){
		try{
			InputImage i = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image1.png")));
			InputImage i1 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image2.png")));
			InputImage i2 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image3.png")));
			InputImage i3 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image4.png")));
			InputImage i4 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image5.png")));
			InputImage i5 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image6.png")));
			InputImage i6 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image7.png")));
			InputImage i7 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image8.png")));
			InputImage i8 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image9.png")));
			InputImage i9 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image10.png")));
			InputImage i10 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image11.png")));
			InputImage i11 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image12.png")));
			InputImage i12 = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/image13.png")));
			
			new CreateGIF(new File("/media/camelcasing/ExtraDrive/helloworld.gif"), i, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12)
			//.addGIFOptions(new GIFOptions().setTimeDelay(120), 12)
			.createGIF();			
		}catch(IOException e){
			logger.debug(e.getMessage());
		}
	}


	public void singleImageTest(){
		try{
			InputImage i = new InputBufferedImage(ImageIO.read(new File("/media/camelcasing/ExtraDrive/Ricci.jpg")));
			new CreateGIF(new File("/media/camelcasing/ExtraDrive/cCole.gif"),192, 144, i)
			//.addGIFOptions(new GIFOptions().setMaxColours(30), 0)
			.createGIF();
		}catch(IOException e){
			logger.debug(e.getMessage());
		}
	}
}
