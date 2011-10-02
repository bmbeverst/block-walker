/**
 * Brooks Beverstock bmb2gf Sep 5, 2011 Player.java
 */
package com.gooogle.code.blockWalker;

// Turn into a siglton test feild if not null new player else attach.

import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.particle.ParticleSystem;
import org.anddev.andengine.entity.particle.emitter.PointParticleEmitter;
import org.anddev.andengine.entity.particle.initializer.AccelerationInitializer;
import org.anddev.andengine.entity.particle.initializer.ColorInitializer;
import org.anddev.andengine.entity.particle.initializer.RotationInitializer;
import org.anddev.andengine.entity.particle.initializer.VelocityInitializer;
import org.anddev.andengine.entity.particle.modifier.AlphaModifier;
import org.anddev.andengine.entity.particle.modifier.ColorModifier;
import org.anddev.andengine.entity.particle.modifier.ExpireModifier;
import org.anddev.andengine.entity.particle.modifier.ScaleModifier;
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
import com.gooogle.code.blockWalker.AI.Monster;

/**
 * @author brooks Sep 5, 2011
 */
public class Player extends AnimatedSprite implements OnKeyDownListener,
		OnKeyUpListener, IUpdateHandler {
	
	private static final float JUMPV = -20f;
	private static final float ELASTICITY = 0f;
	private static final float MASS = 1f;
	private static final float FRICTION = 0f;
	private Sound mExplosionSound = Resources.loadSound("punch1.wav");
	
	public final static float PLAYER_SIZE = 64;
	private static final long[] ANIMATE_DURATION = new long[] { 200, 200, 200,
			200 };
	private static final long[] ANIMATE_CHANRGE = new long[] { 400, 400, 400,
			400 };
	private static final long[] ANIMATE_IDLE = new long[] { 200, 200 };
	private static final float RATE_MIN = 0;
	private static final float RATE_MAX = 0;
	private static final int PARTICLES_MAX = 0;;
	
	private float movementSpeed = 4f;
	private float accelration = movementSpeed / 3;
	private FixedStepPhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
	private Scene mScene = Resources.getmScene();
	private Body playerBody;
	private boolean jumping = false;
	private boolean flipped = false;
	private boolean moving = true;
	private Particels part;
	private boolean attacking;
	private boolean charging; 

	private static TiledTextureRegion mPlayerTiledRegion;
	
	public Player(float pX, float pY) {
		super(pX, pY, PLAYER_SIZE, PLAYER_SIZE,
				mPlayerTiledRegion = Resources
						.loadTiledTexture("Character.png", 128, 128, 4, 4));
		
		
		part = new Particels(this);
		ContactListener contactListener = new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				float contactA = contact.getFixtureA().getBody().getPosition().y;
				float contactB = contact.getFixtureB().getBody().getPosition().y;
				if (contact.getFixtureA().getBody().getType() == BodyType.StaticBody) {
					if (contactA > contactB) {
						jumping = false;
						part.landed();
					}
				} else if (contact.getFixtureB().getBody().getType() == BodyType.StaticBody) {
					if (contactA < contactB) {
						jumping = false;
						part.landed();
					}
				}
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
			velocity.x = movementSpeed * 1.5f;
		}
		else if (velocity.y >= movementSpeed) {
			velocity.y = movementSpeed;
		}
		else if (velocity.x <= -movementSpeed) {
			velocity.x = -movementSpeed * 1.5f;
		}
		else if (velocity.y <= -movementSpeed) {// -2.5f
			velocity.y = -movementSpeed * 2.5f;
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
		if(!charging) {
			charging = true;
			this.animate(ANIMATE_CHANRGE, 8, 11, true);
		}
		Vector2Pool.recycle(velocity);
		Debug.d(this.toString()); 
		Resources.getHUD().increaseHalfEnergy();

	}
	
	void left() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(-accelration, 0);
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
		velocity.x = 0;
	}
	
	void right() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(accelration, 0);
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
		velocity.x = 0;
	}
	
	void remove() {
		mScene.detachChild(this);
		Resources.removePlayer(this);
	}
	
	private void attack() {
		this.animate(ANIMATE_DURATION, 4, 7, false);
		if (!attacking) {
			attacking = true;
			Player.this.mExplosionSound.play();
		}
		LinkedList<Monster> monsterList = Resources.getMonsters();
		for (int i = 0; i < monsterList.size(); i++) {
			// the number here will set player attack range !
			Rectangle monsterRec = new Rectangle(
					monsterList.get(i).getX() - 20, monsterList.get(i).getY(),
					100, 100);
			if (Resources.getmPlayer().collidesWith(monsterRec)) {
				monsterList.get(i).attacked();
				return;
			}// end if
		}// end for
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
	
	@Override
	public boolean onKeyUp(int pKeyCode, KeyEvent pEvent) {
		switch (pKeyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
				//checkSpeed(Vector2Pool.obtain(0, 5));
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				attacking = false;
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				charging = false;
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
	private IUpdateHandler handler;
	
	public void updatePlayer() {
		mScene.registerUpdateHandler(handler = new IUpdateHandler() {
			
			@Override
			public void reset() {
			}
			
			@Override
			public void onUpdate(final float pSecondsElapsed) {
				if (Resources.getExit().collidesWith(Resources.getmPlayer())) {
					mScene.unregisterUpdateHandler(this);
					Resources.removePlayer(Resources.getmPlayer());
					(Resources.getMapManger()).nextMap();
				}// end if
				
				if (Resources.getWater().collidesWith(Resources.getmPlayer())) {
					Resources.getmPlayer().rePosition();
					if (!Resources.getHUD().decreaseLife()) {
						Resources.getMenu().gameOver();
						
					}
				}// end if
				LinkedList<Monster> monsterList = Resources.getMonsters();
				for (int i = 0; i < monsterList.size(); i++) {
					Rectangle monsterRec = new Rectangle(monsterList.get(i)
							.getX() - 5, monsterList.get(i).getY(), 50, 50);
					if (Resources.getmPlayer().collidesWith(monsterRec)) {
						Resources.getHUD().decreaesHalfEnergyCount();
					}// end if
				}// end for
			}// end inner class method
		});
		
	}
	
	//
	// protected boolean isHitting(Monster pmonster) {
	// double distance = Math.sqrt((pmonster.getX() -
	// this.getX())*(pmonster.getX() - this.getX())
	// + (pmonster.getY() - this.getX()) * (pmonster.getY() - this.getX()));
	// Debug.d("DEBUG !!!! monster distance!!!" + distance);
	// double minDis = 540.0;
	//
	// if (distance > minDis){
	// return false;
	// }
	// else return true;
	// }
	
	public IUpdateHandler getHandler() {
		return handler;
	}
	
}