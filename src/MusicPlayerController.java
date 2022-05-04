import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import com.sun.media.jfxmedia.Media;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class MusicPlayerController {
	
	private MusicPlayerModel model;
	private boolean isPlaying;
	private boolean isDark;
	
	private boolean loop;
	public MusicPlayerController(MusicPlayerModel model) {
		this.model = model;
		isPlaying = false;
		isDark = true;
		loop = false;
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
		model.shuffleSongs();
		isPlaying = true;
	}
	
	
	// Added functionality to add by name to allow
	// addition of songs to favorite list from library
	// and from music player. From music player, it will
	// be accessed by getCurrentSong in model and
	// from library it will be accessed through getSong 
	// in model.
	public boolean addFavSong(String name) throws IOException {
		return model.addFavSong(name);
	}
	
	public void removeFavSong(String name) {
		model.removeFavSong(name);
	}
	
	public boolean isFavsong(File curSong) {
		return model.isFavsong(curSong);
	}
	
	public HashSet<String> getFavSongs() {
		return model.getFavSongs();
	}
	
	public File getCurrentSong() {
		return model.getCurrentSong();
	}
	
	public HashSet<String> getLibrary() {
		return model.getLibrary();
	}
	
	public TreeSet<String> getSortedLibrary() {
		return model.getSortedLibrary();
	}
	
	public boolean getIsPlaying() {
		return isPlaying;
	}
	
	public void nextSong() {
		model.nextSong();
		if (model.getIsPlaylistOver()) {
			pauseSong();
			isPlaying = false;
		} else  {
			isPlaying = true;
		}
	}
	
	public void previousSong() {
		model.previousSong();
		isPlaying = true;
	}
	
	public ObservableMap<String, Object> fetchMetadata(File song) {
		return model.fetchMetadata(song);
	}
	
	public File getSong(String name) {
		return model.getSong(name);
	}
	
	public void setCurrentIndex(int index) {
		model.setCurrentIndex(index);
		isPlaying = true;
	}
	
	public int getSongIndex(String song) {
		return model.getSongIndex(song);
	}
	
	public void setAudioPlayerTime(double d) 
	{
		model.setAudioPlayerTime(d);
	}
	
	public Duration getMax()
	{
		return model.getMax();
	}
	
	public MediaPlayer getAudioPlayer() {
		return model.getAudioPlayer();
	}
	
	public ObservableList<EqualizerBand> getEqualizerBand()
	{
		return model.getEqualizerBand();
	}
	
	public void setColorMode(boolean bool) {
		this.isDark = bool;
	}
	public boolean getColorMode() {
		return isDark;
	}
	
}
