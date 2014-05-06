package com.camelcasing.image.gif;

/**
 * Contains 7 bytes,<br />
 * byte 1-2 = width<br />
 * byte 3-4 = height<br />
 * byte 5 = {@link com.camelcasing.image.gif.ScreenDescriptorField}<br />
 * byte 6 = Background colour index<br />
 * byte 7 = pixel aspect ratio 
 */
public class LogicalScreenDescriptor{

	private int[] logicalScreenDescriptor;
	/**
	 * The width of the GIF, represented by 2 bytes (Max size of 4095 pixels)
	 * **NOTE** if reading in bytes second byte read will require a bitwise shift left (<<) 8 bits then add to bytes for int value.
	 */
	private int width;
	/**
	 * The height of the GIF, represented by 2 bytes (Max size of 4095 pixels)
	 */
	private int height;
	/**
	 * Index into the 'Global Colour Table' for the colour used for pixels on the screen that are not cover by an image. 
	 * If the 'Global Colour Table Flag' is 0 then this should be 0.
	 */
	private int bgColorIndex;
	/**
	 * Used on square pixels. formula = Aspect Ratio = (Pixel Aspect Ratio + 16) / 64.
	 * Widest pixel allowed = 4:1 and Tallest allowed = 1:4. A value of 0 means no aspect ratio information was given.
	 */
	private int pixelAspectRatio;
	/**
	 * Contains values for, Colour Table flag, Colour Resolution, Sort Flag and ColourTableSize 
	 * {@link com.camelcasing.image.gif.ScreenDescriptorField}
	 */
	private ScreenDescriptorField packedFields;
	
	/**
	 * 
	 * 
	 * @param width The width of the GIF, represented by 2 bytes (Max size of 4095 pixels)
	 * @param height The height of the GIF, represented by 2 bytes (Max size of 4095 pixels)
	 * @param packedFields Contains values for, Colour Table flag, Colour Resolution, Sort Flag and ColourTableSize
	 * @param bgColorIndexIndex into the 'Global Colour Table' for the colour used for pixels on the screen that are not cover by an image. 
	 * If the 'Global Colour Table Flag' is 0 then this should be 0.
	 * @param pixelAspectRatio Used on square pixels. formula = Aspect Ratio = (Pixel Aspect Ratio + 16) / 64.
	 * Widest pixel allowed = 4:1 and Tallest allowed = 1:4. A value of 0 means no aspect ratio information was given.
	 */
	public LogicalScreenDescriptor(int width, int height, ScreenDescriptorField packedFields, int bgColorIndex, int pixelAspectRatio){
		this.width = width;
		this.height = height;
		this.packedFields = packedFields;
		this.bgColorIndex = bgColorIndex;
		this.pixelAspectRatio = pixelAspectRatio;
			validate();
			create();
	}
	
	
	/**
	 * Check for correct value ranges.
	 */
	private void validate(){
		if(width > 4095 || width < 1) throw new IllegalArgumentException("Width exceeds limit of 4095 or is less then 1");
		if(height > 4095 || height < 1) throw new IllegalArgumentException("Height exceeds limit of 4095 oe is less then 1");
		if(bgColorIndex > Math.pow(2, packedFields.getColorTableSize() + 1) || bgColorIndex < 0){
			throw new IllegalArgumentException("bgColorIndex is outside of range designated in colorTableSize field");
		}
		if(pixelAspectRatio > 255 | pixelAspectRatio < 0) throw new IllegalArgumentException("PixelAspectRatio must be with range of 0-255");
	}
	
	/**
	 * Creates the byte array of 7 bytes from the byte passed to the constructor
	 */
	private void create(){
		int width1 = Integer.valueOf(GIFUtils.getBitsFromInt(width, 8), 2);
		int width2 = Integer.valueOf(GIFUtils.getBitsFromInt(width, 8, 8), 2);
		int height1 = Integer.valueOf(GIFUtils.getBitsFromInt(height, 8), 2);
		int height2 = Integer.valueOf(GIFUtils.getBitsFromInt(height, 8, 8), 2);
		
		logicalScreenDescriptor = new int[7]; 
		logicalScreenDescriptor[0] = width1;
		logicalScreenDescriptor[1] = width2;
		logicalScreenDescriptor[2] = height1;
		logicalScreenDescriptor[3] = height2;
		logicalScreenDescriptor[4] = packedFields.create();
		logicalScreenDescriptor[5] = bgColorIndex;
		logicalScreenDescriptor[6] = pixelAspectRatio;
	}
	
	/**
	 * @return Array of 7 bytes that represent the values passed to constructor 
	 */
	public int[] getLogicalScreenDescriptor(){
		return logicalScreenDescriptor;
	}
}
