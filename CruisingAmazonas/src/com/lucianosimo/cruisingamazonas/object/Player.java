package com.lucianosimo.cruisingamazonas.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lucianosimo.cruisingamazonas.manager.ResourcesManager;

public abstract class Player extends AnimatedSprite{

	private Body body;
	private Boolean canRun = false;
	private int footContacts = 0;
	private float hpCounter = 0;
	private int score = 0;
	
	private static final int MAXHP = 100;
	private static final String NORMAL_STATUS = "normal";
	private static final String POISONED_STATUS = "poisoned";
	
	private static String status = NORMAL_STATUS;
	
	public Player(float pX, float pY, VertexBufferObjectManager vbom, Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourcesManager.getInstance().player_region, vbom);
		createPhysics(camera, physicsWorld);
		initializeHP();
		setPoisonedStatus(false);
		camera.setChaseEntity(this);
	}
	
	public abstract void onDie();
	
	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setUserData("player");
		body.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false) {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				super.onUpdate(pSecondsElapsed);
				camera.onUpdate(0.1f);
				if (getY() <= 0) {
					hpCounter = 0;
					onDie();
				}
				if (canRun) {
					body.setLinearVelocity(new Vector2(4, body.getLinearVelocity().y));
				}
			}
		});
	}
	
	public void setRunning() {
		canRun = true;
		final long[] PLAYER_ANIMATE = new long[] {100, 100, 100};
		animate(PLAYER_ANIMATE, 0, 2, true);
	}
	
	public void playerStop() {
		body.setLinearVelocity(new Vector2(0,0));
		final long[] PLAYER_ANIMATE = new long[] {0, 0};
		animate(PLAYER_ANIMATE, 0, 1, false);
		canRun = false;
	}
	
	public void jump() {
		if (footContacts < 1) {
			return;
		}
		body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
	}
	
	public void increaseFootContacts() {
		footContacts++;
	}
	
	public void decreaseFootContacts() {
		footContacts--;
	}
	
	public void initializeHP() {
		hpCounter = MAXHP;
	}
	
	public void decreaseHP(float hpDamage) {
		if ((hpCounter - hpDamage) <= 0) {
			hpCounter = 0;
			setScore(0);
			onDie();
		} else {
			hpCounter = hpCounter - hpDamage;
		}
	}
	
	public void increaseHP(float hpIncrease) {
		if ((hpCounter + hpIncrease) > MAXHP) {
			hpCounter = MAXHP;
		} else {
			hpCounter = hpCounter + hpIncrease;
		}
	}
	
	public float getHP() {
		return hpCounter;
	}
	
	public void setHP(float hp) {
		this.hpCounter = hp;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void increaseScore(int score) {
		this.score = this.score + score;
	}
	
	public void setPoisonedStatus(Boolean poisoned) {
		if (poisoned == true) {
			status = POISONED_STATUS;
		} else {
			status = NORMAL_STATUS;
		}
	}
	
	public static String getStatus() {
		return status;
	}
	
	public void poisonedDamage(float poisonDamage) {
		decreaseHP(poisonDamage);
	}

}
