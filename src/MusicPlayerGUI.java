
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
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
	
	@Override
	public void start(Stage MainStage) throws Exception {
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
		artist.setId("artist");
	    
	    artist.setTranslateX(0);
	    artist.setTranslateY(windowHeight*3/5 + 3 * textOffset);
	    artist.setMinWidth(windowWidth);
	    artist.setAlignment(Pos.CENTER);
	    artist.setTextFill(whiteColor);
	    artist.setFont(new Font("Arial", 15));
	    
	    
	    title.setId("title");
	    title.setTranslateX(0);
	    title.setTranslateY(windowHeight*3/5 + 0 * textOffset);
	    title.setMinWidth(windowWidth);
	    title.setAlignment(Pos.CENTER);
	    title.setTextFill(whiteColor);
	    title.setFont(new Font("Arial", 25));
	    
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
		Circle exitButton = new Circle(windowWidth, smallButtonRadius, smallButtonRadius/3);
		exitButton.setFill(new ImagePattern(exit));
		exitButton.setScaleX(1.4);
		exitButton.setScaleY(1.4);
		exitButton.setOnMouseClicked(e->{
			changeScene(MainScene);
		});
		
		
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
		
		MenuMode.setOnAction(e->{
			if(controller.getColorMode()) {
				MainScene.setFill(white);
				MenuMode.setText("Light Mode: On");
				Menu.setGraphic(new ImageView(menuDark));
				artist.setTextFill(black);
				title.setTextFill(black);
				controller.setColorMode(false);
				exitButton.setFill(new ImagePattern(exitDark));
			} else {
				MainScene.setFill(black);
				MenuMode.setText("Light Mode: Off");
				Menu.setGraphic(new ImageView(menu));
				artist.setTextFill(white);
				title.setTextFill(white);
				controller.setColorMode(true);
				exitButton.setFill(new ImagePattern(exit));
			}
		});

		createMeta(controller.fetchMetadata(controller.getCurrentSong()));	
		playButton = new Circle(windowWidth/2, windowHeight * 5/6 + 2 * textOffset, playButtonRadius);
		playButton.setFill(new ImagePattern(play));
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
		nextButton.setOnMouseClicked( e ->{
			controller.nextSong();
			playButton.setFill(new ImagePattern(pause));
		});
		
		Circle previousButton = new Circle(windowWidth * 0.3125, windowHeight * 5/6 + 2 * textOffset, smallButtonRadius);
		previousButton.setFill(new ImagePattern(prev));
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
			
		});
		
		//Temporary objects to view metadata.
		// Barely visible in black background.
		// Recommended magnifying glass.
		
		
		
	    
	   
	    // Need a default image
	    albumCover = new ImageView();
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
		progressRec.setStyle("-fx-fill: linear-gradient(to right, #006400 0%, rgba(164, 164, 164, 0.8) 0%);");
		root.getStylesheets().add(this.getClass().getResource("/root.css").toExternalForm());
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
	
	// copy and change to treeset and change name too
	private VBox addMusicLables(HashSet<String> hashSet, VBox musicLabled) {
		System.out.println(hashSet.size());
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
				System.out.println(controller.getCurrentSong().getName());
			});
			musicLabled.getChildren().add(temp[i]);
			i += 1;
		}
		musicLabled.setSpacing(10);
		return musicLabled;
	}
	
	private VBox addMusicLables(TreeSet<String> hashSet, VBox musicLabled) {
		System.out.println(hashSet.size());
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
				System.out.println(controller.getCurrentSong().getName());
			});
			musicLabled.getChildren().add(temp[i]);
			i += 1;
		}
		musicLabled.setSpacing(10);
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
