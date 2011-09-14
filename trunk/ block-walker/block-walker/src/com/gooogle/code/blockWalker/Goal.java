package com.gooogle.code.blockWalker;

import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Goal extends Sprite {

	private FixedStepPhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
	private Scene mScene = Resources.getmScene();
	
	
	public Goal(float pX, float pY) {
		super(pX, pY, 64, 64, Resources.loadImage("HeroOne.png", 64, 128));
		
		ContactListener contactListener = new ContactListener(){
            @Override
            public void beginContact(Contact contact) {
        		Debug.d("Player goal!");
            }
            @Override
            public void endContact(Contact contact) {
        		Debug.d("Player goal END!");
            }
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
    };
   // mPhysicsWorld.setContactListener(contactListener);

		
		
	}

}
