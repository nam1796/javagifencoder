
package com.camelcasing.image.octreecolourquantilizer;

import java.util.ArrayList;

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
		private int[][] colourPalette;
		private int[] imageColourPalette;
		private OctreeSentinal octree;
		private ArrayList<int[]> colourBitArray = new ArrayList<int[]>();
		private int[] quantilizedInput;
		private final int DEPTH = 8;

	public OctreeColourQuantilizer(InputImage image, int maxColours){
		this.image = image;
		octree = new OctreeSentinal(maxColours);
	}
	
	public OctreeColourQuantilizer quantilize(){
		logger.debug("Quantilization Started");
		extractRGBValues();
		pruneTree();
		quantilizedInput = generateIndexedImage();
		colourPalette = createColourPalette();
		return(this);
	}
		
	/**
	 * Processes the image row by row and adds RGB array to rawInput
	 */
	private void extractRGBValues(){
		logger.debug("Extracting RGB Started");
		int w = image.getWidth();
		int h = image.getHeight();
		int current;
			for(int i = 0; i < h; i++){
				for(int j = 0; j < w; j++){
					current = image.getRGB(j, i);
					int r = (current >> 16) & 0xFF; //red value
					int g = (current >> 8) & 0xFF; //green value
					int b = current & 0xFF; //blue value
					int[] colours = getCombinedColourNumbers(r, g, b);
					colourBitArray.add(colours);
					octree.addToNode(colours, r, g, b);
		
				}
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
	
	private void pruneTree(){
		octree.prune();
	}
	
	private int[] generateIndexedImage(){
		int[] indexedColours = new int[colourBitArray.size()];
		int count = 0;
			for(int[] c : colourBitArray){
				int targetCount = 1;
				OctreeNode target = octree.getRoot().getChildIfExists(c[0]);
					while(target.getChildIfExists(c[targetCount]) != null){
						target = target.getChildIfExists(c[targetCount]);
						if(targetCount == 7){
							break;
						}
						targetCount++;
					}
				indexedColours[count] = octree.indexOf(target);
				count++;
			}
		return indexedColours;
	}
	
	public int[] getQuantilizedInput(){
		logger.debug("Quantilization Finished");
		return quantilizedInput;
	}
	
	public int[][] createColourPalette(){
		logger.debug("colourPaleteSize = " + octree.getSize());
		int[][] colourP = new int[octree.getSize()][3];
		OctreeNode otn = octree.getNext();
			for(int i = 0; i < octree.getSize(); i++){
				colourP[i][0] = otn.getRed();
				colourP[i][1] = otn.getGreen();
				colourP[i][2] = otn.getBlue();
				otn = otn.getNext();
			}
		return colourP;
	}
	
	public int[][] getColourPalette(){
		return colourPalette;
	}
	
	public int getColourCount(){
		return octree.getSize();
	}
	
	public int[] getImageColourPalette(){
		return imageColourPalette;
	}
}
