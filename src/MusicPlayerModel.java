import java.io.File;
import java.util.ArrayList;
import java.util.Observable;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayerModel extends Observable{

	private ArrayList<File> allSongs;
	private Media audio;
	private MediaPlayer audioPlayer;
	private File dir;
	private File[] musicFiles;
	private int currentSongIndex;
	
	public MusicPlayerModel() {
		allSongs = new ArrayList<File>();
		
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
	}
	
	public void playSong() {
		audioPlayer.play();
		
	}
	
	public void pauseSong() {
		audioPlayer.pause();
	}
	
}
