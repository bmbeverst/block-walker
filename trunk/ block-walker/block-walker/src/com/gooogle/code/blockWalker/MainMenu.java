package com.gooogle.code.blockWalker;

import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.Debug;

import android.view.KeyEvent;

public class MainMenu implements IOnMenuItemClickListener, OnKeyDownListener {
	
	protected static final int MENU_START = 0;
	protected static final int MENU_SAVE = MENU_START + 1;
	protected static final int MENU_LOAD = MENU_SAVE + 1;
	protected static final int MENU_QUIT = MENU_LOAD + 1;
	private Camera mCamera = Resources.getmCamera();
	private MenuScene mMenuScene;
	private Scene mScene = Resources.getmScene();
	private boolean noPlayer = true;
	private Sound goalSound;
	
	public MainMenu() {
		createMenuScene();
		mScene.setChildScene(mMenuScene, false, true, true);
		
	}
	
	private void createMenuScene() {
		Font mMenuFont = Resources.loadFont("Daville Condensed Slanted.ttf");
		mMenuScene = new MenuScene(mCamera);
		
		// Does not do anything
		mMenuScene.setPosition(10, 10);
		final IMenuItem Game1MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_START, mMenuFont, "Start New Game"),
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		Game1MenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		final IMenuItem SaveMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_SAVE, mMenuFont, "Save Game"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		SaveMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		final IMenuItem LoadMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_LOAD, mMenuFont, "Load Game"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		LoadMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_QUIT, mMenuFont, "Quit"), 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		mMenuScene.addMenuItem(Game1MenuItem);
		mMenuScene.addMenuItem(SaveMenuItem);
		mMenuScene.addMenuItem(LoadMenuItem);
		mMenuScene.addMenuItem(quitMenuItem);
		mMenuScene.buildAnimations();
		mMenuScene.setBackgroundEnabled(false);
		mMenuScene.setOnMenuItemClickListener(this);
		Resources.addOnKeyDownListener(this);
		
	}
	
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene,
			final IMenuItem pMenuItem, final float pMenuItemLocalX,
			final float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_START:

				goalSound = Resources.loadSound("background.ogg");
				
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				if (noPlayer) {
					init();
				}
				return true;
				
			case MENU_SAVE:
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				return true;
				
			case MENU_LOAD:
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				aiTest();
				return true;
				
			case MENU_QUIT:
				/* End Activity. */
				Resources.finish();
				return true;
			default:
				return false;
		}
	}
	
	private void init() {
		
		new TMXMap("stage3.tmx");
		final Player player = new Player(150, 1500, null);
		
		noPlayer = false;
		Resources.getmScene().registerUpdateHandler(
				new TimerHandler(5, new AIupdate()));
		Monster temp = new Monster(100, 1500, null);
		AIupdate.addMonster(temp);
		/* Need to get coords for these monsters
		temp = new Monster(100, 1500, null);
		AIupdate.addMonster(temp);
		temp = new Monster(100, 1500, null);
		AIupdate.addMonster(temp);
		temp = new Monster(100, 1500, null);
		AIupdate.addMonster(temp);
		temp = new Monster(100, 1500, null);
		AIupdate.addMonster(temp);*/
		
		/* The actual collision-checking. */
		mScene.registerUpdateHandler(new IUpdateHandler() {
			private boolean playing = false;
			private int index;
			
			@Override
			public void reset() {
			}
			
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				LinkedList<Rectangle> goals = Resources.getGoals();
				if (!playing) {
					for (index = 0; !playing && index < goals.size(); index++) {
						// Debug.d("GOAL!");
						if (goals.get(index).collidesWith(player)) {
							if (!playing) {
								playing = true;
								goalSound.play();
								break; // i KNOW THIS IS BAD BUT IT IS THE ONLY
										// WAY!
							}
						}
					}
				} else if (!goals.get(index).collidesWith(player)) {
					goalSound.stop();
					playing = false;
				}
			}
			
			private Scene win() {
				
				return mMenuScene;
			}
		});
		
	}
	
	private void aiTest() {
		
		new Borders(800, 400);
		new Player(100, 100, null);
		Resources.getmScene().registerUpdateHandler(
				new TimerHandler(2, new AIupdate()));
		Monster temp = new Monster(200, 100, null);
		AIupdate.addMonster(temp);
	}
	
	@Override
	public boolean onKeyDown(int pKeyCode, KeyEvent pEvent) {
		boolean handeled = false;
		
		switch (pKeyCode) {
			case KeyEvent.KEYCODE_MENU:
				if (pEvent.getAction() == KeyEvent.ACTION_DOWN) {
					if (mScene.hasChildScene()) {
						/* Remove the menu and reset it. */
						mMenuScene.back();
					} else {
						/* Attach the menu. */
						mScene.setChildScene(mMenuScene, false, true, true);
					}
				}
				handeled = true;
				break;
		}
		return handeled;
	}
}
