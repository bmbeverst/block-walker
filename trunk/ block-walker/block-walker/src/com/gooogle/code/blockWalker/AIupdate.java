package com.gooogle.code.blockWalker;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * @author brooks Sep 6, 2011
 */
public class AIupdate implements ITimerCallback {
	
	Random rand = new Random(System.nanoTime());
	private static LinkedList<Monster> monsters = new LinkedList<Monster>();
	private static LinkedList<Body> platforms = new LinkedList<Body>();
	
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
		for (Monster temp : monsters) {
			switch (rand.nextInt(3)) {
				case 0:
					temp.right();
					break;
				case 1:
					temp.left();
					break;
			}
		}
		
	}
	
	static void addMonster(Monster mon) {
		monsters.add(mon);
	}
}
