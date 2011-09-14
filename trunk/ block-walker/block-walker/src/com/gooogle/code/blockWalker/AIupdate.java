package com.gooogle.code.blockWalker;

import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.util.Debug;

/**
 * @author brooks Sep 6, 2011
 */
public class AIupdate implements ITimerCallback {
	
	Random rand = new Random(System.nanoTime());
	private static LinkedList<Monster> monsters = new LinkedList<Monster>();
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		pTimerHandler.reset();
		for (Monster temp: monsters) {
			if (rand.nextInt(2) > 0 ) {
				temp.right();
			} else {
				temp.left();
			}
		}
		
	}

	static void addMonster(Monster mon) {
		monsters.add(mon);
	}
}
