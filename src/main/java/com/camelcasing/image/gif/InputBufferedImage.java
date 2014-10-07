package com.camelcasing.image.gif;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class InputBufferedImage implements InputImage{

		private BufferedImage image;
		private Logger logger = Logger.getLogger(getClass());
	
	public InputBufferedImage(BufferedImage image){
		this.image = image;
	}
	
	public InputBufferedImage(String filePath){
		try{
			image = ImageIO.read(new File(filePath));
		}catch(IOException e){
			logger.error("A problem occured with file: " + filePath + "\n" + e.getMessage());
		}
	}

	@Override
	public int getRGB(int x, int y) {
		return image.getRGB(x, y);
	}

	@Override
	public int getWidth() {
		return image.getWidth();
	}

	@Override
	public int getHeight() {
		return image.getHeight();
	}

	@Override
	public void resize(int myWidth, int myHeight) {
		BufferedImage i = new BufferedImage(myWidth, myHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = i.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.drawImage(image, 0, 0, myWidth, myHeight, null);
		g2d.dispose();
		image = i;
		logger.debug("returned rebuilt image");
		return;
	}
}
