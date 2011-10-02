package com.gooogle.code.blockWalker.AI;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks
 * Oct 1, 2011
 */
public class Boss extends AnimatedSprite implements Attackable {


	protected static final long[] ANIMATE_DURATION = new long[] { 200, 200, 200, 200, 200, 200 };
	protected static final long[] ANIMATE_DURATION2 = new long[] { 200, 200, 200, 200, 200};
	private boolean flipped;
	private AIupdate AI;
	private static TiledTextureRegion mBossTiledRegion;
	
	/**
	 * @param pX
	 * @param pY
	 */
	public Boss(float pX, float pY) {
		super(pX, pY, mBossTiledRegion = Resources.loadTiledTexture("boss.png", 512, 512, 6, 4));
		AI =  new AIupdate(this);
		Resources.addMonster(this);
		Resources.getmScene().attachChild(this);
	}
	//Animation values
	//12-16 5
	//05-09 5
	//00-05 6
	//18-23 6
	//06-11 6
	/**
	 * 
	 */
	public void up() {
		animate(ANIMATE_DURATION, 0, 5, true);
	}
	/**
	 * 
	 */
	public void down() {
		animate(ANIMATE_DURATION, 0, 5, true);
	}
	/**
	 * 
	 */
	public void left() {
		if (!flipped) {
			flipped = true;
			mBossTiledRegion.setFlippedHorizontal(true);
		}
		this.animate(ANIMATE_DURATION2, 5, 9, true);
		
		//animate(ANIMATE_DURATION, 18, 23, true);
	}
	/**
	 * 
	 */
	public void right() {
		if (flipped) {
			flipped = false;
			mBossTiledRegion.setFlippedHorizontal(false);
		}
		animate(ANIMATE_DURATION2, 5, 9, true);
		//animate(ANIMATE_DURATION, 6, 11, true);
	}
	void idle() {
		animate(ANIMATE_DURATION, 18, 23, true);
	}
	/**
	 * 
	 */
	public void attack() {
		animate(ANIMATE_DURATION2, 12, 16, true);
	}
	@Override
	public void attacked() {
		AI.attacked();
	}
}