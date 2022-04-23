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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MusicPlayerGUI extends Application implements Observer{

	private MusicPlayerModel model;
	private MusicPlayerController controller;
	private static Color buttonColor = Color.GREEN;
	private static Color whiteColor = Color.WHITE;
	private static int playButtonRadius = 50;
	private static int smallButtonRadius = 30;
	private static final int albumCoverDim = 300;
	private Label artist;
	private Label album;
	private Label title;
	private Label year;
	private ImageView albumCover;
	private Media media;
	
	@Override
	public void start(Stage stage) throws Exception {
		model = new MusicPlayerModel();
		model.addObserver(this);
		controller = new MusicPlayerController(model);
		CornerRadii corner = new CornerRadii(0);
		Background appBackground = new Background(
				new BackgroundFill(Color.BLACK, corner, Insets.EMPTY));
		AnchorPane root = new AnchorPane();
		root.setBackground(appBackground);
		Scene scene = new Scene(root, 800, 600);
		Image play = new Image("/PlayButton.png");
		Image pause = new Image("/PauseButton.png");
		Image shuffle = new Image("/ShuffleButton.png");
		Image like = new Image("/LikeButton.png");
		Image liked = new Image("/LikedButton.png");
		
		Circle playButton = new Circle(400, 500, playButtonRadius);
		playButton.setFill(new ImagePattern(pause));
		playButton.setOnMouseClicked( e ->{
			createMeta(controller.getCurrentSong());
			if(!controller.getIsPlaying()) {
				controller.playSong();
				playButton.setFill(new ImagePattern(play));
			}
			else {
				controller.pauseSong();
				playButton.setFill(new ImagePattern(pause));
			}
		});
		Circle likeButton = new Circle(600, 500, smallButtonRadius);
		likeButton.setFill(new ImagePattern(like));
		likeButton.setOnMouseClicked(e -> {
			if (controller.addFavSong(controller.getCurrentSong().getName())) {
				likeButton.setFill(new ImagePattern(liked)); 
			} else {
				likeButton.setFill(new ImagePattern(like));
			}
		});
		Circle shuffleButton = new Circle(200, 500, smallButtonRadius);
		shuffleButton.setFill(new ImagePattern(shuffle));
		shuffleButton.setOnMouseClicked(e -> {
			controller.shuffleSongs();
			createMeta(controller.getCurrentSong());
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
	      album.setTextFill(whiteColor);
	    } else if (key.equals("artist")) {
	      artist.setText(value.toString());
	      artist.setTextFill(whiteColor);
	    } if (key.equals("title")) {
	      title.setText(value.toString());
	      title.setTextFill(whiteColor);
	    } if (key.equals("year")) {
	      year.setText(value.toString());
	      year.setTextFill(whiteColor);
	    } if (key.equals("image")) {
	    	System.out.println("Image exists");
	      albumCover.setImage((Image)value);
	      albumCover.setFitHeight(albumCoverDim);
	      albumCover.setFitWidth(albumCoverDim);
	      albumCover.setX(250);
	      albumCover.setY(50);
	    }
	  }
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		// Update cover art when next songs play
		
	}

	
}
