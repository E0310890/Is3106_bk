package com.is3106.common;

import java.util.Random;

public class RandomHelper {

	public static int getRandomRange(int totalDataSize, int limit) {
		int bound = totalDataSize/limit;
		if(bound <= 0) 
			return 0;
		int random = new Random().nextInt(totalDataSize/limit);
		return random;
	}
	
	public static int getRandomNumber(int lowerBound, int upperBound) {
		int random = new Random().nextInt(upperBound-lowerBound) + lowerBound;
		return random;
	}
}
