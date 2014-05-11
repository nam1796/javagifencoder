package com.camelcasing.image.gif;

import java.io.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class CreateGIF{

		private int width;
		private int height;
		private int maxColours = 256;
		private InputImage[] images;
		private int timeDelay = 20;
		private File outputFile;
		private LogicalScreenDescriptor logicalScreenDescriptor;
		private ArrayList<ArrayList<Integer>> imageDataBytes;
		private GIFOptions[] gifOptions;
		
		private Logger logger = Logger.getLogger(getClass());
	
	public CreateGIF(File outputFile, int width, int height, InputImage... images){
		this.images = images;
		this.outputFile = outputFile;
		this.width = width;
		this.height = height;
		imageDataBytes = new ArrayList<ArrayList<Integer>>();
		gifOptions = new GIFOptions[images.length];
	}
	
	public CreateGIF(File outputFile, InputImage... images){
		this(outputFile, images[0].getWidth(), images[0].getHeight(), images);
	}
	
	public boolean createGIF(){
		logicalScreenDescriptor = createLogicalScreenDescriptor();
		getImageBytes();
		writeBytes();
		return true;
	}
	
	private LogicalScreenDescriptor createLogicalScreenDescriptor(){
		ScreenDescriptorField sdf = new ScreenDescriptorField(false, 1, false, 0);
		LogicalScreenDescriptor lsd = new LogicalScreenDescriptor(width, height, sdf, 0, 0);
		return lsd;
	}
	
	private void getImageBytes(){
		for(int i = 0; i < images.length; i++){
			imageDataBytes.add(new ImageData(images[i], timeDelay, maxColours, width, height, gifOptions[i])
			.init()
			.getImageData());
		}
		
		logger.debug("image.length = " + images.length);
		logger.debug("imageDataBytes.size() = " + imageDataBytes.size());
	}
	
	private void writeBytes(){
		try(OutputStream os = new FileOutputStream(outputFile)){
			for(int i : Headers.getHeader89a()) os.write(i);
			for(int i : logicalScreenDescriptor.getLogicalScreenDescriptor()) os.write(i);
			for(int i : new NetscapeApplicationExtension(0).create()) os.write(i);
			
			for(int i = 0; i < imageDataBytes.size(); i++){
				for(int j : imageDataBytes.get(i)) os.write(j);
			}
			
			os.write(Headers.getTrailer());
		}catch(IOException e){
			logger.fatal("Failed to write bytes to file\n" + e.getMessage());
		}
	}
	
	public CreateGIF addGIFOptions(GIFOptions go, int index){
		gifOptions[index] = go;
		return this;
	}
	
	public CreateGIF setMaxColours(int maxColours) {
		this.maxColours = maxColours;
		return this;
	}
	public CreateGIF setTimeDelays(int timeDelay) {
		this.timeDelay = timeDelay;
		return this;
	}
}
