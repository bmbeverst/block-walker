package com.gooogle.code.blockWalker.AI;

import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks Sep 6, 2011
 */
public class AIupdate implements ITimerCallback {

	int health = 10;
	boolean attacked = false;

	enum States {
		RUN, ATTACK, ROAM,
	}

	AstartPathing finder;
	States currentStates = States.ROAM;
	private Random rand = new Random();
	private Boss boss;

	public AIupdate() {
		AstartPathing.setBoss(boss = new Boss(50, 50));

		Resources.getmEngine()
				.registerUpdateHandler(new TimerHandler(5f, this));
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		pTimerHandler.reset();
		switch (currentStates) {
		case ROAM:
			AstartPathing.walkTo(boss.getX() + rand.nextInt(1000), boss.getY()
					+ rand.nextInt(1000), Resources.getmScene());
			break;
		case ATTACK:
			
			break;
		case RUN:
			
			break;



		}
	}

}
