package com.gooogle.code.blockWalker;

import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXLayer;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObject;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXObjectGroup;
import org.anddev.andengine.entity.layer.tiled.tmx.TMXTiledMap;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * @author Decy Sep 5, 2011
 */
public class TMXMap {

	private static final float ELASTICITY = 0f;
	private static final float FRICTION = 0.5f;

	private TMXTiledMap mTMXTiledMap;
	private final PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
	private final Scene mScene = Resources.getmScene();
	private final BoundCamera mCamera = Resources.getmCamera();
	private Rectangle rect;

	TMXMap(String location) {

		// Load the TMX map

		mTMXTiledMap = Resources.loadTMXmap(location);

		// Add the non-object layers to the scene
		for (int i = 0; i < mTMXTiledMap.getTMXLayers().size(); i++) {
			final TMXLayer layer = mTMXTiledMap.getTMXLayers().get(i);
			if (!layer.getTMXLayerProperties().containsTMXProperty("wall",
					"true")) {
				mScene.attachChild(layer);
			}
		}

		// Read in the unwalkable blocks from the object layer and create boxes
		// for each
		createUnwalkableObjects(mTMXTiledMap);

		// Make the camera not exceed the bounds of the TMXEntity.
		final TMXLayer tmxLayer = mTMXTiledMap.getTMXLayers().get(0);
		mCamera.setBounds(0, tmxLayer.getWidth(), 0, tmxLayer.getHeight());
		new Borders(tmxLayer.getWidth(), tmxLayer.getHeight());
		mCamera.setBoundsEnabled(true);
		// Add outer walls
		// new Borders(tmxLayer.getWidth(), tmxLayer.getHeight());
	}

	private void createUnwalkableObjects(final TMXTiledMap map) {
		// Loop through the object groups
		for (final TMXObjectGroup group : mTMXTiledMap.getTMXObjectGroups()) {
			if (group.getTMXObjectGroupProperties().containsTMXProperty("wall",
					"true")) {
				// This is our "wall" layer. Create the boxes from it
				for (final TMXObject object : group.getTMXObjects()) {
					final Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight());
					final FixtureDef boxFixtureDef = PhysicsFactory
							.createFixtureDef(0, ELASTICITY, FRICTION);
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect,
							BodyType.StaticBody, boxFixtureDef);
					rect.setVisible(false);
					mScene.attachChild(rect);
				}
			}
			if (group.getTMXObjectGroupProperties().containsTMXProperty("goal",
					"true")) {
				// This is our "wall" layer. Create the boxes from it
				for (final TMXObject object : group.getTMXObjects()) {
					Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight());
					rect.setVisible(false);
					mScene.attachChild(rect);
					Resources.addGoal(rect);
				}
			}
		}
	}
}
