package com.lucianosimo.cruisingamazonas.scene;

import java.io.IOException;
import java.util.Iterator;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import android.opengl.GLES20;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.lucianosimo.cruisingamazonas.base.BaseScene;
import com.lucianosimo.cruisingamazonas.manager.SceneManager;
import com.lucianosimo.cruisingamazonas.manager.SceneManager.SceneType;
import com.lucianosimo.cruisingamazonas.object.LevelCompleteWindow;
import com.lucianosimo.cruisingamazonas.object.Player;
import com.lucianosimo.cruisingamazonas.object.Snake;
import com.lucianosimo.cruisingamazonas.object.VenusFlyTraper;

public class GameScene extends BaseScene{
	
	//Scene objects
	private AnimatedSprite rain;

	//Scene indicators
	private HUD gameHud;
	private Rectangle healthBar;
	private Sprite healthBarBackground;
	private Sprite statusBarBackground;
	private int score = 0;
	private LevelCompleteWindow levelCompleteWindow;
	private Sprite jumpButton;
	
	//Texts variables
	private Text scoreText;
	private Text statusText;
	private Text gameOverText;
	
	//Instances
	private Player player;
	private VenusFlyTraper venusFlyTraper;
	private Snake snake;
	
	//Booleans
	private Boolean firstTouch = false;
	private boolean levelCompleted = false;
	private Boolean gameOverDisplayed = false;
	
	//Physics world variable
	private PhysicsWorld physicsWorld;
	
	//Counters
	private int countDiamondBlue = 0;
	private int countDiamondYellow = 0;
	private int countDiamondRed = 0;
	
	//Timer Handler for poison damage
	private TimerHandler poisonTimer = new TimerHandler(5f, true, new ITimerCallback() {
		@Override
		public void onTimePassed(TimerHandler pTimerHandler) {
			if (Player.getStatus().equals("poisoned")) {
				player.poisonedDamage(POISON_DAMAGE);
				reduceHealthBar(POISON_DAMAGE);
			}
		}
	});
	
	//Level variable
	private int level;
	
	//Constants
	private static final float POISON_DAMAGE = 5;
	private static final float POTION_RECOVERY = 50;
	
	private static final float HEALTHBARWIDTH = 200;
	private static final float HEALTHBARHEIGTH = 16;
	
	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LANDPLATFORM = "landPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LANDPLATFORMSHORT = "landPlatformShort";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LANDPLATFORMLONG = "landPlatformLong";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_AIRPLATFORM = "airPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_AIRPLATFORMLONG = "airPlatformLong";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDBLUE = "diamondBlue";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDYELLOW = "diamondYellow";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDRED = "diamondRed";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_VENUSFLYTRAPER = "venusFlyTraper";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SNAKE = "snake";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TENT = "tent";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_POTION = "potion";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ANTIDOTE = "antidote";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BRICK = "brick";
	
	//Scene management methods	
	@Override
	public void createScene() {
		level = MapScene.getNextLevel();
		resourcesManager.gameMusic.play();
		if (level == 2) {
			rain = new AnimatedSprite(20, 240, resourcesManager.rain_region, vbom);
			final long[] RAIN_ANIMATE = new long[] {110, 110};
			rain.animate(RAIN_ANIMATE, 0, 1, true);
			this.attachChild(rain);
		}
		createBackground();
		createHud();
		createPhysics();
		loadLevel(level);
		if (level == 3) {
			Sprite darkBackground = new Sprite(5000, 300, resourcesManager.darkBackground_region, vbom);
			Sprite light = new Sprite(170, 32, resourcesManager.lightHalo_region, vbom);
			light.setBlendingEnabled(true);
			light.setBlendFunctionSource(GLES20.GL_DST_COLOR);
			light.setAlpha(0.0f);
			this.attachChild(darkBackground);
			player.attachChild(light);
		}
		createGameOverText();
		levelCompleteWindow = new LevelCompleteWindow(vbom);
	}
	
	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setCenter(400, 240);
		camera.setChaseEntity(null);
	}

	private void createBackground() {
		AutoParallaxBackground background = new AutoParallaxBackground(0, 0, 0, 12);
		background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(400, 240, resourcesManager.background_region, vbom)));
		if (level == 2) {
			background.attachParallaxEntity(new ParallaxEntity(0, rain));
		}
		this.setBackground(background);
	}
	
	private void createHud() {
		gameHud = new HUD();
		
		healthBar = new Rectangle(720, 450, HEALTHBARWIDTH, HEALTHBARHEIGTH, vbom);
		healthBarBackground = new Sprite(688, 450, resourcesManager.healthBarBackground_region, vbom);
		statusBarBackground = new Sprite(688, 410, resourcesManager.statusBarBackground_region, vbom);
		scoreText = new Text(20, 420, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		statusText = new Text(685, 399, resourcesManager.statusFont, "normalpoisoned", new TextOptions(HorizontalAlign.CENTER), vbom);
		jumpButton = new Sprite(65, 240, resourcesManager.jumpButton_region, vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if (!firstTouch) {
						player.setRunning();
						firstTouch = true;
					} else {
						player.jump();
					}
				}
				return false;
			}
		};
		
		scoreText.setAnchorCenter(0, 0);
		scoreText.setText("Score: 0");
		statusText.setAnchorCenter(0, 0);
		statusText.setText(Player.getStatus());
		statusText.setColor(Color.GREEN_ARGB_PACKED_INT);
		
		healthBar.setColor(Color.GREEN_ARGB_PACKED_INT);
		
		gameHud.attachChild(jumpButton);
		gameHud.attachChild(scoreText);
		gameHud.attachChild(healthBarBackground);
		gameHud.attachChild(healthBar);
		gameHud.attachChild(statusBarBackground);
		gameHud.attachChild(statusText);
		
		gameHud.registerTouchArea(jumpButton);
		
		camera.setHUD(gameHud);
	}
	
	private void levelCompleted() {
		levelCompleted = true;
	}
	
	private void createGameOverText() {
		gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!!!!", vbom);
	}
	
	private void displayGameOverText() {
		camera.setChaseEntity(null);
		gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
		attachChild(gameOverText);
		gameOverDisplayed = true;
	}
	
	//Touch and buttons events	
	@Override
	public void onBackKeyPressed() {
		engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				player.setPoisonedStatus(false);
				resourcesManager.gameMusic.stop();
				myGarbageCollection();
				SceneManager.getInstance().loadMapScene(engine, GameScene.this);
			}
		});
	}

	//Level behavior methods
	private void addDiamondBlue() {
		countDiamondBlue++;
	}
	
	private void addDiamondYellow() {
		countDiamondYellow++;
	}
	
	private void addDiamondRed() {
		countDiamondRed++;
	}
	
	private void addToScore(int i) {
		score += i;
		scoreText.setText("Score: " + score);
	}
	
	private void reduceHealthBar(float hp) {
		healthBar.setSize(player.getHP() * 2, HEALTHBARHEIGTH);
		healthBar.setPosition(healthBar.getX() - hp, healthBar.getY());
	}
	
	private void increaseHealthBar(float hp) {
		healthBar.setSize(player.getHP() * 2, HEALTHBARHEIGTH);
		if (player.getHP() == 100) {
			healthBar.setPosition(720, 450);
		} else {
			healthBar.setPosition(healthBar.getX() + hp, healthBar.getY());
		}
	}
	
	private void createPhysics() {
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}
	
	private void setPoisonedTimer() {
		if (Player.getStatus().equals("poisoned")) {
			engine.registerUpdateHandler(poisonTimer);
		} else {
			engine.unregisterUpdateHandler(poisonTimer);
		}
		
	}
	
	//Parse level from XML file
	private void loadLevel (int level) {
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);
		final FixtureDef FIXTURE_DEF= PhysicsFactory.createFixtureDef(0, 0f, 0.5f);
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL) {
			
			@Override
			public IEntity onLoadEntity(String pEntityName, IEntity pParent, Attributes pAttributes, SimpleLevelEntityLoaderData pEntityLoaderData) throws IOException {
				final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
				final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
				camera.setBounds(0, 0, width, height);
				camera.setBoundsEnabled(true);
				return GameScene.this;
			}
		});
		levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY) {

			@Override
			public IEntity onLoadEntity(String pEntityName, IEntity pParent, Attributes pAttributes, SimpleLevelEntityLoaderData pEntityLoaderData)	throws IOException {
				final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
				final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
				final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);
				
				final Sprite levelObject;
				
				if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LANDPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.landPlatform_region, vbom);
					PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF).setUserData("landPlatform");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LANDPLATFORMSHORT)) {
					levelObject = new Sprite(x, y, resourcesManager.landPlatformShort_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("landPlatformShort");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LANDPLATFORMLONG)) {
					levelObject = new Sprite(x, y, resourcesManager.landPlatformLong_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("platform3");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_AIRPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.airPlatform_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("airPlatform");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_AIRPLATFORMLONG)) {
					levelObject = new Sprite(x, y, resourcesManager.airPlatformLong_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("airPlatformLong");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_VENUSFLYTRAPER)) {
					venusFlyTraper = new VenusFlyTraper(x, y, vbom, camera, physicsWorld) {
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								final Sprite venusRef = this; 
								this.setVisible(false);
								destroySprite(venusRef);
							}
							return true;
						};
					};
					venusFlyTraper.setAnimation();
					levelObject = venusFlyTraper;
					GameScene.this.registerTouchArea(levelObject);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SNAKE)) {
					snake = new Snake(x, y, vbom, camera, physicsWorld, resourcesManager.snake_region.deepCopy()) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if ((this.getX() - player.getX()) < 854) {
								this.startMoving();
							}
						};
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								final Sprite snakeRef = this; 
								this.setVisible(false);
								destroySprite(snakeRef);
							}
							return true;
						};
					};
					levelObject = snake;
					GameScene.this.registerTouchArea(levelObject);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDBLUE)) {
					levelObject = new Sprite(x, y, resourcesManager.diamondBlue_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								addToScore(100);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								addDiamondBlue();
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_POTION)) {
					levelObject = new Sprite(x, y, resourcesManager.potion_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								player.increaseHP(POTION_RECOVERY);
								increaseHealthBar(POTION_RECOVERY);
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ANTIDOTE)) {
					levelObject = new Sprite(x, y, resourcesManager.antidote_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								if (Player.getStatus().equals("poisoned")) {
									player.setPoisonedStatus(false);
									setPoisonedTimer();
									statusText.setText(Player.getStatus());
									statusText.setColor(Color.GREEN_ARGB_PACKED_INT);
								}								
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDYELLOW)) {
					levelObject = new Sprite(x, y, resourcesManager.diamondYellow_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								addToScore(200);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								addDiamondYellow();
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDRED)) {
					levelObject = new Sprite(x, y, resourcesManager.diamondRed_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								addToScore(300);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								addDiamondRed();
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BRICK)) {
					levelObject = new Sprite(x, y, resourcesManager.brick_region, vbom) {
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								final Sprite brickRef = this; 
								this.setVisible(false);
								destroySprite(brickRef);
							}
							return true;
						};
					};
					GameScene.this.registerTouchArea(levelObject);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.DynamicBody, FIXTURE_DEF);
					body.setUserData("brick");
					body.setFixedRotation(true);
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false) {
					});
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TENT)) {
					levelObject = new Sprite(x, y, resourcesManager.tent_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								levelCompleted();
								player.playerStop();
								player.setVisible(false);
								levelCompleteWindow.display(countDiamondBlue, countDiamondYellow, countDiamondRed, score, GameScene.this, camera);
							    final Sprite continueButton = new Sprite(530, 40, resourcesManager.continueButton_region, vbom){
							    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							    		if (pSceneTouchEvent.isActionDown()) {
								    		goToNextLevel();
							    		}
							    		return true;
							    	};
							    };
							    GameScene.this.registerTouchArea(continueButton);
							    levelCompleteWindow.attachChild(continueButton);
				                this.setIgnoreUpdate(true);
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
					player = new Player(x, y, vbom, camera, physicsWorld) {
						
						@Override
						public void onDie() {
							if (!gameOverDisplayed && !levelCompleted) {
								displayGameOverText();
							}
							if (getHP() <= 0) {
								player.playerStop();
							}
						}
					};
					levelObject = player;
				} else {
					throw new IllegalArgumentException();
				}
				levelObject.setCullingEnabled(true);				
				return levelObject;
			}
		});
		levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + level + ".xml");
	}
	
	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
			
			@Override
			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				
				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) { 
					if (x2.getBody().getUserData().equals("player")) {
						player.decreaseFootContacts();
					}
				}
			}
			
			@Override
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();
				
				if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null) { 
					if (x2.getBody().getUserData().equals("player")) {
						player.increaseFootContacts();
					}
				}
				
				if (x1.getBody().getUserData().equals("venusFlyTraper") && x2.getBody().getUserData().equals("player")) { 
					resourcesManager.grunt.play();
					player.decreaseHP(25f);
					//1 HP = 2 px
					reduceHealthBar(25f);
					setInactiveBody(x1.getBody());
				}
				
				if (x1.getBody().getUserData().equals("snake") && x2.getBody().getUserData().equals("player")) {
					resourcesManager.grunt.play();
					player.decreaseHP(10f);
					reduceHealthBar(10f);
					if (Player.getStatus().equals("normal")) {
						player.setPoisonedStatus(true);
						setPoisonedTimer();
						statusText.setText(Player.getStatus());
						statusText.setColor(Color.RED_ARGB_PACKED_INT);
					}					
					setInactiveBody(x1.getBody());
				}
				
				if (x1.getBody().getUserData().equals("brick") && x2.getBody().getUserData().equals("player")) {
					player.decreaseHP(1f);
					reduceHealthBar(1f);
				}
			}
		};
		return contactListener;
	}
	
	private void setInactiveBody(final Body body) {
		engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				player.decreaseFootContacts();
				body.setActive(false);
			}
		});
	}
	
	private void destroySprite(final Sprite sp) {
		engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				final PhysicsConnector pc = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sp);
				physicsWorld.unregisterPhysicsConnector(pc);
				Body body = pc.getBody();
				body.setActive(false);
				physicsWorld.destroyBody(body);
				GameScene.this.unregisterTouchArea(sp);
				GameScene.this.detachChild(sp);
			}
		});
	}
	
	private void goToNextLevel() {
        engine.runOnUpdateThread(new Runnable() {
        	
        	@Override
            public void run() {
        		player.setPoisonedStatus(false);
	    		resourcesManager.gameMusic.stop();
        		myGarbageCollection();
        		MapScene.increaseAvailableLevels();
        		SceneManager.getInstance().loadMapScene(engine, GameScene.this);
            }
        });
	}
	
	private void myGarbageCollection() {
		Iterator<Body> allMyBodies = physicsWorld.getBodies();
        while(allMyBodies.hasNext()) {
        	try {
        		final Body myCurrentBody = allMyBodies.next();
                	physicsWorld.destroyBody(myCurrentBody);                
            } catch (Exception e) {
            	Debug.e(e);
            }
        }
               
        this.clearChildScene();
        this.detachChildren();
        this.reset();
        this.detachSelf();
        physicsWorld.clearForces();
        physicsWorld.clearPhysicsConnectors();
        physicsWorld.reset();
 
        System.gc();
	}
	
}
