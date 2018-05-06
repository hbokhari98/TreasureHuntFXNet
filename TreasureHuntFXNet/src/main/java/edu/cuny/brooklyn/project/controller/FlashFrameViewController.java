package edu.cuny.brooklyn.project.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import edu.cuny.brooklyn.project.GameSettings;
import edu.cuny.brooklyn.project.message.I18n;
import edu.cuny.brooklyn.project.net.StatusMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class FlashFrameViewController {
    @FXML
    private Label flashLabel;

    @FXML
    private Button startButton;
    
    @FXML
    private Button settingsButton;
    
	@FXML
	private ListView<StatusMessage> statusMessageListView;
    
	private Thread listener;
	
    /*
     * the initialize() method:
     * see 
     * https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/doc-files/introduction_to_fxml.html#controllers
     */
    public void initialize() {
		flashLabel.setText(I18n.getBundle().getString(GameSettings.MSG_GAME_DESCRIPTION_KEY));
		startButton.setText(I18n.getBundle().getString(GameSettings.MSG_START_GAME_KEY));
		settingsButton.setText(I18n.getBundle().getString(GameSettings.MSG_SETTINGS_KEY));
		
		statusMessageListView.setCellFactory(titleView -> new ListCell<StatusMessage>() {
			@Override
			public void updateItem(StatusMessage item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
				} else {
					setText(item.toString());
				}
			}
		});
		listener = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	try {
					searchPlayers();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
		    }     
		});
		listener.setDaemon(true);
		listener.start();
    }
    
	private void searchPlayers() throws UnknownHostException {				
		int BUFFER_SIZE = 1024;
		SocketAddress address = new InetSocketAddress(InetAddress.getByName("0.0.0.0"), 62017);
		
		try (DatagramSocket socket = new DatagramSocket(address)) {
			byte buf[] = new byte[BUFFER_SIZE];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			while(true) {
				socket.receive(packet);

				ByteArrayInputStream baos = new ByteArrayInputStream(packet.getData());
				ObjectInputStream oos = new ObjectInputStream(baos);
				StatusMessage msg = (StatusMessage) oos.readObject();
				
				Platform.runLater(new Runnable() {
					@Override public void run() {
						for(StatusMessage sm: statusMessageListView.getItems()) {
							if(sm.toString().equals(msg.toString())) {
								return;
							}
						}
						statusMessageListView.getItems().add(msg);
					}
				});
			}
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("player search failed: " + e.toString());
		}
	}
    
    public void setOnStartButtonAction(EventHandler<ActionEvent> handler) {
    	startButton.setOnAction(handler);
    }
    
    public void setOnSettingsButtonAction(EventHandler<ActionEvent> handler) {
    	settingsButton.setOnAction(handler);
    }
}