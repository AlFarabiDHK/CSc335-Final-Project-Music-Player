import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import com.sun.media.jfxmedia.Media;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

public class MusicPlayerController {
	
	private MusicPlayerModel model;
	private boolean isPlaying;
	private ArrayList<String> favSongs;
	
	public MusicPlayerController(MusicPlayerModel model) {
		this.model = model;
		isPlaying = false;
		favSongs = new ArrayList<String>();
	}
	
	public void playSong() {
		model.playSong();
//		printMetaData();
		isPlaying = true;
	}
	
	public void pauseSong() {
		model.pauseSong();
		isPlaying = false;
	}
	
	// Shuffle songs and plays the newest one.
	//
	// TODO add functionality to play new song
	// after shuffling.
	public void shuffleSongs() {
		model.pauseSong();
		model.setCurrentIndex(0);
		model.shuffleSongs();
	}
	
	
	// Added functionality to add by name to allow
	// addition of songs to favorite list from library
	// and from music player. From music player, it will
	// be accessed by getCurrentSong in model and
	// from library it will be accessed through getSong 
	// in model.
	public boolean addFavSong(String name) {
		if (!favSongs.contains(name)) {
			favSongs.add(name);
			System.out.println(favSongs.toString());
			return true;
		} else {
			removeFavSong(name);
			System.out.println(favSongs.toString());
			return false;
		}
	}
	
	public void removeFavSong(String name) {
		favSongs.remove(name);
	}
	
	public ArrayList<String> getFavSongs() {
		return this.favSongs;
	}
	
	public File getCurrentSong() {
		return model.getCurrentSong();
	}
	
	public TreeSet<String> getLibrary() {
		return model.getLibrary();
	}
	
	public boolean getIsPlaying() {
		return isPlaying;
	}
	
	public ObservableMap<String, Object> fetchMetadata(File song)
	{
		return model.fetchMetadata(song);
	}
}
