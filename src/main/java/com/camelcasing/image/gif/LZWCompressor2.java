package com.camelcasing.image.gif;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class LZWCompressor2 implements ImageCompressor{
	
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
	private int dicCount = 0;
	private int next;
	private InputColour pattern = null;
	private int inputLocation = 0;
	private InputColour found = null;
	private ArrayList<Integer> packagedBytes;

	public LZWCompressor2(int[] rawData, int bitSize) {
		DICINCREMENT = ((int) Math.pow(2, bitSize)) + 2;
		STARTBITSIZE = bitSize;
		this.rawData = rawData;
		this.bitSize = bitSize + 1;
		logger.debug("start actual bitSize = " + this.bitSize);
		bitStream = new StringBuilder(8);
		dictionary = new ArrayList<InputColour>();
		currentDictionaryCount = setInitialDictionaryCountValue(this.bitSize);
		logger.debug("currentDictionaryCount = " + currentDictionaryCount);
		maxBit = setMaxBitSize(this.bitSize);
		logger.debug("start maxBit = " + this.maxBit);
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
			maxBit = setMaxBitSize(bitSize);
		}
		dictionary.add(dicCount, colour);
		dicCount++;
		currentDictionaryCount++;
		if (currentDictionaryCount > 4095){
			clearDictionary();
		}
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
		bitStream.insert(0, GIFUtils.getBitsFromInt(i, bitSize));
	}

	public ArrayList<Integer> getPackagedBytes() {
		packagedBytes = new ArrayList<Integer>();
		bitStream.append(GIFUtils.getBitsFromInt(getClearCode(), STARTBITSIZE + 1)); //KEEP
		logger.debug("finalBitSize = " + getFinalBitSize());
		logger.debug("terminationCode = " + this.getTerminationCode());
		bitStream.insert(0, GIFUtils.getBitsFromInt(getTerminationCode(), getFinalBitSize())); //KEEP
		String s = bitStream.toString();
		
		int numberOfBytes = s.length() / 8;
			if(!(s.length() % 8 == 0)) numberOfBytes++;
		logger.debug("numberOfBytes = " + numberOfBytes);
		
		int numberOfFullBlocks = numberOfBytes / 255;
		logger.debug("numberOfFullBlocks = " + numberOfFullBlocks);
		
		int finalBlockSize = numberOfBytes % 255;
		logger.debug("finalBlockSize = " + finalBlockSize);
		
		int i = 0;
		int byteInCount = 0;
		try{
			for (i = s.length();; i -= 8){
				byteInCount++;
				packagedBytes.add(Integer.valueOf(s.substring(i - 8, i), 2));
			}
		}catch (StringIndexOutOfBoundsException e) {
			StringBuilder sb = new StringBuilder(s.substring(0, i));
				while (sb.length() != 8) {
					sb.insert(0, '0');
				}
				byteInCount++;
			packagedBytes.add(Integer.valueOf(sb.toString(), 2));
		}
		logger.debug("byteInCount = " + byteInCount);
		
			if(packagedBytes.size() <= 255){
				packagedBytes.add(0, packagedBytes.size());
			}else{
				packagedBytes.add(0, 255);
					for(int j = 1; j < numberOfFullBlocks; j++){
						packagedBytes.add(((255 * j) + j), 255);
					}
				packagedBytes.add((packagedBytes.size() - finalBlockSize), finalBlockSize);
			}
		
			logger.debug("bytesReturned = " + packagedBytes.size());
		return packagedBytes;
	}

	private void clearDictionary() {
		bitStream.insert(0, GIFUtils.getBitsFromInt(getClearCode(), bitSize));
		bitSize = STARTBITSIZE + 1;
		currentDictionaryCount = setInitialDictionaryCountValue(bitSize);
		dicCount = 0;
		dictionary = new ArrayList<InputColour>();
		maxBit = setMaxBitSize(bitSize);
		logger.debug("Dictionary Cleared");
		
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
