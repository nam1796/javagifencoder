package com.camelcasing.image.gif;

/**
 * 
 * @since 1.0
 * @author Philip Teclaff
 */
public interface InputImage{

	int getRGB(int x, int y);
	int getWidth();
	int getHeight();
	void resize(int width, int height);
	void addText(String text,  int location);
	InputImage addGIFOptions(GIFOptions gifOptions);
	GIFOptions getGIFOptions();
}
