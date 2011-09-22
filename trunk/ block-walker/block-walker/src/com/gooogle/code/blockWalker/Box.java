package com.gooogle.code.blockWalker;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.input.touch.TouchEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gooogle.code.blockWalker.Resources;

final class Box extends Sprite {
	
	private static final float FRICTION = 1f;
	private static final float ELASTICITY = 0f;
	private static final float MASS = 0f;
	private Body boxBody;
	
	// Creates the sprite and registers it with the scene and physicsworld
	Box(float pX, float pY, float pWidth, float pHeight) {
		super(pX, pY, pWidth, pHeight, Resources.loadTexture("box.png", 64, 32));
		
		final FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(MASS,
				ELASTICITY, FRICTION);
		PhysicsWorld pPhysicsWorld = Resources.getmPhysicsWorld();
		boxBody = PhysicsFactory.createBoxBody(pPhysicsWorld , this,
				BodyType.StaticBody, boxFixtureDef);
		
		// boxBody.setLinearDamping(10);
		// boxBody.setAngularDamping(10)
		PhysicsConnector mConnector = new PhysicsConnector(this, boxBody, true, true);
		pPhysicsWorld.registerPhysicsConnector(mConnector);
		
		Resources.getmScene().registerTouchArea(this);
		Resources.getmScene().attachChild(this);
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
	/*	this.setPosition(pSceneTouchEvent.getX() - getWidth() / 2,
				pSceneTouchEvent.getY() - getHeight() / 2);
		boxBody.setTransform(new Vector2(pSceneTouchEvent.getX()
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				pSceneTouchEvent.getY()
						/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT), 0);
						 * 
						 */
		
		return true;
	}
	
	void remove() {
		Resources.getmScene().detachChild(this);
		
	}
	
}