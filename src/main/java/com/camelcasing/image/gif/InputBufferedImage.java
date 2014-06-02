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

public class InputBufferedImage implements InputImage{

		private BufferedImage image;
		private Logger logger = Logger.getLogger(getClass());
		
		private static int fontSize = 1;
	
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

	@Override
	public void addText(String text, int location) {
		Graphics2D g2d = image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setFont(new Font("SansSerif", Font.BOLD, fontSize));
		g2d.setColor(Color.WHITE);
		FontMetrics fm;
		int widthMax = getWidth();
		int heightMax = getHeight() / 4;
			while(true){
				fm = g2d.getFontMetrics();
				if(fm.getHeight() > heightMax || fm.stringWidth(text) > widthMax){
					fontSize--;
					break;
				}
				g2d.setFont(new Font("Impact", Font.BOLD, ++fontSize));
			}
		fm = g2d.getFontMetrics();
		g2d.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, getHeight() - 6);// - fm.getHeight());
		g2d.dispose();
	}
}
