package com.gooogle.code.blockWalker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.IEntity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * @author brooks Sep 6, 2011
 */
public class AIupdate implements ITimerCallback {
	
	Random rand = new Random(System.nanoTime());
	private static LinkedList<Body> platforms = new LinkedList<Body>();
	private static HashMap<IEntity, Monster> brain = new HashMap<IEntity, Monster>();
	
	
	AIupdate() {
		Iterator<Body> temp1 = Resources.getmPhysicsWorld().getBodies();
		while (temp1.hasNext()) {
			Body temp2 = temp1.next();
			if (temp2.getType() == BodyDef.BodyType.StaticBody) {
				platforms.add(temp2);
			}
		}
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		pTimerHandler.reset();
		//update
	}
	
	static void addMonster(Monster mon) {
		brain.put(null, mon);
	}
	
	
}
