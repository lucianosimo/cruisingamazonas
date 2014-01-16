package com.lucianosimo.cruisingamazonas.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Bat extends AnimatedSprite{

	private Body body;
	private boolean secondTouch = false;
	
	public Bat(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld, ITiledTextureRegion region) {
		super(pX, pY, region, vbom);
		startAnimation();
		createPhysics(camera, physicsWorld);
	}
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setFixedRotation(true);
		body.setUserData("bat");
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
	}
	
	public void startAnimation() {
		final long[] BAT_ANIMATE = new long[] {150, 150};
		animate(BAT_ANIMATE, 0, 1, true);
	}
	
	public void startMoving() {
		body.setLinearVelocity(new Vector2(-1, body.getLinearVelocity().y));
	}
	
	public void setInactiveBat() {
		body.setActive(false);
	}
	
	public void setSecondTouch() {
		secondTouch = true;
	}
	
	public boolean getSecondTouch() {
		return secondTouch;
	}
	
	public void initializeSecondTouch() {
		secondTouch = false;
	}
}
