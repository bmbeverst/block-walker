package com.gooogle.code.blockWalker.AI;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.BaseSprite;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.gooogle.code.blockWalker.Resources;

public class Boss extends AnimatedSprite {

	

	protected static final long[] ANIMATE_DURATION = new long[] { 200, 200, 200, 200, 200, 200 };
	protected static final long[] ANIMATE_DURATION2 = new long[] { 200, 200, 200, 200, 200};
	private boolean flipped;
	private boolean moving;
	private static TiledTextureRegion mBossTiledRegion;
	
	public Boss(float pX, float pY) {
		super(pX, pY, mBossTiledRegion = Resources.loadTiledTexture("boss.png", 512, 512, 6, 4));
		Resources.getmScene().attachChild(this);
	}
	//Animation values
	//12-16 5
	//05-09 5
	//00-05 6
	//18-23 6
	//06-11 6
	public void up() {
		animate(ANIMATE_DURATION, 0, 5, true);
	}
	public void down() {
		animate(ANIMATE_DURATION, 0, 5, true);
	}
	public void left() {
		if (!flipped) {
			flipped = true;
			mBossTiledRegion.setFlippedHorizontal(true);
		}
		this.animate(ANIMATE_DURATION2, 5, 9, true);
		
		//animate(ANIMATE_DURATION, 18, 23, true);
	}
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
	public void attack() {
		animate(ANIMATE_DURATION2, 12, 16, true);
	}
}