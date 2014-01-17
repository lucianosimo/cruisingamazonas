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

public class Spider extends AnimatedSprite{

	private Body body;
	private boolean secondTouch = false;
	
	public Spider(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld, ITiledTextureRegion region) {
		super(pX, pY, region, vbom);
		startAnimation();
		createPhysics(camera, physicsWorld);
	}
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setFixedRotation(true);
		body.setUserData("spider");
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
	}
	
	public void startAnimation() {
		final long[] SPIDER_ANIMATE = new long[] {150, 150, 150};
		animate(SPIDER_ANIMATE, 0, 2, true);
	}
	
	public void startMoving() {
		body.setLinearVelocity(new Vector2(-1.2f, body.getLinearVelocity().y));
	}
	
	public void increaseSpiderSpeed() {
		body.setLinearVelocity(new Vector2(-1.8f, body.getLinearVelocity().y));
	}
	
	public void setInactiveSpider() {
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
