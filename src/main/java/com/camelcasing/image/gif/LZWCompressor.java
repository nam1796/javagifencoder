package com.camelcasing.image.gif;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class LZWCompressor{
	
	private Logger logger = Logger.getLogger(getClass());

	private final int DICINCREMENT;
	private final int STARTBITSIZE;

	private int[] rawData;
	private int bitSize;
	private StringBuilder bitStream;
	private int currentDictionaryCount;
	private ArrayList<InputColor> dictionary;
	private int maxBit;
	private int current;
	private int dicCount = 0;
	private int next;
	private InputColor pattern;
	private int inputLocation;
	private InputColor found;
	private ArrayList<Integer> packagedBytes;

	public LZWCompressor(int[] rawData, int bitSize) {
		DICINCREMENT = ((int) Math.pow(2, bitSize)) + 2;
		STARTBITSIZE = bitSize;
		this.rawData = rawData;
		this.bitSize = bitSize + 1;
		inputLocation = 0;
		pattern = null;
		found = null;
		bitStream = new StringBuilder(8);
		dictionary = new ArrayList<InputColor>();
		currentDictionaryCount = setInitialDictionaryCountValue(this.bitSize);
		maxBit = setMaxBitSize(this.bitSize);

		compress();
	}

	private int setInitialDictionaryCountValue(int bs) {
		return ((int) Math.pow(2, bs - 1)) + 2;
	}

	private int setMaxBitSize(int startBit) {
		return ((int) Math.pow(2, startBit));
	}

	private void addToDictionary(InputColor color) {
		if (currentDictionaryCount == maxBit) {
			bitSize++;
			maxBit = setMaxBitSize(bitSize);
		}
		if (currentDictionaryCount > 4095){
			logger.debug("Dictionary Cleared");
			clearDictionary();
		}
		dictionary.add(dicCount, color);
		dicCount++;
		currentDictionaryCount++;
	}

	/**
	 * Assuming that the rawData is an array of references into the global
	 * colorTable
	 */
	private void compress() {
		while (inputLocation < rawData.length){
			setCompressValues();
		}
	}

	private void setCompressValues() {
		if (pattern == null && inputLocation < rawData.length - 1) {
			current = rawData[inputLocation];
			next = rawData[inputLocation + 1];
			pattern = GIFUtils.simpleColorArray(current, next);
			inputLocation++;
		} else if (pattern != null && inputLocation < rawData.length - 1) {
			next = rawData[inputLocation + 1];
			pattern = GIFUtils.simpleColorArray(pattern.getColor(), next);
			inputLocation++;
		} else if (pattern == null && inputLocation == rawData.length - 1) {
			current = rawData[inputLocation];
			inputLocation++;
		} else if (pattern != null && inputLocation == rawData.length - 1) {
			inputLocation++;
			if (dictionary.contains(pattern)) {
				found = new InputColor(pattern.getColor());
			}
			addToStream();
			return;
		}

		if (dictionary.contains(pattern)) {
			found = new InputColor(pattern.getColor());
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

	public ArrayList<Integer> getPackageBytes() {
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
		//bitSize = STARTBITSIZE;
		currentDictionaryCount = setInitialDictionaryCountValue(bitSize);
		dicCount = 0;
		//dictionary = new ArrayList<InputColor>();
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
