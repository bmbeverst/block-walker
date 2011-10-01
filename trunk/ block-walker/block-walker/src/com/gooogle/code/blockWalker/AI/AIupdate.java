package com.gooogle.code.blockWalker.AI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.IEntity;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gooogle.code.blockWalker.Monster;
import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks Sep 6, 2011
 */
public class AIupdate implements ITimerCallback {
	
	int health = 10;
	boolean attacked = false;
	enum States{
		RUN,
		ATTACK,
		ROAM,	
	}
	
	States currentStates = States.ROAM;
	
	
	AIupdate() {
		switch(currentStates) {
			case ROAM:
				
			break;
		
		
		
		}
		
		
		Resources.getmEngine().registerUpdateHandler(
				new TimerHandler(0.5f, this));
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		pTimerHandler.reset();
		//update
	}
	
}
