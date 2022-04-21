import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Observable;
import java.util.Set;
import java.util.TreeSet;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayerModel extends Observable{

	private ArrayList<File> allSongs;
	private Media audio;
	private MediaPlayer audioPlayer;
	private File dir;
	private File[] musicFiles;
	private int currentSongIndex;
	private boolean isNext;
	private Set<String> MetaKey;
	private Collection<Object> MetaValue;
	private ObservableMap<String, Object> MetaData;
	
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
		isNext = false;
		MetaData = audio.getMetadata();
		MetaKey = audio.getMetadata().keySet();
		MetaValue = audio.getMetadata().values(); 
		
	}
	
	
	
	public void playSong() {
		audioPlayer.setOnEndOfMedia( () -> {
			nextSong();
		});
		if (isNext) {
			audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
			audioPlayer = new MediaPlayer(audio);
//			getMetaData(allSongs.get(currentSongIndex));
			isNext = false;
		}
		
		audioPlayer.play();
		System.out.println("I play this bitch like I play ball");
		
		
	}
	
	public void pauseSong() {
		audioPlayer.pause();
	}
	
	public void nextSong() {
		if (currentSongIndex < allSongs.size()) {
			currentSongIndex++;
			isNext = true;
		}
		playSong();
	}
	
	public void shuffleSongs() {
		Collections.shuffle(allSongs);
		isNext = true;
	}
	
	public void setCurrentIndex(int index) {
		this.currentSongIndex = index;
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
	
	public ObservableMap<String, Object> getMetaData() {
		 return MetaData;
	}

	public Set<String> getMetaKey() {
		return MetaKey;
	}
	
	public Collection<Object> getMetaValue() {
		return MetaValue;
	}

	public TreeSet<String> getLibrary() {
		TreeSet<String> lib = new TreeSet<String>();
		for (int i =  0; i < allSongs.size(); i++) {
			lib.add(allSongs.get(i).getName());
		}
		return lib;
	}
	
}
