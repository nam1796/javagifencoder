package com.camelcasing.image.gif;

public class GIFOptions {

		private int timeDelay = -1;
		private int[] imageSize = null;
		private int maxColours = -1;
		private int graphicsControlDisposalMethod = -1;
		private int offsetLeft = -1;
		private int offsetTop = -1;
		private String memeText = null;
		private int textLocation = 1;
		
		public enum MemeLocation {TOP, BOTTOM, CENTRE};
		
	/**
	 * @param l
	 */
	public GIFOptions setMemeLocation(MemeLocation l){
		switch(l){
			case BOTTOM: textLocation = 1;
		return this;
			case TOP: textLocation = 2;
		return this;
			case CENTRE: textLocation = 3;
		return this;
			default:
		return this;
		}
	}
	
	/**
	 * @return
	 */
	public int getMemeLocation(){
		return textLocation;
	}
		
	public GIFOptions setMemeText(String s){
		memeText = s;
		return this;
	}
	
	public String getMemeText(){
		return memeText;
	}
	
	/**
	 * @return time in miliseconds the image will be diaplayed on the screen 
	 */
	public int getTimeDelay(){
		return timeDelay;
	}
	
	/**
	 * 
	 * @return height of the image 
	 */
	public int[] getImageSize(){
		return imageSize;
	}
	
	/**
	 * @return maximum amount of colours used in the image
	 */
	public int getMaxColours(){
		return maxColours;
	}
	
	/**
	 * @return 
	 */
	public int getGraphicsControlDisposalMethod(){
		return graphicsControlDisposalMethod;
	}
	
	/**
	 * @return 
	 */
	public int getOffsetLeft(){
		return offsetLeft;
	}
	
	/**
	 * @return
	 */
	public int getOffsetTop(){
		return offsetTop;
	}

	/**
	 * @param timeDelay
	 * @return this
	 */
	public GIFOptions setTimeDelay(int timeDelay){
		this.timeDelay = timeDelay;
		return this;
	}
	
	/**
	 * New dimensions must be smaller the the declared image size in the {@link com.camelcasing.image.gif.LogicalScreenDescriptor LogicalScreenDescriptor}
	 * else the values will be ignored.
	 * @param width
	 * @param height
	 * @return this
	 */
	public GIFOptions setimageSize(int width, int height){
		this.imageSize = new int[]{width, height};
		return this;
	}
	
	/**
	 * @param maxColours
	 * @return this
	 */
	public GIFOptions setMaxColours(int maxColours){
		this.maxColours = maxColours;
		return this;
	}
	
	/**
	 * @param graphicsControlDisposalMethod
	 * @return this
	 */
	public GIFOptions setGraphicsControlDisposalMethod(int graphicsControlDisposalMethod){
		this.graphicsControlDisposalMethod = graphicsControlDisposalMethod;
		return this;
	}
	
	/**
	 * @param offsetLeft
	 * @return this
	 */
	public GIFOptions setOffsetLeft(int offsetLeft){
		this.offsetLeft = offsetLeft;
		return this;
	}
	
	/**
	 * @param offsetTop
	 * @return this
	 */
	public GIFOptions setOffsetTop(int offsetTop){
		this.offsetTop = offsetTop;
		return this;
	}
}
