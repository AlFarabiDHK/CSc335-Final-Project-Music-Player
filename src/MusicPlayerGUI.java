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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MusicPlayerGUI extends Application implements Observer{

	private MusicPlayerModel model;
	private MusicPlayerController controller;
	private static Color whiteColor = Color.WHITE;
	private static int windowWidth = 800;
	private static int windowHeight = 600;
	private static int playButtonRadius = 40;
	private static int smallButtonRadius = 25;
	private static final int albumCoverDim = windowHeight/2;
	private static double textOffset = 0.025 * windowHeight;
	private Label artist;
	private Label title;
	private ImageView albumCover;
	private Slider progressBar;
	private Rectangle progressRec;
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
		
		
		//deBar sidebar = new SideBar();
		
		
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
		
		
		MenuButton Menu = new MenuButton(" ");
		Menu.setStyle("-fx-background-color: #22b14d;-fx-text-fill: black;");
		Menu.setGraphic(new ImageView(menu));
		MenuItem MenuEqualizer = new MenuItem("Equalizer");
		MenuItem MenuFavSongs = new MenuItem("Favourite");
		MenuItem MenuLibrary = new MenuItem("Music Library");
		Menu.getItems().add(MenuLibrary);
		Menu.getItems().add(MenuEqualizer);
		Menu.getItems().add(MenuFavSongs);
		Circle exitButton = new Circle(windowWidth, smallButtonRadius, smallButtonRadius/3);
		exitButton.setFill(new ImagePattern(exit));
		exitButton.setOnMouseClicked(e->{
			MainStage.setScene(MainScene);
		});
		
		MenuLibrary.setOnAction(e -> {
			BorderPane bp = new BorderPane();
			ScrollPane Sp = new ScrollPane();
			VBox LibraryView = new VBox();
			LibraryView.setPadding(new Insets(20));
			//Sp.setTranslateX(windowWidth/4);
			//Sp.setLayoutX(windowWidth/4);
			bp.setCenter(Sp);
			Sp.setContent(LibraryView);
			Sp.setStyle("-fx-background: black;");
//			Sp.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
//			LibraryView.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
			LibraryView.setBackground(appBackground);
			LibraryView.setAlignment(Pos.CENTER);
			LibraryView.getChildren().add(exitButton);
			LibraryView = addMusicLables(controller.getLibrary(), LibraryView);
			
			Scene Library = new Scene(bp,windowWidth,windowHeight);
			Library.setFill(Color.BLACK);
			MainStage.setScene(Library);
		});
		
		MenuFavSongs.setOnAction(e -> {
			ScrollPane FavScroll = new ScrollPane();
			VBox FavoriteView = new VBox();
			FavoriteView.setPadding(new Insets(20));
			FavScroll.setContent(FavoriteView);
			FavScroll.setStyle("-fx-background: black;");
			FavoriteView.setAlignment(Pos.CENTER);
			FavoriteView.getChildren().add(exitButton);
			FavoriteView = addMusicLables(controller.getFavSongs(), FavoriteView);
			Scene Favorites = new Scene(FavScroll,windowWidth,windowHeight);
			Favorites.setFill(Color.BLACK);
			MainStage.setScene(Favorites);
		});
		
		MenuEqualizer.setOnAction(e -> {
			EqualizerScene equalizer = new EqualizerScene(controller);
			equalizer.getGridPane().add(exitButton, 5, 0);
			equalizer.getGridPane().setBackground(appBackground);
			
			Scene Equalizer = new Scene(equalizer.getGridPane(),windowWidth,windowHeight);
			Equalizer.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
			
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
	    
	    progressBar = new Slider(0, 250, 0);
	    progressBar.setTranslateX(textOffset);
	    progressBar.setTranslateY(windowHeight*3/5 + 5 * textOffset);
	    progressBar.setMinWidth(windowWidth - 2 * textOffset);
	    progressBar.setMinHeight(0);
	    progressBar.setId("color-slider");

		progressRec = new Rectangle();
		
	    progressBarController();
	    root.getChildren().add(progressRec);
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
			temp[i].setAlignment(Pos.CENTER);
			temp[i].setMaxSize(windowWidth/2, windowHeight/10);
			temp[i].setStyle("-fx-background-color: black; -fx-border-color: white; "
					+ "-fx-border-width: 2px; -fx-padding: 20px;"
					+ "-fx-border-radius: 10;");
			temp[i].setTextFill(Color.LIGHTGREEN);
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
	
	private void progressBarController() 
	{
		root.getStylesheets().add(this.getClass().getResource("/root.css").toExternalForm());
		progressRec.heightProperty().bind(progressBar.heightProperty());
        //progressRec.widthProperty().bind(progressBar.widthProperty());
        progressRec.setTranslateX(textOffset);
	    progressRec.setTranslateY(windowHeight*3/5 + 5 * textOffset);
	    progressRec.setWidth(windowWidth - 2 * textOffset);
        progressRec.setFill(Color.web("#969696"));
        progressRec.setArcHeight(15);
        progressRec.setArcWidth(15);
        

	  
	    controller.getAudioPlayer().setOnReady(new Runnable() 
	    {
			
			@Override
			public void run() {
			progressBar.setMax(controller.getMax().toSeconds());
				
			}
		});
	    controller.getAudioPlayer().currentTimeProperty().addListener(new ChangeListener<Duration>() {
	    	@Override
	    	public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
	    		progressBar.setValue(newValue.toSeconds());
	    		
	            
	    		
	    	}
		});

	    progressBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            	if(!Double.isNaN(controller.getMax().toSeconds())) {
            	String style = String.format("-fx-fill: linear-gradient(to right, #006400 %f%%, #969696 %f%%);",
	    			((progressBar.getValue() * 100)/ controller.getMax().toSeconds()), ((progressBar.getValue() * 100)/ controller.getMax().toSeconds()));
	    		progressRec.setStyle(style);
	    		} else {
	    			progressRec.setStyle("-fx-fill: linear-gradient(to right, #006400 0%, #969696 0%);");
	    		}
               
            }
        });
	    progressBar.setOnMousePressed(e -> {
	    	controller.setAudioPlayerTime(progressBar.getValue());
	    	
	    });
	    
	    progressBar.setOnMouseDragged(e -> {
	    	controller.setAudioPlayerTime(progressBar.getValue());

	    });
	    
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
		progressBarController();
	}

	
}
