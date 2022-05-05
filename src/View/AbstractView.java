package View;
import Controller.MusicPlayerController;
import javafx.scene.Node;

/**
 * 
 * @author Dhruv Bhatia, Muhtasim Al-Farabi, Shrey Goyal, Suryashree Ray
 * 
 * This is the super class of the equalizer class. 
 *
 */
public abstract class AbstractView 
{

		protected final MusicPlayerController controller;
		protected final Node viewNode;
		/**
		 * Constructor
		 * 
		 * <p>
		 * 
		 * This is the constructor for the class
		 * @param controller an instantiation of the MusicPlayerController class
		 */
		public AbstractView(MusicPlayerController controller)
		{
			this.controller = controller;
			this.viewNode = initView();
		}
		
		
		protected abstract Node initView();
		
		/**
		 * Returs the view Node
		 * 
		 * <p>
		 * 
		 * This method returns the View Node
		 * 
		 * @return the view node
		 */
		public Node getViewNode()
		{
			return this.viewNode;
		}
		
}
