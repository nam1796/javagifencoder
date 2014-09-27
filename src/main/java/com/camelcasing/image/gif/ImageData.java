package com.camelcasing.image.gif;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.camelcasing.image.lwzcompression.LZWCompressor;
import com.camelcasing.image.lwzcompression.LZWCompressorMark2;
import com.camelcasing.image.octreecolourquantilizer.OctreeColourQuantilizer;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class ImageData {
		
		private Logger logger = Logger.getLogger(getClass());
	
		private int timeDelay;
		private InputImage image;
		private OctreeColourQuantilizer quantilizer;
		private ArrayList<Integer> imageData;
		private int maxColours;
		private int colourCount;
		private int colourTableSize;
		private int width;
		private int height;
		private int bitSize;
		private ImageCompressor compressor;
		private GIFOptions gifOptions;
		private int graphicsControlDisposalMethod = 1;
		private int offsetLeft = 0;
		private int offsetTop = 0;
		private String memeText = null;
		
	/**
	 * @param image The image that the returning byte represent
	 * @param timeDelay length of time image is on screen must be between 0 and 65535, maybe overridden by <code>GIFOptions</code>
	 * @param maxColours maximum amount of colours used in this image, maybe overridden by <code>GIFOptions</code>
	 * @param width width of the image maybe overridden by <code>GIFOptions</code>
	 * @param height height of the image maybe overridden by <code>GIFOptions</code>
	 * @param gifOptions optional specific requirments for the image processing, use null to use defaults {@link com.camelcasing.image.gif.GIFOptions}
	 */
	public ImageData(InputImage image, int timeDelay, int maxColours, int width, int height){
		this.width = width;
		this.height = height;
		this.image = image;
		this.maxColours = maxColours;
		this.timeDelay = timeDelay;
		this.gifOptions = image.getGIFOptions();
		imageData = new ArrayList<Integer>();
	}
	
	public ImageData init(){
			//if(gifOptions != null) getGIFOptions();
		checkImageSize();
			//if(memeText != null) addMemeToImage();
		quantilizeImage();
		addGraphicsControlExtensionBytes();
		addImageDescriptorBytes();
		addColourTableBytes();
		compressImage();
		addCompressedImageBytes();
		return this;
	}
	
	private void getGIFOptions(){
		int temp;
		
		if((temp = gifOptions.getTimeDelay()) != -1) timeDelay = temp;
		
		if(gifOptions.getImageSize() != null){
			int[] newSize = gifOptions.getImageSize();
			if(newSize[0] > 10) width = newSize[0];
			if(newSize[1] > 10) height = newSize[1]; 
		}
		
		if(gifOptions.getMemeText() != null) memeText = gifOptions.getMemeText();
		if((temp = gifOptions.getMaxColours()) != -1) maxColours = temp;
		if((temp = gifOptions.getOffsetLeft()) != -1) offsetLeft = temp;
		if((temp = gifOptions.getOffsetTop()) != -1) offsetTop = temp;
		if((temp = gifOptions.getGraphicsControlDisposalMethod()) != -1) graphicsControlDisposalMethod = temp;
	}
	
	private int getColourTableSize(){
		if(colourCount >= 128) return 7;
		if(colourCount >= 64) return 6;
		if(colourCount >= 32) return 5;
		if(colourCount >= 16) return 4;
		if(colourCount >= 8) return 3;
		return 2;
	}
	
	private void quantilizeImage(){
		quantilizer = new OctreeColourQuantilizer(image, maxColours).quantilize();
		colourCount = quantilizer.getColourCount();
		colourTableSize = getColourTableSize();
	}
	
	private void addGraphicsControlExtensionBytes(){
		int[] graphicsControlExtensionBytes = new GraphicControlExtension(graphicsControlDisposalMethod, false, false, timeDelay, 0).getGraphicControlExtension();
		for(int i : graphicsControlExtensionBytes) imageData.add(i);
	}
	
	private void addImageDescriptorBytes(){
		ImageDescriptorFields imageDescriptorFields = new ImageDescriptorFields(true, false, false, colourTableSize);
		ImageDescriptor imageDescriptor = new ImageDescriptor(offsetLeft, offsetTop, width, height, imageDescriptorFields);
		int[] imageDescriptorBytes = imageDescriptor.getImageDescriptor();
			for(int i : imageDescriptorBytes) imageData.add(i);
	}
		
	private void addColourTableBytes(){
		int[][] colourPalette = quantilizer.getColourPalette();
		ColourTable colourTable = new ColourTable(colourTableSize);
			for(int i = 0; i < colourPalette.length; i++){
				colourTable.addColour(colourPalette[i][0], colourPalette[i][1], colourPalette[i][2]);
			}
		int extraColour = (int)Math.pow(2, colourTableSize + 1);
			if(extraColour > colourPalette.length){
				for(int i = colourPalette.length; i < extraColour; i++){
					colourTable.addColour(0,  0,  0);
				}
			}
		int[] colours = colourTable.getColourTable();
			for(int i : colours) imageData.add(i);
		bitSize = GIFUtils.numberOfBitsRequired(colourTableSize);
	}
	
	private void compressImage(){
		int[] rawInput = quantilizer.getQuantilizedInput();
		compressor = new LZWCompressorMark2(rawInput, bitSize);
		compressor.compress();
	}
	
	private void addCompressedImageBytes(){
		ArrayList<Integer> packagedBytes = compressor.getPackagedBytes();
		imageData.add(bitSize);
		for(int i : packagedBytes) imageData.add(i);
		imageData.add(0);
	}
	
	private void addMemeToImage(){
		image.addText(memeText, 666);
	}
	
	private void checkImageSize(){
		if(image.getWidth() != width || image.getHeight() != height) image.resize(width, height);
	}
	
	public ArrayList<Integer> getImageData(){
		return imageData;
	}
}
