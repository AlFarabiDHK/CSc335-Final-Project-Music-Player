import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.TreeMap;
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
	private TreeMap <File, ObservableMap<String, Object>> metadata;
	
	public MusicPlayerModel() {
		allSongs = new ArrayList<File>();
		metadata = new TreeMap <File, ObservableMap<String, Object>>();
		dir = new File("Songs");
		musicFiles = dir.listFiles();
		currentSongIndex = 0;
		if(musicFiles != null) {
			for (int i = 0; i < musicFiles.length; i++) {
				allSongs.add(musicFiles[i]);
				//Media song = new Media(allSongs.get(i).toURI().toString());
				//metadata.put(musicFiles[i], song.getMetadata());
				System.out.println(musicFiles[i]);
			}
		}
		audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
		metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
		audioPlayer = new MediaPlayer(audio);
		isNext = false;
		
	}
	
	
	
	public void playSong() {
		audioPlayer.setOnEndOfMedia( () -> {
			nextSong();
		});
		System.out.println(isNext);
		if (isNext) 
		{
			this.audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
			if(!metadata.containsKey(allSongs.get(currentSongIndex)))
				metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
			audioPlayer = new MediaPlayer(audio);
			isNext = false;
			setChanged();
			notifyObservers();
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
	
	public ObservableMap<String, Object> fetchMetadata(File song)
	{
		/*//ObservableMap<String, Object> metadata = this.audio.getMetadata();
		HashMap<String, Object> metadata = new HashMap<String, Object>();
		try 
	    {
			System.out.println("here");
	      audio.getMetadata().addListener(new MapChangeListener<String, Object>() 
	      {
	        @Override
	        public void onChanged(Change<? extends String, ? extends Object> ch) 
	        {
	          if (ch.wasAdded()) 
	          {
	        	  System.out.println("here");
	        	  metadata.put(ch.getKey(), ch.getValueAdded());
	          }
	        }
	      });

	    } catch (RuntimeException re) {
	      // Handle construction errors
	      System.out.println("Caught Exception: " + re.getMessage());
	    }
		System.out.println(metadata.keySet());
		/*for(String key: metadata.keySet())
		{
			System.out.println(key + ":"+ metadata.get(key));
		}*/
		return metadata.get(song);
		
	}
	
}
