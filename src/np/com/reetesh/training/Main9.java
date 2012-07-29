package np.com.reetesh.training;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
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
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.graphics.Color;
import android.graphics.Typeface;

public class Main9 extends BaseGameActivity implements IOnMenuItemClickListener{

	private static final int CAMERA_WIDTH = 800;
    private static final int CAMERA_HEIGHT = 480;
    
    private Camera camera;
    
    private Scene gameScene;
    
    private TextureRegion bgRegion;
    private TiledTextureRegion pandaRegion;
    
    private Font mFont;
    
    private static final int MENU_RESTART= 0;
    private static final int MENU_QUIT= 1;
    
    private MenuScene menuScene;
    
    private Music mLoop;
    private Sound sMove;
    
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
		 engineOptions.setNeedsMusic(true);
		 engineOptions.setNeedsSound(true);
		 
		 return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		// Create a Texture Atlas for the background image.
		final BitmapTextureAtlas bgTexture= new BitmapTextureAtlas(1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		// Create the texture region from the image
		bgRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgTexture, this, "blue_bg.png", 0, 0);
		
		final BitmapTextureAtlas pandaTexture= new BitmapTextureAtlas(128, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		pandaRegion= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(pandaTexture, getApplicationContext(), "panda_anim.png", 0, 0, 2, 1);
		
//		FontFactory.setAssetBasePath("font/");
		final BitmapTextureAtlas mFontTexture= new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = new Font(mFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 40, true, Color.WHITE);
//		this.mFont = FontFactory.createFromAsset(this.mFontTexture, this, "Plok.ttf", 40, true, Color.WHITE);
        getEngine().getFontManager().loadFont(this.mFont);

		// Load the textures
		getEngine().getTextureManager().loadTextures(bgTexture, pandaTexture, mFontTexture);
		
		MusicFactory.setAssetBasePath("sounds/");
		SoundFactory.setAssetBasePath("sounds/");
		
		try{
			this.mLoop= MusicFactory.createMusicFromAsset(
					getEngine().getMusicManager(), this, "main_music.mp3");
			this.mLoop.setLooping(true);
			
			this.sMove= SoundFactory.createSoundFromAsset(
					getEngine().getSoundManager(), this, "move.ogg");

		}catch(final IOException e){
			Debug.e(e);
		}
	

	}

	@Override
	public Scene onLoadScene() {
		// Create a scene
		gameScene= createGameScene();
				
		return gameScene;
	}
	
	private Scene createGameScene(){
		
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
						CAMERA_WIDTH- panda.getWidth(), 100, pandaRegion.deepCopy()){
					@Override
					public boolean onAreaTouched(TouchEvent event, float touchX, float touchY){
						if(event.isActionDown()){
							if(this.getY()==100){
								this.setPosition(this.getX(), 200);
							}else{
								this.setPosition(this.getX(), 100);
							}
							sMove.play();
							return true;
						}else{
							return false;
						}
					};
				};
				panda2.setFlippedHorizontal(true);
				panda.setCurrentTileIndex(0);
				gameScene.attachChild(panda2);
				gameScene.registerTouchArea(panda2);
				
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
										menuScene= createMenuScene();
										gameScene.setChildScene(menuScene, false, true, true);
									}
								});
							}
						}
					}

					@Override
					public void reset() {
						
					}});
				
				this.mLoop.play();
				
				return gameScene;
	}
	
	protected MenuScene createMenuScene() {
        final MenuScene menuScene = new MenuScene(this.camera);

        this.mLoop.pause();
        
        final IMenuItem resetMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_RESTART, this.mFont, "RESTART"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
        resetMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(resetMenuItem);

        final IMenuItem quitMenuItem = new ColorMenuItemDecorator(new TextMenuItem(MENU_QUIT, this.mFont, "QUIT"), 1.0f,0.0f,0.0f, 0.0f,0.0f,0.0f);
        quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        menuScene.addMenuItem(quitMenuItem);

        menuScene.buildAnimations();

        menuScene.setBackgroundEnabled(false);

        menuScene.setOnMenuItemClickListener(this);
        return menuScene;
	}

	private void restart(){
		gameScene.reset();
		gameScene= createGameScene();
		menuScene.back();
		getEngine().setScene(gameScene);
	}
	
	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem item,
			float arg2, float arg3) {
		switch(item.getID()){
		case MENU_RESTART:
			restart();
			return true;
		case MENU_QUIT:
			finish();
			return true;
		default:
			return false;
		}
	}
	
   
}
















