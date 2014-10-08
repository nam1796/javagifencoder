
package com.camelcasing.image.octreecolourquantilizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.camelcasing.image.gif.InputImage;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class OctreeColourQuantilizer{
	
		private Logger logger = Logger.getLogger(OctreeColourQuantilizer.class);

		private InputImage image;
		/**
		 * Multi-dimensional Array of the RGB values
		 * format = 
		 * [R][G][B]
		 * [R][G][B]...
		 */
		private int[][] rawInput;
		private int[][] colourPalette;
		private int[] imageColourPalette;
		private OctreeNode root;
		private ArrayList<OctreeNode> colourPointers = new ArrayList<OctreeNode>();
		private ArrayList<int[]> colourBitArray = new ArrayList<int[]>();
		private int[] quantilizedInput;
		private int maxColours;
		
		private final int DEPTH = 8;

	public OctreeColourQuantilizer(InputImage image, int maxColours){
		this.image = image;
		this.maxColours = maxColours;
		root = new OctreeNode(null, 0);
	}
	
	public OctreeColourQuantilizer quantilize(){
		logger.debug("Quantilization Started");
		extractRGBValues();
		createOctree();
		//logger.debug("before pruning colour count = " + colourPointers.size());
			if(!checkIfEnoughColours()){
				pruneTree();
			}
		//logger.debug("After pruning colour count = " + colourPointers.size());
		quantilizedInput = generateIndexedImage();
		colourPalette = createColourPalette();
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
				int[] colours = getCombinedColourNumbers(r, g, b);
				colourBitArray.add(colours);
				addToNode(colours, r, g, b);
			}
	}
	
	public int[] getCombinedColourNumbers(int r, int g, int b){
		int[] colourBitCombinations = new int[DEPTH];
		int count = 8;
			for(int i = 0; i < DEPTH; i++){
				int c = Integer.valueOf((String.valueOf((r >> count) & 0x1) + ((g >> count) & 0x1) + ((b >> count) & 0x1)), 2);
				count--;
				colourBitCombinations[i] = c;
			}
		return colourBitCombinations;
	}
	
	public void addToNode(int[] colours, int r, int g, int b){
		OctreeNode node = root.getChild(colours[0])
			.getChild(colours[1])
			.getChild(colours[2])
			.getChild(colours[3])
			.getChild(colours[4])
			.getChild(colours[5])
			.getChild(colours[6])
			.getChild(colours[7])
			.addColour(r, g, b);
				if(!colourPointers.contains(node)){
					colourPointers.add(node);
				}
	}
	
	private void pruneTree(){
		while(true){
			int count = 0;
			if(!checkIfEnoughColours()){
				addRemoveNode(count);
				count++;
				if(count == colourPointers.size()) count = 0;
			}else{
				return;
			}
		}
	}
	
	private void addRemoveNode(int index){
		OctreeNode o = colourPointers.get(index);
		OctreeNode oP = o.getParent();
		colourPointers.remove(o);
			if(!colourPointers.contains(oP)){
				colourPointers.add(oP);
			}
		o.mergeUp();
	}
	
	private int[] generateIndexedImage(){
		int[] indexedColours = new int[colourBitArray.size()];
		int count = 0;
			for(int[] c : colourBitArray){
				int targetCount = 1;
				OctreeNode target = root.getChildIfExists(c[0]);
					while(target.getChildIfExists(c[targetCount]) != null){
						target = target.getChildIfExists(c[targetCount]);
						if(targetCount == 7){
							break;
						}
						targetCount++;
					}
				indexedColours[count] = colourPointers.indexOf(target);
				count++;
			}
		List<Integer> array = new ArrayList<Integer>();
		for(int i : indexedColours) array.add(i);
		return indexedColours;
	}
	
	public int[] getQuantilizedInput(){
		//logger.info("quantilized input = " + quantilizedInput.length);
		logger.debug("Quantilization Finished");
		return quantilizedInput;
	}
	
	public int[][] createColourPalette(){
		int[][] colourP = new int[colourPointers.size()][3];
			for(int i = 0; i < colourPointers.size(); i++){
				colourP[i][0] = colourPointers.get(i).getRed();
				colourP[i][1] = colourPointers.get(i).getGreen();
				colourP[i][2] = colourPointers.get(i).getBlue();
				if(colourP[i][0] == -1 || colourP[i][1] == -1 || colourP[i][2] == -1){
					logger.error("octree return -1");
				}
			}
		return colourP;
	}
	
	public int[][] getColourPalette(){
		//logger.info("colourPalette = " + colourPalette.length);
		return colourPalette;
	}
	
	public boolean checkIfEnoughColours(){
		return colourPointers.size() <= maxColours;
	}
	
	public int getColourCount(){
		return colourPointers.size();
	}
	
	public OctreeNode getRootNode(){
		return root;
	}
	
	public int[] getImageColourPalette(){
		return imageColourPalette;
	}
}
