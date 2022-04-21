import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MusicPlayerGUI extends Application implements Observer{

	private MusicPlayerModel model;
	private MusicPlayerController controller;
	private static Color buttonColor = Color.GREEN;
	private static int playButtonRadius = 50;
	private static int smallButtonRadius = 30;
	
	private Label artist;
	private Label album;
	private Label title;
	private Label year;
	private ImageView albumCover;
	private Media media;
	
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
		Circle playButton = new Circle(400, 500, playButtonRadius);
		playButton.setFill(buttonColor);
		playButton.setOnMouseClicked( e ->{
			createMeta(controller.getCurrentSong());
			if(!controller.getIsPlaying())
				controller.playSong();
			else
				controller.pauseSong();
		});
		Circle likeButton = new Circle(600, 500, smallButtonRadius);
		likeButton.setFill(buttonColor);
		likeButton.setOnMouseClicked(e -> {
			if (controller.addFavSong(controller.getCurrentSong().getName())) {
				likeButton.setFill(Color.RED); 
			} else {
				likeButton.setFill(buttonColor);
			}
		});
		Circle shuffleButton = new Circle(200, 500, smallButtonRadius);
		shuffleButton.setFill(buttonColor);
		shuffleButton.setOnMouseClicked(e -> {
			controller.shuffleSongs();
			createMeta(controller.getCurrentSong());
			System.out.println(controller.getCurrentSong().getName());
		});
		
		//Temporary objects to view metadata.
		// Barely visible in black background.
		// Recommended magnifying glass.
		artist = new Label();
	    artist.setId("artist");
	    album = new Label();
	    album.setId("album");
	    title = new Label();
	    title.setId("title");
	    year = new Label();
	    year.setId("year");
	    // Need a default image
	    albumCover = new ImageView();
		
		root.getChildren().add(title);
		root.getChildren().add(artist);
		root.getChildren().add(album);
		root.getChildren().add(year);
		root.getChildren().add(albumCover);
		
		
		root.getChildren().add(playButton);
		root.getChildren().add(likeButton);
		root.getChildren().add(shuffleButton);
		stage.setTitle("Music Player");
		stage.setScene(scene);
		stage.show();
	}
	
	// Temporary function that fetches metadata and updates it.
	private void createMeta(File song) {
	    try {
	      media = new Media(song.toURI().toString());
	      media.getMetadata().addListener(new MapChangeListener<String, Object>() {
	        @Override
	        public void onChanged(Change<? extends String, ? extends Object> ch) {
	          if (ch.wasAdded()) {
	            handleMetadata(ch.getKey(), ch.getValueAdded());
	          }
	        }
	      });

	    } catch (RuntimeException re) {
	      // Handle construction errors
	      System.out.println("Caught Exception: " + re.getMessage());
	    }
	  }
	
	private void handleMetadata(String key, Object value) {
	    if (key.equals("album")) {
	      album.setText(value.toString());
	    } else if (key.equals("artist")) {
	      artist.setText(value.toString());
	    } if (key.equals("title")) {
	      title.setText(value.toString());
	    } if (key.equals("year")) {
	      year.setText(value.toString());
	    } if (key.equals("image")) {
	    	System.out.println("Image exists");
	      albumCover.setImage((Image)value);
	    }
	  }
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	
}
