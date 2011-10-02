package com.gooogle.code.blockWalker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLoader;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
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

import com.gooogle.code.blockWalker.AI.Attackable;

/**
 * @author brooks Sep 7, 2011
 */
public class Resources {
	// used for loading resources
	private static BaseGameActivity mBaseGameActivity;
	private static BoundCamera mCamera;
	private static FixedStepPhysicsWorld mPhysicsWorld;
	private static Scene mScene;
	private static Engine mEngine;
	// all the listeners for the different events
	private static LinkedList<OnKeyDownListener> downListeners = new LinkedList<OnKeyDownListener>();
	private static LinkedList<OnKeyUpListener> upListeners = new LinkedList<OnKeyUpListener>();
	// all the goals
	private static LinkedList<Rectangle> goalWatcher = new LinkedList<Rectangle>();
	private static DBManager mDBM;
	private static BitmapTextureAtlas autoParallaxTexture;
	private static AutoParallaxBackground mautoParallaxBackground;
	private static MapManager mMapManager;
	private static Rectangle mexit;
	private static Player mPlayer;
	private static Rectangle mwater;
	private static GameHUD mHUD;
	private static MainMenu mMenu;
	private static LinkedList<Attackable> monsters = new LinkedList<Attackable>();
	private static ArrayList<TMXTile> collideTiles = new ArrayList<TMXTile>();
	
	/**
	 * @return LinkedList<Attackable>
	 */
	public static LinkedList<Attackable> getMonsters() {
		return monsters;
	}
	
	// Create the Resouces and set all the needed values
	/**
	 * @param pCamera
	 * @param pPhysicsWorld
	 * @param pScene
	 * @param pBaseGameActivity
	 * @param pEngine
	 */
	Resources(BoundCamera pCamera, FixedStepPhysicsWorld pPhysicsWorld,
			Scene pScene, BaseGameActivity pBaseGameActivity, Engine pEngine) {
		mCamera = pCamera;
		mPhysicsWorld = pPhysicsWorld;
		mScene = pScene;
		mBaseGameActivity = pBaseGameActivity;
		mEngine = pEngine;
	}
	
	/**
	 * @return Player
	 */
	public static Player getmPlayer() {
		return mPlayer;
	}
	
	/**
	 * @return Rectangle
	 */
	public static Rectangle getExit() {
		return mexit;
	}
	
	/**
	 * @return the mBaseGameActivity
	 */
	public static final BaseGameActivity getmBaseGameActivity() {
		return mBaseGameActivity;
	}
	
	/**
	 * @return the mCamera
	 */
	public static final BoundCamera getmCamera() {
		return mCamera;
	}
	
	/**
	 * @return the mPhysicsWorld
	 */
	public static final FixedStepPhysicsWorld getmPhysicsWorld() {
		return mPhysicsWorld;
	}
	
	/**
	 * @return the mScene
	 */
	public static final Scene getmScene() {
		return mScene;
	}
	
	/**
	 * @return the mEngine
	 */
	public static final Engine getmEngine() {
		return mEngine;
	}
	
	/**
	 * @return LinkedList<Rectangle>
	 */
	static LinkedList<Rectangle> getGoals() {
		return goalWatcher;
	}
	
	/**
	 * @return DBManager
	 */
	public static DBManager getmDBM() {
		return mDBM;
	}
	
	/**
	 * @return AutoParallaxBackground
	 */
	public static AutoParallaxBackground getMautoParallaxBackground() {
		return mautoParallaxBackground;
	}
	
	/**
	 * @return MapManager
	 */
	public static MapManager getMapManger() {
		return mMapManager;
	}
	
	/**
	 * @return Rectangle
	 */
	public static Rectangle getWater() {
		return mwater;
	}
	
	/**
	 * @return GameHUD
	 */
	public static GameHUD getHUD() {
		return mHUD;
	}
	
	/**
	 * @return MainMenu
	 */
	public static MainMenu getMenu() {
		return mMenu;
	}
	
	/**
	 * @param mPlayer
	 */
	public static void setmPlayer(Player mPlayer) {
		Resources.mPlayer = mPlayer;
	}
	
	/**
	 * @param pexit
	 */
	public static void setExit(Rectangle pexit) {
		mexit = pexit;
	}
	
	/**
	 * @param pDBM
	 */
	public static void setmDBM(DBManager pDBM) {
		Resources.mDBM = pDBM;
	}
	
	/**
	 * @param pautoParallaxBackground
	 */
	public static void setBackground(
			AutoParallaxBackground pautoParallaxBackground) {
		mautoParallaxBackground = pautoParallaxBackground;
	}
	
	/**
	 * @param pmapManager
	 */
	public static void setMapManager(MapManager pmapManager) {
		mMapManager = pmapManager;
	}
	
	/**
	 * @param pwater
	 */
	public static void setWater(Rectangle pwater) {
		mwater = pwater;
	}
	
	/**
	 * @param phud
	 */
	public static void setHUD(GameHUD phud) {
		mHUD = phud;
	}
	
	/**
	 * @param pMenu
	 */
	public static void setMenu(MainMenu pMenu) {
		mMenu = pMenu;
	}
	
	// loads a font from assets/font/
	/**
	 * @param location
	 * @return
	 */
	static Font loadFont(String location) {
		BitmapTextureAtlas mMenuFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		FontFactory.setAssetBasePath("font/");
		Font mMenuFont = FontFactory.createFromAsset(mMenuFontTexture,
				mBaseGameActivity, location, 48, true, Color.WHITE);
		mEngine.getTextureManager().loadTexture(mMenuFontTexture);
		mEngine.getFontManager().loadFont(mMenuFont);
		
		return mMenuFont;
	}
	
	// loads a texture from assets/gfx/
	/**
	 * @param location
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
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
	
	// test for parallax background
	/**
	 * @param locationfront
	 * @param locationback
	 * @param locationmid
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
	static BitmapTextureAtlas loadParallax(String locationfront,
			String locationback, String locationmid, int sizeX, int sizeY) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		autoParallaxTexture = new BitmapTextureAtlas(1024, 1024,
				TextureOptions.DEFAULT);
		return autoParallaxTexture;
	}
	
	// loads a tiled texture from assets/gfx/ the size values are from 1 not 0.
	/**
	 * @param location
	 * @param sizeX
	 * @param sizeY
	 * @param tileX
	 * @param tileY
	 * @return TiledTextureRegion
	 */
	public static TiledTextureRegion loadTiledTexture(String location,
			int sizeX, int sizeY, int tileX, int tileY) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas mTiledTexture = new BitmapTextureAtlas(sizeX, sizeY,
				TextureOptions.DEFAULT);
		TiledTextureRegion mTiledRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mTiledTexture, mBaseGameActivity,
						location, 0, 0, tileX, tileY);
		
		mEngine.getTextureManager().loadTextures(mTiledTexture);
		return mTiledRegion;
	}
	
	// loads a TMX map from assets/tmx/
	/**
	 * @param location
	 * @return
	 */
	static TMXTiledMap loadTMXmap(String location) {
		TMXTiledMap mTMXTiledMap = null;
		try {
			final TMXLoader tmxLoader = new TMXLoader(mBaseGameActivity,
					mEngine.getTextureManager(), TextureOptions.NEAREST, null);
			mTMXTiledMap = tmxLoader.loadFromAsset(mBaseGameActivity, "tmx/"
					+ location);
		} catch (final TMXLoadException tmxle) {
			Debug.e(tmxle);
		}
		return mTMXTiledMap;
	}
	
	// loads a sound from assets/mfx/
	/**
	 * @param locations
	 * @return
	 */
	static Sound loadSound(String locations) {
		SoundFactory.setAssetBasePath("mfx/");
		Sound backgroundSound = null;
		try {
			backgroundSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), mBaseGameActivity, locations);
		} catch (final IOException e) {
			Debug.e(e);
		}
		return backgroundSound;
	}
	
	/**
	 * @param location
	 * @return
	 */
	static Music loadMusic(String location) {
		MusicFactory.setAssetBasePath("mfx/");
		Music backgroundMusic = null;
		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(Resources
					.getmEngine().getMusicManager(), Resources
					.getmBaseGameActivity(), location);
			backgroundMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		return backgroundMusic;
	}
	
	// manage the various listeners and goals and adding of said items
	/**
	 * @param listener
	 */
	static void addOnKeyDownListener(OnKeyDownListener listener) {
		downListeners.add(listener);
	}
	
	/**
	 * @param listener
	 */
	static void addOnKeyUpListener(OnKeyUpListener listener) {
		upListeners.add(listener);
	}
	
	/**
	 * @param rect
	 */
	static void addGoal(Rectangle rect) {
		goalWatcher.add(rect);
	}
	
	/**
	 * @param m
	 */
	/**
	 * @param m
	 */
	public static void addMonster(Attackable m) {
		monsters.add(m);
		
	}
	
	/**
	 * 
	 */
	static void clearMonsters() {
		monsters.clear();
	}
	
	/**
	 * @param pKeyCode
	 * @param pEvent
	 * @return
	 */
	static boolean onKeyDownEvent(int pKeyCode, KeyEvent pEvent) {
		boolean handled = false;
		
		for (int i = 0; i < downListeners.size() && !handled; i++) {
			handled = downListeners.get(i).onKeyDown(pKeyCode, pEvent);
		}
		return handled;
	}
	
	/**
	 * @param pKeyCode
	 * @param pEvent
	 * @return
	 */
	static boolean onKeyUpEvent(int pKeyCode, KeyEvent pEvent) {
		boolean handled = false;
		
		for (int i = 0; i < upListeners.size() && !handled; i++) {
			handled = upListeners.get(i).onKeyUp(pKeyCode, pEvent);
		}
		return handled;
	}
	
	/**
	 * @param p
	 */
	static void removePlayer(Player p) {
		upListeners.remove(p);
		downListeners.remove(p);
	}
	
	// Set every thing to null so that the Garbage collector can take it.
	/**
	 * 
	 */
	static void finish() {
		mCamera = null;
		mPhysicsWorld = null;
		mScene = null;
		mEngine = null;
		// Nuke the whole thing
		mBaseGameActivity.finish();
	}
	
	/**
	 * 
	 */
	/**
	 * 
	 */
	public static void detachBossLockLayer() {
		mMapManager.getMap().getBossLayer().setVisible(false);
		mMapManager.getMap().getmLock().setActive(false);
		Resources.getmPhysicsWorld().destroyBody(
				mMapManager.getMap().getmLock());
	}
	
	/**
	 * @param tempTile
	 */
	static void addCollideTile(TMXTile tempTile) {
		collideTiles.add(tempTile);
		
	}
	
	/**
	 * @return ArrayList<TMXTile>
	 */
	public static ArrayList<TMXTile> getCollideTile() {
		return collideTiles;
		
	}
	
}
