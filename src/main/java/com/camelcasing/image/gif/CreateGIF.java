package com.camelcasing.image.gif;

import java.io.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.camelcasing.image.octreecolourquantilizer.OctreeColourQuantilizer;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class CreateGIF{

		private int width;
		private int height;
		private int maxColours = 256;
		private InputImage[] images;
		private int timeDelay = 20;
		private File outputFile;
		private LogicalScreenDescriptor logicalScreenDescriptor;
		private ArrayList<ArrayList<Integer>> imageDataBytes;
		private boolean useGlobalColourTable = false;
		private int[] globalColourTableBytes;
		private int globalColourTableSize = 0;
		
		private Logger logger = Logger.getLogger(getClass());
	
	public CreateGIF(String outputFile, int width, int height, InputImage... images){
		this.images = images;
		this.outputFile = new File(outputFile);
		this.width = width;
		this.height = height;
		imageDataBytes = new ArrayList<ArrayList<Integer>>();
	}
	
	public CreateGIF(String outputFile, InputImage... images){
		this(outputFile, images[0].getWidth(), images[0].getHeight(), images);
	}
	
	public boolean create(){
		
		getImageBytes();
		logicalScreenDescriptor = createLogicalScreenDescriptor();
		writeBytes();
		return true;
	}
	
	private LogicalScreenDescriptor createLogicalScreenDescriptor(){
		ScreenDescriptorField sdf = new ScreenDescriptorField(useGlobalColourTable, 1, false, globalColourTableSize);
		LogicalScreenDescriptor lsd = new LogicalScreenDescriptor(width, height, sdf, 0, 0);
		return lsd;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	private void getImageBytes(){
		if(useGlobalColourTable){
			ArrayList[] imageData = new ArrayList[images.length];
			OctreeColourQuantilizer quantilizer = new OctreeColourQuantilizer(maxColours);
			for(int i = 0; i < images.length; i++){
				imageData[i] = quantilizer.addImage(images[i]);
			}
			quantilizer.quantilize();
			
			int[][] colourPalette = quantilizer.getColourPalette();
			globalColourTableSize = GIFUtils.getColourTableSize(quantilizer.getColourCount());
			ColourTable colourTable = new ColourTable(globalColourTableSize);
			colourTable.populateTableFromPalette(colourPalette);
			globalColourTableBytes = colourTable.getColourTable();
			
			for(int i = 0; i < images.length; i++){
				logger.info("processing image " + (i + 1) + " of " + images.length);
				imageDataBytes.add(new ImageData(timeDelay, !useGlobalColourTable, width, height, quantilizer, imageData[i])
				.init()
				.getImageData());
			}
		}else{
			for(int i = 0; i < images.length; i++){
				logger.info("processing image " + (i + 1) + " of " + images.length);
				OctreeColourQuantilizer quantilizer = new OctreeColourQuantilizer(maxColours);
				ArrayList<int[]> colourTableBits = quantilizer.addImage(images[i]);
				quantilizer.quantilize();
				imageDataBytes.add(new ImageData(timeDelay, !useGlobalColourTable, width, height, quantilizer, colourTableBits)
				.init()
				.getImageData());
			}	
		}
	}
	
	private void writeBytes(){
		try(OutputStream os = new FileOutputStream(outputFile)){
			for(int i : Headers.getHeader89a()) os.write(i);
			for(int i : logicalScreenDescriptor.getLogicalScreenDescriptor()) os.write(i);
				
			if(useGlobalColourTable){
				for(int i : globalColourTableBytes) os.write(i);
			}
				
			if(images.length > 1){
				for(int i : new NetscapeApplicationExtension(0).create()) os.write(i);
			}
			
			for(int i = 0; i < imageDataBytes.size(); i++){
				for(int j : imageDataBytes.get(i)) os.write(j);
			}
			
			os.write(Headers.getTrailer());
		}catch(IOException e){
			logger.error("Failed to write bytes to file: " + outputFile.getPath() + "\n" + e.getMessage());
		}
	}
	
	public CreateGIF enableGlobalColourTable(){
		useGlobalColourTable = true;
		return this;
	}
	
	public CreateGIF setMaxColours(int maxColours) {
		if(maxColours > 256) maxColours = 256;
		if(maxColours < 1) maxColours = 1;
		this.maxColours = maxColours;
		return this;
	}
	public CreateGIF setTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
		return this;
	}
	
	public CreateGIF setWidth(int width){
		this.width = width;
		return this;
	}
	
	public CreateGIF setHeight(int height){
		this.height = height;
		return this;
	}
}
