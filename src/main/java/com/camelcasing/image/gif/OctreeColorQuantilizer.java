/**TODO
 * randomise the colorPointers before quantilization
 */

package com.camelcasing.image.gif;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class OctreeColorQuantilizer{
	
		private Logger logger = Logger.getLogger(OctreeColorQuantilizer.class);

		private BufferedImage image;
		/**
		 * Multi-dimensional Array of the RGB values
		 * format = 
		 * [R][G][B]
		 * [R][G][B]...
		 */
		private int[][] rawInput;
		private int[][] colorPalette;
		private int[] imageColorPalette;
		private OctreeNode root;
		private ArrayList<OctreeNode> colorPointers = new ArrayList<OctreeNode>();
		private ArrayList<int[]> colorBitArray = new ArrayList<int[]>();
		private int[] quantilizedInput;
		
		private final int MAX_COLORS;
		private final int DEPTH = 8;

	public OctreeColorQuantilizer(BufferedImage image, int maxColors){
		this.image = image;
		this.MAX_COLORS = maxColors;
		root = new OctreeNode(null, 0);
	}
	
	public OctreeColorQuantilizer quantilize(){
		extractRGBValues();
		createOctree();
		logger.debug("before purning colour count = " + colorPointers.size());
			if(!checkIfEnoughColors()){
				pruneTree();
			}
		logger.debug("After pruning colour count = " + colorPointers.size());
		quantilizedInput = generateIndexedImage();
		colorPalette = createColorPalette();
		return(this);
	}
		
	/**
	 * Processes the image row by row and adds RGB array to rawInput
	 */
	private void extractRGBValues(){
		int w = image.getWidth();
		int h = image.getHeight();
		int current;
		int indexCount = 0;
		rawInput = new int[w * h][3];
			for(int i = 0; i < h; i++){
				for(int j = 0; j < w; j++){
					current = image.getRGB(j, i);
					rawInput[indexCount][0] = (current >> 16) & 0xFF; //red value
					rawInput[indexCount][1] = (current >> 8) & 0xFF; //green value
					rawInput[indexCount++][2] = current & 0xFF; //blue value
				}
			}
	}
	
	private void createOctree(){
			for(int i = 0; i < rawInput.length; i++){
				int r = rawInput[i][0];
				int g = rawInput[i][1];
				int b = rawInput[i][2];
				int[] colors = getCombinedColorNumbers(r, g, b);
				colorBitArray.add(colors);
				addToNode(colors, r, g, b);
			}
	}
	
	public int[] getCombinedColorNumbers(int r, int g, int b){
		int[] colorBitCombinations = new int[8];
		int count = 7;
			for(int i = 0; i < DEPTH; i++){
				int c = Integer.valueOf((String.valueOf((r >> count) & 0x1) + ((g >> count) & 0x1) + ((b >> count) & 0x1)), 2);
				count--;
				colorBitCombinations[i] = c;
			}
		return colorBitCombinations;
	}
	
	public void addToNode(int[] colors, int r, int g, int b){
		OctreeNode node = root.getChild(colors[0])
			.getChild(colors[1])
			.getChild(colors[2])
			.getChild(colors[3])
			.getChild(colors[4])
			.getChild(colors[5])
			.getChild(colors[6])
			.getChild(colors[7])
			.addColor(r, g, b);
				if(!colorPointers.contains(node)){
					colorPointers.add(node);
				}
	}
	
	private void pruneTree(){
		while(true){
			int count = 0;
			if(!checkIfEnoughColors()){
				addRemoveNode(count);
				count++;
					if(count == colorPointers.size()){
						count = 0;
					}
			}else{
				return;
			}
		}
	}
	
	private void addRemoveNode(int index){
		OctreeNode o = colorPointers.get(index);
		OctreeNode oP = o.getParent();
		colorPointers.remove(o);
			if(!colorPointers.contains(oP)){
				colorPointers.add(oP);
			}
		o.mergeUp();
	}
	
	private int[] generateIndexedImage(){
		int[] indexedColors = new int[colorBitArray.size()];
		logger.debug("colorBitArray.size() = " + colorBitArray.size());
		int count = 0;
			for(int[] c : colorBitArray){
				int targetCount = 1;
				OctreeNode target = root.getChildIfExists(c[0]);
					while(target.getChildIfExists(c[targetCount]) != null){
						target = target.getChildIfExists(c[targetCount]);
						if(targetCount == 7){
							break;
						}
						targetCount++;
					}
				indexedColors[count] = colorPointers.indexOf(target);
				count++;
			}
		return indexedColors;
	}
	
	public int[] getQuantilizedInput(){
		return quantilizedInput;
	}
	
	public int[][] createColorPalette(){
		int[][] colorP = new int[colorPointers.size()][3];
			for(int i = 0; i < colorPointers.size(); i++){
				colorP[i][0] = colorPointers.get(i).getRed();
				colorP[i][1] = colorPointers.get(i).getGreen();
				colorP[i][2] = colorPointers.get(i).getBlue();
			}
		return colorP;
	}
	
	public int[][] getColorPalette(){
		return colorPalette;
	}
	
	public boolean checkIfEnoughColors(){
		return colorPointers.size() <= MAX_COLORS;
	}
	
	public int getColorCount(){
		return colorPointers.size();
	}
	
	public OctreeNode getRootNode(){
		return root;
	}
	
	public int[] getImageColorPalette(){
		return imageColorPalette;
	}
}
