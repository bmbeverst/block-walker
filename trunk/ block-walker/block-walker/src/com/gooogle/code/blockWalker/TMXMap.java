package com.gooogle.code.blockWalker;

import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObject;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObjectGroup;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTile;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gooogle.code.blockWalker.AI.AstartPathing;
import com.gooogle.code.blockWalker.AI.DumbAI;
import com.gooogle.code.blockWalker.AI.Monster;

/**
 * @author Decy Sep 5, 2011
 */
public class TMXMap {
	
	// Physics for the platforms
	private static final float ELASTICITY = 0f;
	private static final float FRICTION = 0.5f;
	private static final int TILE_WIDTH = 32;
	private static final int TILE_HEIGHT = 32;
	
	// the TMX map we load
	private TMXTiledMap mTMXTiledMap;
	// Getting needed resouces for the Resource class
	private final PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
	private final Scene mScene = Resources.getmScene();
	private final BoundCamera mCamera = Resources.getmCamera();
	private TMXLayer TMXMapLayer;
	private TMXLayer mbosslockLayer = null;
	private Body mlock = null;
	
	// ArrayList<TMXLayer> layers = new ArrayList<TMXLayer>();
	// ArrayList<Body> walls = new ArrayList<Body>();
	// ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
	
	// When a new TMX map is created we load in the map
	TMXMap(String location) {
		Resources.getHUD().setLevelText("level " + location.substring(5, 6));
		// Load the TMX map
		mTMXTiledMap = Resources.loadTMXmap(location);
		
		// Add the non-object layers to the scene. This means get all the tiles.
		// Not the Objects.
		for (int i = 0; i < mTMXTiledMap.getTMXLayers().size(); i++) {
			final TMXLayer layer = mTMXTiledMap.getTMXLayers().get(i);
			if (layer.getTMXLayerProperties().containsTMXProperty("bosslock", "true"))
			{
				mbosslockLayer  = layer;
			}
			if (!layer.getTMXLayerProperties().containsTMXProperty("wall",
					"true")) {
				mScene.attachChild(layer);
			}
		}
		// Read in the unwalkable blocks from the object layer and create boxes
		// for each. This also sets up the goals
		AstartPathing.setTMXTiledMap(mTMXTiledMap);
		Resources.getMonsters().clear();
		TMXMapLayer = mTMXTiledMap.getTMXLayers().get(0);
		createUnwalkableObjects(mTMXTiledMap);
		generateMonsters(mTMXTiledMap);
		
		mCamera.setBounds(0, TMXMapLayer.getWidth(), 0, TMXMapLayer.getHeight());
		// add borders to the world so that the player can walk off. Brooks
		// would like to do this in the map.
		new Borders(TMXMapLayer.getWidth(), TMXMapLayer.getHeight());
		mCamera.setBoundsEnabled(true);
		
	}
	
	private void createUnwalkableObjects(final TMXTiledMap map) {
		DumbAI.clearPlatform();
		// Loop through the object groups
		for (final TMXObjectGroup group : mTMXTiledMap.getTMXObjectGroups()) {
			if (group.getTMXObjectGroupProperties().containsTMXProperty("wall",
					"true")) {
				// This is our "wall" layer. Create the physical boxes from it
				for (final TMXObject object : group.getTMXObjects()) {
					// Create the rectangle
					final Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight());
					// make the body
					final FixtureDef boxFixtureDef = PhysicsFactory
							.createFixtureDef(0, ELASTICITY, FRICTION);
					// connect the body to the physics engine.
					Body tempbody = PhysicsFactory.createBoxBody(mPhysicsWorld,
							rect, BodyType.StaticBody, boxFixtureDef);
					
										
					// make it invisible
					rect.setVisible(false);
					// add it to the scene
					// walls.add(tempbody);
					// rects.add(rect);
					DumbAI.addPlatform(rect);
					mScene.attachChild(rect);
				}
			}
			if (group.getTMXObjectGroupProperties().containsTMXProperty("exit",
					"true")) {
				for (final TMXObject object : group.getTMXObjects()) {
					// Create the rectangle
					Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight());
					// make it invisible
					rect.setVisible(false);
					// add it to the scene
					mScene.attachChild(rect);
					Resources.setExit(rect);
					
				}
				
			}
			
			// only place game create player !
			if (group.getTMXObjectGroupProperties().containsTMXProperty(
					"spawn", "true")) {
				for (final TMXObject object : group.getTMXObjects()) {
					new Player(object.getX(), object.getY());
				}
				
			}
			
			if (group.getTMXObjectGroupProperties().containsTMXProperty("die",
					"true")) {
				for (final TMXObject object : group.getTMXObjects()) {
					// Create the rectangle
					Rectangle water = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight());
					// make it invisible
					water.setVisible(false);
					// add it to the scene
					mScene.attachChild(water);
					Resources.setWater(water);
					
				}
			}//end if
			
			if (group.getTMXObjectGroupProperties().containsTMXProperty("spin",
				"true")) { 
				 new Spin();			
			}//end if
			
			if (group.getTMXObjectGroupProperties().containsTMXProperty("boss", "true"))
				{

				for (final TMXObject object : group.getTMXObjects()) {
					// Create the rectangle
					Rectangle bosslock = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight());
					final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(0, ELASTICITY, FRICTION);
 					Body lock = PhysicsFactory.createBoxBody(mPhysicsWorld,	bosslock, BodyType.StaticBody, boxFixtureDef);
					bosslock.setVisible(false);
					mScene.attachChild(bosslock); 
					mlock = lock;
				}//end for
				}//end if 
		}//end for
	}//end method
	
	private void generateMonsters(final TMXTiledMap map) {
		for (final TMXObjectGroup group : mTMXTiledMap.getTMXObjectGroups()) {
			if (group.getTMXObjectGroupProperties().containsTMXProperty("monster",
					"true")) {
				// This is our "wall" layer. Create the physical boxes from it
				for (final TMXObject object : group.getTMXObjects()) {
					new Monster(object.getX(), object.getY());
				}
			}
		}
	}

	public IEntity getBossLayer() {
		return mbosslockLayer;
	}
	
	public Body getmLock(){
		return mlock;
	}
}
