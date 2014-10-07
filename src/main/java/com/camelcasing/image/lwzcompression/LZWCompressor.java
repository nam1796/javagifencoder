package com.camelcasing.image.lwzcompression;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.camelcasing.image.gif.GIFUtils;

/**
 * @author Philip Teclaff
 * @since 1.0
 */
public class LZWCompressor{
	
	private Logger logger = Logger.getLogger(getClass());

	private final int DICINCREMENT;
	private final int STARTBITSIZE;

	private int[] rawData;
	private int bitSize;
	private StringBuilder bitStream;
	private int currentDictionaryCount;
	private ArrayList<InputColour> dictionary;
	private int maxBit;
	private int current;
	private int next;
	private InputColour pattern = null;
	private int inputLocation = 0;
	private InputColour found = null;
	private ArrayList<Integer> packagedBytes;

	public LZWCompressor(int[] rawData, int bitSize) {
		DICINCREMENT = ((int) Math.pow(2, bitSize)) + 2;
		//logger.debug("DICINCREMENT = " + DICINCREMENT);
		STARTBITSIZE = bitSize;
		this.rawData = rawData;
		this.bitSize = bitSize + 1;
		//logger.debug("start bit size = " + this.bitSize);
		bitStream = new StringBuilder(8);
		dictionary = new ArrayList<InputColour>();
		currentDictionaryCount = setInitialDictionaryCountValue(this.bitSize);
		maxBit = setMaxBitSize(this.bitSize);
	}

	private int setInitialDictionaryCountValue(int bs) {
		return ((int) Math.pow(2, bs - 1)) + 2;
	}

	private int setMaxBitSize(int startBit) {
		return ((int) Math.pow(2, startBit));
	}

	private void addToDictionary(InputColour colour) {
		if (currentDictionaryCount == maxBit) {
			bitSize++;
			//logger.debug("bit size is now " + bitSize);
			maxBit = setMaxBitSize(bitSize);
		}
		dictionary.add(colour);
		currentDictionaryCount++;
		if (currentDictionaryCount == 4095){
			//logger.debug("dictionary cleared");
			clearDictionary();
		}
	}
	
	private void clearDictionary() {
		bitStream.insert(0, GIFUtils.getBitsFromInt(getClearCode(), bitSize));
		bitSize = STARTBITSIZE + 1;
		currentDictionaryCount = setInitialDictionaryCountValue(bitSize);
		dictionary = new ArrayList<InputColour>();
		maxBit = setMaxBitSize(bitSize);	
	}

	/**
	 * Assuming that the rawData is an array of references into the global
	 * colourTable
	 */
	public void compress() {
		while (inputLocation < rawData.length){
			setCompressValues();
		}
	}

	private void setCompressValues() {
		if (pattern == null && inputLocation < rawData.length - 1) {
			current = rawData[inputLocation];
			next = rawData[inputLocation + 1];
			pattern = GIFUtils.simpleColourArray(current, next);
			inputLocation++;
		} else if (pattern != null && inputLocation < rawData.length - 1) {
			next = rawData[inputLocation + 1];
			pattern = GIFUtils.simpleColourArray(pattern.getColour(), next);
			inputLocation++;
		} else if (pattern == null && inputLocation == rawData.length - 1) {
			current = rawData[inputLocation];
			inputLocation++;
		} else if (pattern != null && inputLocation == rawData.length - 1) {
			inputLocation++;
			if (dictionary.contains(pattern)) {
				found = new InputColour(pattern.getColour());
			}
			addToStream();
			return;
		}

		if (dictionary.contains(pattern)) {
			found = new InputColour(pattern.getColour());
			return;
		} else {
			addToStream();
			addToDictionary(pattern);
			found = null;
			pattern = null;
		}
	}

	/**
	 * Add a String representation of the binary bits to a StringBuilder reader
	 * for packaging
	 */
	private void addToStream() {
		int i;
		if (found != null) {
			i = dictionary.indexOf(found) + DICINCREMENT;
		} else {
			i = current;
		}
		//logger.debug(Integer.valueOf(GIFUtils.getBitsFromInt(i, bitSize), 2));
		//if(found != null) logger.debug("Taken from dictionary");
		bitStream.insert(0, GIFUtils.getBitsFromInt(i, bitSize));
	}

	public ArrayList<Integer> getPackagedBytes() {
		packagedBytes = new ArrayList<Integer>();
		bitStream.append(GIFUtils.getBitsFromInt(getClearCode(), STARTBITSIZE + 1)); //KEEP
		String s = bitStream.toString();
		
		int numberOfBytes = s.length() / 8;
			if(!(s.length() % 8 == 0)) numberOfBytes++;
		
		int numberOfFullBlocks = numberOfBytes / 255;
		
		int finalBlockSize = numberOfBytes % 255;
		
		int i = 0;
		try{
			for (i = s.length();; i -= 8){
				packagedBytes.add(Integer.valueOf(s.substring(i - 8, i), 2));
			}
		}catch (StringIndexOutOfBoundsException e) {
			StringBuilder sb = new StringBuilder(s.substring(0, i));
				while (sb.length() != 8) {
					sb.insert(0, '0');
				}
			packagedBytes.add(Integer.valueOf(sb.toString(), 2));
		}
		
			if(packagedBytes.size() <= 255){
				packagedBytes.add(0, packagedBytes.size());
			}else{
				packagedBytes.add(0, 255);
					for(int j = 1; j < numberOfFullBlocks; j++){
						packagedBytes.add(((255 * j) + j), 255);
					}
				packagedBytes.add((packagedBytes.size() - finalBlockSize), finalBlockSize);
			}
		packagedBytes.add(getTerminationCode());
		//for(int r : packagedBytes) logger.debug(r);
		return packagedBytes;
	}

	private int getTerminationCode() {
		return getClearCode() + 1;
	}

	public int getClearCode() {
		return ((int) Math.pow(2, STARTBITSIZE));
	}

	private int getFinalBitSize() {
		return bitSize;
	}
}
