package com.camelcasing.image.gif;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.camelcasing.image.lwzcompression.LZWCompressor;
import com.camelcasing.image.octreecolourquantilizer.OctreeColourQuantilizer;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class ImageData {
		
		private Logger logger = Logger.getLogger(getClass());
	
		private int timeDelay;
		private OctreeColourQuantilizer quantilizer;
		private ArrayList<Integer> imageData;
		private int colourCount;
		private int colourTableSize;
		private ArrayList<int[]> colourBitArray;
		private int width;
		private int height;
		private int bitSize;
		private LZWCompressor compressor;
		private int graphicsControlDisposalMethod = 1;
		private boolean transparency = false;
		private boolean localColourTable;
		private int offsetLeft = 0;
		private int offsetTop = 0;
		
	/**
	 * @param timeDelay length of time image is on screen must be between 0 and 65535, maybe overridden by <code>GIFOptions</code>
	 * @param localColourTable true if local colour table is to be added to image
	 * @param width width of the image maybe overridden by <code>GIFOptions</code>
	 * @param height height of the image maybe overridden by <code>GIFOptions</code>
	 * @param quantilizer the OctreeColourQuantilizer associated with this image, as may be shared if using Global Colour Table
	 * @param colourBitArray lsit of the original RGB values from the image (before quantilization)
	 */
	 //* @param gifOptions optional specific requirments for the image processing, use null to use defaults {@link com.camelcasing.image.gif.GIFOptions}
	 //*/
	public ImageData(int timeDelay, boolean localColourTable, int width, int height, OctreeColourQuantilizer quantilizer,
			ArrayList<int[]> colourBitArray){
		this.width = width;
		this.height = height;
//		this.image = image;
		this.quantilizer = quantilizer;
		this.colourBitArray = colourBitArray;
		this.timeDelay = timeDelay;
		this.localColourTable = localColourTable;
		this.colourTableSize = 0;
		imageData = new ArrayList<Integer>();
	}
	
	public ImageData init(){
//		checkImageSize();
//		if(quantilizer == null){
//			quantilizer = new OctreeColourQuantilizer(maxColours);
//		}
		finalizeQuantilization();
		addGraphicsControlExtensionBytes();
		addImageDescriptorBytes();
		if(localColourTable){
			addColourTableBytes();
		}
		compressImage();
		addCompressedImageBytes();
		return this;
	}
	
	private void finalizeQuantilization(){
		colourCount = quantilizer.getColourCount();
		colourTableSize = GIFUtils.getColourTableSize(colourCount);
	}
	
	private void addGraphicsControlExtensionBytes(){
		int[] graphicsControlExtensionBytes = 
				new GraphicControlExtension(graphicsControlDisposalMethod, false, transparency, timeDelay, 0).getGraphicControlExtension();
		for(int i : graphicsControlExtensionBytes) imageData.add(i);
	}
	
	private void addImageDescriptorBytes(){
		ImageDescriptorFields imageDescriptorFields = new ImageDescriptorFields(localColourTable, false, false, 
				localColourTable ? colourTableSize : 0);
		logger.debug("colour table size = " + localColourTable != null ? colourTableSize : 0);
		ImageDescriptor imageDescriptor = new ImageDescriptor(offsetLeft, offsetTop, width, height, imageDescriptorFields);
		int[] imageDescriptorBytes = imageDescriptor.getImageDescriptor();
			for(int i : imageDescriptorBytes) imageData.add(i);
	}
		
	private void addColourTableBytes(){
		int[][] colourPalette = quantilizer.getColourPalette();
//		logger.debug("ColourPalette size retrived from quantilizer = " + colourPalette.length);
		ColourTable colourTable = new ColourTable(colourTableSize);
		colourTable.populateTableFromPalette(colourPalette);
		int[] colours = colourTable.getColourTable();
			for(int i : colours) imageData.add(i);
		
	}
	
	private void compressImage(){
		bitSize = GIFUtils.numberOfBitsRequired(colourTableSize);
		int[] rawInput = quantilizer.getQuantilizedInput(colourBitArray);
		compressor = new LZWCompressor(rawInput, bitSize);
		compressor.compress();
	}
	
	private void addCompressedImageBytes(){
		imageData.add(bitSize);
		for(int i : compressor.getPackagedBytes()) imageData.add(i);
		imageData.add(0);
	}
	
//	private InputImage checkImageSize(InputImage image){
//		if(image.getWidth() != width || image.getHeight() != height) {
//			logger.debug("image needed resizing");
//			image.resize(width, height);
//		}
//		return image;
//	}
	
	public ArrayList<Integer> getImageData(){
		return imageData;
	}
}
