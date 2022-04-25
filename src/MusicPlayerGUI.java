import java.io.File;
import java.util.Collection;
import java.util.HashMap;
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
import javafx.scene.layout.GridPane;
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
	private static int windowWidth = 800;
	private static int windowHeight = 600;
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
		controller = new MusicPlayerController(model);
		model.addObserver(this);
		CornerRadii corner = new CornerRadii(0);
		Background appBackground = new Background(
				new BackgroundFill(Color.BLACK, corner, Insets.EMPTY));
		AnchorPane root = new AnchorPane();
		root.setBackground(appBackground);
		Scene scene = new Scene(root,windowWidth,windowHeight);
		Image play = new Image("/PlayButton.png");
		Image pause = new Image("/PauseButton.png");
		Image shuffle = new Image("/ShuffleButton.png");
		Image like = new Image("/LikeButton.png");
		Image liked = new Image("/LikedButton.png");
		Image next = new Image("/NextButton.png");
		Image prev = new Image("/PreviousButton.png");
		
		//controller.playSong();
		ObservableMap<String, Object> metadata1 = controller.fetchMetadata(controller.getCurrentSong());
		for(String key: metadata1.keySet())
		{
			System.out.println(key + ":"+ metadata1.get(key));
			handleMetadata(key, metadata1.get(key));
		}
		
		Circle playButton = new Circle(windowWidth/2, windowHeight * 5/6, playButtonRadius);
		playButton.setFill(new ImagePattern(play));
		playButton.setOnMouseClicked( e ->{

			ObservableMap<String, Object> metadata = controller.fetchMetadata(controller.getCurrentSong());
			System.out.println(metadata.keySet());
			for(String key: metadata.keySet())
			{
				System.out.println(key + ":"+ metadata.get(key));
				handleMetadata(key, metadata.get(key));
			}
			if(!controller.getIsPlaying()) {
				controller.playSong();
				playButton.setFill(new ImagePattern(pause));
			}
			else {
				controller.pauseSong();
				playButton.setFill(new ImagePattern(play));
			}
		});
		
		Circle nextButton = new Circle(windowWidth * 0.6875, windowHeight * 5/6, smallButtonRadius);
		nextButton.setFill(new ImagePattern(next));
		nextButton.setOnMouseClicked( e ->{
			// go next
		});
		Circle previousButton = new Circle(windowWidth * 0.3125, windowHeight * 5/6, smallButtonRadius);
		previousButton.setFill(new ImagePattern(prev));
		previousButton.setOnMouseClicked( e ->{
			// go prev
		});
		Circle likeButton = new Circle(windowWidth * 7/8, windowHeight * 5/6, smallButtonRadius);
		likeButton.setFill(new ImagePattern(like));
		likeButton.setOnMouseClicked(e -> {
			if (controller.addFavSong(controller.getCurrentSong().getName())) {
				likeButton.setFill(new ImagePattern(liked)); 
			} else {
				likeButton.setFill(new ImagePattern(like));
			}
		});
		
		Circle shuffleButton = new Circle(windowWidth * 1/8, windowHeight * 5/6, smallButtonRadius);
		shuffleButton.setFill(new ImagePattern(shuffle));
		shuffleButton.setOnMouseClicked(e -> {
			controller.shuffleSongs();
			ObservableMap<String, Object> metadata = controller.fetchMetadata(controller.getCurrentSong());
			System.out.println(metadata.keySet());
			for(String key: metadata.keySet())
			{
				System.out.println(key + ":"+ metadata.get(key));
				handleMetadata(key, metadata.get(key));
			}
			System.out.println(controller.getCurrentSong().getName());
			
		});
		
		//Temporary objects to view metadata.
		// Barely visible in black background.
		// Recommended magnifying glass.
		
		double textOffset = 0.025 * windowHeight;
		artist = new Label();
	    artist.setId("artist");
	    artist.setTranslateX(windowWidth/2);
	    artist.setTranslateY(windowHeight*3/5 + 0 * textOffset);
	    album = new Label();
	    album.setId("album");
	    album.setTranslateX(windowWidth/2);
	    album.setTranslateY(windowHeight*3/5 + 1 * textOffset);
	    
	    title = new Label();
	    title.setId("title");
	    title.setTranslateX(windowWidth/2);
	    title.setTranslateY(windowHeight*3/5 + 2 * textOffset);
	    
	    year = new Label();
	    year.setId("year");
	    year.setTranslateX(windowWidth/2);
	    year.setTranslateY(windowHeight*3/5 + 3 * textOffset);
	   
	    // Need a default image
	    Image defaultImage = new Image("/default-cover.jpg");
	    albumCover = new ImageView(defaultImage);
	    albumCover.setFitHeight(albumCoverDim);
	    albumCover.setFitWidth(albumCoverDim);
	    albumCover.setX(windowWidth * 0.3125);
	    albumCover.setY(windowHeight * 1/12);
		root.getChildren().add(title);
		root.getChildren().add(artist);
		root.getChildren().add(album);
		root.getChildren().add(year);
		root.getChildren().add(albumCover);
		
		root.getChildren().add(playButton);
		root.getChildren().add(likeButton);
		root.getChildren().add(shuffleButton);
		root.getChildren().add(nextButton);
		root.getChildren().add(previousButton);
		stage.setTitle("Music Player");
		stage.setScene(scene);
		stage.show();
	}
	
	// Temporary function that fetches metadata and updates it.
	/*private void createMeta(File song) {
	    try 
	    {
	      media = new Media(song.toURI().toString());
	      media.getMetadata().addListener(new MapChangeListener<String, Object>() 
	      {
	        @Override
	        public void onChanged(Change<? extends String, ? extends Object> ch) 
	        {
	          if (ch.wasAdded()) 
	          {
	            handleMetadata(ch.getKey(), ch.getValueAdded());
	          }
	        }
	      });

	    } catch (RuntimeException re) {
	      // Handle construction errors
	      System.out.println("Caught Exception: " + re.getMessage());
	    }
	  }*/
	
	private void handleMetadata(String key, Object value) {
	    if (key.equals("album")) {
	      album.setText(value.toString());
	      album.setTextFill(whiteColor);
	      album.setTextAlignment(TextAlignment.CENTER);
	    } else if (key.equals("artist")) {
	      artist.setText(value.toString());
	      artist.setTextFill(whiteColor);
	      artist.setTextAlignment(TextAlignment.CENTER);
	    } if (key.equals("title")) {
	      title.setText(value.toString());
	      title.setTextFill(whiteColor);
	      title.setTextAlignment(TextAlignment.CENTER);
	    } if (key.equals("year")) {
	      year.setText(value.toString());
	      year.setTextFill(whiteColor);
	      year.setTextAlignment(TextAlignment.CENTER);
	    } if (key.equals("image")) {
	    	System.out.println("Image exists");
	      albumCover.setImage((Image)value);
	      albumCover.setFitHeight(albumCoverDim);
	      albumCover.setFitWidth(albumCoverDim);
	      albumCover.setX(windowWidth * 0.3125);
		  albumCover.setY(windowHeight * 1/12);
	    }
	  }
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		System.out.println("happening");
		ObservableMap<String, Object> metadata = model.fetchMetadata(controller.getCurrentSong());
		System.out.println(metadata.keySet());
		for(String key: metadata.keySet())
		{
			System.out.println(key + ":"+ metadata.get(key));
			handleMetadata(key, metadata.get(key));
		}
		// Update cover art when next songs play
		
	}

	
}
