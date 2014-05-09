package com.camelcasing.image.gif.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.camelcasing.image.gif.CreateGIF;

public class AniGifTest {

		private static Logger logger = Logger.getLogger(AniGifTest.class);
	
	@Test
	public void createAniGIF(){
		try{
			BufferedImage i = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image1.png"));
			BufferedImage i1 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image2.png"));
			BufferedImage i2 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image3.png"));
			BufferedImage i3 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image4.png"));
			BufferedImage i4 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image5.png"));
			BufferedImage i5 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image6.png"));
			BufferedImage i6 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image7.png"));
			BufferedImage i7 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image8.png"));
			BufferedImage i8 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image9.png"));
			BufferedImage i9 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image10.png"));
			BufferedImage i10 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image11.png"));
			BufferedImage i11 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image12.png"));
			BufferedImage i12 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/image13.png"));
			
			new CreateGIF(new File("/media/camelcasing/ExtraDrive/helloworld.gif"), i, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12)
			.setTimeDelays(20)
			.createGIF();			
		}catch(IOException e){
			logger.debug(e.getMessage());
		}
	}
}
