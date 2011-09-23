package com.gooogle.code.blockWalker.AI;

import java.util.LinkedList;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.util.Debug;

import com.gooogle.code.blockWalker.Monster;
import com.gooogle.code.blockWalker.Player;
import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks
 * Sep 22, 2011
 */
public class DumbAI implements ITimerCallback {
	
	float right;
	float left;
	Monster mMonster;
	private boolean running;
	private TimerHandler time;
	private static LinkedList<Rectangle> platforms =  new LinkedList<Rectangle>();
	
	
	/**
	 * @param pMonster
	 */
	public DumbAI(Monster pMonster) {
		mMonster = pMonster;
		Rectangle closest = platforms.get(0);
		float monsterX = Math.abs(mMonster.getX());
		float monsterY = Math.abs(mMonster.getY());
		float closeX = Float.MAX_VALUE;
		float closeY = Float.MAX_VALUE;
		Rectangle temp;
		float x;
		float y;
		float diffX;
		float diffY;
		for(int i = 0; i < platforms.size(); i++) {
			temp = platforms.get(i);
			x = Math.abs(temp.getX());
			y = Math.abs(temp.getY());
			x += temp.getWidth()/2;
			y += temp.getHeight()/2;
			//Debug.d(x + " " + y + "\n");
			
			diffX = Math.abs(x - monsterX);
			diffY = Math.abs(y - monsterY);
			if(diffX < closeX && diffY < closeY) {
				closeX = diffX;
				closeY = diffY;
				closest = temp;
			}
		}
		//Debug.d("closest" + closest.getX() + "  " + closest.getY());
		left =  closest.getX()-(closest.getWidth()/4);
		right =  closest.getX() + closest.getWidth() - (closest.getWidth()/4);
		Resources.getmScene().registerUpdateHandler(time = new TimerHandler(1, this));
	}
	/**
	 * @param rect
	 */
	public static void addPlatform(Rectangle rect) {
		platforms.add(rect);
	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		Player player = Resources.getmPlayer();
		Line temp = new Line(mMonster.getX(), mMonster.getY(), player.getX(), player.getY(), 16);
		boolean lineOfSight = true;
		for(int i = 0; i < platforms.size() && lineOfSight; i++) {
			if(temp.collidesWith(platforms.get(i))) {
				//Debug.d(temp.collidesWith(platforms.get(i)) + "");
				lineOfSight = false;
				break;
			}
		}
		if(lineOfSight) {
			//Debug.d("Line of sight!!!!!!!!!!!!!!!           " + (1*(mMonster.getX() - player.getX() )));
			float direction = 1*(mMonster.getX() - player.getX() );
			if(direction < 0) {
				mMonster.right();
			} else {
				mMonster.left();
			}
		} else {
			//Debug.d("Random!!!!!!!!!!!!!!            " + (right - mMonster.getX() < mMonster.getX() - left));
			//Debug.d((right - mMonster.getX() ) + " " + (mMonster.getX() - left));
			if(right - mMonster.getX() < mMonster.getX() - left) {
				mMonster.left();
			} else {
				mMonster.right();
			}
		}
		if(running) {
			pTimerHandler.reset();
		}
	}
	public void stop() {
		Resources.getmScene().unregisterUpdateHandler(time);
		running = false;
	}
}
