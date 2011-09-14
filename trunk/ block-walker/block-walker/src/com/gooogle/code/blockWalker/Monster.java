package com.gooogle.code.blockWalker;

	import org.anddev.andengine.engine.camera.BoundCamera;
	import org.anddev.andengine.entity.Entity;
	import org.anddev.andengine.entity.scene.Scene;
	import org.anddev.andengine.entity.sprite.AnimatedSprite;
	import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
	import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
	import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
	import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
	import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

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
 * @author brooks
 * Sep 8, 2011
 */
public class Monster extends AnimatedSprite{
	/**
	 * Brooks Beverstock bmb2gf Sep 5, 2011 Player.java
	 */
	

		private static final int JUMPV = -10;
		private static final float ELASTICITY = 0f;
		private static final int MASS = 30;
		private static final float FRICTION = 0f;
		
		private final static float PLAYER_SIZE = 64;
		private static final long[] ANIMATE_DURATION = new long[]{200, 200, 200};
		private static final long[] ANIMATE_CHARGE = new long[]{400, 400, 400};
		private static final long[] ANIMATE_IDLE = new long[]{200, 200};
		
		private float movementSpeed = 4f;
		private float accelration = movementSpeed;
		private FixedStepPhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
		private Scene mScene = Resources.getmScene();
		private Body playerBody;

		private boolean jumping = false;
		private boolean animated = false;

		private static TiledTextureRegion mPlayerTiledRegion;
		
		/**
		 * @param pX
		 * @param pY
		 * @param pTiledTextureRegion pass null
		 */
		public Monster(float pX, float pY,
				TiledTextureRegion pTiledTextureRegion) {
			super(pX, pY, PLAYER_SIZE, PLAYER_SIZE, mPlayerTiledRegion = Resources.loadTiledImage("monsterO.png", 128, 128, 3, 4));
			
			
			final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(MASS,
					ELASTICITY, FRICTION);
			playerBody = PhysicsFactory.createBoxBody(mPhysicsWorld, this,
					BodyType.DynamicBody, playerFixtureDef);
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
					playerBody, true, false));
			idle();
			mScene.attachChild(this);
		}

		void up() {
			final Vector2 velocity = Vector2Pool.obtain();
			if(!jumping){
	            jumping = true;
	            velocity.add(0, JUMPV);
	            velocity.add(playerBody.getLinearVelocity());
			}
			this.animate(ANIMATE_DURATION, 9 ,11, false);
			Vector2Pool.recycle(velocity);
		}
		void down() {
			final Vector2 velocity = Vector2Pool.obtain();
			velocity.set(0, accelration);
            velocity.add(playerBody.getLinearVelocity());
			this.animate(ANIMATE_CHARGE,  0, 2, false);
			Vector2Pool.recycle(velocity);
		}
		void left() {
			final Vector2 velocity = Vector2Pool.obtain();
			velocity.set(-accelration, 0);
            velocity.add(playerBody.getLinearVelocity());
			this.animate(ANIMATE_DURATION, 3 , 5, false);
			Vector2Pool.recycle(velocity);
		}
		void right() {
			final Vector2 velocity = Vector2Pool.obtain();
			velocity.set(accelration, 0);
            velocity.add(playerBody.getLinearVelocity());
			this.animate(ANIMATE_DURATION, 6 , 8, false);
			Vector2Pool.recycle(velocity);
		}
		void remove() {
			mScene.detachChild(this);
		}
		void idle() {
			this.animate(ANIMATE_IDLE, 3 , 4, true);
		}
}
