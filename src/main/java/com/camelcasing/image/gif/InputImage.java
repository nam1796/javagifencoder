package com.camelcasing.image.gif;

//import com.camelcasing.image.gifeffect.GifEffect;

/**
 * 
 * @since 1.0
 * @author Philip Teclaff
 */
public interface InputImage{

	int getRGB(int x, int y);
	InputImage resize(int width, int height);
//	
//	void addEffect(GifEffect effect);
//	
//	InputImage setWidth(int width);
	int getWidth();
//	InputImage setHeight(int height);
	int getHeight();
//	
//	InputImage setTimeDelay(int timeDelay);
//	int getTimeDelay();
//	InputImage setMaxColours(int maxColours);
//	int getMaxColours();
//	
//	InputImage setOffSetLeft(int offSetLeft);
//	int getOffSetLeft();
//	InputImage setOffSetTop(int offSetTop);
//	int getOffSetTop();
//	
//	InputImage setGraphicsControlDisposalMethod(int disposalMethod);
//	int getGraphicsControlDisposalMethod();
//	
//	InputImage enableTransparency(int pixel);
//	InputImage disableTanspanency();
//	int getTransparencyIndex(int index);
}
