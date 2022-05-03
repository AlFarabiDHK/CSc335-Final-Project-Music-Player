import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.MediaPlayer;

public class EqualizerScene extends AbstractView
{
	private static GridPane grid;

	public EqualizerScene(MusicPlayerController controller) 
	{
		super(controller);
		createEqualizer();
	}

	@Override
	protected Node initView() 
	{
		final GridPane grid = new GridPane();
		grid.setPadding(new Insets(10));
		grid.setHgap(20);
		
		RowConstraints middle = new RowConstraints();
		RowConstraints outside = new RowConstraints();
		outside.setVgrow(Priority.ALWAYS);
		
		grid.getRowConstraints().addAll(outside, middle, outside);
		return grid;
	}
	
	private void createEqualizer() 
	{
		grid = (GridPane) getViewNode();
		final MediaPlayer audioPlayer = controller.getAudioPlayer();
		
		createBands(grid);
	}

	private void createBands(GridPane grid) 
	{
		final ObservableList<EqualizerBand> bands = controller.getEqualizerBand();
		
		//bands.clear();
		
		double minimum = EqualizerBand.MIN_GAIN;
		double maximum = EqualizerBand.MAX_GAIN;
		
		for (int i = 0; i < bands.size(); i++)
		{
			EqualizerBand band = bands.get(i);
			
			Slider slide = createSlider(band, minimum, maximum);
			final Label label = new Label(frequencyFormat(band.getCenterFrequency()));
			label.getStyleClass().addAll("mediaText", "eqLabel");
			
			GridPane.setHalignment(label, HPos.CENTER);
			GridPane.setHalignment(slide, HPos.CENTER);
			GridPane.setHgrow(slide, Priority.ALWAYS);
			
			grid.add(label, i, 1);
			grid.add(slide, i, 2);
		}
	}

	private String frequencyFormat(double centerFrequency) {
		if (centerFrequency < 1000)
		{
			return String.format("%.0f Hz", centerFrequency);
		}
		return String.format("%.1f KHz", centerFrequency/1000);
	}

	private Slider createSlider(EqualizerBand band, double minimum, double maximum) {
		final Slider slide = new Slider(minimum, maximum, band.getGain());
		slide.getStyleClass().add("-fx-background-radius: 5; -fx-background-color: #222, #888, black;"
				+ "-fx-background-insets: 0, 1 0 0 1, 1; -fx-padding: 2 10;");
		
		slide.setOrientation(Orientation.VERTICAL);
		slide.valueProperty().bindBidirectional(band.gainProperty());
		slide.setPrefWidth(44);
		return slide;
	}
	
	public GridPane getGridPane()
	{
		return this.grid;
	}

}
