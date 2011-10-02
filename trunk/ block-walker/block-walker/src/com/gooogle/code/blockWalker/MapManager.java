package com.gooogle.code.blockWalker;

import java.util.Iterator;

import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.gooogle.code.blockWalker.AI.Boss;

/**
 * @author brooks Sep 21, 2011
 */
public class MapManager {
	
	/**
	 * 
	 */
	public static final int BOSS_X = 1000;
	
	/**
	 * 
	 */
	public static final int BOSS_Y = 620;
	
	private String currentMapName;
	
	private static TMXMap map;
	
	/**
	 * @return
	 */
	/**
	 * @return TMXMap
	 */
	public TMXMap getMap() {
		return map;
	}
	
	/**
	 * @return
	 */
	/**
	 * @return String
	 */
	public String getCurrentMapNumber() {
		return currentMapName;
	}
	
	/**
	 * @param startLocation
	 */
	public MapManager(String startLocation) {
		currentMapName = startLocation;
		map = new TMXMap(startLocation);
	}
	
	/**
	 * 
	 */
	public void nextMap() {
		
		// detach all physic bodies
		PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
		mPhysicsWorld.clearPhysicsConnectors();
		// Debug.d("!! PHYSIC  !! at the end of map " + currentMapNumber);
		
		Resources.clearMonsters();
		Iterator<Body> iter = mPhysicsWorld.getBodies();
		// final PhysicsConnector playerPhysicsConnector =
		// mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(pplayer);
		while (iter.hasNext()) {
			Body temp = iter.next();
			// if (!temp.equals(playerPhysicsConnector.getBody()))
			mPhysicsWorld.destroyBody(temp);
			// else
			// {
			// Debug.d("PLAYER PHYSIC SAVED ! at the end of map " +
			// currentMapNumber);
			// }
		}
		
		// detach all children
		Resources.getmScene().detachChildren();
		System.gc();
		
		// make string new map file name
		// works now under 10 maps.
		int i = Integer.parseInt(currentMapName.substring(5, 6));
		i++;
		currentMapName = currentMapName.substring(0, 5) + i + ".tmx";
		// create new map and map will create new player !
		map = new TMXMap(currentMapName);
		if (i == MainMenu.BOSS_LEVEL) {
			new Boss(BOSS_X, BOSS_Y);
		}
	}
	
	/**
	 * @param pMap
	 */
	public void reloadMap(String pMap) {
		Resources.getmScene().unregisterUpdateHandler(
				Resources.getmPlayer().getHandler());
		Resources.removePlayer(Resources.getmPlayer());
		PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
		mPhysicsWorld.clearPhysicsConnectors();
		Iterator<Body> iter = mPhysicsWorld.getBodies();
		while (iter.hasNext()) {
			Body temp = iter.next();
			mPhysicsWorld.destroyBody(temp);
		}
		Resources.getmScene().detachChildren();
		System.gc();
		currentMapName = pMap;
		map = new TMXMap(currentMapName);
		int i = Integer.parseInt(currentMapName.substring(5, 6));
		if (i == MainMenu.BOSS_LEVEL) {
			new Boss(BOSS_Y, BOSS_X);
		}
	}
}
