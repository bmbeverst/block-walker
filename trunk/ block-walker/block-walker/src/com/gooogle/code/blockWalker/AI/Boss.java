package com.gooogle.code.blockWalker.AI;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.gooogle.code.blockWalker.Resources;

public class Boss extends AnimatedSprite {

	public Boss(float pX, float pY) {
		super(pX, pY, Resources.loadTiledTexture("boss.png", 512, 512, 7, 7));
		
	}
	
}
