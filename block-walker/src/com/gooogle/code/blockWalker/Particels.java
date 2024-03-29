package com.gooogle.code.blockWalker;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
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

/**
 * @author brooks Sep 22, 2011
 */
public class Particels implements ITimerCallback {
	
	private static final float RATE_MIN = 8;
	private static final float RATE_MAX = 12;
	private static final int PARTICLES_MAX = 200;
	private boolean landed;
	Player mPlayer;
	final ParticleSystem particleSystem;
	private PointParticleEmitter particleEmiter;
	
	Particels(Player player) {
		mPlayer = player;
		particleSystem = new ParticleSystem(
				particleEmiter = new PointParticleEmitter(mPlayer.getX()
						+ Player.PLAYER_SIZE / 2, mPlayer.getY()
						+ Player.PLAYER_SIZE), RATE_MIN, RATE_MAX,
				PARTICLES_MAX, Resources.loadTexture("part.png", 16, 16));
		particleSystem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		
		particleSystem.addParticleInitializer(new VelocityInitializer(-30, 30,
				-1, -20));
		particleSystem.addParticleInitializer(new AccelerationInitializer(-5,
				-11));
		particleSystem.addParticleInitializer(new RotationInitializer(0.0f,
				360.0f));
		particleSystem.addParticleInitializer(new ColorInitializer(1.0f, 0.5f,
				0.5f));
		
		particleSystem.addParticleModifier(new ScaleModifier(2f, .5f, 0, 5));
		particleSystem.addParticleModifier(new ExpireModifier(1.5f));
		particleSystem.addParticleModifier(new ColorModifier(1.0f, 2.5f, 1.0f,
				1.5f, 0.0f, 1.0f, 2.5f, 5.5f));
		particleSystem.addParticleModifier(new AlphaModifier(1.0f, 0.0f, 2.5f,
				6.5f));
		Resources.getmScene().attachChild(particleSystem);
		
		Resources.getmEngine().registerUpdateHandler(
				new TimerHandler(0.1f, this));
		
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		if (landed) {
			particleEmiter.setCenter(mPlayer.getX() + Player.PLAYER_SIZE / 2,
					mPlayer.getY() + Player.PLAYER_SIZE);
			particleSystem.setParticlesSpawnEnabled(true);
			landed = false;
		} else {
			particleSystem.setParticlesSpawnEnabled(false);
		}
		pTimerHandler.reset();
	}
	
	void landed() {
		landed = true;
	}
}
