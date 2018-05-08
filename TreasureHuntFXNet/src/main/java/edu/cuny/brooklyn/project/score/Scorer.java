package edu.cuny.brooklyn.project.score;

import edu.cuny.brooklyn.project.GameSettings;

public class Scorer {

	private int totalScore;
	private int roundScore;
	private int totalRounds;
	
	public Scorer() {
		totalScore = 0;
		roundScore = 0;
		totalRounds = 0;
	}
	
	public void nextRound() {
		totalRounds++;
		roundScore = 0;
	}
	
	public int getTotalScore() {
		return totalScore;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public int getTotalRounds() {
		return totalRounds;
	}
	public void updateScore(int attempts) {
		roundScore =  GameSettings.MAX_SCORE - (attempts - 1) * GameSettings.SCORE_PENALTY;
		totalScore += roundScore;
	}

}
