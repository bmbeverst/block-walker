package com.gooogle.code.blockWalker;

import org.anddev.andengine.entity.scene.Scene;

import android.view.KeyEvent;

/**
 * @author brooks
 * Sep 7, 2011
 */
public class MainLoop {
	
	private MainMenu mMainMenu;
	private Scene mScene = Resources.getmScene();
	private Scene mMenuScene;
	
	
	MainLoop() {
		mMainMenu = new MainMenu();
	}
}
