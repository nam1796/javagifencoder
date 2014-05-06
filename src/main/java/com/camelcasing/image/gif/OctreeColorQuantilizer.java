package com.camelcasing.image.gif;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

//import org.apache.log4j.Logger;

public class OctreeColorQuantilizer{
	
		//private static Logger log = Logger.getLogger(OctreeColorQuantilizer.class);

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
		private int[] quantilizedInput;
		
		private final int MAX_COLORS;
		private final int DEPTH = 6;

	public OctreeColorQuantilizer(BufferedImage image, int maxColors){
		this.image = image;
		this.MAX_COLORS = maxColors;
		root = new OctreeNode(null);
		extractRGBValues();
		createOctree();
			if(!checkIfEnoughColors()){
				pruneTree();
			}
		quantilizedInput = generateIndexedImage();
		colorPalette = createColorPalette();
	}
		
	private void extractRGBValues(){
		int w = image.getWidth();
		int h = image.getHeight();
		int current;
		int indexCount = 0;
		rawInput = new int[w * h][3];
			for(int i = 0; i < w; i++){
				for(int j = 0; j < h; j++){
					current = image.getRGB(i, j);
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
			.addColor()
			.getChild(colors[1])
			.addColor()
			.getChild(colors[2])
			.addColor()
			.getChild(colors[3])
			.addColor()
			.getChild(colors[4])
			.addColor()
			.getChild(colors[5])
			.addColor()
			.getChild(colors[6])
			.addColor()
			.getChild(colors[7])
			.addColor()
			.addRGBValue(r, g, b);
			if(!colorPointers.contains(node)){
				colorPointers.add(node);
			}
	}
	
	//add code to start with least or most popular colours
	private void pruneTree(){
		divideColorTotals();
		OctreeNode addNode = null;
			while(true){
				for(int i = 0; i < colorPointers.size(); i++){
					OctreeNode o = colorPointers.get(i);
						if(o.getParent() != null){
							addNode = o.getParent();
						}
					int c = 0;
					int r = 0;
					int g = 0;
					int b = 0;
						for(int j = 0; j < 8; j++){
							OctreeNode on;
							if(addNode != null && (on = addNode.getChildIfExists(j)) != null){
								colorPointers.remove(on);
								c += on.getColorCount();
								r += on.getRed();
								g += on.getGreen();
								b += on.getBlue();
							}
						}
					addNode.massacreChildren(r, g, b, c);
					colorPointers.add(0, addNode);
						if(checkIfEnoughColors()){
							return;
						}
				}
			}
	}
	
	private void divideColorTotals(){
		for(OctreeNode otn : colorPointers){
			otn.setBlue(otn.getBlue() / otn.getColorCount());
			otn.setRed(otn.getRed() / otn.getColorCount());
			otn.setGreen(otn.getGreen() / otn.getColorCount());
		}
	}
	
	private int[] generateIndexedImage(){
		int[] ret = new int[rawInput.length];
		int retCount = 0;
			for(int i = 0; i < rawInput.length; i++){
				int r = rawInput[i][0];
				int g = rawInput[i][1];
				int b = rawInput[i][2];
				int[] colors = getCombinedColorNumbers(r, g, b);
				OctreeNode node = root.getChild(colors[0]);
				int count = 1;
					while(!node.isEnd()){
						node = node.getChild(colors[count]);
						count++;
					}
				ret[retCount] = colorPointers.indexOf(node);
				retCount++;
			}
		return ret;
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
