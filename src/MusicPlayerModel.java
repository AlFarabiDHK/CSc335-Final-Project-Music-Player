import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.ProgressBar;
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
	private TreeMap <File, ObservableMap<String, Object>> metadata;
	private int loops;
	
	public MusicPlayerModel() 
	{
		allSongs = new ArrayList<File>();
		metadata = new TreeMap <File, ObservableMap<String, Object>>();
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
		audioPlayer.setOnEndOfMedia( () -> {
			nextSong();
		});
		metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
		isNext = false;
		loops = 0;
		
	}
	
	public void playSong() {
		System.out.println(isNext);
		if (isNext) 
		{
			audioPlayer.stop();
			audioPlayer.dispose();
			this.audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
			audioPlayer = new MediaPlayer(audio);
			audioPlayer.setOnEndOfMedia( () -> {
				nextSong();
			});
			if(!metadata.containsKey(allSongs.get(currentSongIndex))) {
				metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
			}
			isNext = false;
			setChanged();
			notifyObservers();
		}
		if (loops != 0)
			audioPlayer.pause();
		else
			audioPlayer.play();
		
		
	}
	
	public void pauseSong() {
		audioPlayer.pause();
	}
	
	public void nextSong() 
	{
		if (currentSongIndex < allSongs.size()-1) 
		{
			currentSongIndex++;
		} 
		
		else 
		{
			currentSongIndex = 0;
			loops++;
		}
		isNext = true;
		playSong();
	}
	
	public void previousSong() {
		if (currentSongIndex != 0) {
			currentSongIndex--;
			isNext = true;
			playSong();
		}
	}
	
	public void shuffleSongs() {
		Collections.shuffle(allSongs);
		currentSongIndex = 0;
		isNext = true;
		playSong();
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

	public TreeSet<String> getLibrary() {
		TreeSet<String> lib = new TreeSet<String>();
		for (int i =  0; i < allSongs.size(); i++) {
			lib.add(allSongs.get(i).getName());
		}
		return lib;
	}
	
	public ObservableMap<String, Object> fetchMetadata(File song)
	{
		return metadata.get(song);
		
	}
	
	public Media getAudio() {
		return audio;
	}
	
	public MediaPlayer getAudioPlayer() {
		return audioPlayer;
	}
	
	public int getLoops()
	{
		return this.loops;
	}
	
	
}
