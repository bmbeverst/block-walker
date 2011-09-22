package com.gooogle.code.blockWalker.AI;

import java.util.LinkedList;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.util.Debug;

import com.gooogle.code.blockWalker.Monster;
import com.gooogle.code.blockWalker.Player;
import com.gooogle.code.blockWalker.Resources;

public class DumbAI implements IUpdateHandler{
	
	float right;
	float left;
	Monster mMonster;
	private static LinkedList<Rectangle> platforms =  new LinkedList<Rectangle>();
	
	
	public DumbAI(Monster pMonster) {
		mMonster = pMonster;
		Rectangle closest = platforms.get(0);
		float monsterX = mMonster.getX();
		float monsterY = mMonster.getY();
		float closeX = Float.MAX_VALUE;
		float closeY = Float.MAX_VALUE;
		Rectangle temp;
		float x;
		float y;
		float diffX;
		float diffY;
		for(int i = 0; i < platforms.size(); i++) {
			temp = platforms.get(i);
			x = temp.getX();
			y = temp.getY();
			x += temp.getWidth()/2;
			y += temp.getHeight()/2;
			diffX = x - monsterX;
			diffY = y - monsterY;
			if(diffX < closeX && diffY < closeY) {
				closeX = diffX;
				closeY = diffY;
				closest = temp;
			}
		}
		Debug.d(closest.getX() + " " + closest.getY());
		left =  closest.getX()-(closest.getWidth()/4);
		right =  closest.getX() + closest.getWidth() - (closest.getWidth()/4);
		Resources.getmScene().registerUpdateHandler(this);
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		
		Player player = Resources.getmPlayer();
		Line temp = new Line(mMonster.getX(), mMonster.getY(), player.getX(), player.getY(), 32);
		boolean lineOfSight = true;
		for(int i = 0; i < platforms.size() && lineOfSight; i++) {
			if(temp.collidesWith(platforms.get(i))) {
				lineOfSight = false;
			}
		}
		if(lineOfSight) {
			Debug.d("Line of sight!!!!!!!!!!!!!!!!!!!!!!!!");
			float direction = 1*(player.getX() - mMonster.getX());
			if(direction > 0) {
				mMonster.right();
			} else {
				mMonster.left();
			}
		} else {
			if(right - mMonster.getX() < mMonster.getX() - left) {
				mMonster.left();
			} else {
				mMonster.right();
			}
		}
		reset();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param rect
	 */
	public static void addPlatform(Rectangle rect) {
		platforms.add(rect);
	}
}
