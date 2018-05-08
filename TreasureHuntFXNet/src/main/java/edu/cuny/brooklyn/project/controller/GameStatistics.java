package edu.cuny.brooklyn.project.controller;

import edu.cuny.brooklyn.project.score.Scorer;

public class GameStatistics {
/*
 * the number of rounds that the user has played;
 * the total number of attempts that the user has made;
 * the average number of attempts to answer a puzzler;
 * the minimum number of attempts to answer a puzzler;
 * the maximum number of attempts to answer a puzzler;
 * the average number of attempts to locate a treasure;
 * the minimum number of attempts to locate a treasure;
 * the maximum number of attempts to locate a treasure;
 * the round score and the total score.
 */
	private TreasureFrameViewController tfcview;
	
	private int totalRounds;
	private int totalAttempts;
	private int totalLocatingAttempts;
	private int totalPuzzlerAttempts;
	private int avgPuzzlerAttempts;
	private int minPuzzlerAttempts;
	private int maxPuzzlerAttempts;
	private int avgLocatingAttempts;
	private int minLocatingAttempts;
	private int maxLocatingAttempts;
	private int roundScore;
	private int totalScore;
	
	public GameStatistics() {
		totalRounds = 0;
		totalAttempts = 0;
		totalPuzzlerAttempts = 0;
		totalLocatingAttempts = 0;
		avgPuzzlerAttempts = 0;
		avgLocatingAttempts = 0;
		roundScore = 0;
		totalScore = 0;
		minPuzzlerAttempts = 100;
		maxPuzzlerAttempts = 0;
		minLocatingAttempts = 100;
		maxLocatingAttempts = 0;
	}
	
	public void setTfvc(TreasureFrameViewController tfvc) {
		tfcview = tfvc;		
	}
	
	public void updateGameStats() {
		totalRounds = tfcview.getTotalRounds();
		totalPuzzlerAttempts += tfcview.getPuzzlerAttempts();
		totalLocatingAttempts += tfcview.getLocatingAttempts();
		totalScore = tfcview.getTotalScore();
		roundScore = tfcview.getRoundScore();
		if(maxPuzzlerAttempts < tfcview.getPuzzlerAttempts()) {
			maxPuzzlerAttempts = tfcview.getPuzzlerAttempts();
		}
		if(minPuzzlerAttempts > tfcview.getPuzzlerAttempts()) {
			minPuzzlerAttempts = tfcview.getPuzzlerAttempts();
		}
		if(minLocatingAttempts > tfcview.getLocatingAttempts()) {
			minLocatingAttempts = tfcview.getLocatingAttempts();
		}
		if(maxLocatingAttempts < tfcview.getLocatingAttempts()) {
			maxLocatingAttempts = tfcview.getLocatingAttempts();
		}
	}
	
	public void averages() {
		totalAttempts = totalPuzzlerAttempts + totalLocatingAttempts;
		avgPuzzlerAttempts = totalPuzzlerAttempts/totalLocatingAttempts;
		avgLocatingAttempts = totalLocatingAttempts/totalLocatingAttempts;	
	}
}
