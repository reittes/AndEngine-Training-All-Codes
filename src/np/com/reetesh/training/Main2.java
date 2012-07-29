package np.com.reetesh.training;

/**
 *  @author Reittes
 *  Basic scene setup with red background
 */
 
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class Main2 extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    
    private Camera camera;
    
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
	}

	@Override
	public Engine onLoadEngine() {
		 // Camera settings
		 this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		 // Engine Settings
		 final EngineOptions engineOptions= new EngineOptions(
				 true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(
						 CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
       
		 return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Scene onLoadScene() {
		// Create a scene
		final Scene gameScene= new Scene();
		
		// Create a colored background
		final ColorBackground redBG= new ColorBackground(1, 0, 0);
		
		// Set the colored background as the gameScenes background
		gameScene.setBackground(redBG);
		
		return gameScene;
	}
   
}
















