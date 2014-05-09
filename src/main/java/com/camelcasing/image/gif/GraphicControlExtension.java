package com.camelcasing.image.gif;


/**
 * Total of 8 Bytes<br />
 * This class will throw an {@link java.lang.IllegalArgumentException IllegalArgumentException} if the input values are outside the defined ranges.
 * 
 * @author camelCasing
 * @version 0.2
 * @since 0.2
 */
public class GraphicControlExtension{

		/**
		 * value of 0 mean no disposal specified
		 * value of 1 means Do not dispose, graphics to be left in place
		 * value of 2 means restore to background colour
		 * value of 3 means restore to the previous image
		 * 
		 * should be 1 for an animated GIF
		 */
		private int disposalMethod;
		/**
		 * hard to imagine a case when this would not be 0
		 */
		private boolean userInputFlag;
		/**
		 * true if GIF is using transparency
		 */
		private boolean transparencyFlag;
		/**
		 * must be between 0 and 65535
		 */
		private int timeDelay;
		/**
		 * index (in active <code>Color Table</code> of the colour to use as a transparent pixel 
		 */
		private int transparencyIndex;
		/**
		 * One byte that contains (most significant bit first)<br />
		 * 1) Transparent Color Flag (1 bit)<br />
		 * 2) User Input Flag (1 bit)<br />
		 * 3) Disposal Method (3 bits)<br />
		 * 4) Reserved for future use (3 bits)
		 */
		private int packedField;
		/**
		 * int array that represents all the values of the <Code>Graphic Control Extension</code>
		 */
		private int[] graphicControlExtension;

	/**
	 * @param disposalMethod should be 1 for an animated GIF
	 * @param userInputFlag hard to imagine a case when this would not be 0
	 * @param transparencyFlag true if GIF is using transparency
	 * @param timeDelay must be between 0 and 65535
	 * @param transparencyIndex index (in active <code>Color Table</code> of the colour to use as a transparent pixel 
	 */
	public GraphicControlExtension(int disposalMethod, boolean userInputFlag, boolean transparencyFlag, int timeDelay, int transparencyIndex){
		this.disposalMethod = disposalMethod;
		this.userInputFlag = userInputFlag;
		this.transparencyFlag = transparencyFlag;
		this.timeDelay = timeDelay;
		this.transparencyIndex = transparencyIndex;
		
		validateInput();
		addFixedHeaders();
		createPackedField();
		addRemaining();
	}
	
	/**
	 * Adds
	 * Extension byte
	 * Graphic Control Extension byte
	 * Graphic Control Extension Block Size Byte
	 * and the Tail byte
	 */
	private void addFixedHeaders(){
		graphicControlExtension = new int[8];
		graphicControlExtension[0] = Headers.getNewExtension();
		graphicControlExtension[1] = Headers.getGraphicControl();
		graphicControlExtension[2] = Headers.getGraphicControlBlockSize();
		graphicControlExtension[7] = 0;
	}

	/**
	 * Throws a {@link java.lang.IllegalArgumentException IllegalArgumentException} if user input is not within valid ranges
	 * Does not validate transparency index !!!
	 * 
	 * @throws IllegalArgumentException If DisposalMethod < 0 / > 7 or TimeDelay < 0 / > 65535
	 */
	private void validateInput() throws IllegalArgumentException{
		if(disposalMethod < 0 || disposalMethod > 3) throw new IllegalArgumentException("DisposalMethod must be between 0 and 3");
		if(timeDelay < 0 || timeDelay > 65535) throw new IllegalArgumentException("Time Delay must be between 0 and 65535");
	}
	
	/**
	 * Creates the byte that represents the
	 * Transparency Flag
	 * User Input Flag 
	 * and the Disposal Method
	 */
	private void createPackedField(){
		StringBuilder sb = new StringBuilder(8);
		sb.append((transparencyFlag ? "1" : "0"));
		sb.insert(0, (userInputFlag ? "1" : "0"));
		sb.insert(0, GIFUtils.getBitsFromInt(disposalMethod, 3));
		sb.insert(0, "000");
		packedField = Integer.parseInt(sb.toString(), 2);
		graphicControlExtension[3] = packedField;
	}
	
	/**
	 * Adds the Time Delay and Transparency Index
	 */
	public void addRemaining(){
		int delay1 = Integer.parseInt(GIFUtils.getBitsFromInt(timeDelay, 8), 2);
		int delay2 = Integer.parseInt(GIFUtils.getBitsFromInt(timeDelay, 8, 8), 2);
		graphicControlExtension[4] = delay1;
		graphicControlExtension[5] = delay2;
		graphicControlExtension[6] = transparencyIndex;
	}
	
	/**
	 * @return int array that represents all the values of the <Code>Graphic Control Extension</code>
	 */
	public int[] getGraphicControlExtension(){
		return graphicControlExtension;
	}
}
