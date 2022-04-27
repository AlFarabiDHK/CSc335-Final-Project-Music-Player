import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import com.sun.media.jfxmedia.Media;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.ProgressBar;

public class MusicPlayerController {
	
	private MusicPlayerModel model;
	private boolean isPlaying;
	private ArrayList<String> favSongs;
	private Timer progressTimer;
	private TimerTask timerTask;
	private ProgressBar progressBar;
	
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
		model.shuffleSongs();
		isPlaying = true;
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
	
	public boolean isFavsong(File curSong) {
		return this.favSongs.contains(curSong.getName());
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
	
	public void nextSong() {
		model.nextSong();
		isPlaying = true;
	}
	
	public void previousSong() {
		model.previousSong();
		isPlaying = true;
	}
	
	public ObservableMap<String, Object> fetchMetadata(File song)
	{
		return model.fetchMetadata(song);
	}
	public void beginProgress() {
		progressTimer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				isPlaying = true;
				double curr = model.getAudioPlayer().getCurrentTime().toSeconds();
				double finish = model.getAudio().getDuration().toSeconds();
				progressBar.setProgress(curr/finish);
				if(curr/finish == 1) {
					cancelProgress();
				}
			}
		};
		progressTimer.scheduleAtFixedRate(timerTask, 1000, 1000);
		
	}
	
	public void cancelProgress() {
		isPlaying  = false;
		progressTimer.cancel();
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
}
