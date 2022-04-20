import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.TreeSet;

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
	
	public void shuffleSongs() {
		Collections.shuffle(allSongs);
	}
	
	public File getCurrentSong() {
		return allSongs.get(currentSongIndex);
	}
	
	public File getSong(String name) {
		for (int i = 0; i < allSongs.size(); i++) {
			if (name.equals(allSongs.get(i).getName())) {
				return allSongs.get(i);
			}
		}
		return null;
	}
	
	public TreeSet<String> getLibrary() {
		TreeSet<String> lib = new TreeSet<String>();
		for (int i =  0; i < allSongs.size(); i++) {
			lib.add(allSongs.get(i).getName());
		}
		return lib;
	}
	
}
