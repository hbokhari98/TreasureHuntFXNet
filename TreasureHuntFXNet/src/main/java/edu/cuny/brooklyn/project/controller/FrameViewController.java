package edu.cuny.brooklyn.project.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.cuny.brooklyn.project.controller.DecisionWrapper.UserDecision;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusBroadcaster;
import edu.cuny.brooklyn.project.state.TreasureHuntState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class FrameViewController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FrameViewController.class);

	@FXML
	private Canvas targetCanvas;

	@FXML
	private ComboBox<Locale> lcComboBox;

	@FXML
	private StackPane frameHolder;

	private TreasureHuntState treasureHunt;

	private StatusBroadcaster statusBroadCaster;

	@FXML
	void exitGame(ActionEvent event) {
		LOGGER.debug("calling exitGame(ActionEvent event).");
		exitGame((Event) event);
	}

	private void exitGame(Event event) {
		LOGGER.debug("calling exitGame(Event event).");
		validateTreasureHunt();
		validateStatusBroadcaster();
		if (treasureHunt.isGameStateChanged()) {
			UserDecision decision = NotificationHelper
					.askUserDecision(new DecisionWrapper(UserDecision.CancelPendingAction));
			switch (decision) {
			case CancelPendingAction:
				event.consume();
				break;
			case DiscardGame:
				statusBroadCaster.close();
				Platform.exit();
				break;
			case SaveGame:
				try {
					treasureHunt.saveTheGame();
					LOGGER.debug(String.format("Saved the game at %s.", treasureHunt.getTheGameFilePath()));
					statusBroadCaster.close();
					Platform.exit();
				} catch (FileNotFoundException e) {
					LOGGER.error(String.format("Cannot found the file %s while saving the game.",
							treasureHunt.getTheGameFilePath()), e);
					NotificationHelper.showFileNotFound(treasureHunt.getTheGameFilePath());
				} catch (IOException e) {
					LOGGER.error(String.format("Cannot write to the file %s while saving the game.",
							treasureHunt.getTheGameFilePath()), e);
					NotificationHelper.showWritingError(treasureHunt.getTheGameFilePath());
				}
				break;
			default:
				throw new IllegalArgumentException(String.format("User decision's value (%s) is unexpected", decision));
			}
		} else {
			statusBroadCaster.close();
			Platform.exit();
		}
	}

	@FXML
	void newGame(ActionEvent event) {

	}

	@FXML
	void openGame(ActionEvent event) {
		LOGGER.debug("openning a saved game: not implemented yet");
	}

	@FXML
	void saveTheGame(ActionEvent event) {
		LOGGER.debug("saving the game: not implemented yet");
	}

	private void initializeI18n() throws IOException, URISyntaxException {
		List<Locale> lcList = I18n.getSupportedLocale();
		lcComboBox.getItems().addAll(lcList);
		Callback<ListView<Locale>, ListCell<Locale>> lcCellFactory = new Callback<ListView<Locale>, ListCell<Locale>>() {

			@Override
			public ListCell<Locale> call(ListView<Locale> lv) {
				return new ListCell<Locale>() {
					@Override
					protected void updateItem(Locale lc, boolean empty) {
						super.updateItem(lc, empty);
						if (lc == null || empty) {
							setText("");
						} else {
							setText(I18n.getDisplayLC(lc));
						}
					}
				};
			}
		};
		lcComboBox.setValue(I18n.getSelectedLocale());
		lcComboBox.setConverter(new StringConverter<Locale>() {

			@Override
			public Locale fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Locale lc) {
				return I18n.getDisplayLC(lc);
			}
		});
		lcComboBox.setCellFactory(lcCellFactory);
		lcComboBox.valueProperty().addListener((observedLocale, oldLocale, newLocale) -> {
			LOGGER.debug(String.format("Change locale from %s to %s.", oldLocale, newLocale));
			// try {
			// LOGGER.debug("TODO: change language results to a new game. Need to handle it
			// better.");
			// // reLoadScene(stage, newLocale);
			// } catch (IOException e) {
			// LOGGER.error("failed to load locale specific scene.", e);
			// }
		});
	}

	// private void reLoadScene(Stage stage, Locale locale) throws IOException {
	// I18n.setSelectedLocale(locale);
	// I18n.setBundle(ResourceBundle.getBundle(I18n.getBundleBaseName(), locale));
	// FXMLLoader loader = new
	// FXMLLoader(TargetGameApp.class.getResource(TargetGameApp.FXML_MAIN_SCENE)
	// , I18n.getBundle());
	// Parent pane = loader.load();
	//
	// StackPane viewHolder = (StackPane)stage.getScene().getRoot();
	//
	// viewHolder.getChildren().clear();
	// viewHolder.getChildren().add(pane);
	//
	// GameController controller = loader.getController();
	// controller.setStage(stage);
	// stage.setTitle(I18n.getBundle().getString(TargetGameApp.APP_TITLE_KEY));
	//
	// LOGGER.debug(targetGame.getTarget() == null? "No target set
	// yet.":targetGame.getTarget().toString());
	// }

	private void validateStatusBroadcaster() {
		if (statusBroadCaster == null) {
			throw new IllegalStateException("StatusBroadcaster object must not be null");
		}
	}

	private void validateTreasureHunt() {
		if (treasureHunt == null) {
			throw new IllegalStateException("TreasureHunt object must not be null");
		}
	}

	public void setFrameOnTop(Parent view) {
		frameHolder.getChildren().clear();
		frameHolder.getChildren().add(view);
	}
}
