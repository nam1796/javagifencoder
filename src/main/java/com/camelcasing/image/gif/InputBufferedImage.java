package com.camelcasing.image.gif;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class InputBufferedImage implements InputImage{

		private BufferedImage image;
	
	public InputBufferedImage(BufferedImage image){
		this.image = image;
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
	public void resize(int width, int height) {
		Image img = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			if(img instanceof BufferedImage){
				image = (BufferedImage) img;
				return;
			}
		BufferedImage i = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = i.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();
		image = i;
		return;
	}

	@Override
	public void addText(String text, int location) {
		Graphics2D g2d = image.createGraphics();
		//unimplemented
		g2d.dispose();
	}
}
