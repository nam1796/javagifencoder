package com.camelcasing.image.gif;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

public class CreateGIF{

		private int width;
		private int height;
		private int maxColours = 256;
		private BufferedImage[] images;
		private int timeDelay = 50;
		private File outputFile;
		private LogicalScreenDescriptor logicalScreenDescriptor;
		private ArrayList<ArrayList<Integer>> imageDataBytes;
		
		private Logger logger = Logger.getLogger(getClass());
	
	public CreateGIF(File outputFile, BufferedImage... bufferedImages){
		this.images = bufferedImages;
		this.outputFile = outputFile;
		width = images[0].getWidth();
		height = images[0].getHeight();
		imageDataBytes = new ArrayList<ArrayList<Integer>>(images.length);
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
			imageDataBytes.add(new ImageData(images[i], timeDelay, maxColours, width, height, null)
			.init()
			.getImageData());
		}
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
	
	public CreateGIF setMaxColours(int maxColours) {
		this.maxColours = maxColours;
		return this;
	}
	public CreateGIF setTimeDelays(int timeDelay) {
		this.timeDelay = timeDelay;
		return this;
	}
}
