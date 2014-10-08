package com.camelcasing.image.gif;

import java.io.*;
import java.util.ArrayList;

import org.apache.log4j.Logger;

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
		logicalScreenDescriptor = createLogicalScreenDescriptor();
		getImageBytes();
		writeBytes();
		return true;
	}

	private void createStream(){
		//add option to pass OutputStream instead of File
	}
	
	private LogicalScreenDescriptor createLogicalScreenDescriptor(){
		ScreenDescriptorField sdf = new ScreenDescriptorField(false, 1, false, 0);
		LogicalScreenDescriptor lsd = new LogicalScreenDescriptor(width, height, sdf, 0, 0);
		return lsd;
	}
	
	private void getImageBytes(){
		for(int i = 0; i < images.length; i++){
			logger.info("processing image " + (i + 1) + " of " + images.length);
			logger.debug("imageBytes = " + imageDataBytes.size());
			imageDataBytes.add(new ImageData(images[i], timeDelay, maxColours, width, height)
			.init()
			.getImageData());
			logger.debug("imageBytes = " + imageDataBytes.size());
		}
	}
	
	private void writeBytes(){
		try(OutputStream os = new FileOutputStream(outputFile)){
			for(int i : Headers.getHeader89a()) os.write(i);
			for(int i : logicalScreenDescriptor.getLogicalScreenDescriptor()) os.write(i);
				if(images.length > 1){
					for(int i : new NetscapeApplicationExtension(0).create()) os.write(i);
				}
			for(int i = 0; i < imageDataBytes.size(); i++){
				int count = 0;
				for(int j : imageDataBytes.get(i)){
					os.write(j);
					count++;
				}
				logger.debug("added " + count + " bytes");
				count = 0;
			}
			os.write(Headers.getTrailer());
		}catch(IOException e){
			logger.error("Failed to write bytes to file: " + outputFile.getPath() + "\n" + e.getMessage());
		}
	}
	
	public CreateGIF setMaxColours(int maxColours) {
		this.maxColours = maxColours;
		return this;
	}
	public CreateGIF setTimeDelays(int timeDelay) {
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
