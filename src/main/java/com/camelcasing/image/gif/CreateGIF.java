package com.camelcasing.image.gif;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CreateGIF{

		private int width;
		private int height;
		private int maxColours = 256;
		private BufferedImage[] images;
		private int timeDelay = 50;
		private int colorTableSize = 7;
		private File outputFile;
		private OctreeColorQuantilizer[] colorQuantilizers;
		private LogicalScreenDescriptor logicalScreenDescriptor;
		private int[][] imageData;
		
		private Logger logger = Logger.getLogger(getClass());
	
	public CreateGIF(File outputFile, BufferedImage... bufferedImages){
		this.images = bufferedImages;
		this.imageData = new int[images.length][];
		logger.debug("images.length = " + images.length);
		this.outputFile = outputFile;
		width = images[0].getWidth();
		height = images[0].getHeight();
	}
	
	public boolean createGIF(){
		quantilizeImages();
		logicalScreenDescriptor = createLogicalScreenDescriptor();
			for(int i = 0; i < images.length; i++){
				imageData[i] = getImageData(images[i], i);
			}
		
		writeBytes();
		return true;
	}
	
	private void quantilizeImages(){
		colorQuantilizers = new OctreeColorQuantilizer[images.length];
			for(int i = 0; i < colorQuantilizers.length; i++){
				colorQuantilizers[i] = new OctreeColorQuantilizer(images[i], 256);
				colorQuantilizers[i].quantilize();
			}
	}
	
	private LogicalScreenDescriptor createLogicalScreenDescriptor(){
		ScreenDescriptorField sdf = new ScreenDescriptorField(false, 1, false, 0);
		LogicalScreenDescriptor lsd = new LogicalScreenDescriptor(width, height, sdf, 0, 0);
		return lsd;
	}
	
	private int[] getImageData(BufferedImage b, int index){

		int[] graphExtensionBytes1 = new GraphicControlExtension(1, false, false, timeDelay, 0).getGraphicControlExtension();
		
			ImageDescriptorFields idf = new ImageDescriptorFields(true, false, false, 7);
			ImageDescriptor id = new ImageDescriptor(0, 0, width, height, idf);
		
		int[] imageDescriptorBytes = id.getImageDescriptor();
		
			int[] rawInput = colorQuantilizers[index].getQuantilizedInput();
		
			int[][] colorPalette = colorQuantilizers[index].getColorPalette();
			GlobalColorTable ct = new GlobalColorTable(colorTableSize);
				for(int i = 0; i < colorPalette.length; i++){
					ct.addColor(colorPalette[i][0], colorPalette[i][1], colorPalette[i][2]);
				}
			int extraColor = (int)Math.pow(2, colorTableSize + 1);
				for(int i = colorPalette.length; i < extraColor; i++){
					ct.addColor(0,  0,  0);
				}
		
		int[] colorTable = ct.getColorTable();
		
			int bitSize = GIFUtils.numberOfBitsRequired(ct.getColorTableSize());
			LZWCompressor compressor = new LZWCompressor(rawInput, bitSize);
			ArrayList<Integer> packagedBytes = compressor.getPackageBytes();
		Object[] packagedBytesArray = packagedBytes.toArray();
		int end = 0;
		
		int[]returnBytes = new int[graphExtensionBytes1.length + imageDescriptorBytes.length + colorTable.length + packagedBytesArray.length + 2];
		System.arraycopy(graphExtensionBytes1, 0, returnBytes, 0, graphExtensionBytes1.length);
		System.arraycopy(imageDescriptorBytes, 0, returnBytes, graphExtensionBytes1.length, imageDescriptorBytes.length);
		int g = imageDescriptorBytes.length + graphExtensionBytes1.length;
		System.arraycopy(colorTable, 0, returnBytes, g, colorTable.length);
		g += colorTable.length;
		returnBytes[g] = bitSize;
		g++;
			for(int i = 0; i < packagedBytesArray.length; i++){
				returnBytes[g] = (int)packagedBytesArray[i];
				g++;
			}
		//System.arraycopy(packagedBytesArray, 0, returnBytes, g, packagedBytesArray.length);
		returnBytes[returnBytes.length - 1] = end;
		logger.debug("returnBytes.length - 2 = " + returnBytes[returnBytes.length - 2]);
		
		return returnBytes;
	}
	
	private void writeBytes(){
		try(OutputStream os = new FileOutputStream(outputFile)){
			for(int i : Headers.getHeader89a()) os.write(i);
			for(int i : logicalScreenDescriptor.getLogicalScreenDescriptor()) os.write(i);
			for(int i : new NetscapeApplicationExtension(0).create()) os.write(i);
			for(int i = 0; i < imageData.length; i++){
				for(int j : imageData[i]) os.write(j);
			}
			os.write(Headers.getTrailer()); //changed from byte cast to integer
		}catch(IOException e){
			logger.fatal("Failed to write bytes to file\n" + e.getMessage());
		}
	}
	
	public void setMaxColours(int maxColours) {
		this.maxColours = maxColours;
	}
	public void setTimeDelays(int timeDelay) {
		this.timeDelay = timeDelay;
	}
	public void setColorTableSize(int colorTableSize) {
		this.colorTableSize = colorTableSize;
	}
}
