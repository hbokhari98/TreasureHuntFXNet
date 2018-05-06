package edu.cuny.brooklyn.project.controller;

import java.io.IOException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusBroadcaster;
import edu.cuny.brooklyn.project.state.TreasureHuntState;
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
	
	private TreasureHuntState treasureHuntState;
	
	private StatusBroadcaster statusBroadcaster;
	
	public FrameContainer(Stage stage, ResourceBundle bundle) throws IOException {
		initializeContainer(stage, bundle);
	}
	

	public void reload(ResourceBundle bundle) throws IOException {
		initializeContainer(stage, bundle);
		showFlashScreen(true);
	}
	


	public void setStatusBroadcaster(StatusBroadcaster statusBroadcaster) {
		if (statusBroadcaster == null) {
			throw new IllegalArgumentException("StatusBroadcaster object must not be null.");
		}
		this.statusBroadcaster = statusBroadcaster;
		
		mainViewController.setStatusBroadcaster(this.statusBroadcaster);
	}
	
	public void showFlashScreen() {
		showFlashScreen(false);
	}

	public void showFlashScreen(boolean reload) {
		LOGGER.debug("showing flash screen.");
		showScreenWithFrame(reload, this.flashFrame, GameSettings.MSG_APP_TITLE_FLASH_KEY);
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
	
	private void initializeContainer(Stage stage, ResourceBundle bundle) throws IOException {
		this.stage = stage;
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.FRAME_VIEW_PATH), bundle);
		mainView = fxmlLoader.load();
		mainViewController = fxmlLoader.getController();
		mainViewController.setContainer(this);
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.TREASURE_VIEW_PATH), bundle);
		treasureFrame = fxmlLoader.load();
		treasureFrameController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.PUZZLER_VIEW_PATH), bundle);
		puzzlerFrame = fxmlLoader.load();
		puzzlerFrameController = fxmlLoader.getController();
		
		fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(GameSettings.FLASH_VIEW_PATH), bundle);
		flashFrame = fxmlLoader.load();
		flashFrameController = fxmlLoader.getController();
		
		
		flashFrameController.setOnStartButtonAction(e -> startGame());
		flashFrameController.setOnSettingsButtonAction(e -> showSettingScreen());
		puzzlerFrameController.setOnAnswerButtonAction(e -> answerPuzzler());
		treasureFrameController.setOnNextAction(e -> {
			treasureFrameController.nextRound();
			showPuzzlerScreen();
		});
		treasureFrameController.setOnButtonTreasureAction(e -> treasureFrameController.doTreasureLocationAction());
		
		if (treasureHuntState == null) {
			treasureGenerator = new TreasureGenerator();
		}
		treasureFrameController.getTreasureField().setTreasureGenerator(treasureGenerator);
		
		if (treasureHuntState == null) {
			treasureHuntState = new TreasureHuntState();
		}
		mainViewController.setTreasureHuntState(treasureHuntState);
		
		if (this.statusBroadcaster != null) {
			mainViewController.setStatusBroadcaster(this.statusBroadcaster);
		} else {
			LOGGER.debug("this.statusBroadcaster is null");
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
	
	private void showSettingScreen() {
		LOGGER.debug("showing settings screen.");
		showScreenWithFrame(this.treasureFrame, GameSettings.MSG_SETTINGS_KEY);
	}
	
	private void showScreenWithFrame(Parent view, String title_key) {
		showScreenWithFrame(false, view, title_key);
	}
	
	private void showScreenWithFrame(boolean reload, Parent view, String title_key) {
		if (mainViewController == null) {
			throw new IllegalStateException("mainViewcontroller must not be null.");
		}
		
		if (reload || stage.getScene() == null) {
			scene = new Scene(mainView);
			stage.setScene(scene);
			stage.show();
		} 
		mainViewController.setFrameOnTop(view);
		if (title_key != null && !title_key.isEmpty()) {
			stage.setTitle(I18n.getBundle().getString(title_key));
		}
	}
	
	private void startGame() {
		showPuzzlerScreen();
		mainViewController.disableLocaleChange();
	}
}
