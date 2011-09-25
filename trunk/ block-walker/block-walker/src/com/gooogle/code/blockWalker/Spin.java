package com.gooogle.code.blockWalker;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

public class Spin {
 
 		// ===========================================================
		// Fields
		// ===========================================================
 
		private PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld(); 
 		private TextureRegion mBoxFaceTextureRegion;
		private TextureRegion mCircleFaceTextureRegion;
		private final int CAMERA_HEIGHT = 480;
		private final int CAMERA_WIDTH = 800;

 
		// ===========================================================
		// Constructors
		// =========================================================== 

		public Spin(){
			Debug.d("new joint created!!");
			mBoxFaceTextureRegion = Resources.loadTexture("box.png", 64, 32);
			mCircleFaceTextureRegion = Resources.loadTexture("rock.png", 32, 32);
			initJoints(Resources.getmScene());
		}
        // ===========================================================
        // Methods
        // ===========================================================

        private void initJoints(final Scene pScene) {
                final int centerX = CAMERA_WIDTH ;
                final int centerY = CAMERA_HEIGHT + 100;

                final int spriteWidth = this.mBoxFaceTextureRegion.getWidth();
                final int spriteHeight = this.mBoxFaceTextureRegion.getHeight();

                final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(10, 0.2f, 0.5f);

                for(int i = 0; i < 3; i++) {
                        final float anchorFaceX = centerX - spriteWidth * 0.5f + 280 * (i - 1);
                        final float anchorFaceY = centerY - spriteHeight * 0.5f;

                        final Sprite anchorFace = new Sprite(anchorFaceX, anchorFaceY, this.mBoxFaceTextureRegion);
                        final Body anchorBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, anchorFace, BodyType.StaticBody, objectFixtureDef);

                        final Sprite movingFace = new  Sprite(anchorFaceX, anchorFaceY + 90, this.mCircleFaceTextureRegion);
                        final Body movingBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, movingFace, BodyType.DynamicBody, objectFixtureDef);
                      
                        pScene.attachChild(anchorFace);
                        pScene.attachChild(movingFace);

                        final Line connectionLine = new Line(anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2, anchorFaceX + spriteWidth / 2, anchorFaceY + spriteHeight / 2);
                        pScene.attachChild(connectionLine);
                        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(anchorFace, anchorBody, true, true){
                                @Override
                                public void onUpdate(final float pSecondsElapsed) {
                                        super.onUpdate(pSecondsElapsed);
                                        final Vector2 movingBodyWorldCenter = movingBody.getWorldCenter();
                                        connectionLine.setPosition(connectionLine.getX1(), connectionLine.getY1(), movingBodyWorldCenter.x * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, movingBodyWorldCenter.y * PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
                                }
                        });
                        this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(movingFace, movingBody, true, true));


                        final RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
                        revoluteJointDef.initialize(anchorBody, movingBody, anchorBody.getWorldCenter());
                        revoluteJointDef.enableMotor = true;
                        revoluteJointDef.motorSpeed = 40;
                        revoluteJointDef.maxMotorTorque = 1000;

                        this.mPhysicsWorld.createJoint(revoluteJointDef);
                }
  

	} 
        
}
