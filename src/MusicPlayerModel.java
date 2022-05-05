import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.Scanner;
import java.util.TreeSet;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * @author Dhruv Bhatia, Muhtasim Al-Farabi, Shrey Goyal, Suryashree Ray
 * 
 * This class contains the model for the music player. It contains all the necessary information
 * about the musics and the media player and also handles the state of music player. 
 * It uses some Javafx objects like media and media player that we use to play the music. 
 * It can play and pause songs using the playSong() and pauseSongs() method, and can simulate a music player. 
 *
 */

public class MusicPlayerModel extends Observable
{

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
	
	/**
	 * 
	 * Constructor
	 * 
	 * <p>
	 * 
	 * This is the constructor for the model. It initializes the model, the audio files arraylist,
	 * initial audio, audioplayer, the first metadata and also creates the favorites.txt
	 * if it doesn't exist already.
	 * @throws IOException happens when the user input in wrong
	 * 
	 */
	
	public MusicPlayerModel() throws IOException 
	{
		allSongs = new ArrayList<File>();
		favSongs = new HashSet<String>();
		metadata = new HashMap <File, ObservableMap<String, Object>>();
		dir = new File("Songs");
		musicFiles = dir.listFiles();
		currentSongIndex = 0;
		
		if(musicFiles != null) 
		{
			for (int i = 0; i < musicFiles.length; i++) 
			{
				allSongs.add(musicFiles[i]);
				System.out.println(musicFiles[i]);
			}
		}
		
		audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
		audioPlayer = new MediaPlayer(audio);
		audioPlayer.setOnEndOfMedia( () -> 
		{
			nextSong();
		});
		
		metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
		isNext = false;
		isPlaylistOver = false;
		
		try 
		{
			File favFile = new File("favorites.txt");
			
			if (!favFile.exists()) 
			{
				FileWriter createFile = new FileWriter("favorites.txt");
				createFile.close();
			} 
			
			Scanner favReader = new Scanner(favFile);
			
			while (favReader.hasNextLine()) 
			{
		        favSongs.add(favReader.nextLine());
		    }
			
			favReader.close();
			
		 } 
		
		catch (IOException e) 
		{
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
		
		try 
		{
			FileWriter writeGit = new FileWriter(".gitignore");
		      File checkGit = new File(".gitignore");
		      Scanner myReader = new Scanner(checkGit);
		      boolean checkFavs = false;
		      
		      while (myReader.hasNextLine()) 
		      {
		    	  if (myReader.nextLine().equals("favorites.txt")) 
		    	  {
		    		  checkFavs = true;
		    		  break;
		    	  }
		      }
		      
		      myReader.close();
		      if (!checkFavs) 
		      {
		    	  writeGit.write("favorites.txt");
		    	  writeGit.close();
		      }
		}
		
		catch (FileNotFoundException e) 
		{
		    System.out.println("An error occurred.");
		    e.printStackTrace();
		}
		
	}
	
	/**
	 * Plays the song
	 * <p>
	 * 
	 * This method plays the current song and also makes
	 * sure to get the new audio if we are going to another song.
	 * It notifies the observers of the change.
	 *
	 */
	public void playSong() 
	{
		System.out.println(isNext);
		
		if (isNext) 
		{
			audioPlayer.stop();
			audioPlayer.dispose();
			this.audio = new Media(allSongs.get(currentSongIndex).toURI().toString());
			audioPlayer = new MediaPlayer(audio);
			
			audioPlayer.setOnEndOfMedia( () -> 
			{
				nextSong();
			});
			
			if(!metadata.containsKey(allSongs.get(currentSongIndex))) 
			{
				metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
			}
			
			isNext = false;
			setChanged();
			notifyObservers();
		}
		
		if (!isPlaylistOver) 
		{
			audioPlayer.play();
		} 
		
		else 
		{
			isPlaylistOver = false;
		}
		
	}
	
	/**
	 * Pauses the song.
	 * 
	 * <p>
	 * 
	 * This method pauses the current song that we are playing.
	 *
	 */
	public void pauseSong() 
	{
		audioPlayer.pause();
	}
	
	/**
	 * Goes to the next song in the library
	 * 
	 * <p>
	 * 
	 * This method helps us to go to the next song in the library
	 * and also account for if we have played the total playlist 
	 * or not.
	 *
	 */
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
	
	/**
	 * Goes to the previous song in the library
	 * 
	 * <p>
	 * 
	 * This method helps us to go to the previous song in the library
	 * and also account for if we have played the first song or not.
	 *
	 */
	public void previousSong() 
	{
		if (currentSongIndex != 0) 
		{
			currentSongIndex--;
		} 
		
		else 
		{
			currentSongIndex = musicFiles.length - 1;
		}
		
		isNext = true;
		playSong();
	}
	
	/**
	 * Shuffles the song library
	 * 
	 * <p>
	 * 
	 * This method shuffles all the song in the library
	 * and will have a new library
	 *
	 */
	public void shuffleSongs() 
	{
		Collections.shuffle(allSongs);
		currentSongIndex = 0;
		isNext = true;
		playSong();
	}
	
	/**
	 * Adds song to our favorite file
	 * 
	 * <p>
	 * 
	 * This function helps to add favorite songs into 
	 * our file and uses the Filewiter to do that.
	 * 
	 * @param name is a string with the name of the song to be added to our favorites
	 * 
	 * @return true if the favorite is added and false if the favorite is not added.
	 * @throws IOException happens when the user input in wrong
	 *
	 */
	public boolean addFavSong(String name) throws IOException 
	{
		if (!favSongs.contains(name)) 
		{
			favSongs.add(name);
			try 
			{
			      FileWriter favWriter = new FileWriter("favorites.txt", true);
			      favWriter.write(name + "\n");
			      favWriter.close();
			      System.out.println("Successfully wrote to the file.");
			} 
			
			catch (IOException e) 
			{
			      System.out.println("An error occurred.");
			      e.printStackTrace();
			}
			System.out.println(favSongs.toString());
			return true;
		}
		
		else 
		{
			removeFavSong(name);
			removeFavSongsFromFile(name);
			System.out.println(favSongs.toString());
			return false;
		}
	}
	
	/**
	 * Removes song from the favorite file
	 * 
	 * <p>
	 * 
	 * This function helps to remove song from our
	 * favorite file and this is done by making a
	 * temporary file.
	 * 
	 * @param name is a string with the name of the song to be removed to our favorites
	 * @throws IOException happens when the user input in wrong
	 *
	 */
	public void removeFavSongsFromFile(String name) throws IOException 
	{
		   File inputFile = new File("favorites.txt");
		   File tempFile = new File("TempFav.txt");

		   BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		   BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		   String currentLine;

		   while((currentLine = reader.readLine()) != null) 
		   {
			   String trimmedLine = currentLine.trim();
			   if(trimmedLine.equals(name)) continue;
			   writer.write(currentLine + "\n");
		   }
		   
		   reader.close();
		   writer.close();
		   inputFile.delete();
		   tempFile.renameTo(inputFile);
	}
	
	/**
	 * Removes song from the favorite song set
	 * 
	 * <p>
	 * 
	 * This function helps to remove song from our
	 * favorite song set.
	 * 
	 * @param name is a string with the name of the song to be removed to our favorites
	 *
	 */
	public void removeFavSong(String name) 
	{
		favSongs.remove(name);
	}
	
	/**
	 * See if the current song is a favorite song
	 * 
	 * <p>
	 * 
	 * This function helps to see if a current song is
	 * a favorite or not
	 * 
	 * @param curSong is a file that is the current song playing.
	 * 
	 * @return true if the current song is in the favorite song else false
	 *
	 */
	public boolean isFavsong(File curSong) 
	{
		return this.favSongs.contains(curSong.getName());
	}
	
	/**
	 * Gets the total set of favorite song
	 * 
	 * <p>
	 * 
	 * This function helps to see if a current song is
	 * a favorite or not
	 * 
	 * 
	 * @return favorite songs which is a hashset.
	 *
	 */
	public HashSet<String> getFavSongs() 
	{
		return this.favSongs;
	}
	
	/**
	 * Sets the current song index
	 * 
	 * <p>
	 * 
	 * It helps to set the current index of the song
	 * to the index we want
	 * 
	 * @param index is an integer that we want to set the current index as
	 *
	 */
	public void setCurrentIndex(int index) 
	{
		this.currentSongIndex = index;
		isNext = true;
		playSong();
		
	}
	
	/**
	 * Gets the current son
	 * 
	 * <p>
	 * 
	 * It helps to get the current song that we are
	 * playing or supposed to play.
	 * 
	 * @return File name of the current song.
	 *
	 */
	public File getCurrentSong() 
	{
		return allSongs.get(currentSongIndex);
	}
	
	/**
	 * Gets if the play list is over or not.
	 * 
	 * <p>
	 * 
	 * It helps to get if our play list is over
	 * or not.
	 *
	 *@return true if the playlist is over and false if not
	 */
	public boolean getIsPlaylistOver() 
	{
		return isPlaylistOver;
	}
	
	/**
	 * Gets the song by its name.
	 * 
	 * <p>
	 * 
	 * This function helps to get the song 
	 * whose name is passed in.
	 * 
	 * @param name is a string with the name of the song to get.
	 * 
	 * @return File whose name is given
	 *
	 */
	public File getSong(String name) 
	{
		for (int i = 0; i < allSongs.size(); i++) 
		{
			if (name.equals(allSongs.get(i).getName())) 
			{
				return allSongs.get(i);
			}
		}
		return null;
	}

	/**
	 * Gets the library of all songs
	 * 
	 * <p>
	 * 
	 * This function helps to get the library 
	 * containing all the songs on the local folder
	 * 
	 * @return lib is a hashset with the library of songs
	 *
	 */
	public HashSet<String> getLibrary() 
	{
		HashSet<String> lib = new HashSet<String>();
		for (int i =  0; i < allSongs.size(); i++) 
		{
			lib.add(allSongs.get(i).getName());
		}
		return lib;
	}
	
	/**
	 * Gets the sorted library of all songs
	 * 
	 * <p>
	 * 
	 * This function helps to get the sorted library 
	 * containing all the songs on the local folder
	 * 
	 * @return lib is a treeset with the library of songs
	 *
	 */
	public TreeSet<String> getSortedLibrary() 
	{
		TreeSet<String> lib = new TreeSet<String>();
		for (int i =  0; i < allSongs.size(); i++) 
		{
			lib.add(allSongs.get(i).getName());
		}
		return lib;
	}
	
	/**
	 * Gets the metadata of the song
	 * 
	 * <p>
	 * 
	 * This function helps to get metadata of the
	 * song file sent in.
	 * 
	 * @param song is a file with the song.
	 * 
	 * @return metadata is an observable map of all the metadata of the song
	 *
	 */
	public ObservableMap<String, Object> fetchMetadata(File song)
	{
		return metadata.get(song);
		
	}
	
	/**
	 * Gets the current audio
	 * 
	 * <p>
	 * 
	 * This function helps to get the current audio.
	 * 
	 * @return audio which is a media of the current audio.
	 *
	 */
	public Media getAudio() 
	{
		return audio;
	}
	
	/**
	 * Gets the current audioplayer
	 * 
	 * <p>
	 * 
	 * This function helps to get the current audioplayer.
	 * 
	 * @return audioPlayer which is a mediaplayer of the current audio.
	 *
	 */
	public MediaPlayer getAudioPlayer() 
	{
		return audioPlayer;
	}

	/**
	 * Gets the song index of the song we sent in
	 * 
	 * <p>
	 * 
	 * This function helps to get the index of the song.
	 * 
	 * @param song is a String with the name of the song
	 * 
	 * @return i is the an integer of the index we are trying to find.
	 *
	 */
	public int getSongIndex(String song) 
	{
		int i = 0;
		while (i < allSongs.size()) 
		{
			if (allSongs.get(i).getName().equals(song)) 
			{
				break;
			}
		i++;
		}
		
		System.out.println("Got till here");
		return i;
	}
	
	/**
	 * Gets the equalizer bands of the current song
	 * 
	 * <p>
	 * 
	 * This function helps to get equalizer bands of the
	 * current song
	 * 
	 * @return equalizerBands that is an Observable list containing
	 *         the equalizer bands
	 *
	 */
	public ObservableList<EqualizerBand> getEqualizerBand()
	{
		return audioPlayer.getAudioEqualizer().getBands();
	}
	
	/**
	 * Sets the AudioPlayertime
	 * 
	 * <p>
	 * 
	 * This function helps to set the audioplayer time
	 * to the duration that is sent by the slider
	 * 
	 * @param duration is a double with the duration to set out time
	 *
	 */
	public void setAudioPlayerTime(double duration) 
	{
		audioPlayer.seek(Duration.seconds(duration));
	}
	
	/**
	 * Gets the duration of the audio
	 * 
	 * <p>
	 * 
	 * This function helps to get the maximum duration of
	 * the current audio
	 * 
	 * @return duration which is the maximum duration of the current audio.
	 *
	 */
	public Duration getMax()
	{
		return audio.getDuration();
	}
	
	
	
	
}
