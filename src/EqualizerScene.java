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

/**
 * 
 * @author Dhruv Bhatia, Muhtasim Al-Farabi, Shrey Goyal, Suryashree Ray
 * 
 * This class can changes the frequencies of our music app by increasing or decreasing the frequency.
 * It inherits the abstract class AbstructView. 
 */
public class EqualizerScene extends AbstractView
{
	private static GridPane grid;
	
	/**
	 * Constructor
	 * 
	 * <p>
	 * 
	 * This is the constructor of the equalizerScene class. It calls the constructor of the super class
	 * and passes in the controller.
	 * @param controller an instantiation of the MusicPlayerController class 
	 */
	public EqualizerScene(MusicPlayerController controller) 
	{
		super(controller);
		createEqualizer();
	}
	
	/**
	 * 
	 * Returns the initial Node
	 * 
	 * <p>
	 * 
	 * This method returns a gridPane that will contain the equalizer sliders.
	 * @return a Node that is the initial equalizer view
	 */

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
	
	/**
	 * Creates the equalizers
	 * 
	 * <p>
	 * 
	 * This method calls the createBands method in order to create the sliders
	 */
	
	private void createEqualizer() 
	{
		grid = (GridPane) getViewNode();

		
		createBands(grid);
	}
	
	/**
	 * Creates the equalizer sliders
	 * 
	 * <p>
	 * 
	 * This method creates the sliders and adds them to the gridpane
	 */

	private void createBands(GridPane grid) 
	{
		final ObservableList<EqualizerBand> bands = controller.getEqualizerBand();
		

		
		double minimum = EqualizerBand.MIN_GAIN;
		double maximum = EqualizerBand.MAX_GAIN;
		
		for (int i = 0; i < bands.size(); i++)
		{
			EqualizerBand band = bands.get(i);
			
			Slider slide = createSlider(band, minimum, maximum);
			final Label label = new Label(frequencyFormat(band.getCenterFrequency()));
			if(controller.getColorMode())
				label.setStyle("-fx-text-fill:WHITE;");
			else
				label.setStyle("-fx-text-fill: BLACK;");
			label.getStyleClass().addAll("mediaText", "eqLabel");
			
			GridPane.setHalignment(label, HPos.CENTER);
			GridPane.setHalignment(slide, HPos.CENTER);
			GridPane.setHgrow(slide, Priority.ALWAYS);
			
			grid.add(label, i, 1);
			grid.add(slide, i, 2);
		}
	}

	/**
	 * returns the frequency format
	 * 
	 * <p>
	 * 
	 * This method returns the frequency format either in Hz or KHz
	 * @param centerFrequency the actual frequency
	 * @return the frequency format either in Hz or KHz
	 */

	private String frequencyFormat(double centerFrequency) 
	{
		if (centerFrequency < 1000)
		{
			return String.format("%.0f Hz", centerFrequency);
		}
		return String.format("%.1f KHz", centerFrequency/1000);
	}
	
	/**
	 * 
	 * Creates Sliders
	 * 
	 * <p>
	 * 
	 * This method creates sliders for the equalizer grid pane
	 * 
	 * @param band an EqualizerBand object 
	 * @param minimum a double, indicates the minimum of a slider
	 * @param maximuma double, indicates the maximum of a slider
	 * @return a slider
	 */

	private Slider createSlider(EqualizerBand band, double minimum, double maximum) 
	{
		final Slider slide = new Slider(minimum, maximum, band.getGain());
		slide.getStyleClass().add("-fx-background-radius: 5; -fx-background-color: #222, #888, black;"
				+ "-fx-background-insets: 0, 1 0 0 1, 1; -fx-padding: 2 10;");
		
		slide.setOrientation(Orientation.VERTICAL);
		slide.valueProperty().bindBidirectional(band.gainProperty());
		slide.setPrefWidth(44);
		return slide;
	}
	
	/**
	 * gets the GridPane
	 * 
	 * <p>
	 * 
	 * This method returns the GridPane on which every slider exists
	 * @return the grid field
	 */
	
	public GridPane getGridPane()
	{
		return this.grid;
	}

}
