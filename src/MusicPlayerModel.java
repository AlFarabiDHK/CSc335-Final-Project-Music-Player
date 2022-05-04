import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.ProgressBar;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayerModel extends Observable{

	private ArrayList<File> allSongs;
	private Media audio;
	private MediaPlayer audioPlayer;
	private File dir;
	private File[] musicFiles;
	private int currentSongIndex;
	private boolean isNext;
	private HashMap<File, ObservableMap<String, Object>> metadata;
	private boolean isPlaylistOver;
	private HashSet<String> favSongs;
	
	public MusicPlayerModel() throws IOException 
	{
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
		audioPlayer.setOnEndOfMedia( () -> {
			nextSong();
		});
		metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
		isNext = false;
		isPlaylistOver = false;
		
		try {
			File favFile = new File("favorites.txt");
			if (!favFile.exists()) {
				FileWriter createFile = new FileWriter("favorites.txt");
				createFile.close();
			} 
			Scanner favReader = new Scanner(favFile);
			while (favReader.hasNextLine()) {
		        favSongs.add(favReader.nextLine());
		      }
			favReader.close();
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
		try {
			FileWriter writeGit = new FileWriter(".gitignore");
		      File checkGit = new File(".gitignore");
		      Scanner myReader = new Scanner(checkGit);
		      boolean checkFavs = false;
		      while (myReader.hasNextLine()) {
		    	  if (myReader.nextLine().equals("favorites.txt")) {
		    		  checkFavs = true;
		    		  break;
		    	  }
		      }
		      myReader.close();
		      if (!checkFavs) {
		    	  writeGit.write("favorites.txt");
		    	  writeGit.close();
		      }
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
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
		if (!isPlaylistOver) {
			audioPlayer.play();
		} else {
			isPlaylistOver = false;
		}
		
	}
	
	public void pauseSong() {
		audioPlayer.pause();
	}
	
	public void nextSong() 
	{
		if (currentSongIndex < allSongs.size()-1) 
		{
			currentSongIndex++;
			isNext = true;
			playSong();
		} 
		
		else 
		{
			currentSongIndex = 0;
			isNext = true;
			isPlaylistOver = true;
			playSong();
		}
	}
	
	public void previousSong() {
		if (currentSongIndex != 0) {
			currentSongIndex--;
		} else {
			currentSongIndex = musicFiles.length - 1;
		}
		isNext = true;
		playSong();
	}
	
	public void shuffleSongs() {
		Collections.shuffle(allSongs);
		currentSongIndex = 0;
		isNext = true;
		playSong();
	}
	
	public boolean addFavSong(String name) throws IOException {
		if (!favSongs.contains(name)) {
			favSongs.add(name);
			try {
			      FileWriter favWriter = new FileWriter("favorites.txt", true);
			      favWriter.write(name + "\n");
			      favWriter.close();
			      System.out.println("Successfully wrote to the file.");
			    } catch (IOException e) {
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			    }
			System.out.println(favSongs.toString());
			return true;
		} else {
			removeFavSong(name);
			removeFavSongsFromFile(name);
			System.out.println(favSongs.toString());
			return false;
		}
	}
	
	public void removeFavSongsFromFile(String name) throws IOException {
		   File inputFile = new File("favorites.txt");
		   File tempFile = new File("TempFav.txt");

		   BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		   BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		   String currentLine;

		   while((currentLine = reader.readLine()) != null) {
		   String trimmedLine = currentLine.trim();
		   if(trimmedLine.equals(name)) continue;
		   writer.write(currentLine + "\n");
		 }
		   reader.close();
		   writer.close();
		   inputFile.delete();
		   tempFile.renameTo(inputFile);
		 }
	
	public void removeFavSong(String name) {
		favSongs.remove(name);
	}
	
	public boolean isFavsong(File curSong) {
		return this.favSongs.contains(curSong.getName());
	}
	
	public HashSet<String> getFavSongs() {
		return this.favSongs;
	}
	
	public void setCurrentIndex(int index) {
		this.currentSongIndex = index;
		isNext = true;
		playSong();
		
	}
	
	public File getCurrentSong() {
		return allSongs.get(currentSongIndex);
	}
	
	public boolean getIsPlaylistOver() {
		return isPlaylistOver;
	}
	
	public File getSong(String name) {
		for (int i = 0; i < allSongs.size(); i++) {
			if (name.equals(allSongs.get(i).getName())) {
				return allSongs.get(i);
			}
		}
		return null;
	}

	public HashSet<String> getLibrary() {
		HashSet<String> lib = new HashSet<String>();
		for (int i =  0; i < allSongs.size(); i++) {
			lib.add(allSongs.get(i).getName());
		}
		return lib;
	}
	
	public TreeSet<String> getSortedLibrary() {
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

	public int getSongIndex(String song) {
		int i = 0;
		while (i < allSongs.size()) {
			if (allSongs.get(i).getName().equals(song)) {
				break;
			}
		i++;
		}
		System.out.println("Got till here");
		return i;
	}
	
	public ObservableList<EqualizerBand> getEqualizerBand()
	{
		return audioPlayer.getAudioEqualizer().getBands();
	}
	
	public void setAudioPlayerTime(double duration) 
	{
		audioPlayer.seek(Duration.seconds(duration));
	}
	
	public Duration getMax()
	{
		return audio.getDuration();
	}
	
	
	
	
}
