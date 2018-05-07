package edu.cuny.brooklyn.project.puzzler;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductMathPuzzler extends MathPuzzler {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProductMathPuzzler.class);
	
	private double answerValue;
	
	private Random rng;
	
	public ProductMathPuzzler(int minNumber, int maxNumber) {
		rng = new Random();
		if (minNumber <= 0) {
			LOGGER.warn("minNumber = " + minNumber + ", but expecting a number > 0. Use 2 instead ");
		}
		if (maxNumber <= 2) {
			LOGGER.warn("maxNumber = " + maxNumber + ", but expecting a number > 1. Use 2 instead ");			
		}
		int num1 = minNumber + rng.nextInt(maxNumber - minNumber + 1);
		int num2 = minNumber + rng.nextInt(maxNumber - minNumber + 1);
		String message = num1 + " * " + num2 + " = ?";
		answerValue = num1 * num2;
		String answer = ""+answerValue;
		setMessage(message);
		setAnswer(answer);
	}
	

	public boolean isCorrect(String enteredAnswer) {
		double entered = Integer.parseInt(enteredAnswer);
		if (entered == answerValue) {
			LOGGER.debug("Correct answer");
			return true;
		} else {
			LOGGER.debug("Incorrect answer");
			return false;
		}
	}

}
