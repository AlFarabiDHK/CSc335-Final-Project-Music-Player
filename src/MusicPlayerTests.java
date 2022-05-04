
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Application;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


class MusicPlayerTests 
{
	private static MusicPlayerModel m;
	private static MusicPlayerController c;
	private static ArrayList<File> allSongs;
	private static Media audio;
	private static MediaPlayer audioPlayer;
	private static File dir;
	private static File[] musicFiles;
	private static int currentSongIndex;
	private static boolean isNext;
	private static HashMap<File, ObservableMap<String, Object>> metadata;
	private static boolean isPlaylistOver;
	private static HashSet<String> favSongs;
	
	public static class AsNonApp extends Application {
	    @Override
	    public void start(Stage primaryStage) throws Exception {
	        // noop
	    }
	}

	@BeforeClass
	public static void initJFX() 
	{
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(AsNonApp.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	}
	
	/*@BeforeAll
	public static void init() throws IOException
	{
		m = new MusicPlayerModel();
		c = new MusicPlayerController(m);
		allSongs = new ArrayList<File>();
		favSongs = new HashSet<String>();
		metadata = new HashMap <File, ObservableMap<String, Object>>();
		dir = new File("Songs");
		musicFiles = dir.listFiles();
		currentSongIndex = 0;
		if(musicFiles != null) {
			for (int i = 0; i < musicFiles.length; i++) {
				allSongs.add(musicFiles[i]);
				System.out.println(musicFiles[i]);
			}
		}
		audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
		audioPlayer = new MediaPlayer(audio);
		metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
		isNext = false;
		isPlaylistOver = false;
	}*/
	
	@Test
	void testPlaySong() throws IOException
	{
		m = new MusicPlayerModel();
		c = new MusicPlayerController(m);
		
		//c.playSong();
		/*audioPlayer.play();
		c.pauseSong();
		audioPlayer.pause();*/
		
	}
	
	/*@Test
	void testGetMax()
	{
		assertEquals(c.getMax(), audio.getDuration());
	}*/
	
}
	