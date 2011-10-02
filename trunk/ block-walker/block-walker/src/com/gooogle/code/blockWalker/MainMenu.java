package com.gooogle.code.blockWalker;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.opengl.font.Font;

import com.gooogle.code.blockWalker.AI.Boss;
import com.gooogle.code.blockWalker.AI.DumbAI;

import android.view.KeyEvent;


/**
 * @author brooks
 * Oct 1, 2011
 */
public class MainMenu implements IOnMenuItemClickListener, OnKeyDownListener {
	

	/**
	 * 
	 */
	public static final int BOSS_LEVEL = 8;
	private static final String FIRSTMAP = "final8.tmx";
	
	private static final int MENU_START = 0;
	private static final int MENU_SAVE= MENU_START + 1;
	private static final int MENU_SAVE1= MENU_SAVE + 1;
	private static final int MENU_SAVE2 = MENU_SAVE1 + 1;
	private static final int MENU_LOAD = MENU_SAVE2 + 1;
	private static final int MENU_LOAD1 = MENU_LOAD + 1;
	private static final int MENU_LOAD2 = MENU_LOAD1 + 1;
	private static final int MENU_QUIT = MENU_LOAD2 + 1;
	private static final int MENU_RESUME = MENU_QUIT + 1;
 	private Camera mCamera = Resources.getmCamera();
	private MenuScene mMenuScene,mResumeScene,mSaveScene, mLoadScene;
	
	private Scene mScene = Resources.getmScene();
 	private MapManager mmanager;
	private boolean hasStarted = false;
	private Music mMusic = Resources.loadMusic("Winter_Overture.mp3");
	private Text gameOverTex; 
 	 
	/**
	 * 
	 */
	public MainMenu() {
		// creates the main menu. Ask Decy :)
		createMenuScene();
		// Makes it so that the menu scene is overlaid on the current scene and
		// stops all updates to engine until a item is clicked
		mScene.setChildScene(mMenuScene, false, true, true);
		Resources.setMenu(this);		
	}
	
 	private void createMenuScene() {
		Font mMenuFont = Resources.loadFont("Daville Condensed Slanted.ttf");
		mMenuScene = new MenuScene(mCamera);
		mResumeScene = new MenuScene(mCamera);
		mSaveScene = new MenuScene(mCamera);
		mLoadScene = new MenuScene(mCamera);
		
		// Does not do anything
		mMenuScene.setPosition(10, 10);
		final IMenuItem Game1MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_START, mMenuFont, "Start New Game"),
				1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		Game1MenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
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
		
		//resume menu ............
		
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
		Save2MenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		final IMenuItem Load2MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_LOAD, mMenuFont, "Load Game"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		Load2MenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		 		
		final IMenuItem quit2MenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_QUIT, mMenuFont, "Quit"), 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 0.0f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);  
		
		

		final IMenuItem LoadMenuItem1 = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_LOAD1, mMenuFont, "Load Game [1]"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		LoadMenuItem1.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);

		final IMenuItem LoadMenuItem2 = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_LOAD2, mMenuFont, "Load Game [2]"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		LoadMenuItem2.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		

		
		final IMenuItem Save2MenuItem1 = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_SAVE1, mMenuFont, "Save Game [1]"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		Save2MenuItem1.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		

		
		final IMenuItem Save2MenuItem2 = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_SAVE2, mMenuFont, "Save Game [2]"), 1.0f,
				0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		Save2MenuItem2.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		mMenuScene.addMenuItem(Game1MenuItem);
		mMenuScene.addMenuItem(LoadMenuItem);
 		mMenuScene.addMenuItem(quitMenuItem);
 		
 		mLoadScene.addMenuItem(LoadMenuItem1);
 		mLoadScene.addMenuItem(LoadMenuItem2);
 		
		mResumeScene.addMenuItem(ResumeMenuItem);
		mResumeScene.addMenuItem(Game2MenuItem);
		mResumeScene.addMenuItem(Save2MenuItem);
		mResumeScene.addMenuItem(Load2MenuItem);
 		mResumeScene.addMenuItem(quit2MenuItem);
 		
		mSaveScene.addMenuItem(Save2MenuItem1);
		mSaveScene.addMenuItem(Save2MenuItem2);

 
		
		mMenuScene.buildAnimations();
		mMenuScene.setBackgroundEnabled(false);
		mMenuScene.setOnMenuItemClickListener(this);
		mResumeScene.buildAnimations();
		mResumeScene.setBackgroundEnabled(false);
		mResumeScene.setOnMenuItemClickListener(this);

		mLoadScene.buildAnimations();
		mLoadScene.setBackgroundEnabled(false);
		mLoadScene.setOnMenuItemClickListener(this);

		mSaveScene.buildAnimations();
		mSaveScene.setBackgroundEnabled(false);
		mSaveScene.setOnMenuItemClickListener(this);
		
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
				mSaveScene.back();
				mLoadScene.back();
				this.getmMusic().getMediaPlayer().start();
				return true;
				
			case MENU_START:
				// Should be moved to goal
				//goalSound = Resources.loadSound("background.ogg");
				// Removes the Menu so you can start playing
							
				mScene.detachChild(mMenuScene);
				// Tells the menu to put every thing back
				mMenuScene.back();
				mResumeScene.back();
				mSaveScene.back();
				mLoadScene.back();
				if (!hasStarted)
				{
					//TODO reset to 0
					init(FIRSTMAP);
				}
				else{
				mmanager.reloadMap(FIRSTMAP);
				Resources.getHUD().setEnergyCount(100);
				Resources.getHUD().setLifeCount(3);	
				}
				
				Music find = Resources.loadMusic("find.mp3");
				find.getMediaPlayer().setLooping(false);
				find.getMediaPlayer().start();
				this.getmMusic().getMediaPlayer().start();
				return true;
				
			case MENU_SAVE:
 				mMenuScene.back();
				mResumeScene.back(); 
				mLoadScene.back();
 				mScene.setChildScene(mSaveScene, false, true, true); 
				return true;
				

			case MENU_SAVE1:
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				mResumeScene.back();
				mSaveScene.back();
				mLoadScene.back();
 				Resources.getmDBM().saveAll(1, 						
						mmanager.getCurrentMapNumber(),
						Resources.getHUD().getLifeCount(),
						Resources.getHUD().getEnergyCount());
				this.getmMusic().getMediaPlayer().start();
				return true;

				
			case MENU_SAVE2:
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				mResumeScene.back();
				mSaveScene.back();
				mLoadScene.back();
 				Resources.getmDBM().saveAll(2, 						
						mmanager.getCurrentMapNumber(),
						Resources.getHUD().getLifeCount(),
						Resources.getHUD().getEnergyCount());
				this.getmMusic().getMediaPlayer().start();
				return true;
				
			case MENU_LOAD:
				//goalSound = Resources.loadSound("background.ogg");
 				mMenuScene.back();
				mResumeScene.back();	
				mSaveScene.back(); 
				
				
			 
				mScene.setChildScene(mLoadScene, false, true, true); 
				return true;
				
			case MENU_LOAD1:
				//goalSound = Resources.loadSound("background.ogg");
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				mResumeScene.back();
				mSaveScene.back();
				mLoadScene.back();
				Resources.getHUD().setEnergyCount(Resources.getmDBM().loadEnergy(1));
				Resources.getHUD().setLifeCount(Resources.getmDBM().loadLife(1));
				if (!hasStarted)
				{
					init(Resources.getmDBM().loadMap(1));
				}
				else{
				mmanager.reloadMap(Resources.getmDBM().loadMap(1));
				}
				this.getmMusic().getMediaPlayer().start(); 
				return true;
				 
			case MENU_LOAD2:
				//goalSound = Resources.loadSound("background.ogg");
				mScene.detachChild(mMenuScene);
				mMenuScene.back();
				mResumeScene.back();
				mSaveScene.back();
				mLoadScene.back();
				Resources.getHUD().setEnergyCount(Resources.getmDBM().loadEnergy(2));
				Resources.getHUD().setLifeCount(Resources.getmDBM().loadLife(2));
				if (!hasStarted)
				{
					init(Resources.getmDBM().loadMap(2));
				}
				else{
				mmanager.reloadMap(Resources.getmDBM().loadMap(2));
				}
				this.getmMusic().getMediaPlayer().start(); 
				return true;
				
				
			case MENU_QUIT: 
				Resources.finish();
				return true;
				
				
			default:
				this.getmMusic().getMediaPlayer().start(); 
				return false;
		}
	}
	
	private void init(String pstring) {
		int i = Integer.parseInt(pstring.substring(5,6));
		mmanager = new MapManager(pstring);
		Resources.setMapManager(mmanager);
		new DumbAI();
		//Resources.getmScene().registerUpdateHandler( new TimerHandler(5, new AIupdate()));
		hasStarted = true;
		if(i == BOSS_LEVEL) {
			new Boss(1000, 500);
		}
 
		
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
						this.getmMusic().getMediaPlayer().start(); 
						mResumeScene.back();
					} else {
						/* Attach the menu. */
						mScene.setChildScene(mResumeScene, false, true, true);
						this.getmMusic().getMediaPlayer().pause();
					}
				}
				handeled = true;
				break;
				 
		}
		
		return handeled;
	}

	/**
	 * 
	 */
	public void gameOver() {
		 	 gameOverTex = new Text(300 , 70, Resources.loadFont("Zrnic.ttf"), "Game Over!");
			 gameOverTex.setColor(1f, 0f, 0f);	
			 mScene.setChildScene(mMenuScene, false, true, true);
			 mMenuScene.attachChild(gameOverTex); 
			 getmMusic().getMediaPlayer().pause();
	}
 
	/**
	 * @return mMusic
	 */
	public Music getmMusic() {
		return mMusic;
	}
	
}
