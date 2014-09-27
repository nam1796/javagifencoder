package com.camelcasing.image.lwzcompression;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.camelcasing.image.gif.GIFUtils;
import com.camelcasing.image.gif.ImageCompressor;
import com.camelcasing.image.gif.InputColour;

/**
 * Implements the LZW compression algorithm, takes as input an array of indexes into the global/local colour table
 * and returns an array of compressed and packaged bytes (including the block-size bytes)
 * 
 * @author Philip Teclaff
 * @since 1.0
 */
public class LZWCompressorMark2 implements ImageCompressor{
	
		private Logger logger = Logger.getLogger(getClass().getName());
	
		/**
		 * Array of indexes into the global/local colour table.
		 */
		protected int[] rawData;
		/**
		 * bitSize Minimum number of bits required to represent the highest value contained in the <code>rawData</code>
		 */
		protected int bitSize;
		
		/**
		 * First available compression code
		 */
		protected int startDictionaryEntry; 
		
		/**
		 * After a dictionary clear bitsize with revert to this value
		 */
		protected int resetBitSize; 
		
		/**
		 * stores the compression output as a binary string
		 */
		protected StringBuilder outputStream = new StringBuilder();
		
		/**
		 * <pre>
		 * List of the compression codes,
		 * add the <code>startDictionaryEntry</code> to index of the code to get the actual code value.
		 * </pre> 
		 */
		protected LZWDictionary dictionary;
		
		/**
		 * Highest number that can be stored using current <code>bitSize</code> before it needs to be increased  
		 */
		protected int currentMaximumBitSize;
		
		/**
		 * When dictionaryCount reaches 4095 then the dictionary will need to be cleared to its original state
		 */
		protected int dictionaryCount;
		
		/**
		 * The byte blocks that will be returned (including block sizes)
		 */
		protected ArrayList<Integer> packagedBytes;
		
		/**
		 * Code that indicates to the decoder that the dictionary has been cleared
		 */
		private final int CLEAR_CODE;
		
		/**
		 * Indicates the end of the image compression data
		 */
		private final int TERMINATION_CODE;
	
	/**
	 * @param rawData Array of indexes into the global/local colour table. 
	 * @param bitSize Minimum number of bits required to represent the highest value contained in the <code>rawData</code>
	 */
	public LZWCompressorMark2(int[] rawData, int bitSize){
		CLEAR_CODE = (int)Math.pow(2, bitSize);
		TERMINATION_CODE = CLEAR_CODE + 1;
		startDictionaryEntry = TERMINATION_CODE + 1;
		
		dictionary = new LZWDictionary(startDictionaryEntry);
		
		this.rawData = rawData;
		this.bitSize = bitSize + 1; //adding 1 to account for the termination code
		logger.debug("startBitSize = " + this.bitSize);
		currentMaximumBitSize = (int)Math.pow(2, this.bitSize);
		dictionaryCount = ((int) Math.pow(2, this.bitSize - 1)) + 2;
		resetBitSize = this.bitSize;
		outputStream.append(GIFUtils.getBitsFromInt(CLEAR_CODE, bitSize));
	}

	/* 
	 * @see com.camelcasing.image.gif.ImageCompressor#compress()
	 */
	@Override
	public void compress(){
		boolean takeFromDictionary = false;
		String currentValue = "";
		String nextValue = "";
		String combinedCurrentNext = "";

			for(int i = 0; i <= rawData.length - 2; i++){
				if(!takeFromDictionary){
					currentValue += Integer.toString(rawData[i]);
					nextValue = Integer.toString(rawData[i + 1]);
					combinedCurrentNext = currentValue + nextValue;
				}else{
					nextValue = Integer.toString(rawData[i]);
					combinedCurrentNext = currentValue + nextValue;
				}
				
					if(!dictionary.contains(combinedCurrentNext)){
						addToDictionary(combinedCurrentNext);
						//addToStream(takeFromDictionary ? dictionary.get(currentValue) : Integer.parseInt(currentValue));
							if(takeFromDictionary){
								addToStream(dictionary.get(currentValue));
								i--;
							}else{
								addToStream(Integer.parseInt(currentValue));
							}
						currentValue = "";
						takeFromDictionary = false;
					}else{
						currentValue = combinedCurrentNext;
						takeFromDictionary = true;
						i++;
					}
					
			}//exit loop and process the last bit
		combinedCurrentNext = currentValue + Integer.toString(rawData[rawData.length - 1]);
			if(dictionary.contains(combinedCurrentNext)){
				addToStream(dictionary.get(combinedCurrentNext));
			}else{
				addToStream(takeFromDictionary ? dictionary.get(currentValue) : Integer.parseInt(currentValue));
				addToStream(rawData[rawData.length - 1]);
			}
	}
	
	public void addToStream(int i){
		outputStream.insert(0, GIFUtils.getBitsFromInt(i, bitSize));
	}

	public void addToDictionary(String colour){
		if(dictionaryCount == currentMaximumBitSize){
			bitSize++;
			//logger.debug("bitsize id now " + bitSize);
			currentMaximumBitSize = (int)Math.pow(2, bitSize);
		}
		dictionary.add(colour);
		dictionaryCount++;
		
		if(dictionaryCount == 4095){
			clearDictionary();
		}
	}
	
	public void clearDictionary(){
		outputStream.insert(0, GIFUtils.getBitsFromInt(CLEAR_CODE, bitSize));
		bitSize = resetBitSize;
		dictionary.clear();
		currentMaximumBitSize = (int)Math.pow(2, bitSize);
		//dictionaryCount = startDictionaryEntry;
		dictionaryCount = ((int) Math.pow(2, bitSize - 1)) + 2;
	}
	
	/* 
	 * @see com.camelcasing.image.gif.ImageCompressor#getPackagedBytes()
	 */
	@Override
	public ArrayList<Integer> getPackagedBytes(){
		
		//outputStream.insert(0, GIFUtils.getBitsFromInt(TERMINATION_CODE, bitSize));
		outputStream.append(GIFUtils.getBitsFromInt(CLEAR_CODE, resetBitSize));
		String bytes = outputStream.toString();
		packagedBytes = new ArrayList<Integer>();
		
		int numberOfBytes = bytes.length() / 8;
		if(!(bytes.length() % 8 == 0)) numberOfBytes++;
		
		int numberOfFullBlocks = numberOfBytes / 255;
		
		int finalBlockSize = numberOfBytes % 255;
		
		int i = 0;
		try{
			for (i = bytes.length();; i -= 8){
				packagedBytes.add(Integer.valueOf(bytes.substring(i - 8, i), 2));
			}
		}catch (StringIndexOutOfBoundsException e) {
			StringBuilder sb = new StringBuilder(bytes.substring(0, i));
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
		
		packagedBytes.add(TERMINATION_CODE);
		//for(int r : packagedBytes) logger.debug(r);
		return packagedBytes;
	}
}
























