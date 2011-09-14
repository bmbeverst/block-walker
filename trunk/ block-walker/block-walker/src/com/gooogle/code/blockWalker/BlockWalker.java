package com.gooogle.code.blockWalker;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.FixedStepEngine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.util.GLHelper;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.Debug;

import android.graphics.Color;
import android.hardware.SensorManager;
import android.opengl.GLU;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * @author brooks Aug 31, 2011
 */
public class BlockWalker extends BaseGameActivity implements
		IOnSceneTouchListener {
	
	// ===========================================================
	// Constants
	// ===========================================================

	
	
	private static final int GRAVITY = 10;
	public final int CAMERA_HEIGHT = 480;
	public final int CAMERA_WIDTH = 800;
	private boolean jumped = false;
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	private  BoundCamera mCamera;
	
	private MenuScene mMenuScene;
	private  FixedStepPhysicsWorld mPhysicsWorld;
	
	private Scene mScene = new Scene();
	private Sound backgroundSound;
	private MainLoop mMainLoop;
	
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
	@Override
	public boolean onKeyDown(int pKeyCode, KeyEvent pEvent) {
		boolean handeled = Resources.onKeyDownEvent(pKeyCode, pEvent);
		if (!handeled) {
			return super.onKeyDown(pKeyCode, pEvent);
		}
		return handeled;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
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
		mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT) {
			//Forces the camera to move in integer steps. Helps prevent artifacts from appearing when scrolling
			@Override
			public void onApplySceneMatrix(GL10 pGL) {
				GLHelper.setProjectionIdentityMatrix(pGL);

				GLU.gluOrtho2D(pGL, (int) this.getMinX(), (int) this.getMaxX(), (int) this.getMaxY(),
						(int) this.getMinY());
			}
		};
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		engineOptions.setNeedsSound(true);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		
		return new FixedStepEngine(engineOptions, 60);
	}
	
	@Override
	public void onLoadResources() {
		
	
		// sound
		
		 

	}
	
	@Override
	public Scene onLoadScene() {
		
		mEngine.registerUpdateHandler(new FPSLogger());
		//mEngine.registerUpdateHandler(new TimerHandler(2f, true, new AIupdate()));
		
		mScene.setOnSceneTouchListener(this);
		mScene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
		
		// Default 60, 8 and 8
		mPhysicsWorld = new FixedStepPhysicsWorld(GRAVITY, new Vector2(0, 1), false,
				8, 8);
		mPhysicsWorld.setAutoClearForces(true);
		mScene.registerUpdateHandler(mPhysicsWorld);

		//mScene.setChildScene(mMenuScene, false, true, true);
		// mScene.setOnAreaTouchListener(this);
		// May help with holding on to a block
		// mScene.setTouchAreaBindingEnabled(true);
		if (backgroundSound != null) {
			backgroundSound.play();
		}
		
		//mCamera.setBounds(0, 10000, 0, 10000);// TODO set to some thing real.
		//mCamera.setBoundsEnabled(false);
		new Resources(mScene, mCamera, mPhysicsWorld, mScene, this, mEngine);
		mMainLoop = new MainLoop();
		return mScene;
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.anddev.andengine.ui.activity.BaseGameActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		mScene.detachChildren();
		System.gc();
		mEngine.stop();
		mCamera.reset();
		super.onDestroy();
		System.runFinalizersOnExit(true);
		System.exit(0);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mPhysicsWorld != null) {
			if (pSceneTouchEvent.isActionDown()) {
				new Box(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 64, 64);
				return true;
			}
		}
		return false;
		
	}
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}