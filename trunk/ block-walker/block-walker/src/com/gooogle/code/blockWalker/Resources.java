package com.gooogle.code.blockWalker;

import java.io.IOException;
import java.util.LinkedList;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.graphics.Color;
import android.view.KeyEvent;

/**
 * @author brooks Sep 7, 2011
 */
public class Resources {
	//used for loading resources
	private static BaseGameActivity mBaseGameActivity;
	private static BoundCamera mCamera;
	private static FixedStepPhysicsWorld mPhysicsWorld;
	private static Scene mScene;
	private static Engine mEngine;
	//all the listeners for the different events
	private static LinkedList<OnKeyDownListener> downListeners = new LinkedList<OnKeyDownListener>();
	private static LinkedList<OnKeyUpListener> upListeners = new LinkedList<OnKeyUpListener>();
	// all the goals
	private static LinkedList<Rectangle> goalWatcher = new LinkedList<Rectangle>();
	private static DBManager mDBM;
	private static Object mParallaxLayerFront;
	private static Object mParallaxLayerBack;
	private static Object mParallaxLayerMid;
	private static BitmapTextureAtlas autoParallaxTexture;
	private static AutoParallaxBackground mautoParallaxBackground;
	private static MapManager mMapManager;
	private static Rectangle mexit;
	private static Player mPlayer;
	private static Rectangle mwater;
	private static GameHUD mHUD;

	
	public static Player getmPlayer() {
		return mPlayer;
	}

	public static void setmPlayer(Player mPlayer) {
		Resources.mPlayer = mPlayer;
	}

	public static Rectangle getExit() {
		return mexit;
	}

	public static void setExit(Rectangle pexit) {
		mexit = pexit;
	}

	//Create the Resouces and set all the needed values
	Resources(BoundCamera pCamera, FixedStepPhysicsWorld pPhysicsWorld,
			Scene pScene, BaseGameActivity pBaseGameActivity,
			Engine pEngine) {
		mCamera = pCamera;
		mPhysicsWorld = pPhysicsWorld;
		mScene = pScene;
		mBaseGameActivity = pBaseGameActivity;
		mEngine = pEngine;
	}

	/**
	 * @return the mBaseGameActivity
	 */
	static final BaseGameActivity getmBaseGameActivity() {
		return mBaseGameActivity;
	}
	
	/**
	 * @return the mCamera
	 */
	static final BoundCamera getmCamera() {
		return mCamera;
	}
	
	/**
	 * @return the mPhysicsWorld
	 */
	static final FixedStepPhysicsWorld getmPhysicsWorld() {
		return mPhysicsWorld;
	}
	
	/**
	 * @return the mScene
	 */
	static final Scene getmScene() {
		return mScene;
	}
	
	/**
	 * @return the mEngine
	 */
	static final Engine getmEngine() {
		return mEngine;
	}
	// loads a font from assets/font/
	static Font loadFont(String location) {
		BitmapTextureAtlas mMenuFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		FontFactory.setAssetBasePath("font/");
		Font mMenuFont = FontFactory.createFromAsset(mMenuFontTexture,
				mBaseGameActivity, location, 48, true,
				Color.WHITE);
		mEngine.getTextureManager().loadTexture(mMenuFontTexture);
		mEngine.getFontManager().loadFont(mMenuFont);
		
		return mMenuFont;
	}

	// loads a texture from assets/gfx/
	static TextureRegion loadTexture(String location, int sizeX, int sizeY) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas mBitmapTexture = new BitmapTextureAtlas(sizeX,
				sizeY, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTexture, mBaseGameActivity, location,
						0, 0);
		
		mEngine.getTextureManager().loadTexture(mBitmapTexture);
		return mTextureRegion;
	}
	
	
	//test for parallax background
	static BitmapTextureAtlas loadParallax(String locationfront, String locationback, String locationmid, int sizeX, int sizeY){
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

	BitmapTextureAtlas parallaxTexture = new BitmapTextureAtlas(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	autoParallaxTexture = new BitmapTextureAtlas(1024, 1024, TextureOptions.DEFAULT);
	return autoParallaxTexture;


	}
	
	
	
	
	// loads a tiled texture from assets/gfx/ the size values are from 1 not 0.
	static TiledTextureRegion loadTiledTexture(String location, int sizeX, int sizeY, int tileX, int tileY) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas mTiledTexture = new BitmapTextureAtlas(sizeX, sizeY,
					TextureOptions.DEFAULT);
		TiledTextureRegion mTiledRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(mTiledTexture, mBaseGameActivity, location, 0,
							0, tileX, tileY);

			mEngine.getTextureManager().loadTextures(mTiledTexture);
		return mTiledRegion;
	}

	// loads a TMX map from assets/tmx/
	static TMXTiledMap loadTMXmap(String location) {
		TMXTiledMap mTMXTiledMap = null;
		try {
			final TMXLoader tmxLoader = new TMXLoader(mBaseGameActivity,
					mEngine.getTextureManager(), TextureOptions.NEAREST, null);
			mTMXTiledMap = tmxLoader.loadFromAsset(mBaseGameActivity, "tmx/" + location);
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}
		return mTMXTiledMap;
	}

	// loads a sound from assets/mfx/
	static Sound loadSound(String locations) {
		SoundFactory.setAssetBasePath("mfx/");
		Sound backgroundSound = null;
		try {
			backgroundSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), mBaseGameActivity, "background.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		} 
		return backgroundSound;
	}
	
	//manage the various listeners and goals and adding of said items
	static void addOnKeyDownListener(OnKeyDownListener listener) {
		downListeners.add(listener);
	}
	static boolean onKeyDownEvent(int pKeyCode, KeyEvent pEvent) {
		boolean handled = false;
		
		for(int i = 0; i < downListeners.size() && !handled ; i++) {
			handled = downListeners.get(i).onKeyDown(pKeyCode, pEvent);
		}
		return handled;
	}
	static void addOnKeyUpListener(OnKeyUpListener listener) {
		upListeners.add(listener);
	}
	static boolean onKeyUpEvent(int pKeyCode, KeyEvent pEvent) {
		boolean handled = false;
		
		for(int i = 0; i < upListeners.size() && !handled ; i++) {
			handled = upListeners.get(i).onKeyUp(pKeyCode, pEvent);
		}
		return handled;
	}
	
	static void removePlayer(Player p) {
		upListeners.remove(p);
		downListeners.remove(p);
	}
	static void addGoal(Rectangle rect) {
		goalWatcher.add(rect);
	}
	static LinkedList<Rectangle> getGoals() {
		return goalWatcher;
	}
	
	//Set every thing to null so that the Garbage collector can take it.
	static void finish() {
		mCamera = null;
		mPhysicsWorld = null;
		mScene = null;
		mEngine = null;
		// Nuke the whole thing
		mBaseGameActivity.finish();
	}
 

	public static void setmDBM(DBManager pDBM) {
		Resources.mDBM = pDBM;
	}
 
	public static DBManager getmDBM() {
		return mDBM;
	}

	public static void setBackground(AutoParallaxBackground pautoParallaxBackground) {
		mautoParallaxBackground= pautoParallaxBackground;
	}

	public static AutoParallaxBackground getMautoParallaxBackground() {
		return mautoParallaxBackground;
	}

	public static void setMapManager(MapManager pmapManager) {
		mMapManager = pmapManager;
	}

	public static MapManager getMapManger() {
		return mMapManager;
	}

	public static Rectangle getWater() {
		return mwater;
	}

	public static void setWater(Rectangle pwater) {
		mwater = pwater;
	}

	public static void setHUD(GameHUD phud) {
		mHUD=phud;
	}
	
	public static GameHUD getHUD() {
		return mHUD;
	}

 
 

}
