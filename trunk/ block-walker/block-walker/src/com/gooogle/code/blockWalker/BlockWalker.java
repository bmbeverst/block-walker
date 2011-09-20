package com.gooogle.code.blockWalker;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.FixedStepEngine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.opengl.GLU;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.badlogic.gdx.math.Vector2;

/**
 * @author brooks Aug 31, 2011
 */
public class BlockWalker extends BaseGameActivity implements
		IOnSceneTouchListener {
	
	// ===========================================================
	// Constants
	// ===========================================================
	
	private static final int GRAVITY = 14;
	private final int CAMERA_HEIGHT = 480;
	private final int CAMERA_WIDTH = 800;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	// The camera that follow the player
	private BoundCamera mCamera;
	private int i = 0;
	   private boolean isDrawing = false;
	   
	   Rectangle[] rec = new Rectangle[250];
	// I am using fixed step because it is easier to use in a multiplayer game
	private FixedStepPhysicsWorld mPhysicsWorld;
	
	// this is the scene that every thing uses
	private Scene mScene = new Scene();
	private DBManager mDBM;
	private AutoParallaxBackground autoParallaxBackground;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	// this is called when ever a key is pressed. It just sends it to Resource
	// so that it can be passed to all the KeyDown listeners
	@Override
	public boolean onKeyDown(int pKeyCode, KeyEvent pEvent) {
		boolean handeled = Resources.onKeyDownEvent(pKeyCode, pEvent);
		if (!handeled) {
			return super.onKeyDown(pKeyCode, pEvent);
		}
		return handeled;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	// this is called when ever a key is released. It just sends it to Resource
	// so that it can be passed to all the KeyUp listeners
	@Override
	public boolean onKeyUp(int pKeyCode, KeyEvent pEvent) {
		boolean handeled = Resources.onKeyUpEvent(pKeyCode, pEvent);
		if (!handeled) {
			return super.onKeyUp(pKeyCode, pEvent);
		}
		return handeled;
	}
	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Engine onLoadEngine() {
		// saving ? decy 
		this.mDBM = new DBManager(getBaseContext());
		Resources.setmDBM(mDBM);

		// create the camera. It is not bound or following any thing yet.
		mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT) {
			// Forces the camera to move in integer steps. Helps prevent
			// artifacts from appearing when scrolling
			@Override
			public void onApplySceneMatrix(GL10 pGL) {
				GLHelper.setProjectionIdentityMatrix(pGL);
				
				GLU.gluOrtho2D(pGL, (int) this.getMinX(), (int) this.getMaxX(),
						(int) this.getMaxY(), (int) this.getMinY());
			}
		};
		// now done yet need to add more options
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		// need this to play sound
		engineOptions.setNeedsSound(true);
		engineOptions.setNeedsMusic(true);
		// without this errors are thrown when ever you touch the screen
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		
		// this is the final engine with all the options set.
		return new FixedStepEngine(engineOptions, 60);
	}
	
	@Override
	public void onLoadResources() {
	}
	
	@Override
	public Scene onLoadScene() {
		// Just a FPS(Frames Per a Second) looger
		mEngine.registerUpdateHandler(new FPSLogger());
		// Add this as a touch listener to be able to add blocks. Needs to go
		// else where.
		mScene.setOnSceneTouchListener(this);
		// set the back ground color
//		mScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		// Default 60, 8 and 8
		// lol I think we really messed up here. Gravity is StepsPerSecond not
		// gravity. I fixed this but you need to figure out how to set gravity.
		mPhysicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, GRAVITY), false,
				8, 8);
		
		// makes mario physics easy. Tutorial don't ask me.
		mPhysicsWorld.setAutoClearForces(true);
		// makes it so the PhysicsWorld will get updated.
		mScene.registerUpdateHandler(mPhysicsWorld);
		
		// Create the Singelton for all the Resources. Always use this is there
		// is any thing you need from this class.
		new Resources(mCamera, mPhysicsWorld, mScene, this, mEngine);
		// Start the game!
		
		//parallax background loading ! 
		//------------------------------------------------------------------------------------
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		BitmapTextureAtlas parallaxTexture = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
 
		BitmapTextureAtlas autoParallaxTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);

 		autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 3);
 		
 		TextureRegion mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxTexture, this,  "parallax_background_layer_front.png", 0, 0);
 		TextureRegion mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxTexture, this, "parallax_background_layer_back.png",  0, 188);
 		TextureRegion mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(autoParallaxTexture, this, "parallax_background_layer_mid.png", 0, 669);

 		mEngine.getTextureManager().loadTextures(parallaxTexture, autoParallaxTexture);
   
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, CAMERA_HEIGHT - mParallaxLayerBack.getHeight(), mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-15.0f, new Sprite(0, 20, mParallaxLayerMid)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-25.0f, new Sprite(0, 20, mParallaxLayerFront)));
		mScene.setBackground(autoParallaxBackground);
		Resources.setBackground(autoParallaxBackground);
 

		//------------------------------------------------------------------------------------
		GameHUD hud = new GameHUD();
		mCamera.setHUD(hud);
		Resources.setHUD(hud);
		//------------------------------------------------------------------------------------

		new MainMenu();
		
		// Engine's scene is set to this behind the scenes. No pun :)
		return mScene;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.anddev.andengine.ui.activity.BaseGameActivity#onDestroy()
	 */
	// This is to make sure the game will never run again.
	@Override
	protected void onDestroy() {
		// remove every thing in the scene
		mScene.detachChildren();
		// then get the Garbage collector to delete them
		System.gc();
		// Stoop the engine
		mEngine.stop();
		// reset tha camera
		mCamera.reset();
		// call super to do what ever it does.
		super.onDestroy();
		// This does not seem to be working because of Finalize in Shape is
		// messed up. But it works so who cares.
		System.runFinalizersOnExit(true);
		// Just shut the whole thing down!
		System.exit(0);
	}
	
	// Need to put this some where else. Probably in the HUD(Heads Up Display)
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
            isDrawing = true;
            i = 0;
         }
         if(pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
            isDrawing = false;
         }
         if (isDrawing = true) {
           // rec[i] = new Rectangle(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 1, 1);
            if (i != 0) {
               new Box(pSceneTouchEvent.getX(),pSceneTouchEvent.getY(),20,20);
            }
            i++;
         }
         return true;
		
	}
}