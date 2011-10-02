package com.gooogle.code.blockWalker.AI;

import java.util.Random;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.util.Debug;

import com.gooogle.code.blockWalker.Player;
import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks Sep 6, 2011
 */
public class AIupdate implements ITimerCallback {

	private static final double SIGHT = 800;
	private int health = 10;
	private boolean attacked = false;

	enum States {
		RUN, ATTACK, ROAM,
	}

	boolean debug = false;
	AstartPathing finder;
	States currentStates = States.ROAM;
	private Random rand = new Random();
	private Boss boss;
	private Player player = Resources.getmPlayer();
	private TimerHandler time;

	/**
	 * @param pBoss
	 */
	public AIupdate(Boss pBoss) {
		AstartPathing.setBoss(pBoss);
		boss = pBoss;

		Resources.getmEngine()
				.registerUpdateHandler(time = new TimerHandler(1f, this));
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		int playerDistance = (int) Math.sqrt(Math.pow((player.getX() - boss.getX()), 2)
				+ (player.getY() - boss.getY()));
		pTimerHandler.reset();
		switch (currentStates) {
		case ROAM:
			AstartPathing.walkTo(boss.getX() + rand.nextInt(500) - 250, boss.getY()
					+ rand.nextInt(500) - 250);
			if(playerDistance < SIGHT) {
				if(debug)Debug.d("ATTACK!");
				currentStates = States.ATTACK;
			}
			break;
		case ATTACK:
			AstartPathing.walkTo(player.getX(), player.getY());
			if(playerDistance < 100) {
				boss.attack();
				if(playerDistance < 50) {
					Resources.getHUD().decreaesHalfEnergyCount();
				}
			}
			if(attacked) {
				if(debug)Debug.d("RUN!");
				currentStates = States.RUN;
			}
			if(playerDistance > SIGHT) {
				if(debug)Debug.d("ROAM!");
				currentStates = States.ROAM;
			}
			break;
		case RUN:
			if(playerDistance > SIGHT) {
				if(debug)Debug.d("ROAM!");
				attacked = false;
				currentStates = States.ROAM;
			}
			break;



		}
	}

	/**
	 * 
	 */
	public void attacked() {
		if(debug)Debug.d("Attacked!");
		health--;
		attacked = true;
		if(health < 0) {
			Resources.getMonsters().remove(boss);
			boss.detachChildren();
			//Resources.getmScene().detachChild(boss);
			boss.setVisible(false);
			Resources.getmEngine().unregisterUpdateHandler(time);
			Resources.detachBossLockLayer();
			System.gc();
		}
	}

}
