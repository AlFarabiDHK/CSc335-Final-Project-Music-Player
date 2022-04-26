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
import javafx.scene.control.ProgressBar;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MusicPlayerGUI extends Application implements Observer{

	private MusicPlayerModel model;
	private MusicPlayerController controller;
	private static Color buttonColor = Color.GREEN;
	private static Color whiteColor = Color.WHITE;
	private static int windowWidth = 800;
	private static int windowHeight = 600;
	private static int playButtonRadius = 40;
	private static int smallButtonRadius = 25;
	private static final int albumCoverDim = windowHeight/2;
	private Label artist;
	private Label title;
	private ImageView albumCover;
	private ProgressBar progressBar;
	private static Image defaultImage = new Image("/default-cover.jpg");
	private AnchorPane root;
	@Override
	public void start(Stage stage) throws Exception {
		model = new MusicPlayerModel();
		controller = new MusicPlayerController(model);
		model.addObserver(this);
		CornerRadii corner = new CornerRadii(0);
		Background appBackground = new Background(
				new BackgroundFill(Color.BLACK, corner, Insets.EMPTY));
		root = new AnchorPane();
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
		createMeta(controller.fetchMetadata(controller.getCurrentSong()));
//		ObservableMap<String, Object> metadata1 = controller.fetchMetadata(controller.getCurrentSong());
//		for(String key: metadata1.keySet())
//		{
//			System.out.println(key + ":"+ metadata1.get(key));
//			handleMetadata(key, metadata1.get(key));
//		}
		
		double textOffset = 0.025 * windowHeight;
		
		Circle playButton = new Circle(windowWidth/2, windowHeight * 5/6 + 2 * textOffset, playButtonRadius);
		playButton.setFill(new ImagePattern(play));
		playButton.setOnMouseClicked( e ->{
			
//			ObservableMap<String, Object> metadata = controller.fetchMetadata(controller.getCurrentSong());
//			System.out.println(metadata.keySet());
//			for(String key: metadata.keySet())
//			{
//				System.out.println(key + ":"+ metadata.get(key));
//				handleMetadata(key, metadata.get(key));
//			}
			
			
			if(!controller.getIsPlaying()) {
				controller.playSong();
				playButton.setFill(new ImagePattern(pause));
			}
			else {
				controller.pauseSong();
				playButton.setFill(new ImagePattern(play));
			}
			createMeta(controller.fetchMetadata(controller.getCurrentSong()));
		});
		
		Circle nextButton = new Circle(windowWidth * 0.6875, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		nextButton.setFill(new ImagePattern(next));
		nextButton.setOnMouseClicked( e ->{
			controller.nextSong();
			playButton.setFill(new ImagePattern(pause));
		});
		
		Circle previousButton = new Circle(windowWidth * 0.3125, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		previousButton.setFill(new ImagePattern(prev));
		previousButton.setOnMouseClicked( e ->{
			controller.previousSong();
//			createMeta(controller.fetchMetadata(controller.getCurrentSong()));
			playButton.setFill(new ImagePattern(pause));
		});
		
		Circle likeButton = new Circle(windowWidth * 7/8, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		likeButton.setFill(new ImagePattern(like));
		likeButton.setOnMouseClicked(e -> {
			if (controller.addFavSong(controller.getCurrentSong().getName())) {
				likeButton.setFill(new ImagePattern(liked)); 
			} else {
				likeButton.setFill(new ImagePattern(like));
			}
		});
		
		Circle shuffleButton = new Circle(windowWidth * 1/8, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		shuffleButton.setFill(new ImagePattern(shuffle));
		shuffleButton.setOnMouseClicked(e -> {
			controller.shuffleSongs();
			createMeta(controller.fetchMetadata(controller.getCurrentSong()));
			playButton.setFill(new ImagePattern(pause));
//			ObservableMap<String, Object> metadata = controller.fetchMetadata(controller.getCurrentSong());
//			System.out.println(metadata.keySet());
//			for(String key: metadata.keySet())
//			{
//				System.out.println(key + ":"+ metadata.get(key));
//				handleMetadata(key, metadata.get(key));
//			}
//			System.out.println(controller.getCurrentSong().getName());
//			
		});
		
		//Temporary objects to view metadata.
		// Barely visible in black background.
		// Recommended magnifying glass.
		
		
		artist = new Label();
	    artist.setId("artist");
	    
	    artist.setTranslateX(0);
	    artist.setTranslateY(windowHeight*3/5 + 3 * textOffset);
	    artist.setMinWidth(windowWidth);
	    artist.setAlignment(Pos.CENTER);
	    artist.setFont(new Font("Arial", 15));
	    
	    title = new Label();
	    title.setId("title");
	    title.setTranslateX(0);
	    title.setTranslateY(windowHeight*3/5 + 0 * textOffset);
	    title.setMinWidth(windowWidth);
	    title.setAlignment(Pos.CENTER);
	    title.setFont(new Font("Arial", 25));
	    
	   
	    // Need a default image
	    albumCover = new ImageView(defaultImage);
	    albumCover.setFitHeight(albumCoverDim);
	    albumCover.setFitWidth(albumCoverDim);
	    albumCover.setX(windowWidth * 0.3125);
	    albumCover.setY(windowHeight * 1/12);
	    
	    
		root.getChildren().add(title);
		root.getChildren().add(artist);
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
	private void createMeta(ObservableMap<String, Object> metamap) {
		if (!(metamap.size() == 0)) {
			for (String key : metamap.keySet()) {
				handleMetadata(key, metamap.get(key));
			}
		}
		metamap.addListener(new MapChangeListener<String, Object>() {
	    	  
	        @Override
	        public void onChanged(Change<? extends String, ? extends Object> ch) {
	        	System.out.println(ch.getKey());
	        	if (ch.wasAdded()) {
	            handleMetadata(ch.getKey(), ch.getValueAdded());
	          }
	        }
	      });

	    } 
	
	private void handleMetadata(String key, Object value) {;
	    if (key.equals("artist")) {
	      artist.setText(value.toString());
	      artist.setTextFill(whiteColor);
	      artist.setTextAlignment(TextAlignment.CENTER);
	    } if (key.equals("title")) {
	      title.setText(value.toString());
	      title.setTextFill(whiteColor);
	      title.setTextAlignment(TextAlignment.CENTER);
	    }
	    if (key.equals("image")) {
	      albumCover.setImage((Image)value);
	      albumCover.setFitHeight(albumCoverDim);
	      albumCover.setFitWidth(albumCoverDim);
	      albumCover.setX(windowWidth * 0.3125);
		  albumCover.setY(windowHeight * 1/12);
	    }
	  }
	
	
	@Override
	public void update(Observable o, Object arg) {
		createMeta(controller.fetchMetadata(controller.getCurrentSong()));
		//progressBar = model.getProgressBar();
		//root.getChildren().add(progressBar);
	}

	
}
