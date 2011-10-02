package com.gooogle.code.blockWalker.AI;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks Sep 8, 2011
 */
public class Monster extends AnimatedSprite implements Attackable {
	/**
	 * Brooks Beverstock bmb2gf Sep 5, 2011 Player.java
	 */
	
	private static final int JUMPV = -10;
	private static final float ELASTICITY = 0f;
	private static final int MASS = 10;
	private static final float FRICTION = 0f;
	
	final static float MONSTER_SIZE = 64;
	private static final long[] ANIMATE_DURATION = new long[] { 200, 200, 200 };
	private static final long[] ANIMATE_CHARGE = new long[] { 400, 400, 400 };
	private static final long[] ANIMATE_IDLE = new long[] { 200, 200 };
	
	private float movementSpeed = 1f;
	private float accelration = movementSpeed;
	private FixedStepPhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
	private Body monsterBody;
	
	private boolean jumping = false;
	private boolean animated = false;
	
	private float left = 0;
	private float right = 0;
	
	/**
	 * @param pX
	 * @param pY
	 */
	public Monster(float pX, float pY) {
		super(pX, pY - 20, MONSTER_SIZE, MONSTER_SIZE, Resources
				.loadTiledTexture("monsterO.png", 128, 128, 3, 4));
		
		// Debug.d(pX + ", " + pY + " Monster!!!!!!!!!!!");
		
		final FixtureDef playerFixtureDef = PhysicsFactory.createFixtureDef(
				MASS, ELASTICITY, FRICTION);
		monsterBody = PhysicsFactory.createBoxBody(mPhysicsWorld, this,
				BodyType.DynamicBody, playerFixtureDef);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				monsterBody, true, false));
		monsterBody.setLinearDamping(1);
		Resources.addMonster(this);
		idle();
		DumbAI.setPatrol(this);
		Resources.getmScene().attachChild(this);
	}
	
	public void attacked() {
		this.remove();
	}
	
	/**
	 * void
	 */
	/**
	 * void
	 */
	public void up() {
		final Vector2 velocity = Vector2Pool.obtain();
		if (!jumping) {
			jumping = true;
			velocity.add(0, JUMPV);
			velocity.add(monsterBody.getLinearVelocity());
		}
		this.animate(ANIMATE_DURATION, 9, 11, false);
		Vector2Pool.recycle(velocity);
	}
	
	/**
	 * void
	 */
	public void down() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(0, accelration);
		velocity.add(monsterBody.getLinearVelocity());
		this.animate(ANIMATE_CHARGE, 0, 2, false);
		Vector2Pool.recycle(velocity);
	}
	
	/**
	 * void
	 */
	public void left() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(-accelration, 0);
		// velocity.add(playerBody.getLinearVelocity());
		monsterBody.setLinearVelocity(velocity);
		this.animate(ANIMATE_DURATION, 3, 5, true);
		Vector2Pool.recycle(velocity);
	}
	
	/**
	 * void
	 */
	public void right() {
		final Vector2 velocity = Vector2Pool.obtain();
		velocity.set(accelration, 0);
		// velocity.add(playerBody.getLinearVelocity());
		monsterBody.setLinearVelocity(velocity);
		this.animate(ANIMATE_DURATION, 6, 8, true);
		Vector2Pool.recycle(velocity);
	}
	
	/**
	 * void
	 */
	public void remove() {
		Resources.getMonsters().remove(this);
		this.detachChildren();
		this.onDetached();
		// Resources.getmScene().detachChild(this);
		this.setVisible(false);
		mPhysicsWorld.destroyBody(monsterBody);
		System.gc();
	}
	
	/*
	 * (non-Javadoc)
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
		if (monsterBody == null) {
			if (other.monsterBody != null)
				return false;
		} else if (!monsterBody.equals(other.monsterBody))
			return false;
		return true;
	}
	
	void idle() {
		this.animate(ANIMATE_IDLE, 3, 4, true);
	}
	
	@Override
	public boolean isBoss() {
		return false;
	}
	
	/**
	 * @return the right
	 */
	public float getRight() {
		return right;
	}
	
	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(float right) {
		this.right = right;
	}
	
	/**
	 * @return the left
	 */
	public float getLeft() {
		return left;
	}
	
	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(float left) {
		this.left = left;
	}
}
