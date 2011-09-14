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
	
	private static BaseGameActivity mBaseGameActivity;
	private static Scene mBaseScene;
	private static BoundCamera mCamera;
	private static FixedStepPhysicsWorld mPhysicsWorld;
	private static Scene mScene;
	private static Engine mEngine;
	private static LinkedList<OnKeyDownListener> downListeners = new LinkedList<OnKeyDownListener>();
	private static LinkedList<OnKeyUpListener> upListeners = new LinkedList<OnKeyUpListener>();
	private static LinkedList<Rectangle> goalWatcher = new LinkedList<Rectangle>();
	
	Resources(Scene pBaseScene, BoundCamera pCamera,
			FixedStepPhysicsWorld pPhysicsWorld, Scene pScene,
			BaseGameActivity pBaseGameActivity, Engine pEngine) {
		mBaseScene = pBaseScene;
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
	 * @return the mBaseScene
	 */
	static final Scene getmBaseScene() {
		return mBaseScene;
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
	
	static TextureRegion loadImage(String location, int sizeX, int sizeY) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas mBitmapTexture = new BitmapTextureAtlas(sizeX,
				sizeY, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		TextureRegion mTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mBitmapTexture, mBaseGameActivity, location,
						0, 0);
		
		mEngine.getTextureManager().loadTexture(mBitmapTexture);
		return mTextureRegion;
	}
	
	static void finish() {
		mBaseScene = null;
		mCamera = null;
		mPhysicsWorld = null;
		mScene = null;
		mEngine = null;
		mBaseGameActivity.finish();
	}

	static TiledTextureRegion loadTiledImage(String location, int sizeX, int sizeY, int tileX, int tileY) {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		BitmapTextureAtlas mTiledTexture = new BitmapTextureAtlas(sizeX, sizeY,
					TextureOptions.DEFAULT);
		TiledTextureRegion mTiledRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(mTiledTexture, mBaseGameActivity, location, 0,
							0, tileX, tileY);

			mEngine.getTextureManager().loadTextures(mTiledTexture);
		return mTiledRegion;
	}
	
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

	static void addGoal(Rectangle rect) {
		goalWatcher.add(rect);
	}
	static LinkedList<Rectangle> getGoals() {
		return goalWatcher;
	}
}