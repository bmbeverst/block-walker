package com.gooogle.code.blockWalker.AI;

import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;

import com.gooogle.code.blockWalker.Player;
import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks Sep 6, 2011
 */
public class AIupdate implements ITimerCallback {

	private static final double SIGHT = 1000;
	private int health = 10;
	private boolean attacked = true;

	enum States {
		RUN, ATTACK, ROAM,
	}

	AstartPathing finder;
	States currentStates = States.ROAM;
	private Random rand = new Random();
	private Boss boss;
	private Player player = Resources.getmPlayer();

	public AIupdate(float pX, float pY) {
		AstartPathing.setBoss(boss = new Boss(pX, pY));

		Resources.getmEngine()
				.registerUpdateHandler(new TimerHandler(1f, this));
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		pTimerHandler.reset();
		switch (currentStates) {
		case ROAM:
			AstartPathing.walkTo(boss.getX() + rand.nextInt(500) - 250, boss.getY()
					+ rand.nextInt(500) - 250, Resources.getmScene());
			if(Math.sqrt(Math.pow((player.getX() - boss.getX()), 2)
					+ (player.getY() - boss.getY())) < SIGHT) {
				currentStates = States.ATTACK;
			}
			break;
		case ATTACK:
			boss.attack();
			if(attacked) {
				currentStates = States.RUN;
			}
			break;
		case RUN:
			boss.attack();
			if(attacked) {
				currentStates = States.ROAM;
			}
			break;



		}
	}

}
