package com.camelcasing.image.lwzcompression;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.camelcasing.image.gif.GIFUtils;

/**
 * Implements the LZW compression algorithm, takes as input an array of indexes into the global/local colour table
 * and returns an array of compressed and packaged bytes (including the block-size bytes)
 * 
 * @author Philip Teclaff
 * @since 1.0
 */
public class LZWCompressor{
	
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
	public LZWCompressor(int[] rawData, int bitSize){
		CLEAR_CODE = (int)Math.pow(2, bitSize);
		TERMINATION_CODE = CLEAR_CODE + 1;
		startDictionaryEntry = TERMINATION_CODE + 1;
		//logger.debug("startDictionaryEntry = " + startDictionaryEntry);
		
		dictionary = new LZWDictionary(startDictionaryEntry);
		
		this.rawData = rawData;
		this.bitSize = bitSize + 1; //adding 1 to account for the termination code
		//logger.debug("startBitSize = " + this.bitSize);
		currentMaximumBitSize = (int)Math.pow(2, this.bitSize);
		dictionaryCount = ((int) Math.pow(2, this.bitSize - 1)) + 2;
		resetBitSize = bitSize + 1;
	}

	/* 
	 * @see com.camelcasing.image.gif.ImageCompressor#compress()
	 * use InputColour Class
	 */
	public void compress(){
		logger.debug("Compression Started");
		boolean takeFromDictionary = false;
		InputColour currentValue = null;
		int nextValue;
		InputColour combinedCurrentNext;
		
			for(int i = 0; i < rawData.length - 1; i++){
				if(!takeFromDictionary){
					currentValue = new InputColour(rawData[i]);
					nextValue = rawData[i + 1];
					combinedCurrentNext = simpleColourArray(currentValue, nextValue);
				}else{
					nextValue = rawData[i];
					combinedCurrentNext = simpleColourArray(currentValue, nextValue);
				}
					//logger.debug("currentValue = " + currentValue);
					if(!dictionary.contains(combinedCurrentNext)){
							if(takeFromDictionary){
								addToStream(dictionary.get(currentValue));
								//logger.debug("Taken from dictionary");
								i--;
							}else{
								addToStream(currentValue.getFirst());
							}
						addToDictionary(combinedCurrentNext);
						takeFromDictionary = false;
					}else{
						currentValue = combinedCurrentNext;
						if(!takeFromDictionary) i++;
						takeFromDictionary = true;
					}
					
			}//exit loop and process the last bit
		combinedCurrentNext = simpleColourArray(currentValue, rawData[rawData.length - 1]);
			if(dictionary.contains(combinedCurrentNext)){
				addToStream(dictionary.get(combinedCurrentNext));
			}else{
				//addToStream(takeFromDictionary ? dictionary.get(currentValue) : Integer.parseInt(currentValue));
				addToStream(rawData[rawData.length - 1]);
			}
	}
	
	public void addToStream(int i){
		//logger.debug(Long.parseLong(GIFUtils.getBitsFromInt(i, bitSize), 2));
		//logger.debug(GIFUtils.getBitsFromInt(i, bitSize));
		outputStream.insert(0, GIFUtils.getBitsFromInt(i, bitSize));
	}
	
	public void addToStream2(int i){
		//logger.debug(Long.parseLong(GIFUtils.getBitsFromInt(i, bitSize), 2));
		//logger.debug(GIFUtils.getBitsFromInt(i, bitSize));
		outputStream.insert(0, GIFUtils.getBitsFromInt(i, bitSize));
	}

	public void addToDictionary(InputColour colour){
		if(dictionaryCount == currentMaximumBitSize){
			bitSize++;
			//logger.debug("bitsize is now " + bitSize);
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
	
	/**
	 * @param a first input values.
	 * @param b value to be appended to a.
	 * @return InputColour with <code>Integer</code> appended to the array
	 */
	private InputColour simpleColourArray(InputColour a, int b){
		int[] c = new int[a.getLength() + 1];
		System.arraycopy(a.getColour(), 0, c, 0, a.getLength());
		c[c.length - 1] = b;
		return new InputColour(c);
	}
	
	public void initialisePackagedBytes(){
		packagedBytes = new ArrayList<Integer>();
	}
	
	private void finalisePackagedBytes(){
		packagedBytes.add(TERMINATION_CODE);
	}
	
	private void addFullBlock(String block){
		packagedBytes.add(255);
//		for(int j = 0; j < 255; j++){
		for(int i = 255; i >= 0; i -= 8){
			packagedBytes.add(Integer.valueOf(block.substring(i - 8, i), 2));
		}
	}
	
	private void addRemaining(StringBuilder fBlock){
		fBlock.append(GIFUtils.getBitsFromInt(CLEAR_CODE, resetBitSize));
		if(fBlock.length() > 255){
			int extra = fBlock.length() % 255;
			int fitInBytes = fBlock.length() % 8;
			if(fitInBytes > 0){
				for(int i = 8 - fitInBytes; i > 0; i--){
					fBlock.insert(0, '0');
				}
			}
			addFullBlock(fBlock.substring(0, 255 * 8));
			String remaining = fBlock.substring(255 * 8);
			for(int i = remaining.length(); i >= 0; i -= 8){
				packagedBytes.add(Integer.valueOf(remaining.substring(i - 8, i), 2));
			}
		}else{
			String block = fBlock.toString();
			for(int i = 255; i >= 0; i -= 8){
				packagedBytes.add(Integer.valueOf(block.substring(i - 8, i), 2));
			}
		}
		finalisePackagedBytes();
	}
	
	public ArrayList<Integer> getPackagedBytes(){
		
		outputStream.append(GIFUtils.getBitsFromInt(CLEAR_CODE, resetBitSize));
		packagedBytes = new ArrayList<Integer>();
		
		int numberOfBytes = outputStream.length() / 8;
		int extraBits = outputStream.length() % 8;
			if(extraBits > 0){
				for(int i = 8 - extraBits; i > 0; i--){
					outputStream.insert(0, '0');
				}
				++numberOfBytes;
			}
		int numberOfFullBlocks = numberOfBytes / 255;
		int finalBlockSize = numberOfBytes % 255;
		
		logger.debug("bytes to process = " + numberOfBytes);
		logger.debug("number of bytes % 8 = " + outputStream.length() % 8);
		logger.debug("number of fullBlocks = " + numberOfFullBlocks);
		logger.debug("finalBlockSize = " + finalBlockSize);		 
		
		String bytes = outputStream.toString();
		
		if(numberOfFullBlocks > 0){
			int index = bytes.length();
			logger.debug("begin index = " + index);
			for(int i = 0; i < numberOfFullBlocks; i++){
				packagedBytes.add(255);
				for(int j = 0; j < 255; j++){
					packagedBytes.add(Integer.valueOf(bytes.substring(index - 8, index), 2));
					index -= 8;
				}
			}
			if(finalBlockSize != 0){
				packagedBytes.add(finalBlockSize);
				logger.debug("finalBlockIndex = " + index);
				for(int i = 0; i < finalBlockSize; i++){
					packagedBytes.add(Integer.valueOf(bytes.substring(index - 8, index), 2));
					index -= 8;
				}
			}
		}else{
			packagedBytes.add(numberOfBytes);
			for(int i = bytes.length(); i == 0; i -= 8){
				packagedBytes.add(Integer.valueOf(bytes.substring(i - 8, i), 2));
			}
		}
		
		packagedBytes.add(TERMINATION_CODE);
		logger.debug("Compression Finished");
		return packagedBytes;
	}
}