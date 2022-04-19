import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MusicPlayerGUI extends Application implements Observer{

	private MusicPlayerModel model;
	private MusicPlayerController controller;
	
	
	@Override
	public void start(Stage stage) throws Exception {
		AnchorPane root = new AnchorPane();
		Scene scene = new Scene(root, 800, 600);
		stage.setTitle("Music Player");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	
}
