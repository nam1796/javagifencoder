package com.camelcasing.image.gif;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
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
	 * @param l <code>MemeLocation</code> where the meme is to be displayed on the image
	 * @return this
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
	 * @return <code>Integer</code> value that represents meme location within the image (top, centre or bottom).
	 */
	protected int getMemeLocation(){
		return textLocation;
	}
		
	
	/**
	 * @param s <code>String</code> that will be added to the image.
	 * @return this
	 */
	public GIFOptions setMemeText(String s){
		memeText = s;
		return this;
	}
	
	/**
	 * @return The meme text to be added to the image or null if nothing has been set.
	 */
	protected String getMemeText(){
		return memeText;
	}
	
	/**
	 * @return time in milliseconds the image will be displayed on the screen 
	 */
	protected int getTimeDelay(){
		return timeDelay;
	}
	
	/**
	 * 
	 * @return height of the image 
	 */
	protected int[] getImageSize(){
		return imageSize;
	}
	
	/**
	 * @return maximum amount of colours used in the image
	 */
	protected int getMaxColours(){
		return maxColours;
	}
	
	/**
	 * <pre>
	 * value of 0 mean no disposal specified
	 * value of 1 means Do not dispose, graphics to be left in place
	 * value of 2 means restore to background colour
	 * value of 3 means restore to the previous image
	 * 
	 * should be 1 for an animated GIF
	 * </pre>
	 * @return <code>Integer</code> that represents how to dispose of the image 
	 */
	protected int getGraphicsControlDisposalMethod(){
		return graphicsControlDisposalMethod;
	}
	
	/**
	 * @return offsetLeft offset in pixels for the left of the container frame this image is to be displayed
	 */
	protected int getOffsetLeft(){
		return offsetLeft;
	}
	
	/**
	 * @return offSetTop offset in pixels for the top of the container frame this image is to be displayed
	 */
	protected int getOffsetTop(){
		return offsetTop;
	}

	/**
	 * @param timeDelay time in milliseconds the image will be displayed on the screen
	 * @return this
	 */
	public GIFOptions setTimeDelay(int timeDelay){
		this.timeDelay = timeDelay;
		return this;
	}
	
	/**
	 * New dimensions must be smaller the the declared image size in the {@link com.camelcasing.image.gif.LogicalScreenDescriptor LogicalScreenDescriptor}
	 * else the values will be ignored.
	 * @param width new image width
	 * @param height new image height
	 * @return this
	 */
	public GIFOptions setimageSize(int width, int height){
		this.imageSize = new int[]{width, height};
		return this;
	}
	
	/**
	 * @param maxColours Maximum number of colours this image will use
	 * @return this
	 */
	public GIFOptions setMaxColours(int maxColours){
		this.maxColours = maxColours;
		return this;
	}
	
	/**
	 * @param graphicsControlDisposalMethod <pre>
	 * value of 0 mean no disposal specified
	 * value of 1 means Do not dispose, graphics to be left in place
	 * value of 2 means restore to background colour
	 * value of 3 means restore to the previous image
	 * 
	 * should be 1 for an animated GIF
	 * </pre>
	 * @return this
	 */
	public GIFOptions setGraphicsControlDisposalMethod(int graphicsControlDisposalMethod){
		this.graphicsControlDisposalMethod = graphicsControlDisposalMethod;
		return this;
	}
	
	/**
	 * @param offsetLeft offset in pixels for the left of the container frame this image is to be displayed
	 * @return this
	 */
	public GIFOptions setOffsetLeft(int offsetLeft){
		this.offsetLeft = offsetLeft;
		return this;
	}
	
	/**
	 * @param offsetTop offset in pixels for the top of the container frame this image is to be displayed
	 * @return this
	 */
	public GIFOptions setOffsetTop(int offsetTop){
		this.offsetTop = offsetTop;
		return this;
	}
}
