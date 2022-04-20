import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MusicPlayerGUI extends Application implements Observer{

	private MusicPlayerModel model;
	private MusicPlayerController controller;
	private static Color buttonColor = Color.GREEN;
	private static int buttonRadius = 50;
	
	@Override
	public void start(Stage stage) throws Exception {
		model = new MusicPlayerModel();
		controller = new MusicPlayerController(model);
		CornerRadii corner = new CornerRadii(0);
		Background appBackground = new Background(
				new BackgroundFill(Color.BLACK, corner, Insets.EMPTY));
		AnchorPane root = new AnchorPane();
		root.setBackground(appBackground);
		Scene scene = new Scene(root, 800, 600);
		Circle playButton = new Circle(400, 500, buttonRadius);
		playButton.setFill(buttonColor);
		playButton.setOnMouseClicked( e ->{
			if(!controller.getIsPlaying())
				controller.playSong();
			else
				controller.pauseSong();
		});
		root.getChildren().add(playButton);
		stage.setTitle("Music Player");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	
}
