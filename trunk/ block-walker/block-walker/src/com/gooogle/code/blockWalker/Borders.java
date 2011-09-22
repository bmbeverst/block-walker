/**
 * Brooks Beverstock bmb2gf Sep 5, 2011 BaseScene.java
 */
package com.gooogle.code.blockWalker;

import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks Sep 5, 2011
 */
public class Borders {
	//add invisible borders with around the x y.
	Borders(float xSize, float ySize) {
		FixedStepPhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();
		Scene mScene = Resources.getmScene();

		final Shape bottomOuter = new Rectangle(0,
				ySize - 2, xSize, 2);
		bottomOuter.setVisible(false);
		final Shape topOuter = new Rectangle(0, 0, xSize, 2);
		topOuter.setVisible(false);
		final Shape leftOuter = new Rectangle(0, 0, 2,
				ySize);
		leftOuter.setVisible(false);
		final Shape rightOuter = new Rectangle(xSize - 2,
				0, 2, ySize);
		rightOuter.setVisible(false);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0,
				0f, 0.1f);
		PhysicsFactory.createBoxBody(mPhysicsWorld, bottomOuter,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mPhysicsWorld, topOuter,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mPhysicsWorld, leftOuter,
				BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(mPhysicsWorld, rightOuter,
				BodyType.StaticBody, wallFixtureDef);

		mScene.attachChild(bottomOuter);
		mScene.attachChild(topOuter);
		mScene.attachChild(leftOuter);
		mScene.attachChild(rightOuter);
	}
}
