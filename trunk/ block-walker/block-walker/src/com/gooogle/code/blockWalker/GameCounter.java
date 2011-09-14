package com.gooogle.code.blockWalker;

import org.anddev.andengine.engine.handler.IUpdateHandler;

/**
 * A class to keep track of different counts.
 * @author brooks
 * Sep 4, 2011
 */
public class GameCounter implements IUpdateHandler {
	
	
	float jumpCoolDown = 0;
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		if (jumpCoolDown > 0) {
			jumpCoolDown -= pSecondsElapsed;
		}
	}
	
	void setJump(float amount) {
		jumpCoolDown = amount;
	}
	
	float getJump() {
		return jumpCoolDown;
	}
	
	@Override
	public void reset() {
		jumpCoolDown = 0;
	}
	
}
