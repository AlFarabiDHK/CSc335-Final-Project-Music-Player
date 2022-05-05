package Utilities;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import Controller.MusicPlayerController;
import Model.MusicPlayerModel;
import de.saxsys.javafx.test.JfxRunner;
import de.saxsys.javafx.test.TestInJfxThread;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * @author Dhruv Bhatia, Muhtasim Al-Farabi, Shrey Goyal, Suryashree Ray
 * 
 * Test Cases for MusicPlayer. 
 * 
 * These testcases won't run as we have JavaFX 
 * components in our model and controller and it won't let us instantiate
 * a model or controller. We tried making it into a Maven project and
 * adding dependency on JavaFX but to be very honest nothing 
 * seems to work at all. We tried our best.
 *
 */

@RunWith(JfxRunner.class)
class MusicPlayerTests 
{
	private static MusicPlayerModel m;
	private static MusicPlayerController c;
	private static ArrayList<File> allSongs;
	private static Media audio;
	private static MediaPlayer audioPlayer;
	private static File dir;
	private static File[] musicFiles;
	private static int currentSongIndex;
	private static boolean isNext;
	private static HashMap<File, ObservableMap<String, Object>> metadata;
	private static boolean isPlaylistOver;
	private static HashSet<String> favSongs;

	
	
	@BeforeAll
	public static void init() throws IOException
	{
		m = new MusicPlayerModel();
		c = new MusicPlayerController(m);
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
		metadata.put(allSongs.get(currentSongIndex), audio.getMetadata());
		isNext = false;
		isPlaylistOver = false;
	}
	
	@Test
	@TestInJfxThread
	void testPlaySong() throws IOException
	{
		m = new MusicPlayerModel();
		c = new MusicPlayerController(m);
		
		c.playSong();
		audioPlayer.play();
		c.pauseSong();
		audioPlayer.pause();
		
		assertEquals(audio, m.getAudio());
		
	}
	
	@Test
	@TestInJfxThread
	void testGetMax()
	{
		assertEquals(c.getMax(), audio.getDuration());
	}
	
	@Test
	@TestInJfxThread
	void testSetAudioTime()
	{
		c.setAudioPlayerTime(0.9);
		assertEquals(c.getAudioPlayer().getCurrentTime().toSeconds(), 0.9);
	}
	
	@Test
	@TestInJfxThread
	void testGetEQBand()
	{
		assertEquals(c.getEqualizerBand(), audioPlayer.getAudioEqualizer().getBands());
	}
	
	@Test
	@TestInJfxThread
	void testGetSongIndex()
	{
		assertEquals(c.getSongIndex("Beautiful Now"), 0);
	}
	
	@Test
	@TestInJfxThread
	void testGetAudioPlayer()
	{
		assertEquals(c.getAudioPlayer(), audioPlayer);
	}
	
	@Test
	@TestInJfxThread
	void testGetAudio()
	{
		assertEquals(m.getAudio(), audio);
	}
	
	@Test
	@TestInJfxThread
	void testfetchMetaData()
	{
		assertEquals(c.fetchMetadata(allSongs.get(currentSongIndex)), audio.getMetadata());
	}
	
	@Test
	@TestInJfxThread
	void testGetPlaylistOver()
	{
		assertEquals(m.getIsPlaylistOver(), false);
	}
	
	@Test
	@TestInJfxThread
	void testGetCurrentSong()
	{
		assertEquals(c.getCurrentSong(), allSongs.get(currentSongIndex));
	}
	
}
	