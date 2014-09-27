package com.camelcasing.image.gif;

/**
 * Utility class that contains static getters for the various headers within a GIF file. This Class can not be instantized
 * 
 * @author Philip Teclaff
 * @since 1.0
 */
public class Headers{

		public static final int[] HEADER89 = {'G', 'I', 'F', '8', '9', 'a'};
		public static final int[] HEADER87 = {'G', 'I', 'F', '8', '7', 'a'};
		public static final int IMAGESEPARATOR = 0x2C;
		public static final int TRAILER = 59;
		public static final int NEWEXTENSION = 0x21;
		public static final int GRAPHICCONTROL = 0xF9;
		public static final int GRAPHICCONTROLBLOCKSIZE = 4; 
		public static final int APPLICATIONEXTENSIONHEADER = 0xFF;
		public static final int APPLICATIONEXTENSIONBLOCKSIZE = 0x0B;
		public static final int[] APPLICATIONEXTENSIONID = {'N', 'E', 'T', 'S', 'C', 'A', 'P', 'E'};
		public static final int[] APPLICATIONEXTENSIONCODE = {'2', '.', '0'};
		
	private Headers(){
		//private constructor as can not be instantized
	}
	
	/**
	 * Get the GIF header for version 87a
	 * @return An array of 6 bytes with the value GIF87a
	 */
	public static int[] getHeader87a(){
		return HEADER87;	
	}

	/**
	 * Get the GIF header for version 89a
	 * @return An array of 6 bytes with the value GIF89a
	 */
	public static int[] getHeader89a(){
		return HEADER89;
	}
	
	/**
	 * First byte of all new images.
	 * @return fixed value of 44 (0x2C)
	 */
	public static int getImageSeparator(){
		return IMAGESEPARATOR;
	}
	
	/**
	 * final byte and the file is complete (finally!)
	 * @return fixed value of 59 (0x3B)
	 */
	public static int getTrailer(){
		return TRAILER;
	}

	/**
	 * byte used at the start of every new extension block
	 * @return fixed value of 33 (0x21)
	 */
	public static int getNewExtension() {
		return NEWEXTENSION;
	}

	/**
	 * byte to identify the start of Graphic Control Block
	 * @return fixed value of 249 (0xF9)
	 */
	public static int getGraphicControl() {
		return GRAPHICCONTROL;
	}

	/**
	 * byte that declares the size of the Graphic Control Block size
	 * @return fixed value of 4 (0x4)
	 */
	public static int getGraphicControlBlockSize() {
		return GRAPHICCONTROLBLOCKSIZE;
	}

	/**
	 * byte to identify the start of an Application Extension
	 * @return fixed value of 255 (0xFF)
	 */
	public static int getApplicationextensionheader() {
		return APPLICATIONEXTENSIONHEADER;
	}

	/**
	 * byte to identify the size of the netscape application extension block
	 * @return fixed value of 4 (0x04)
	 */
	public static int getApplicationextensionblocksize() {
		return APPLICATIONEXTENSIONBLOCKSIZE;
	}

	/**
	 * int array that represents the value of netscape application extension id
	 * @return fixed value of 'NETSCAPE'
	 */
	public static int[] getApplicationextensionid() {
		return APPLICATIONEXTENSIONID;
	}

	/**
	 * int array that represents the value of netscape application extension code
	 * @return fixed value of '2.0'
	 */
	public static int[] getApplicationextensioncode() {
		return APPLICATIONEXTENSIONCODE;
	}
}
