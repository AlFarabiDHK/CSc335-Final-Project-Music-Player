import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;


import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * @author Dhruv Bhatia, Muhtasim Al-Farabi, Shrey Goyal, Suryashree Ray
 * 
 * This is the controller class of our Music Player. It acts as a middle
 * class for the model and the view to communicate with each other. It returns
 * mostly essential methods from the model.
 *
 */
public class MusicPlayerController 
{
	
	private MusicPlayerModel model;
	private boolean isPlaying;
	private boolean isDark;
	private boolean isSorted;
	
	/**
	 * 
	 * Constructor
	 * 
	 * <p>
	 * 
	 * This is the constructor for the controller. It initializes the model, isPlaying and
	 * isDark
<<<<<<< HEAD
=======
	 * @param model the model of the Music Player
>>>>>>> 534bff00e3536b2753909552c518ba6846d43874
	 * 
	 */
	public MusicPlayerController(MusicPlayerModel model) 
	{
		this.model = model;
		isPlaying = false;
		isDark = true;

		isSorted = false;

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
		model.playSong();
		isPlaying = true;
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
		model.pauseSong();
		isPlaying = false;
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
		model.shuffleSongs();
		isPlaying = true;
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
<<<<<<< HEAD
=======
	 * @throws IOException happens when the user input in wrong
>>>>>>> 534bff00e3536b2753909552c518ba6846d43874
	 *
	 */
	public boolean addFavSong(String name) throws IOException 
	{
		return model.addFavSong(name);
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
		model.removeFavSong(name);
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
		return model.isFavsong(curSong);
	}
	
	/**
	 * Gets the total set of favorite song
	 * 
	 * <p>
	 * 
	 * This function helps to see if a current song is
	 * a favorite or not
	 * 
<<<<<<< HEAD
	 * @param curSong is a file that is the current song playing.
=======
>>>>>>> 534bff00e3536b2753909552c518ba6846d43874
	 * 
	 * @return favorite songs which is a hashset.
	 *
	 */
	public HashSet<String> getFavSongs() 
	{
		return model.getFavSongs();
	}
	
	/**
	 * Gets the current song
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
		return model.getCurrentSong();
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
		return model.getLibrary();
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
		return model.getSortedLibrary();
	}
	
	/**
	 * Gets if the state is now playing
	 * 
	 * <p>
	 * 
	 * This function helps to get the state of the
	 * audioplayer, that is, if it's playing or not
	 * 
	 * @return true if the audioplayer is playing false otherwises
	 *
	 */
	public boolean getIsPlaying() 
	{
		return isPlaying;
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
		model.nextSong();
		if (model.getIsPlaylistOver()) 
		{
			pauseSong();
			isPlaying = false;
		} 
		
		else  
		{
			isPlaying = true;
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
		model.previousSong();
		isPlaying = true;
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
		return model.fetchMetadata(song);
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
		return model.getSong(name);
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
		model.setCurrentIndex(index);
		isPlaying = true;
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
		return model.getSongIndex(song);
	}
	
	/**
	 * Sets the AudioPlayertime
	 * 
	 * <p>
	 * 
	 * This function helps to set the audioplayer time
	 * to the duration that is sent by the slider
	 * 
<<<<<<< HEAD
	 * @param duration is a double with the duration to set out time
=======
	 * @param d is a double with the duration to set out time
>>>>>>> 534bff00e3536b2753909552c518ba6846d43874
	 *
	 */
	public void setAudioPlayerTime(double d) 
	{
		model.setAudioPlayerTime(d);
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
		return model.getMax();
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
		return model.getAudioPlayer();
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
		return model.getEqualizerBand();
	}
	
	/**
	 * Sets the isDark to the boolean taken
	 * 
	 * <p>
	 * 
	 * This function helps to set the isDark according
	 * to the boolean value taken in
	 * 
	 * @param bool is a boolean that takes in true when our 
	 *        view is dark and false otherwise
	 *
	 */
	public void setColorMode(boolean bool) 
	{
		this.isDark = bool;
	}
	
	/**
	 * Gets if the mode is dark or not
	 * 
	 * <p>
	 * 
	 * This function helps to get if the mode is dark or not
	 * 
	 * @return isDark is a boolean that is true if the mode
	 *         is dark and false otherwise
	 */
	public boolean getColorMode() 
	{
		return isDark;
	}
	
	/**
	 * Gets if library is sorted
	 * 
	 * <p>
	 * 
	 * This function helps to get the boolean isSorted
	 * 
	 * @return the boolean isSorted
	 */
	
	public boolean getIsSorted() {
		return isSorted;
	}
	
	/**
	 * Sets the isSorted field
	 * 
	 * <p>
	 * 
	 * This method sets the isSorted field
	 * @param b a boolean which indicates the sort status of the library
	 */
	public void setIsSorted(boolean b) {
		isSorted = b;
	}
	
	
}
