package com.camelcasing.image.gif;

/**
 * Required for every image, may optionally be <b>preceded</b> by a <code>Control Block</code> 
 * for example the <code>Graphics Control Block</code><br />
 * {@link com.camelcasing.image.gif.ImageDescriptor} is always followed by {@link com.camelcasing.image.gif.ImageData} 
 * <br /><br />
 * Contains 10 bytes<br />
 * byte 1 = Image Separator (fixed value of 0x2C) retrieved from {@link com.camelcasing.image.gif.Headers#getImageSeparator()}<br />
 * bytes 2-3 = Image left Position<br />
 * bytes 4-5 = Image right Position<br />
 * bytes 6-7 = Image Width<br />
 * bytes 8-9 = Image Height<br />
 * byte 10 = {@link com.camelcasing.image.gif.ImageDescriptorFields}
 * 
 * 
 * @author camelCasing
 *
 */
public class ImageDescriptor{
	
		private int[] imageDescriptor;
		private int imageLeftPosition;
		private int imageRightPosition;
		private int imageWidth;
		private int imageHeight;
		private ImageDescriptorFields packedFields;
	
	/**
	 * @param imgLeftPos 
	 * @param imgRightPos
	 * @param imgWidth
	 * @param imgHt
	 * @param pkedFlds
	 */
	public ImageDescriptor(int imgLeftPos, int imgRightPos, int imgWidth, int imgHt, ImageDescriptorFields pkedFlds){
		imageLeftPosition = imgLeftPos;
		imageRightPosition = imgRightPos;
		imageWidth = imgWidth;
		imageHeight = imgHt;
		packedFields = pkedFlds;
		
		validate();
		create();
	}
	
	/**
	 * Check for correct value ranges.
	 * @return true if does not throw an {@link java.lang.IllegalArgumentException}
	 */
	private boolean validate(){
		return false;
	}
	
	/**
	 * Creates the byte array of 10 bytes from the bytes passed to the constructor
	 */
	private void create(){
		int width1 = Integer.valueOf(GIFUtils.getBitsFromInt(imageWidth, 8), 2);
		int width2 = Integer.valueOf(GIFUtils.getBitsFromInt(imageWidth, 8, 8), 2);
		int height1 = Integer.valueOf(GIFUtils.getBitsFromInt(imageHeight, 8), 2);
		int height2 = Integer.valueOf(GIFUtils.getBitsFromInt(imageHeight, 8, 8), 2);
		int posLeft1 = Integer.valueOf(GIFUtils.getBitsFromInt(imageLeftPosition, 8), 2);
		int posLeft2 = Integer.valueOf(GIFUtils.getBitsFromInt(imageLeftPosition, 8, 8), 2);
		int posRight1 = Integer.valueOf(GIFUtils.getBitsFromInt(imageRightPosition, 8), 2);
		int posRight2 = Integer.valueOf(GIFUtils.getBitsFromInt(imageRightPosition, 8, 8), 2);
		
		imageDescriptor = new int[10];
		
		imageDescriptor[0] = Headers.getImageSeparator();
		imageDescriptor[1] = posLeft1;
		imageDescriptor[2] = posLeft2;
		imageDescriptor[3] = posRight1;
		imageDescriptor[4] = posRight2;
		imageDescriptor[5] = width1;
		imageDescriptor[6] = width2;
		imageDescriptor[7] = height1;
		imageDescriptor[8] = height2;
		imageDescriptor[9] = packedFields.create();
	}
	
	/**
	 * @return Array of 10 bytes that represent the values passed to constructor 
	 */
	public int[] getImageDescriptor(){
		return imageDescriptor;
	}
}
