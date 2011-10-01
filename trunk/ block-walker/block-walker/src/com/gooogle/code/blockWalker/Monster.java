package com.gooogle.code.blockWalker;

	import org.anddev.andengine.entity.scene.Scene;
	import org.anddev.andengine.entity.sprite.AnimatedSprite;
	import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
	import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
	import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
	import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.util.Debug;

	import com.badlogic.gdx.math.Vector2;
	import com.badlogic.gdx.physics.box2d.Body;
	import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gooogle.code.blockWalker.Resources;
import com.gooogle.code.blockWalker.AI.DumbAI;

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
		private static final int MASS = 10;
		private static final float FRICTION = 0f;
		
		private final static float PLAYER_SIZE = 64;
		private static final long[] ANIMATE_DURATION = new long[]{200, 200, 200};
		private static final long[] ANIMATE_CHARGE = new long[]{400, 400, 400};
		private static final long[] ANIMATE_IDLE = new long[]{200, 200};
		
		private float movementSpeed = 1f;
		private float accelration = movementSpeed;
		private FixedStepPhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
		private Scene mScene = Resources.getmScene();
		private Body playerBody;

		private boolean jumping = false;
		private boolean animated = false;
		private DumbAI ai;
		
		/**
		 * @param pX
		 * @param pY
		 */
		public Monster(float pX, float pY) {
			super(pX, pY - 20, PLAYER_SIZE, PLAYER_SIZE, Resources.loadTiledTexture("monsterO.png", 128, 128, 3, 4));

			//Debug.d(pX + ", " + pY + " Monster!!!!!!!!!!!");
			
			final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(MASS,
					ELASTICITY, FRICTION);
			playerBody = PhysicsFactory.createBoxBody(mPhysicsWorld, this,
					BodyType.DynamicBody, playerFixtureDef);
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
					playerBody, true, false));
			playerBody.setLinearDamping(1);
			Resources.addMonster(this);
			ai = new DumbAI(this);
			idle();
			mScene.attachChild(this);
		}

		public void up() {
			final Vector2 velocity = Vector2Pool.obtain();
			if(!jumping){
	            jumping = true;
	            velocity.add(0, JUMPV);
	            velocity.add(playerBody.getLinearVelocity());
			}
			this.animate(ANIMATE_DURATION, 9 ,11, false);
			Vector2Pool.recycle(velocity);
		}
		public void down() {
			final Vector2 velocity = Vector2Pool.obtain();
			velocity.set(0, accelration);
            velocity.add(playerBody.getLinearVelocity());
			this.animate(ANIMATE_CHARGE,  0, 2, false);
			Vector2Pool.recycle(velocity);
		}
		public void left() {
			final Vector2 velocity = Vector2Pool.obtain();
			velocity.set(-accelration, 0);
            //velocity.add(playerBody.getLinearVelocity());
            playerBody.setLinearVelocity(velocity);
			this.animate(ANIMATE_DURATION, 3 , 5, true);
			Vector2Pool.recycle(velocity);
		}
		public void right() {
			final Vector2 velocity = Vector2Pool.obtain();
			velocity.set(accelration, 0);
           // velocity.add(playerBody.getLinearVelocity());
            playerBody.setLinearVelocity(velocity);
			this.animate(ANIMATE_DURATION, 6 , 8, true);
			Vector2Pool.recycle(velocity);
		}
		void remove() {
			Resources.getMonsters().remove(this);
			this.detachChildren();
			this.onDetached();
			//Resources.getmScene().detachChild(this);
			this.setVisible(false);
			mPhysicsWorld.destroyBody(playerBody);
			ai.stop();
			System.gc();
  		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Monster other = (Monster) obj;
			if (Float.floatToIntBits(accelration) != Float
					.floatToIntBits(other.accelration))
				return false;
			if (animated != other.animated)
				return false;
			if (jumping != other.jumping)
				return false;
			if (Float.floatToIntBits(movementSpeed) != Float
					.floatToIntBits(other.movementSpeed))
				return false;
			if (playerBody == null) {
				if (other.playerBody != null)
					return false;
			} else if (!playerBody.equals(other.playerBody))
				return false;
			return true;
		}

		void idle() {
			this.animate(ANIMATE_IDLE, 3 , 4, true);
		}
}
