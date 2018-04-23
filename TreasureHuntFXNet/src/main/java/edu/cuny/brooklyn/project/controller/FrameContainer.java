package edu.cuny.brooklyn.project.controller;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.treasure.TreasureGenerator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FrameContainer {
	private final static Logger LOGGER = LoggerFactory.getLogger(FrameContainer.class);
	
	private Stage stage;
	
	private Scene scene;
	
	private Parent mainView;
	private FrameViewController mainViewController;
	
	private Parent treasureFrame;
	private TreasureFrameViewController treasureFrameController;
	
	private Parent puzzlerFrame;
	private PuzzlerFrameViewController puzzlerFrameController;
	
	private Parent flashFrame;
	private FlashFrameViewController flashFrameController;
	
	private TreasureGenerator treasureGenerator;
	
	public FrameContainer(Stage stage, ResourceBundle bundle) throws IOException {
		this.stage = stage;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.FRAME_VIEW_PATH), bundle);
		mainView = fxmlLoader.load();
		mainViewController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.TREASURE_VIEW_PATH), bundle);
		treasureFrame = fxmlLoader.load();
		treasureFrameController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.PUZZLER_VIEW_PATH), bundle);
		puzzlerFrame = fxmlLoader.load();
		puzzlerFrameController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.FLASH_VIEW_PATH), bundle);
		flashFrame = fxmlLoader.load();
		flashFrameController = fxmlLoader.getController();
		
		
		flashFrameController.setOnStartButtonAction(e -> showPuzzlerScreen());
		puzzlerFrameController.setOnAnswerButtonAction(e -> answerPuzzler());
		treasureFrameController.setOnButtonTreasureAction(e -> treasureFrameController.doTreasureLocationAction());
		
		treasureGenerator = new TreasureGenerator();
		treasureFrameController.getTreasureField().setTreasureGenerator(treasureGenerator);
	}

	public void showFlashScreen() {
		LOGGER.debug("showing flash screen.");
		showScreenWithFrame(this.flashFrame, GameSettings.MSG_APP_TITLE_FLASH_KEY);
	}
	
	private void answerPuzzler() {
		LOGGER.debug("solving puzzler.");
		if (puzzlerFrameController.answerPuzzler()) {
			String clue = TreasureClue.getClue(treasureFrameController.getTreasureField().getTreasureXLeft(),
					treasureFrameController.getTreasureField().getTreasureYTop(),
					treasureFrameController.getTreasureField().getTreasureBoundingBoxWidth(),
					treasureFrameController.getTreasureField().getTreasureBoundingBoxLength(),
					puzzlerFrameController.getAnsweringAttempts());
			treasureFrameController.setAttempts(puzzlerFrameController.getAnsweringAttempts());
			treasureFrameController.startLocatingTreasure(clue);
			showTreasureScreen();
		}
	}

	private void showTreasureScreen() {
		LOGGER.debug("showing treasure screen.");
		showScreenWithFrame(this.treasureFrame, GameSettings.MSG_APP_TITLE_TREASURE_HUNT_KEY);
	}
	
	
	private void showPuzzlerScreen() {
		LOGGER.debug("showing puzzler screen.");
		treasureFrameController.getTreasureField().placeTreasure();
		LOGGER.debug("placed a treasure");
		this.puzzlerFrameController.showNewPuzzler();
		showScreenWithFrame(this.puzzlerFrame, GameSettings.MSG_APP_TITLE_PUZZLER_KEY);
	}
	
	private void showScreenWithFrame(Parent view, String title_key) {
		if (mainViewController == null) {
			throw new IllegalStateException("mainViewcontroller must not be null.");
		}
		mainViewController.setFrameOnTop(view);
		if (stage.getScene() == null) {
			scene = new Scene(mainView);
			stage.setScene(scene);
			stage.show();
		} 
		if (title_key != null && !title_key.isEmpty()) {
			stage.setTitle(I18n.getBundle().getString(title_key));
		}
	}
}
