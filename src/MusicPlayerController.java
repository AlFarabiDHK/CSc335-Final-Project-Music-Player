
public class MusicPlayerController {
	
	private MusicPlayerModel model;
	private boolean isPlaying;
	public MusicPlayerController(MusicPlayerModel model) {
		this.model = model;
		isPlaying = false;
	}
	
	public void playSong() {
		model.playSong();
		isPlaying = true;
	}
	
	public void pauseSong() {
		model.pauseSong();
		isPlaying = false;
	}
	
	public boolean getIsPlaying() {
		return isPlaying;
	}
}
