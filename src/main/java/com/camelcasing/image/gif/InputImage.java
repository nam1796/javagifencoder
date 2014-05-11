package com.camelcasing.image.gif;

public interface InputImage {

	int getRGB(int x, int y);
	int getWidth();
	int getHeight();
	void resize(int width, int height);
	void addText(String text,  int location);
}
