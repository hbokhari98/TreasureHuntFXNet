package edu.cuny.brooklyn.project.state;
import edu.cuny.brooklyn.project.controller.GameStatistics;
import java.io.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreasureHuntState {
    private static Logger LOGGER = LoggerFactory.getLogger(TreasureHuntState.class);
    private GameStatistics gs;
    
    private boolean gameStateChanged;
    
    private Path theGameFilePath;
    
    public boolean isGameStateChanged() {
        return gameStateChanged;
    }

    public void setGameStateChanged(boolean gameStateChanged) {
        this.gameStateChanged = gameStateChanged;
    }

    public void saveTheGame() throws FileNotFoundException, IOException {
        // TODO Auto-generated method stub
        LOGGER.debug("not yet implemented");
         try {
        	BufferedWriter br = new BufferedWriter(new FileWriter(theGameFilePath.toFile()));
        	br.write("Total Attempts: "+gs.getTotalAttempts());
        	br.write("Total Rounds: "+gs.getTotalRounds());
        	br.write("Round Score: "+gs.getRoundScore());
        	br.write("Total Score: "+gs.getTotalScore());
        	br.write("Total Locating Attempts: "+gs.getTotalLocatingAttempts());
        	br.write("Average Locating Attempts: "+gs.getAvgLocatingAttempts());
        	br.write("Minimum Locating Attempts: "+gs.getMinLocatingAttempts());
        	br.write("Maximum Locating Attempts: "+gs.getMaxLocatingAttempts());
        	br.write("Total Puzzler Attempts: "+gs.getTotalPuzzlerAttempts());
        	br.write("Average Puzzler Attempts: "+gs.getAvgPuzzlerAttempts());
        	br.write("Minimum Puzzler Attempts: "+gs.getMinPuzzlerAttempts());
        	br.write("Maximum Puzzler Attempts: "+gs.getMaxPuzzlerAttempts());
        	setGameStateChanged(true);
        	br.close();
        }catch (FileNotFoundException e) {
        	e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error is : " + e.getMessage());
		}
    }

    public Path getTheGameFilePath() {
        return theGameFilePath;
    }
}
