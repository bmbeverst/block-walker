package com.gooogle.code.blockWalker;

import java.io.IOException;
import java.util.LinkedList;
import javax.microedition.khronos.opengles.GL10;
import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.util.Debug;

import android.view.KeyEvent;


public class MainMenu implements IOnMenuItemClickListener, OnKeyDownListener {
	
	protected static final int MENU_START = 0;
	protected static final int MENU_SAVE = MENU_START + 1;
	protected static final int MENU_LOAD = MENU_SAVE + 1;
	protected static final int MENU_QUIT = MENU_LOAD + 1;
	protected static final int MENU_RESUME = MENU_QUIT + 1;
	protected static final int MENU_OPTIONS = MENU_RESUME + 1;
	private Camera mCamera = Resources.getmCamera();
	private MenuScene mMenuScene,mResumeScene;
	public MenuScene getmMenuScene() {
		return mMenuScene;
	}
	private Scene mScene = Resources.getmScene();
	//private Sound goalSound;
	private MapManager mmanager;
	private boolean hasStarted = false;
	private Music mMusic = Resources.loadMusic("Winter_Overture.mp3");
	private Text gameOverTex; 
 	 
	public MainMenu() {
		// creates the main menu. Ask Decy :)
		createMenuScene();
		// Makes it so that the menu scene is overlaid on the current scene and
		// stops all updates to engine until a item is clicked
		mScene.setChildScene(mMenuScene, false, true, true);
		Resources.setMenu(this);		
	}
	
	// Ask Decy
	private void createMenuScene() {
		Font mMenuFont = Resources.loadFont("Daville Condensed Slanted.ttf");
		mMenuScene = new MenuScene(mCamera);
		mResumeScene = new MenuScene(mCamera);
		
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
		
		final IMenuItem ResumeMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_RESUME, mMenuFont, "Resume"),
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		ResumeMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		final IMenuItem Game2MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_START, mMenuFont, "Start New Game"),
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		Game1MenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		final IMenuItem Save2MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_SAVE, mMenuFont, "Save Game"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		SaveMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		final IMenuItem Load2MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_LOAD, mMenuFont, "Load Game"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		LoadMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		final IMenuItem quit2MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_QUIT, mMenuFont, "Quit"), 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		final IMenuItem options1MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_OPTIONS, mMenuFont, "Options"), 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		final IMenuItem options2MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_OPTIONS, mMenuFont, "Options"), 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		
		mMenuScene.addMenuItem(Game1MenuItem);
		mMenuScene.addMenuItem(LoadMenuItem);
		mMenuScene.addMenuItem(options1MenuItem);
		mMenuScene.addMenuItem(quitMenuItem);
		
		mResumeScene.addMenuItem(ResumeMenuItem);
		mResumeScene.addMenuItem(Game2MenuItem);
		mResumeScene.addMenuItem(Load2MenuItem);
		mResumeScene.addMenuItem(Save2MenuItem);
		mResumeScene.addMenuItem(options2MenuItem);
		mResumeScene.addMenuItem(quit2MenuItem);
		mMenuScene.buildAnimations();
		mMenuScene.setBackgroundEnabled(false);
		mMenuScene.setOnMenuItemClickListener(this);
		mResumeScene.buildAnimations();
		mResumeScene.setBackgroundEnabled(false);
		mResumeScene.setOnMenuItemClickListener(this);
		Resources.addOnKeyDownListener(this);
		
		
		
	}
	
	// THis is where the whole prgram starts and is controlled from.
	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene,
			final IMenuItem pMenuItem, final float pMenuItemLocalX,
			final float pMenuItemLocalY) {
		 mMenuScene.detachChild(gameOverTex);
		switch (pMenuItem.getID()) {
			case MENU_RESUME:
				mResumeScene.back();
				this.mMusic.getMediaPlayer().start();
				return true;
				
			case MENU_START:
				// Should be moved to goal
				//goalSound = Resources.loadSound("background.ogg");
				// Removes the Menu so you can start playing
							
				mScene.detachChild(mMenuScene);
				// Tells the menu to put every thing back
				mMenuScene.back();
				mResumeScene.back();
				if (!hasStarted)
				{
					init("final0.tmx");
				}
				else{
				mmanager.reloadMap("final0.tmx");
				Resources.getHUD().setEnergyCount(100);
				Resources.getHUD().setLifeCount(3);	
				}
				
				this.mMusic.getMediaPlayer().start();
				return true;
			case MENU_SAVE:
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				mResumeScene.back();
				// Need to implement this
				Resources.getmDBM().saveAll(
						mmanager.getCurrentMapNumber(),
						Resources.getHUD().getLifeCount(),
						Resources.getHUD().getEnergyCount());
				return true;
				
			case MENU_LOAD:
				//goalSound = Resources.loadSound("background.ogg");
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				Resources.getHUD().setEnergyCount(Resources.getmDBM().loadEnergy());
				Resources.getHUD().setLifeCount(Resources.getmDBM().loadLife());
				if (!hasStarted)
				{
					init(Resources.getmDBM().loadMap());
				}
				else{
				mmanager.reloadMap(Resources.getmDBM().loadMap());
				}
				this.mMusic.getMediaPlayer().start();
				//restore life and energy 
				//aiTest(); // A test scene Brooks is using remove it when ever.
				return true;
				
			case MENU_QUIT:
				/* End Activity. Nuke it! */
				Resources.finish();
				return true;
			default:
				return false;
		}
	}
	
	private void init(String pstring) {
		
		mmanager = new MapManager(pstring);
		Resources.setMapManager(mmanager);
		//Resources.getmScene().registerUpdateHandler( new TimerHandler(5, new AIupdate()));
		hasStarted = true;
		
		// Add the random moster so we can complete the homework.
		//Monster temp = new Monster(100, 1500, null);
		// Probably should be in Monster.
		//AIupdate.addMonster(temp);
		/*
		 * Need to get coords for these monsters temp = new Monster(100, 1500,
		 * null); temp = new Monster(100, 1500, null); temp = new Monster(100,
		 * 1500, null); temp = new Monster(100, 1500, null);
		 */
		
		/* The actual collision-checking. */
//		mScene.registerUpdateHandler(new IUpdateHandler() {
//			private boolean playing = false;
//			private int index;
//			
//			@Override
//			public void reset() {
//			}
//			
//			// When ever there is a collision this is called. Shoud optimize.
//			@Override
//			public void onUpdate(final float pSecondsElapsed) {
//				// this is random junk that makes it so that the goal sound will
//				// only play is the play is in one of the goals. I don't know
//				// why it works, only because I did not know why it wasn't
//				// working.
//				LinkedList<Rectangle> goals = Resources.getGoals();
//				if (!playing) {
//					for (index = 0; !playing && index < goals.size(); index++) {
//						// Debug.d("GOAL!");
//						if (goals.get(index).collidesWith(player)) {
//							if (!playing) {
//								playing = true;
//								goalSound.play();
//								break; // i KNOW THIS IS BAD BUT IT IS THE ONLY
//										// WAY!
//							}
//						}
//					}
//				} else if (!goals.get(index).collidesWith(player)) {
//					goalSound.stop();
//					playing = false;
//				}
//				
//				
//				if (mmanager.getMap().getExit().collidesWith(player))
//				{
//					// a test by decy to do map change! 
//					mmanager.nextMap(player);
//				}
//				
//				
//				
//			}
			//This was suppose to be a bit of text saying "You Win" but never got around to it.
//			private Scene win() {
//				
//				return mMenuScene;
//			}
//		});
		
	}
	// The temp scene Brooks is using to test his AI.
	private void aiTest() {
		
		new Borders(800, 400);
		new Player(100, 100, null);
		//Resources.getmScene().registerUpdateHandler(
		//		new TimerHandler(2, new AIupdate()));
		Monster temp = new Monster(200, 100, null);
		//AIupdate.addMonster(temp);
	}
	//When ever a key is pressed this is called. Brings up the menu when ever menu is pressed.
	@Override
	public boolean onKeyDown(int pKeyCode, KeyEvent pEvent) {
		boolean handeled = false;
		 
		switch (pKeyCode) {
			case KeyEvent.KEYCODE_MENU:
				if (pEvent.getAction() == KeyEvent.ACTION_DOWN) {
					
					if (mScene.hasChildScene()) {
						/* Remove the menu and reset it. */
						mResumeScene.back();
					} else {
						/* Attach the menu. */
						mScene.setChildScene(mResumeScene, false, true, true);
						this.mMusic.getMediaPlayer().pause();
					}
				}
				handeled = true;
				break;
		}
		
		return handeled;
	}

	public void gameOver() {
		 	 gameOverTex = new Text(300 , 70, Resources.loadFont("Zrnic.ttf"), "Game Over!");
			 gameOverTex.setColor(1f, 0f, 0f);	
			 mScene.setChildScene(Resources.getMenu().getmMenuScene(), false, true, true);
			 mMenuScene.attachChild(gameOverTex); 
			 mMusic.getMediaPlayer().pause();
	   
	}
	
}
