/**
 * Brooks Beverstock bmb2gf Sep 5, 2011 Player.java
 */
package com.gooogle.code.blockWalker;


//Turn into a siglton test feild if not null new player else attach.

import java.util.LinkedList;

import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;

import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * @author brooks Sep 5, 2011
 */
public class Player extends AnimatedSprite implements OnKeyDownListener,
		OnKeyUpListener, IUpdateHandler {

	private static final float JUMPV = -10f;
	private static final float ELASTICITY = 0f;
	private static final float MASS = 1f;
	private static final float FRICTION = 0f;

	private final static float PLAYER_SIZE = 64;
	private static final long[] ANIMATE_DURATION = new long[] { 200, 200, 200,
			200 };
	private static final long[] ANIMATE_CHANRGE = new long[] { 400, 400, 400,
			400 };
	private static final long[] ANIMATE_IDLE = new long[] { 200, 200 };;

	private float movementSpeed = 4f;
	private float accelration = movementSpeed / 3;
	private FixedStepPhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
	private Scene mScene = Resources.getmScene();
	private Body playerBody;

	private boolean jumping = false;
	private boolean flipped = false;
	private boolean moving = true;
	private float mPX;
	private float mPY;

	private static TiledTextureRegion mPlayerTiledRegion;

	public Player(float pX, float pY, TiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, PLAYER_SIZE, PLAYER_SIZE,
				pTiledTextureRegion = mPlayerTiledRegion = Resources
						.loadTiledTexture("Character.png", 128, 128, 4, 4));
		mPX =pX;
		mPY =pY;
		ContactListener contactListener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				jumping = false;
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}
		};
		mPhysicsWorld.setContactListener(contactListener);

		final BoundCamera mCamera = Resources.getmCamera();

		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(
				MASS, ELASTICITY, FRICTION);
		playerBody = PhysicsFactory.createBoxBody(mPhysicsWorld, this,
				BodyType.DynamicBody, playerFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				playerBody, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				mCamera.updateChaseEntity();
			}
		});

		playerBody.setLinearDamping(1);
		// playerBody.setAngularDamping(10);
		mCamera.setChaseEntity(this);
		mScene.attachChild(this);
		Resources.addOnKeyDownListener(this);
		Resources.addOnKeyUpListener(this);
		Resources.setmPlayer(this);
		updatePlayer();
	}

	private void checkSpeed(Vector2 velocity) {

		final Vector2 temp = playerBody.getLinearVelocity();
		velocity.add(temp);
		if (velocity.x >= movementSpeed) {
			velocity.x = movementSpeed;
		}
		if (velocity.y >= movementSpeed) {
			velocity.y = movementSpeed;
		}
		if (velocity.x <= -movementSpeed) {
			velocity.x = -movementSpeed;
		}
		if (velocity.y <= -movementSpeed) {// -2.5f
			velocity.y = -movementSpeed;
		}
		playerBody.setLinearVelocity(velocity);
	}

	void up() {
		final Vector2 velocity = Vector2Pool.obtain();
		if (!jumping) {
			jumping = true;
			velocity.add(0, JUMPV);
			checkSpeed(velocity);
			this.animate(ANIMATE_DURATION, 12, 15, false);
		}
 
		Vector2Pool.recycle(velocity);
	}

	void down() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(0, accelration);
		checkSpeed(velocity);
		this.animate(ANIMATE_CHANRGE, 8, 11, false);
 
		Vector2Pool.recycle(velocity);
		Debug.d(this.toString());
	}

	void left() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(-accelration, 1);
		checkSpeed(velocity);
		if (!flipped) {
			flipped = true;
			mPlayerTiledRegion.setFlippedHorizontal(true);
		}
		if (moving) {
			moving = false;
			this.animate(ANIMATE_DURATION, 0, 3, true);
		}
 
		Vector2Pool.recycle(velocity);
	}

	void right() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(accelration, 1);
		checkSpeed(velocity);
		if (flipped) {
			flipped = false;
			mPlayerTiledRegion.setFlippedHorizontal(false);
		}
		if (moving) {
			moving = false;
			this.animate(ANIMATE_DURATION, 0, 3, true);
		}
 
		Vector2Pool.recycle(velocity);
	}

	void remove() {
		mScene.detachChild(this);
		Resources.removePlayer(this);
	}

	@Override
	public boolean onKeyDown(int pKeyCode, KeyEvent pEvent) {
		boolean handeled = false;

		switch (pKeyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			up();
			handeled = true;
			break;
		case KeyEvent.KEYCODE_SPACE:
			up();
			handeled = true;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			down();
			handeled = true;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			left();
			handeled = true;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			right();
			handeled = true;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			attack();
			handeled = true;
			break;
		}
		return handeled;
	} 

	private void attack() {
		this.animate(ANIMATE_DURATION, 4, 7, false);
	}

	@Override
	public boolean onKeyUp(int pKeyCode, KeyEvent pEvent) {
		switch (pKeyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			checkSpeed(Vector2Pool.obtain(0, 5));
			break;
		}
		if (!moving) {
			moving = true;
			this.animate(ANIMATE_IDLE, 0, 1, false);
 		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Player [jumping=" + jumping + ", flipped=" + flipped
				+ ", moving=" + moving + ", mX=" + mX + ", mY=" + mY + "]";
	}

	public void rePosition() { 
		playerBody.setTransform(5, 20, 0.0f);
 	}

	// When ever there is a collision this is called. optimize.
 	public void updatePlayer() {
 		mScene.registerUpdateHandler(new IUpdateHandler() {
		 	
			@Override
			public void reset() {
			}
			
 			@Override
			public void onUpdate(final float pSecondsElapsed) {
 			if (Resources.getExit().collidesWith(Resources.getmPlayer())) {
			mScene.unregisterUpdateHandler(this);
			Resources.removePlayer(Resources.getmPlayer());
			(Resources.getMapManger()).nextMap();
		}
 			
 			if (Resources.getWater().collidesWith(Resources.getmPlayer())) {
 				Resources.getmPlayer().rePosition();
 				if(!Resources.getHUD().decreaseLife()){
 					//game is over 
 					//need to implement how ! 					
 				
 				}
   			}
 			

	}
 		});
 	}

}