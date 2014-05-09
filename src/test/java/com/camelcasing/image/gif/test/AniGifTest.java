package com.camelcasing.image.gif.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.camelcasing.image.gif.CreateGIF;

import static org.junit.Assert.assertTrue;

public class AniGifTest {

		private static Logger logger = Logger.getLogger(AniGifTest.class);
	
	@Test
	public void createAniGIF(){
		try{
			BufferedImage image = ImageIO.read(new File("/media/camelcasing/ExtraDrive/aniGif1.png"));
			BufferedImage image1 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/aniGif2.png"));
			BufferedImage image2 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/aniGif3.png"));
			BufferedImage image3 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/aniGif4.png"));
			BufferedImage image4 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/aniGif5.png"));
			BufferedImage image5 = ImageIO.read(new File("/media/camelcasing/ExtraDrive/aniGif6.png"));
			
			CreateGIF cg = new CreateGIF(new File("/media/camelcasing/ExtraDrive/hellogif.gif"), image, image1, image2, image3, image4, image5);
			assertTrue(cg.createGIF());
			
		}catch(IOException e){
			logger.debug(e.getMessage());
		}
	}
}
