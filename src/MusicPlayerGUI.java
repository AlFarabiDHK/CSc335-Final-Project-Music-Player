import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.TreeSet;

import javafx.application.Application;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.layout.VBox;
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
	private VBox LibRoot;
	private Circle playButton;
	private Circle likeButton;
	private Image pause;
	private Image like;
	private Image liked;
	private Image exit;
	private Scene MainScene;
	private Stage MainStage;
	
	
	@Override
	public void start(Stage MainStage) throws Exception {
		this.MainStage = MainStage;
		model = new MusicPlayerModel();
		controller = new MusicPlayerController(model);
		model.addObserver(this);
		CornerRadii corner = new CornerRadii(0);
		Background appBackground = new Background(
				new BackgroundFill(Color.BLACK, corner, Insets.EMPTY));
		root = new AnchorPane();
		root.setBackground(appBackground);
		
		
		
		
		
		MainScene = new Scene(root,windowWidth,windowHeight);
		MainScene.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
		Image play = new Image("/PlayButton.png");
//		Image pause = new Image("/PauseButton.png");
		Image shuffle = new Image("/ShuffleButton.png");
		Image next = new Image("/NextButton.png");
		Image prev = new Image("/PreviousButton.png");
		Image menu = new Image("/MenuButton.png");
		like = new Image("/LikeButton.png");
		liked = new Image("/LikedButton.png");
		pause = new Image("/PauseButton.png");
		exit = new Image("/ExitButton.png");
		ImageView exitImageView = new ImageView(exit);
		exitImageView.setFitHeight(20);
		exitImageView.setFitWidth(20);
		
		MenuButton Menu = new MenuButton("Options");
		Menu.setStyle("-fx-background-color: #22b14d;-fx-text-fill: black;");
		Menu.setGraphic(new ImageView(menu));
		MenuItem MenuEqualizer = new MenuItem("Equalizer");
		MenuItem MenuFavSongs = new MenuItem("Favourite");
		MenuItem MenuLibrary = new MenuItem("Music Library");
		Menu.getItems().add(MenuLibrary);
		Menu.getItems().add(MenuEqualizer);
		Menu.getItems().add(MenuFavSongs);
		
		exitImageView.setOnMouseClicked(e ->{
			MainStage.setScene(MainScene);
		});
		
		MenuLibrary.setOnAction(e -> {
			VBox LibraryView = new VBox();
			LibraryView.setBackground(appBackground);
			LibraryView.setAlignment(Pos.CENTER);
			LibraryView = addMusicLables(controller.getLibrary(), LibraryView);
			exitImageView.setX(windowWidth - 50);
			exitImageView.setY(50);
			LibraryView.getChildren().add(exitImageView);
			Scene Library = new Scene(LibraryView,windowWidth,windowHeight);
			MainStage.setScene(Library);
		});
		
		MenuFavSongs.setOnAction(e -> {
			VBox FavoriteView = new VBox();
			FavoriteView.setBackground(appBackground);
			FavoriteView.setAlignment(Pos.CENTER);
			exitImageView.setX(windowWidth - 50);
			exitImageView.setY(50);
			FavoriteView.getChildren().add(exitImageView);
			FavoriteView = addMusicLables(controller.getFavSongs(), FavoriteView);
			Scene Favorites = new Scene(FavoriteView,windowWidth,windowHeight);
			MainStage.setScene(Favorites);
		});
		
		MenuEqualizer.setOnAction(e -> {
			EqualizerScene equalizer = new EqualizerScene(controller);
			exitImageView.setX(windowWidth - 50);
			exitImageView.setY(50);
			equalizer.getGridPane().getChildren().add(exitImageView);
			Scene Equalizer = new Scene(equalizer.getGridPane(),windowWidth,windowHeight);
			MainStage.setScene(Equalizer);
		});
		
		//controller.playSong();
		createMeta(controller.fetchMetadata(controller.getCurrentSong()));
//		ObservableMap<String, Object> metadata1 = controller.fetchMetadata(controller.getCurrentSong());
//		for(String key: metadata1.keySet())
//		{
//			System.out.println(key + ":"+ metadata1.get(key));
//			handleMetadata(key, metadata1.get(key));
//		}
		
		double textOffset = 0.025 * windowHeight;
		
		playButton = new Circle(windowWidth/2, windowHeight * 5/6 + 2 * textOffset, playButtonRadius);
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
		
		likeButton = new Circle(windowWidth * 7/8, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		if (controller.isFavsong(controller.getCurrentSong())) {
			likeButton.setFill(new ImagePattern(liked));
		} else {
			likeButton.setFill(new ImagePattern(like));
		}
		
		likeButton.setOnMouseClicked(e -> {
			try {
				if (controller.addFavSong(controller.getCurrentSong().getName())) {
					likeButton.setFill(new ImagePattern(liked)); 
				} else {
					likeButton.setFill(new ImagePattern(like));
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
	    
	    progressBar = new ProgressBar(0.5);
	    progressBar.setTranslateX(textOffset);
	    progressBar.setTranslateY(windowHeight*3/5 + 5 * textOffset);
	    progressBar.setMinWidth(windowWidth - 2 * textOffset);
	    
	    String progressBarCSS = 
	    		 "{--fx-background-color: linear-gradient(to bottom, derive(-fx-accent, -7%), derive(-fx-accent, 0%), derive(-fx-accent, -3%), derive(-fx-accent, -9%));"
	    		+ "--fx-background-insets: 3 3 4 3;"
	    		+ "--fx-background-radius: 2;"
	    		+ "--fx-padding: 0.75em;"
	    		+ "}";
	    progressBar.setStyle(progressBarCSS);
		root.getChildren().add(title);
		root.getChildren().add(artist);
		root.getChildren().add(albumCover);
		root.getChildren().add(progressBar);
		
		root.getChildren().add(playButton);
		root.getChildren().add(likeButton);
		root.getChildren().add(shuffleButton);
		root.getChildren().add(nextButton);
		root.getChildren().add(previousButton);
		root.getChildren().add(Menu);
		
		MainStage.setTitle("Music Player");
		MainStage.setScene(MainScene);
		MainStage.show();
	}
	
	private VBox addMusicLables(TreeSet<String> Library, VBox musicLabled) {
		System.out.println(Library.size());
		Label temp[] = new Label[Library.size()];
		int i = 0;
		for(String songName: Library) {
			temp[i] = new Label(songName);
			temp[i].setMaxSize(windowWidth/2, windowHeight/10);
			temp[i].setStyle("-fx-background-color: black;");
			temp[i].setTextFill(Color.GREEN);
			temp[i].setOnMouseClicked(e -> {
				controller.setCurrentIndex(controller.getSongIndex(songName));
				MainStage.setScene(MainScene);
				playButton.setFill(new ImagePattern(pause));
				System.out.println(controller.getCurrentSong().getName());
			});
			musicLabled.getChildren().add(temp[i]);
			i += 1;
		}
		return musicLabled;
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
		if(controller.isFavsong(controller.getCurrentSong())) {
			likeButton.setFill(new ImagePattern(liked));
		}
		else {
			
			likeButton.setFill(new ImagePattern(like));
		}
		if (model.getIsPlaylistOver()) {
			playButton.setFill(new ImagePattern(pause));
		}
	}

	
}
