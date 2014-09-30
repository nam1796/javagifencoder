package com.camelcasing.image.gif.test;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.camelcasing.image.gif.CreateGIF;
import com.camelcasing.image.gif.GIFOptions;
import com.camelcasing.image.gif.InputImage;
import com.camelcasing.image.gif.InputBufferedImage;
import com.camelcasing.image.gif.GIFOptions.MemeLocation;

public class AniGifTest {

		private static Logger logger = Logger.getLogger(AniGifTest.class);
	
	//@Test
	public void createAniGIF(){
		InputImage i = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image1.png");
		InputImage i1 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image2.png");
		InputImage i2 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image3.png");
		InputImage i3 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image4.png");
		InputImage i4 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image5.png");
		InputImage i5 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image6.png");
			//.addGIFOptions(new GIFOptions().setTimeDelay(200).setMemeText("interval"));
		InputImage i6 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image7.png");
		InputImage i7 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image8.png");
		InputImage i8 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image9.png");
		InputImage i9 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image10.png");
		InputImage i10 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image11.png");
		InputImage i11 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image12.png");
		InputImage i12 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/image13.png");
			//.addGIFOptions(new GIFOptions().setTimeDelay(300).setMemeText("MarsBar anyone?").setMemeLocation(MemeLocation.BOTTOM));
			
		new CreateGIF("/media/camelcasing/ExtraDrive/helloworld.gif", 1300, 1100, i, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12)
		.create();			
	}

	//@Test
	public void singleImageTest(){

		InputImage i1 = new InputBufferedImage("/media/camelcasing/ExtraDrive/Picture610.jpg");
		InputImage i2 = new InputBufferedImage("/media/camelcasing/ExtraDrive/Picture611.jpg");
		InputImage i3 = new InputBufferedImage("/media/camelcasing/ExtraDrive/Picture612.jpg");
		new CreateGIF("/media/camelcasing/ExtraDrive/singleImage.gif", i1, i2, i3)
//		.addGIFOptions(new GIFOptions().setMemeText("Text"), 0)
//		.setMaxColours(10)
		.create();
	}
	
	//@Test
	public void allSameImageTest(){
		InputImage i1 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate1.png");
		InputImage i2 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate2.png");
		InputImage i3 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate3.png");
		InputImage i4 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate4.png");
		InputImage i5 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate5.png");
		InputImage i6 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate6.png");
		InputImage i7 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate7.png");
		InputImage i8 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate8.png");
		InputImage i9 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate9.png");
		InputImage i10 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/skate10.png");
		
		new CreateGIF("/media/camelcasing/ExtraDrive/skate_sequence2.gif", i10,i9,i8,i7,i6,i5,i4,i3,i2,i1).create();
	}
	
	//@Test
	public void yoadTest(){
		InputImage i1 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/yoda1.png");
		InputImage i2 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/yoda2.png");
		InputImage i3 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/yoda3.png");
		InputImage i4 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/yoda4.png");
		InputImage i5 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/yoda5.png");
		
		new CreateGIF("/media/camelcasing/ExtraDrive/yoda_small.gif", 400, 300, i1, i2, i3, i4, i5).create();
	}
	
	//@Test
	public void cherylColeTest(){
		InputImage i1 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/cc1.jpg");
		InputImage i2 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/cc2.jpg");
		InputImage i3 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/cc3.jpg");
		InputImage i4 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/cc4.jpg");
		InputImage i5 = new InputBufferedImage("/media/camelcasing/ExtraDrive/GIFs/gif_source/cc5.jpg");
		new CreateGIF("/media/camelcasing/ExtraDrive/cherylCole5.gif", i1, i2, i3, i4, i5).create();
	}
}
