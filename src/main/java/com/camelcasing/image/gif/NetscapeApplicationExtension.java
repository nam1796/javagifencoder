package com.camelcasing.image.gif;

public class NetscapeApplicationExtension{

	private int iterations;
	private int[] ext;

	/**
	 * @param iterations Must be between 0 and 65535 (0 for infinite)
	 */
	public NetscapeApplicationExtension(int iterations){
		this.iterations = iterations;
		ext = new int[19];
		validate();
	}
	
	private void validate(){
		if(iterations < 0 || iterations > 65535) throw new IllegalArgumentException("Number of Iteration must be between 0 and 65535"); 
	}
	
	public int[] create(){
		ext[0] = Headers.getNewExtension();
		ext[1] = Headers.getApplicationextensionheader();
		ext[2] = Headers.getApplicationextensionblocksize();
		addBytes(Headers.getApplicationextensionid(), 3);
		addBytes(Headers.getApplicationextensioncode(), 11);
		ext[14] = 3;
		ext[15] = 1;
		int iter1 = Integer.parseInt(GIFUtils.getBitsFromInt(iterations, 8), 2);
		int iter2 = Integer.parseInt(GIFUtils.getBitsFromInt(iterations, 8, 8), 2);
		ext[16] = iter1;
		ext[17] = iter2;
		ext[18] = 0;
		return ext;
	}
	
	private void addBytes(int[] bytes, int offset){
		int j = 0;
		for(int i = offset; j < bytes.length; i++, j++){
			ext[i] = bytes[j];
		}
	}
}
