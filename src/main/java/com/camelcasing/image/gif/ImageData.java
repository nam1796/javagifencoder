package com.camelcasing.image.gif;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageData {

		private int timeDelay;
		private BufferedImage image;
		private OctreeColorQuantilizer quantilizer;
		private ArrayList<Integer> imageData;
		private int maxColors;
		private int width;
		private int height;
		private int bitSize;
		private LZWCompressor compressor;
		private GIFOptions gifOptions;
		
	/**
	 * @param image The image that the returning byte represent
	 * @param timeDelay length of time image is on screen must be between 0 and 65535, maybe overridden by <code>GIFOptions</code>
	 * @param maxColors maximum amount of colours used in this image, maybe overridden by <code>GIFOptions</code>
	 * @param width width of the image maybe overridden by <code>GIFOptions</code>
	 * @param height height of the image maybe overridden by <code>GIFOptions</code>
	 * @param gifOptions optional specific requirments for the image processing, use null to use defaults {@link com.camelcasing.image.gif.GIFOptions}
	 */
	public ImageData(BufferedImage image, int timeDelay, int maxColors, int width, int height, GIFOptions gifOptions){
		this.width = width;
		this.height = height;
		this.image = image;
		this.maxColors = maxColors;
		this.timeDelay = timeDelay;
		this.gifOptions = gifOptions;
		//this.bitSize = getBitsize();
		this.bitSize = 7;
		imageData = new ArrayList<Integer>();
	}
	
	public ImageData init(){
			if(gifOptions != null) getGIFOptions();
		quantilizeImage();
		addGraphicsControlExtensionBytes();
		addImageDescriptorBytes();
		addColorTableBytes();
		addCompressedImageBytes();
		return this;
	}
	
	private void getGIFOptions(){
		
	}
	
	private int getBitsize(){
		if(maxColors == 256) return 7;
		if(maxColors >= 128) return 6;
		if(maxColors >= 64) return 5;
		if(maxColors >= 32) return 4;
		if(maxColors >= 16) return 3;
		return 2;
	}
	
	private void quantilizeImage(){
		quantilizer = new OctreeColorQuantilizer(image, 256).quantilize();
	}
	
	public void addGraphicsControlExtensionBytes(){
		int[] graphicsControlExtensionBytes = new GraphicControlExtension(1, false, false, 20, 0).getGraphicControlExtension();
		for(int i : graphicsControlExtensionBytes) imageData.add(i);
	}
	
	public void addImageDescriptorBytes(){
		ImageDescriptorFields imageDescriptorFields = new ImageDescriptorFields(true, false, false, 7);
		ImageDescriptor imageDescriptor = new ImageDescriptor(0, 0, width, height, imageDescriptorFields);
		int[] imageDescriptorBytes = imageDescriptor.getImageDescriptor();
			for(int i : imageDescriptorBytes) imageData.add(i);
	}
		
	public void addColorTableBytes(){
		int[] rawInput = quantilizer.getQuantilizedInput();
		compressor = new LZWCompressor(rawInput, bitSize);
		
		int[][] colorPalette = quantilizer.getColorPalette();
		ColorTable colorTable = new ColorTable(7); //could cause problems
			for(int i = 0; i < colorPalette.length; i++){
				colorTable.addColor(colorPalette[i][0], colorPalette[i][1], colorPalette[i][2]);
			}
		int extraColor = (int)Math.pow(2, bitSize + 1);
			for(int i = colorPalette.length; i < extraColor; i++){
				colorTable.addColor(0,  0,  0);
			}
		int[] colors = colorTable.getColorTable();
			for(int i : colors) imageData.add(i);
	}
	
	public void addCompressedImageBytes(){
		ArrayList<Integer> packagedBytes = compressor.getPackageBytes();
		imageData.add(bitSize);
			for(int i : packagedBytes) imageData.add(i);
		imageData.add(0);
	}
	
	public ArrayList<Integer> getImageData(){
		return imageData;
	}
}
