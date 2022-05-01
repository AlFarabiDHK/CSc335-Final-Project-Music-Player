import javafx.scene.Node;

public abstract class AbstractView 
{

		protected final MusicPlayerController controller;
		protected final Node viewNode;
		
		public AbstractView(MusicPlayerController controller)
		{
			this.controller = controller;
			this.viewNode = initView();
		}

		protected abstract Node initView();
		
		public Node getViewNode()
		{
			return this.viewNode;
		}
		
}
