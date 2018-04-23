package edu.cuny.brooklyn.project.state;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TreasureHuntState {
    private static Logger LOGGER = LoggerFactory.getLogger(TreasureHuntState.class);

    
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
    }

    public Path getTheGameFilePath() {
        return theGameFilePath;
    }
}
