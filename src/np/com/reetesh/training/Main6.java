package np.com.reetesh.training;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.ScaleModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

public class Main6 extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    
    private Camera camera;
    
    private TextureRegion bgRegion;
    private TiledTextureRegion pandaRegion;
    
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
	}

	@Override
	public Engine onLoadEngine() {
		 // Camera settings
		 this.camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		 // Engine Settings
		 final EngineOptions engineOptions= new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.camera);
       
		 return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		// Create a Texture Atlas for the background image.
		final BitmapTextureAtlas bgTexture= new BitmapTextureAtlas(1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		// Create the texture region from the image
		bgRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgTexture, getApplicationContext(), "blue_bg.png", 0, 0);
		
		final BitmapTextureAtlas pandaTexture= new BitmapTextureAtlas(128, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		pandaRegion= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pandaTexture, getApplicationContext(), "panda_anim.png", 0, 0, 2, 1);
		
		// Load the textures
		getEngine().getTextureManager().loadTextures(bgTexture, pandaTexture);
	}

	@Override
	public Scene onLoadScene() {
		// Create a scene
		final Scene gameScene= new Scene();
		
		// Create a Sprite
		final Sprite background_image= new Sprite(0, 0, bgRegion);
		
		// Create a sprite background from the sprite.
		final SpriteBackground background= new SpriteBackground(background_image);
		
		// Set the sprite background as the scenes background
		gameScene.setBackground(background);
		
		// Create an Animated Sprite object for panda
		final AnimatedSprite panda= new AnimatedSprite(100, 100, pandaRegion.deepCopy());
		
		panda.animate(150);
				
		// Attach rectangle to gameScene
		gameScene.attachChild(panda);
		
		final AnimatedSprite panda2= new AnimatedSprite(
				CAMERA_WIDTH- panda.getWidth(), 100, pandaRegion.deepCopy());
		panda2.setFlippedHorizontal(true);
		panda.setCurrentTileIndex(0);
		gameScene.attachChild(panda2);
		
		final PhysicsHandler pandaHandler= new PhysicsHandler(panda);
		panda.registerUpdateHandler(pandaHandler);
		
		pandaHandler.setVelocityX(150);
		
		gameScene.registerUpdateHandler(new IUpdateHandler(){

			@Override
			public void onUpdate(float arg0) {
				if(panda.getX()>=CAMERA_WIDTH){
					panda.setPosition(0, panda.getY());
				}
				
				if(panda.collidesWith(panda2) && panda.getScaleX()==1){
					final ScaleModifier vanish= new ScaleModifier(0.3f, 1, 0);
					panda.registerEntityModifier(vanish);
				}
				
				if(panda!= null){
					if(panda.getScaleX()==0){
						runOnUpdateThread(new Runnable(){
							@Override
							public void run() {
								panda.detachSelf();
							}
						});
					}
				}
			}

			@Override
			public void reset() {
				
			}});
		
		return gameScene;
	}
   
}
















