import java.io.IOException;
import java.util.HashSet;

import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import static java.lang.Math.random;

/**
 * 
 * @author Dhruv Bhatia, Muhtasim Al-Farabi, Shrey Goyal, Suryashree Ray
 * 
 * This is the View class in our MVC model. It runs the JavaFX scene that runs the music player app.
 * It has several EventHandlers to listen for user mouse clicks and drags. It also has multiple scenes and our
 * logic can switch between them. It can play, pause, seek, and shuffle a song. It uses
 * observer/observable design pattern to communicate with the model directly. Our app can change between dark
 * and light mode, can change equalizer settings, and can select any song from the library. Finally, every user
 * can choose their favorite songs (and it's stored locally only). 
 *
 */
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
	private AnchorPane root;
	private Circle playButton;
	private Circle likeButton;
	private Image pause;
	private Image like;
	private Image liked;
	private Image exit;
	private Scene MainScene;
	private Stage MainStage;
	private Color white;
	private Color black;
	private Group blendModeGroup;
	
	
	/**
	 * Starts the scene
	 * 
	 * <p>
	 * 
	 * This method initializes everything that needs to be shown on the scene. It has most of the functionalities that
	 * is required to run the music player. The in-line comments describes the purpose of each block of code. 
	 * @param MainStage a stage on which the scenes will be placed.
	 */
	
	@Override
	public void start(Stage MainStage) throws Exception {
		// initializing most global variables
		this.MainStage = MainStage;
		model = new MusicPlayerModel();
		controller = new MusicPlayerController(model);
		model.addObserver(this);
		CornerRadii corner = new CornerRadii(0);
		Background appBackground = new Background(
				new BackgroundFill(black, corner, Insets.EMPTY));
		Background appBackgroundWhite = new Background(
				new BackgroundFill(white, corner, Insets.EMPTY));
		root = new AnchorPane();
		root.setBackground(appBackground);
		white = Color.WHITE;
		black = Color.BLACK;
		MainScene = new Scene(root,windowWidth,windowHeight);
		MainScene.setFill(black);
		MainScene.getStylesheets().add(getClass().getResource("/MenuDark.css").toExternalForm());
		
		// loading up all assets 
		Image play = new Image("/PlayButton.png");
		Image shuffle = new Image("/ShuffleButton.png");
		Image next = new Image("/NextButton.png");
		Image prev = new Image("/PreviousButton.png");
		Image menu = new Image("/MenuButton.png");
		Image menuDark = new Image("/MenuButtonDak.png");
		Image exitDark = new Image("/ExitButtonDark.png");
		like = new Image("/LikeButton.png");
		liked = new Image("/LikedButton.png");
		pause = new Image("/PauseButton.png");
		exit = new Image("/ExitButton.png");
		artist = new Label();
		title = new Label();
		
		// background circle
		Group circles = new Group();
        for (int i = 0; i < 30; i++) 
        {
           Circle circle = new Circle(150, Color.web("white", 0.05));
           circle.setStrokeType(StrokeType.OUTSIDE);
           circle.setStroke(Color.web("white", 0.16));
           circle.setStrokeWidth(4);
           circles.getChildren().add(circle);
        }

        circles.setEffect(new BoxBlur(10, 10, 3));

        Rectangle colors = new Rectangle(MainScene.getWidth(), MainScene.getHeight(),
                 new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new 
                     Stop[]{
                        new Stop(0, Color.web("#f8bd55")),
                        new Stop(0.14, Color.web("#c0fe56")),
                        new Stop(0.28, Color.web("#5dfbc1")),
                        new Stop(0.43, Color.web("#64c2f8")),
                        new Stop(0.57, Color.web("#be4af7")),
                        new Stop(0.71, Color.web("#ed5fc2")),
                        new Stop(0.85, Color.web("#ef504c")),
                        new Stop(1, Color.web("#f2660f")),}));
            colors.widthProperty().bind(MainScene.widthProperty());
            colors.heightProperty().bind(MainScene.heightProperty());

            blendModeGroup = new Group(new Group(new Rectangle(MainScene.getWidth(), MainScene.getHeight(),
                    Color.BLACK), circles), colors);
		// setting up all artist properties
		artist.setId("artist");
	    artist.setTranslateX(0);
	    artist.setTranslateY(windowHeight*3/5 + 3 * textOffset);
	    artist.setMinWidth(windowWidth);
	    artist.setAlignment(Pos.CENTER);
	    artist.setTextFill(whiteColor);
	    artist.setFont(new Font("Arial", 15));
	    
	    // setting up all title properties
	    
	    title.setId("title");
	    title.setTranslateX(0);
	    title.setTranslateY(windowHeight*3/5 + 0 * textOffset);
	    title.setMinWidth(windowWidth);
	    title.setAlignment(Pos.CENTER);
	    title.setTextFill(whiteColor);
	    title.setFont(new Font("Arial", 25));
	    
	    // Menu functionality
	    
		MenuButton Menu = new MenuButton(" ");
		Menu.setGraphic(new ImageView(menu));
		MenuItem MenuEqualizer = new MenuItem("Equalizer");
		MenuItem MenuFavSongs = new MenuItem("Favourite");
		MenuItem MenuLibrary = new MenuItem("Music Library");
		MenuItem MenuMode = new MenuItem("Light mode: Off");
		Menu.getItems().add(MenuLibrary);
		Menu.getItems().add(MenuEqualizer);
		Menu.getItems().add(MenuFavSongs);
		Menu.getItems().add(MenuMode);
		
		// styling exit button
		Circle exitButton = new Circle(windowWidth, smallButtonRadius, smallButtonRadius/3);
		exitButton.setFill(new ImagePattern(exit));
		exitButton.setScaleX(1.4);
		exitButton.setScaleY(1.4);
		
		// changes the scene when the button is clicked
		exitButton.setOnMouseClicked(e->{
			changeScene(MainScene);
		});
		
		// generates everything for the main library
		MenuLibrary.setOnAction(e -> {
			BorderPane bp = new BorderPane();
			HBox addLabel = new HBox(330);
			ScrollPane sp = new ScrollPane();
			Label label = new Label("Library");
			VBox LibraryView = new VBox();
			
			LibraryView = addMusicLables(controller.getLibrary(), LibraryView);
			
			if(controller.getColorMode()) {
				sp.setStyle("-fx-background: black;");
				LibraryView.setStyle("-fx-background: black;");
				bp.setBackground(appBackground);
				sp.setBorder(new Border(new BorderStroke(black, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
				label.setStyle("-fx-text-fill:WHITE; -fx-font-weight: bold;-fx-font-size: 20px;");
				sp.getStylesheets().add(getClass().getResource("/MenuDark.css").toExternalForm());
			}else {
				sp.setStyle("-fx-background: white;");
				LibraryView.setStyle("-fx-background: white;");
				bp.setBackground(appBackgroundWhite);
				sp.setBorder(new Border(new BorderStroke(white, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
				label.setStyle("-fx-text-fill: BLACK; -fx-font-weight: bold;-fx-font-size: 20px;");
				sp.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
			}
			
			Button b = new Button("Sort");
			LibraryView.setPadding(new Insets(20));
			bp.setCenter(sp);
			sp.setContent(LibraryView);
			
			sp.setFitToWidth(true);
			LibraryView.setBackground(appBackground);
			LibraryView.setAlignment(Pos.CENTER);
			
			addLabel.getChildren().add(0, b);
			addLabel.getChildren().add(0, label);
			addLabel.getChildren().add(0, exitButton);
			
			
			bp.setTop(addLabel);
			
			Scene Library = new Scene(bp,windowWidth,windowHeight);
			if(controller.getColorMode()) {
				Library.getStylesheets().add(getClass().getResource("/MenuDark.css").toExternalForm());
				Library.setFill(black);
			} else {
				Library.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
				Library.setFill(white);
			}
			
			changeScene(Library);
			
			ScrollPane spSorted = new ScrollPane();
			VBox LibraryViewSorted = new VBox();
			
			LibraryViewSorted = addMusicLables(controller.getSortedLibrary(), LibraryViewSorted);
			if(controller.getColorMode()) {
				spSorted.setStyle("-fx-background: black;");
				spSorted.setBorder(new Border(new BorderStroke(black, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
				spSorted.getStylesheets().add(getClass().getResource("/MenuDark.css").toExternalForm());
			}else {
				spSorted.setStyle("-fx-background: white;");
				spSorted.setBorder(new Border(new BorderStroke(white, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
				spSorted.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
			}

			LibraryViewSorted.setPadding(new Insets(20));

			spSorted.setContent(LibraryViewSorted);			
			spSorted.setFitToWidth(true);
			LibraryViewSorted.setBackground(appBackground);
			LibraryViewSorted.setAlignment(Pos.CENTER);

			b.setOnMouseClicked(f -> {
				if (!controller.getIsSorted()) {
					controller.setIsSorted(true);
					bp.getChildren().remove(sp);
					bp.setCenter(spSorted);
				} else {
					controller.setIsSorted(false);
					bp.getChildren().remove(spSorted);
					bp.setCenter(sp);
				}
			});
		});
		
		// generates everything for the fav song library 
		
		MenuFavSongs.setOnAction(e -> {
			BorderPane gp = new BorderPane();
			HBox addLabel = new HBox(332);
			ScrollPane favScroll = new ScrollPane();
			Label label = new Label("Favorite Songs");

			VBox FavoriteView = new VBox();
			if(controller.getColorMode()) {
				gp.setBackground(appBackground);
				FavoriteView.setStyle("-fx-background-color: black");
				favScroll.setStyle("-fx-background-color: black");
				favScroll.setBorder(new Border(new BorderStroke(black, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
				favScroll.getStylesheets().add(getClass().getResource("/MenuDark.css").toExternalForm());
				label.setStyle("-fx-text-fill:WHITE; -fx-font-weight: bold; -fx-font-size: 20px;");
			}
			else {
				gp.setBackground(appBackgroundWhite);
				FavoriteView.setStyle("-fx-background-color: white");
				favScroll.setStyle("-fx-background-color: white");
				favScroll.setBorder(new Border(new BorderStroke(white, BorderStrokeStyle.NONE, corner, BorderWidths.EMPTY)));
				favScroll.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
				label.setStyle("-fx-text-fill: BLACK; -fx-font-weight: bold; -fx-font-size: 20px;");
			}
			gp.setCenter(favScroll);
			FavoriteView.setPadding(new Insets(15));
			favScroll.setContent(FavoriteView);
			
			favScroll.setFitToWidth(true);
			FavoriteView.setAlignment(Pos.CENTER);
			
			addLabel.getChildren().add(0, label);
			addLabel.getChildren().add(0, exitButton);
			
			gp.setTop(addLabel);
			FavoriteView = addMusicLables(controller.getFavSongs(), FavoriteView);
			Scene Favorites = new Scene(gp,windowWidth,windowHeight);
			if(controller.getColorMode()) {
				Favorites.setFill(black);
				Favorites.getStylesheets().add(getClass().getResource("/MenuDark.css").toExternalForm());
			}
			else {
				Favorites.setFill(white);
				Favorites.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
			}
			changeScene(Favorites);
		});
		// generates everything for the equalizer
		MenuEqualizer.setOnAction(e -> {
			EqualizerScene equalizer = new EqualizerScene(controller);
			HBox addLabel = new HBox(330);
			Label label = new Label("Equalizer");
			equalizer.getGridPane().setBackground(appBackground);
			
			Scene Equalizer = new Scene(equalizer.getGridPane(),windowWidth,windowHeight);
			if(controller.getColorMode()) {
				Equalizer.setFill(black);
				Equalizer.getStylesheets().add(getClass().getResource("/MenuDark.css").toExternalForm());
				label.setStyle("-fx-text-fill:WHITE; -fx-font-weight: bold;");
			}
			else {
				Equalizer.setFill(white);
				Equalizer.getStylesheets().add(getClass().getResource("/Menu.css").toExternalForm());
				label.setStyle("-fx-text-fill: BLACK; -fx-font-weight: bold;");
			}
			
			equalizer.getGridPane().add(exitButton, 0, 0);
			equalizer.getGridPane().add(label, 4, 0);
			
			changeScene(Equalizer);

		});
		// switches between dark and light mode
		MenuMode.setOnAction(e->{
			if(controller.getColorMode()) {
				MainScene.setFill(white);
				MenuMode.setText("Light Mode: On");
				Menu.setGraphic(new ImageView(menuDark));
				artist.setTextFill(black);
				title.setTextFill(black);
				controller.setColorMode(false);
				exitButton.setFill(new ImagePattern(exitDark));
				// animation black
				root.getChildren().remove(blendModeGroup);
                blendModeGroup = 
                        new Group(new Group(new Rectangle(MainScene.getWidth(), MainScene.getHeight(),
                            Color.MEDIUMPURPLE), circles), colors);

                colors.setBlendMode(BlendMode.OVERLAY);
                root.getChildren().add(0, blendModeGroup);
			} else {
				MainScene.setFill(black);
				MenuMode.setText("Light Mode: Off");
				Menu.setGraphic(new ImageView(menu));
				artist.setTextFill(white);
				title.setTextFill(white);
				controller.setColorMode(true);
				exitButton.setFill(new ImagePattern(exit));
				// animation whiite
				root.getChildren().remove(blendModeGroup);
                blendModeGroup = 
                        new Group(new Group(new Rectangle(MainScene.getWidth(), MainScene.getHeight(),
                            black), circles), colors);
                colors.setBlendMode(BlendMode.OVERLAY);
                root.getChildren().add(0, blendModeGroup);
			}
		});

		createMeta(controller.fetchMetadata(controller.getCurrentSong()));	
		playButton = new Circle(windowWidth/2, windowHeight * 5/6 + 2 * textOffset, playButtonRadius);
		playButton.setFill(new ImagePattern(play));
		
		// event handler for the play button
		playButton.setOnMouseClicked( e ->{
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
		
		// event handler for the play button
		nextButton.setOnMouseClicked( e ->{
			controller.nextSong();
			playButton.setFill(new ImagePattern(pause));
		});
		
		Circle previousButton = new Circle(windowWidth * 0.3125, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		previousButton.setFill(new ImagePattern(prev));
		// event handler for the previous button
		previousButton.setOnMouseClicked( e ->{
			controller.previousSong();
			playButton.setFill(new ImagePattern(pause));
		});
		
		likeButton = new Circle(windowWidth * 7/8, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		if (controller.isFavsong(controller.getCurrentSong())) {
			likeButton.setFill(new ImagePattern(liked));
		} else {
			likeButton.setFill(new ImagePattern(like));
		}
		// event handler for the like button
		likeButton.setOnMouseClicked(e -> {
			try {
				if (controller.addFavSong(controller.getCurrentSong().getName())) {
					likeButton.setFill(new ImagePattern(liked)); 
				} else {
					likeButton.setFill(new ImagePattern(like));
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		Circle shuffleButton = new Circle(windowWidth * 1/8, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		shuffleButton.setFill(new ImagePattern(shuffle));
		// event handler for the shuffle button
		shuffleButton.setOnMouseClicked(e -> {
			controller.shuffleSongs();
			createMeta(controller.fetchMetadata(controller.getCurrentSong()));
			playButton.setFill(new ImagePattern(pause));
			
		});
		
		// setting up all album cover properties
		
	    albumCover = new ImageView();
	    albumCover.setFitHeight(albumCoverDim);
	    albumCover.setFitWidth(albumCoverDim);
	    albumCover.setX(windowWidth * 0.3125);
	    albumCover.setY(windowHeight * 1/12);
		// setting up all progress bar properties
	    progressBar = new Slider(0, 250, 0);
	    progressBar.setTranslateX(textOffset);
	    progressBar.setTranslateY(windowHeight*3/5 + 5 * textOffset);
	    progressBar.setMinWidth(windowWidth - 2 * textOffset);
	    progressBar.setMinHeight(0);
	    progressBar.setId("color-slider");

		progressRec = new Rectangle();
		progressRec.setStyle("-fx-fill: linear-gradient(to right, #006400 0%, rgba(164, 164, 164, 0.8) 0%);");
		root.getStylesheets().add(this.getClass().getResource("/root.css").toExternalForm());
	    progressBarController();
	    
	    colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);
        // animation timeline
        Timeline timeline = new Timeline();
        for (Node circle: circles.getChildren()) {
            timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, // set start position at 0
                    new KeyValue(circle.translateXProperty(), random() * 800),
                    new KeyValue(circle.translateYProperty(), random() * 600)
                ),
                new KeyFrame(new Duration(40000), // set end position at 40s
                    new KeyValue(circle.translateXProperty(), random() * 800),
                    new KeyValue(circle.translateYProperty(), random() * 600)
                )
            );
        }
        // play 40s of animation
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.play();
	    
	    // adding everything to the root
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
	
	/**
	 * Adds unsorted musicLabel to a VBox
	 * 
	 * <p>
	 * 
	 * This method takes a set that contains music title information. Then it loops though the set and
	 * adds each song name to the VBox
	 * @param hashSet a TreeSet of Strings that is unsorted and contains the song names
	 * @param musicLabled a VBox to be modified
	 * @return the VBox that was inputed will be modified and returned
	 */
	private VBox addMusicLables(HashSet<String> hashSet, VBox musicLabled) {
		Label temp[] = new Label[hashSet.size()];
		int i = 0;
		for(String songName: hashSet) {
			temp[i] = new Label(songName);
			temp[i].setId("lib-label");
			if(controller.getColorMode()) {
				temp[i].getStylesheets().add(getClass().getResource("/label.css").toExternalForm());
				temp[i].setTextFill(white);
			}
			else {
				temp[i].getStylesheets().add(getClass().getResource("/labelLight.css").toExternalForm());
				temp[i].setTextFill(black);
			}
			
			temp[i].setAlignment(Pos.CENTER);
			temp[i].setMaxSize(windowWidth/2, windowHeight/10);
			
			temp[i].setOnMouseClicked(e -> {
				controller.setCurrentIndex(controller.getSongIndex(songName));
				changeScene(MainScene);
				playButton.setFill(new ImagePattern(pause));
			});
			musicLabled.getChildren().add(temp[i]);
			i += 1;
		}
		musicLabled.setSpacing(10);
		return musicLabled;
	}
	/**
	 * Adds sorted musicLabel to a VBox
	 * 
	 * <p>
	 * 
	 * This method takes a set that contains music title information. Then it loops though the set and
	 * adds each song name to the VBox
	 * @param hashSet a TreeSet of Strings that is sorted and contains the song names
	 * @param musicLabled a VBox to be modified
	 * @return the VBox that was inputed will be modified and returned
	 */
	
	private VBox addMusicLables(TreeSet<String> hashSet, VBox musicLabled) {
		Label temp[] = new Label[hashSet.size()];
		int i = 0;
		for(String songName: hashSet) {
			temp[i] = new Label(songName);
			temp[i].setId("lib-label");
			if(controller.getColorMode()) {
				temp[i].getStylesheets().add(getClass().getResource("/label.css").toExternalForm());
				temp[i].setTextFill(white);
			}
			else {
				temp[i].getStylesheets().add(getClass().getResource("/labelLight.css").toExternalForm());
				temp[i].setTextFill(black);
			}
			
			temp[i].setAlignment(Pos.CENTER);
			temp[i].setMaxSize(windowWidth/2, windowHeight/10);
			
			temp[i].setOnMouseClicked(e -> {
				controller.setCurrentIndex(controller.getSongIndex(songName));
				changeScene(MainScene);
				playButton.setFill(new ImagePattern(pause));
			});
			musicLabled.getChildren().add(temp[i]);
			i += 1;
		}
		musicLabled.setSpacing(10);
		return musicLabled;
	}

	

	/**
	 * Fetches metadata and updates it.
	 * <p>
	 * 
	 * This method loops through the map and uses the handleMetadata method to set metadata
	 * to the scene. It also listens to the change in the mao and repeats the process.
	 * 
	 * @param metamap an ObservableMap<String, Object> map that holds the metadata
	 */
	private void createMeta(ObservableMap<String, Object> metamap) {
		if (!(metamap.size() == 0)) {
			for (String key : metamap.keySet()) {
				handleMetadata(key, metamap.get(key));
			}
		}
		metamap.addListener(new MapChangeListener<String, Object>() {
	    	  
	        @Override
	        public void onChanged(Change<? extends String, ? extends Object> ch) {
	        	if (ch.wasAdded()) {
	            handleMetadata(ch.getKey(), ch.getValueAdded());
	          }
	        }
	      });

	    } 
	
	/**
	 * Sets the metadata to the scene
	 * 
	 * <p>
	 * This method takes a key and a value and checks if it is an artist, a title, or
	 * an album cover. Then, it sets the value to the appropriate nodes. 
	 * @param key a String, contains the strings artist, title, or image
	 * @param value a Object that can be a text or an image
	 */
	
	private void handleMetadata(String key, Object value) {;
	    if (key.equals("artist")) {
	      artist.setText(value.toString());
	      artist.setTextAlignment(TextAlignment.CENTER);
	    } if (key.equals("title")) {
	      title.setText(value.toString());
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
	/**
     * Controls the progress bar
     * 
     * <p>
     * 
     * This method is responsible for multiple event handlers that enables a working progress bar
     * that can be used to seek a song.
     */
	
	private void progressBarController() 
	{
		
		progressRec.heightProperty().bind(progressBar.heightProperty());
		progressRec.translateXProperty().bind(progressBar.translateXProperty());
		progressRec.translateYProperty().bind(progressBar.translateYProperty());
	    progressRec.setWidth(windowWidth - 2 * textOffset);
        progressRec.setFill(black);
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
            	String style = String.format("-fx-fill: linear-gradient(to right, #006400 %f%%, rgba(164, 164, 164, 0.8) %f%%);",
	    			((progressBar.getValue() * 100)/ controller.getMax().toSeconds()) + 0.1, ((progressBar.getValue() * 100)/ controller.getMax().toSeconds()) + 0.1);
	    		progressRec.setStyle(style);
	    		} else {
	    			progressRec.setStyle("-fx-fill: linear-gradient(to right, #006400 0%, rgba(164, 164, 164, 0.8) 0%);");
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
	
	/**
     * Changes a scene
     * 
     * <p>
     * 
     * This method takes a scene as input and sets the main stage to that scene. It also invokes a
     * fade animation. 
     * @param sc Scene to be set to the main stage
     */
	
	private void changeScene(Scene sc) {
		Timeline timeline = new Timeline();
        KeyFrame key = new KeyFrame(Duration.millis(400),
                       new KeyValue (MainStage.getScene().getRoot().opacityProperty(), 0)); 
        timeline.getKeyFrames().add(key);  
        timeline.play();
        timeline.setOnFinished((ae) -> {
        	Scene oldScene = MainStage.getScene();
        	MainStage.setScene(sc); 
        	oldScene.getRoot().setOpacity(1);
        });
        
	}
	
	/**
     * This method is called whenever the observed object is changed.
     * 
     * <p>
     * This method is called whenever the observed object is changed. 
     * An application calls an Observable object's notifyObservers method 
     * to have all the object's observers notified of the change. 
     * @param o the observable object
     * @param arg an argument passed to the notifyObservers method
     * 
     */
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

	
}}