package com.lucianosimo.cruisingamazonas.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lucianosimo.cruisingamazonas.manager.ResourcesManager;

public class VenusFlyTraper extends AnimatedSprite{

	private Body body;
	private int touchCount = 0;
	
	public VenusFlyTraper(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourcesManager.getInstance().venusFlyTraper_region.deepCopy(), vbom);
		createPhysics(camera, physicsWorld);
	}
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setUserData("venusFlyTraper");
		body.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
		});
	}
	
	public void setAnimation() {
		final long[] VENUSFLYTRAPER_ANIMATE = new long[] {100, 100, 100, 100};
		animate(VENUSFLYTRAPER_ANIMATE, 0, 3, true);
	}
	
	public void addTouchCount() {
		touchCount += 1;
	}
	
	public void initializeTouchCount() {
		touchCount = 0;
	}
	
	public int getTouchCount() {
		return touchCount;
	}
	
}
