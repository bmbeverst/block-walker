package com.gooogle.code.blockWalker;

import java.util.Iterator;

import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.physics.box2d.Body;
import com.gooogle.code.blockWalker.Resources;
import com.gooogle.code.blockWalker.TMXMap;


/**
 * @author brooks
 * Sep 21, 2011
 */
public class MapManager {
	private String currentMapNumber;

	private static TMXMap map;
	
	/**
	 * @return
	 */
	public TMXMap getMap() {
		return map;
	}

	/**
	 * @return
	 */
	public String getCurrentMapNumber() {
		return currentMapNumber;
	}
	 

	/**
	 * @param startLocation
	 */
	public MapManager (String startLocation){
		currentMapNumber = startLocation;
		map = new TMXMap(startLocation);
	}
	
	/**
	 * 
	 */
	public void nextMap(){ 
 

		//detach all physic bodies  
		PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
		mPhysicsWorld.clearPhysicsConnectors();
		//Debug.d("!! PHYSIC  !! at the end of map " + currentMapNumber);

		Resources.clearMonsters();
         Iterator<Body> iter = mPhysicsWorld.getBodies();
     //   final PhysicsConnector playerPhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(pplayer);
        while (iter.hasNext())
        {
        	Body temp = iter.next();
        	//if (!temp.equals(playerPhysicsConnector.getBody()))
        	mPhysicsWorld.destroyBody(temp);
//        	else 
//        	{
//        		Debug.d("PLAYER PHYSIC SAVED ! at the end of map " + currentMapNumber);
//        	}
        } 

        //detach all children
		Resources.getmScene().detachChildren();
        System.gc();
		
        //make string new map file name
		// works now under 10 maps. 
		int i = Integer.parseInt(currentMapNumber.substring(5,6));
		i++;
		currentMapNumber = currentMapNumber.substring(0,5) + i + ".tmx";
 		
		//create new map and map will create new player ! 
		map = new TMXMap(currentMapNumber); 
	}

	/**
	 * @param pMap
	 */
	public void reloadMap(String pMap) {
        Resources.getmScene().unregisterUpdateHandler(Resources.getmPlayer().getHandler());
		Resources.removePlayer(Resources.getmPlayer());
		PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
		mPhysicsWorld.clearPhysicsConnectors();
        Iterator<Body> iter = mPhysicsWorld.getBodies();
            while (iter.hasNext())
           {
           	Body temp = iter.next();
            	mPhysicsWorld.destroyBody(temp);
           }
		Resources.getmScene().detachChildren();
        System.gc();
        currentMapNumber=pMap;
		map = new TMXMap(currentMapNumber);
		
	}
	
	//to be called when boss is killed . 
	public void detachBossLockLayer(){
		Resources.getmScene().detachChild(map.getBossLayer());
		Resources.getmPhysicsWorld().destroyBody(  map.getmLock());
	}
 
}
