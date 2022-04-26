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
	private Timer progressTimer;
	private TimerTask timerTask;
	private ProgressBar progressBar;
	private boolean inProgress;
	
	public MusicPlayerModel() {
		allSongs = new ArrayList<File>();
		metadata = new TreeMap <File, ObservableMap<String, Object>>();
		dir = new File("Songs");
		musicFiles = dir.listFiles();
		progressBar = new ProgressBar();
		currentSongIndex = 0;
		if(musicFiles != null) {
			for (int i = 0; i < musicFiles.length; i++) {
				allSongs.add(musicFiles[i]);
				System.out.println(musicFiles[i]);
			}
		}
		audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
		audioPlayer = new MediaPlayer(audio);
		inProgress = false;
		audioPlayer.setOnEndOfMedia( () -> {
			nextSong();
		});
		metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
		isNext = false;
		
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
		
		//beginProgress();
		audioPlayer.play();
		inProgress = true;
		
		
	}
	
	public void pauseSong() {
		//cancelProgress();
		audioPlayer.pause();
		inProgress = false;
	}
	
	public void nextSong() {
		//progressBar.setProgress(0);
		if (currentSongIndex < allSongs.size()) {
			currentSongIndex++;
		} else {
			currentSongIndex = 0;
		}
		isNext = true;
		playSong();
	}
	
	public void previousSong() {
		
		//if(inProgress)
			//cancelProgress();
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
	
	public void beginProgress() {
		progressTimer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				inProgress = true;
				double curr = audioPlayer.getCurrentTime().toSeconds();
				double finish = audio.getDuration().toSeconds();
				progressBar.setProgress(curr/finish);
				if(curr/finish == 1) {
					cancelProgress();
				}
			}
		};
		
		setChanged();
		notifyObservers();
		progressTimer.scheduleAtFixedRate(timerTask, 1000, 1000);
		
	}
	
	public void cancelProgress() {
		inProgress = false;
		progressTimer.cancel();
		setChanged();
		notifyObservers();
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	
}
