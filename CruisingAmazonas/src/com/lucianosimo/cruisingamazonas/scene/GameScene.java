package com.lucianosimo.cruisingamazonas.scene;

import java.io.IOException;
import java.util.ArrayList;
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
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
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

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.opengl.GLES20;
import android.preference.PreferenceManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.lucianosimo.cruisingamazonas.base.BaseScene;
import com.lucianosimo.cruisingamazonas.manager.SceneManager;
import com.lucianosimo.cruisingamazonas.manager.SceneManager.SceneType;
import com.lucianosimo.cruisingamazonas.object.Bat;
import com.lucianosimo.cruisingamazonas.object.Bee;
import com.lucianosimo.cruisingamazonas.object.LevelCompleteWindow;
import com.lucianosimo.cruisingamazonas.object.Player;
import com.lucianosimo.cruisingamazonas.object.Snake;
import com.lucianosimo.cruisingamazonas.object.Spider;

public class GameScene extends BaseScene{

	//Scene indicators
	private HUD gameHud;
	private Rectangle healthBar;
	private Sprite healthBarBackground;
	private Sprite statusBarBackground;
	private LevelCompleteWindow levelCompleteWindow;
	private Sprite pauseWindow;
	private Sprite jumpButton;
	private Sprite shortJumpButton;
	private Sprite gameOverWindow;
	private Sprite firstWindowHelp;
	private Sprite secondWindowHelp;
	
	
	//Texts variables
	private Text scoreText;
	private Text statusText;
	//private Text gameOverText;
	
	//Instances
	private Player player;
	private Bat bat;
	private Bee bee;
	private Snake snake;
	private Spider spider;
	
	//Booleans
	private Boolean firstTouch = false;
	private boolean levelCompleted = false;
	private Boolean gameOverDisplayed = false;
	private boolean availablePause = true;
	
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
				pTimerHandler.reset();
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
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTHPLATFORMLONG = "earthPlatformLong";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTHPLATFORMSHORT = "earthPlatformShort";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DARKEARTHPLATFORMLONG = "darkEarthPlatformLong";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DARKEARTHPLATFORMSHORT = "darkEarthPlatformShort";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DARKEARTHPLATFORM = "darkEarthPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTHPLATFORM = "earthPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_AIRPLATFORM = "airPlatform";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_AIRPLATFORMLONG = "airPlatformLong";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BRIDGE = "bridge";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASSHALFLEFT = "grassHalfLeft";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASSHALFRIGHT = "grassHalfRight";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_WATER = "water";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_WATERPLATFORM = "waterPlatform";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDBLUE = "diamondBlue";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDYELLOW = "diamondYellow";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDRED = "diamondRed";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_POTION = "potion";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ANTIDOTE = "antidote";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCK = "rock";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX = "box";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOXPOSITION = "boxPosition";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPIKE = "spike";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TORCH = "torch";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TORCHLIGHTHALO = "torchLightHalo";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SIGN = "sign";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SIGN2 = "sign2";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BAT = "bat";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BEE = "bee";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SNAKE = "snake";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPIDER = "spider";	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BUSH= "bush";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MUSHROOMBROWN = "mushroombrown";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MUSHROOMRED = "mushroomred";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLANT = "plant";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCKDECO = "rockdeco";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TREE = "tree";
	
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TENT = "tent";
	
	
	//Scene management methods	
	@Override
	public void createScene() {
		level = MapScene.getNextLevel();
		resourcesManager.gameMusic.play();
		createBackground();
		createHud();
		createPhysics();
		loadLevel(level);
		//createGameOverText();
		levelCompleteWindow = new LevelCompleteWindow(vbom);
		pauseWindow = new Sprite(427, 240, resourcesManager.pause_window_region, vbom);
		gameOverWindow = new Sprite(427, 240, resourcesManager.game_over_window_region, vbom);
		switch (level) {
		case 1:
			firstWindowHelp = new Sprite(427, 240, resourcesManager.first_help_window_region, vbom);
			secondWindowHelp = new Sprite(427, 240, resourcesManager.second_help_window_region, vbom);
			break;
		case 2:
			firstWindowHelp = new Sprite(427, 240, resourcesManager.third_help_window_region, vbom);
			break;
		case 3:
			firstWindowHelp = new Sprite(427, 240, resourcesManager.fourth_help_window_region, vbom);
			break;
		case 4:
			Sprite darkBackground = new Sprite(5000, 300, resourcesManager.darkBackground_region, vbom);
			Sprite light = new Sprite(170, 32, resourcesManager.lightHalo_region, vbom);
			light.setBlendingEnabled(true);
			light.setBlendFunctionSource(GLES20.GL_DST_COLOR);
			light.setAlpha(0.2f);
			this.attachChild(darkBackground);
			player.attachChild(light);
			break;
		case 5:
			firstWindowHelp = new Sprite(427, 240, resourcesManager.fifth_help_window_region, vbom);
			break;
		default:
			break;
		}
		loadSavedPreferences();
	}
	
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		int score = sharedPreferences.getInt("score", 0);
		float hp = sharedPreferences.getFloat("hp", 100);
		player.setHP(hp);
		player.setScore(score);
		scoreText.setText("Score: " + player.getScore());
		if (hp < 100) {
			reduceHealthBar((100 - hp));
		}
	}
	
	private void saveHighScore(String key, int score) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		Editor editor = sharedPreferences.edit();
		if (sharedPreferences.getInt("highScore", 0) < score) {
			editor.putInt("highScore", score);
		}		
		editor.commit();
	}

	private void saveScore(String key, int score) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, score);	
		editor.commit();
	}
	
	private void saveHP(String key, float hp) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, hp);
		editor.commit();
	}
	
	private void saveAvailableLevels(String key, int availableLevels) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, availableLevels);
		editor.commit();
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
		this.setBackground(background);
	}
	
	private void createHud() {
		gameHud = new HUD();
		
		healthBar = new Rectangle(720, 450, HEALTHBARWIDTH, HEALTHBARHEIGTH, vbom);
		healthBarBackground = new Sprite(688, 450, resourcesManager.healthBarBackground_region, vbom);
		statusBarBackground = new Sprite(688, 410, resourcesManager.statusBarBackground_region, vbom);
		scoreText = new Text(20, 430, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		statusText = new Text(680, 400, resourcesManager.statusFont, "normalpoisoned", new TextOptions(HorizontalAlign.CENTER), vbom);
		jumpButton = new Sprite(90, 320, resourcesManager.jumpButton_region, vbom){
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
		shortJumpButton = new Sprite(90, 130, resourcesManager.shortJumpButton_region, vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if (!firstTouch) {
						player.setRunning();
						firstTouch = true;
					} else {
						player.shortJump();
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
		gameHud.attachChild(shortJumpButton);
		gameHud.attachChild(scoreText);
		gameHud.attachChild(healthBarBackground);
		gameHud.attachChild(healthBar);
		gameHud.attachChild(statusBarBackground);
		gameHud.attachChild(statusText);
		
		gameHud.registerTouchArea(jumpButton);
		gameHud.registerTouchArea(shortJumpButton);
		
		camera.setHUD(gameHud);
	}
	
	private void reloadHud() {
		gameHud.setVisible(true);
	}
	
	private void levelCompleted() {
		levelCompleted = true;
	}
	
	/*private void createGameOverText() {
		gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!!!!", vbom);
	}*/
	
	/*private void displayGameOverText() {
		camera.setChaseEntity(null);
		gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
		attachChild(gameOverText);
		gameOverDisplayed = true;
	}*/
	
	//Touch and buttons events	
	@Override
	public void onBackKeyPressed() {
		engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				if (availablePause) {
					final boolean wasPoisoned;
					availablePause = false;
					GameScene.this.setIgnoreUpdate(true);
					if (Player.getStatus().equals("poisoned")) {
						player.setPoisonedStatus(false);
						wasPoisoned = true;
					} else {
						wasPoisoned = false;
					}
			        camera.setChaseEntity(null);
			        pauseWindow.setPosition(camera.getCenterX(), camera.getCenterY());
					GameScene.this.attachChild(pauseWindow);
				    final Sprite continuePauseButton = new Sprite(355, 45, resourcesManager.continue_pause_button_region, vbom){
				    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				    		if (pSceneTouchEvent.isActionDown()) {
				    			availablePause = true;
				    			if (wasPoisoned) {
									player.setPoisonedStatus(true);
								}
				    			GameScene.this.detachChild(pauseWindow);
				    			GameScene.this.setIgnoreUpdate(false);
				    			camera.setChaseEntity(player);
				    			reloadHud();
				    		}
				    		return true;
				    	};
				    };
				    final Sprite quitButton = new Sprite(95, 45, resourcesManager.quit_button_region, vbom){
				    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				    		if (pSceneTouchEvent.isActionDown()) {
				        		player.setPoisonedStatus(false);
								resourcesManager.gameMusic.stop();
								myGarbageCollection();
								SceneManager.getInstance().loadMapScene(engine, GameScene.this);
				    		}
				    		return true;
				    	};
				    };
				    GameScene.this.registerTouchArea(continuePauseButton);
				    GameScene.this.registerTouchArea(quitButton);
				    pauseWindow.attachChild(quitButton);
				    pauseWindow.attachChild(continuePauseButton);
				} else {
					player.setPoisonedStatus(false);
					resourcesManager.gameMusic.stop();
					myGarbageCollection();
					SceneManager.getInstance().loadMapScene(engine, GameScene.this);
				}
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
		player.increaseScore(i);
		scoreText.setText("Score: " + player.getScore());
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
		engine.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				if (Player.getStatus().equals("poisoned")) {
					engine.registerUpdateHandler(poisonTimer);
				} else {
					engine.unregisterUpdateHandler(poisonTimer);
				}
				
			}
		});		
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
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTHPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.earthPlatform_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("earthPlatform");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTHPLATFORMSHORT)) {
					levelObject = new Sprite(x, y, resourcesManager.earthPlatformShort_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("earthPlatformShort");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_EARTHPLATFORMLONG)) {
					levelObject = new Sprite(x, y, resourcesManager.earthPlatformLong_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("earthPlatformLong");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BUSH)) {
					levelObject = new Sprite(x, y, resourcesManager.bush_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MUSHROOMBROWN)) {
					levelObject = new Sprite(x, y, resourcesManager.mushroomBrown_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_MUSHROOMRED)) {
					levelObject = new Sprite(x, y, resourcesManager.mushroomRed_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLANT)) {
					levelObject = new Sprite(x, y, resourcesManager.plant_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCKDECO)) {
					levelObject = new Sprite(x, y, resourcesManager.rockDeco_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TREE)) {
					levelObject = new Sprite(x, y, resourcesManager.tree_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DARKEARTHPLATFORMLONG)) {
					levelObject = new Sprite(x, y, resourcesManager.darkEarthPlatformLong_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DARKEARTHPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.darkEarthPlatform_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DARKEARTHPLATFORMSHORT)) {
					levelObject = new Sprite(x, y, resourcesManager.darkEarthPlatformShort_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BRIDGE)) {
					levelObject = new Sprite(x, y, resourcesManager.bridge_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("bridge");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASSHALFLEFT)) {
					levelObject = new Sprite(x, y, resourcesManager.grassHalfLeft_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("grassHalfLeft");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_GRASSHALFRIGHT)) {
					levelObject = new Sprite(x, y, resourcesManager.grassHalfRight_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("grassHalfRight");
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_WATER)) {
					levelObject = new Sprite(x, y, resourcesManager.water_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOXPOSITION)) {
					levelObject = new Sprite(x, y, resourcesManager.box_position_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_WATERPLATFORM)) {
					levelObject = new Sprite(x, y, resourcesManager.waterPlatform_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("waterPlatform");
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false){
					});
				}  else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPIKE)) {
					levelObject = new Sprite(x, y, resourcesManager.spike_region, vbom);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("spike");
				}  else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BOX)) {
					levelObject = new Sprite(x, y, resourcesManager.box_region, vbom){
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							final Body box = physicsWorld.getPhysicsConnectorManager().findBodyByShape(this);
							box.setTransform(pSceneTouchEvent.getX() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, pSceneTouchEvent.getY() / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, box.getAngle());
							return true;
						};
					};
					GameScene.this.registerTouchArea(levelObject);
					GameScene.this.setTouchAreaBindingOnActionDownEnabled(true);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.DynamicBody, FIXTURE_DEF);
					body.setUserData("box");
					body.setFixedRotation(true);
					MassData massData = new MassData();
					massData.mass = 1500f;
					body.setMassData(massData);
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false) {
					});
				}  else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TORCH)) {
					levelObject = new Sprite(x, y, resourcesManager.torch_region, vbom);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TORCHLIGHTHALO)) {
					levelObject = new Sprite(x, y, resourcesManager.torch_lightHalo_region, vbom);
					levelObject.setBlendingEnabled(true);
					levelObject.setBlendFunctionSource(GLES20.GL_DST_COLOR);
					levelObject.setAlpha(0.2f);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SNAKE)) {
					final AnimatedSprite points = new AnimatedSprite(x, y + 35, resourcesManager.points500_region, vbom);
					points.setVisible(false);
					snake = new Snake(x, y, vbom, camera, physicsWorld, resourcesManager.snake_region.deepCopy()) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if ((this.getX() - player.getX()) < 854) {
								this.startMoving();
							}
							if ((this.getX() - player.getX()) < 300) {
								this.increaseSnakeSpeed();
							}
						};
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								if (snake.getSecondTouch()) {
									resourcesManager.enemiesDeath.play();
									final Sprite snakeRef = this; 
									this.setVisible(false);
									destroySprite(snakeRef);
									points.setPosition(snakeRef.getX(), snakeRef.getY());
									points.setVisible(true);
									final long[] POINTS_ANIMATE = new long[] {75, 75, 250, 75};
									points.animate(POINTS_ANIMATE, 0, 3, false);
									GameScene.this.attachChild(points);
									addToScore(500);
									snake.initializeSecondTouch();
								} else {
									snake.setSecondTouch();
								}
								
							}
							return true;
						};
					};
					levelObject = snake;
					GameScene.this.registerTouchArea(levelObject);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SPIDER)) {
					final AnimatedSprite points = new AnimatedSprite(x, y + 35, resourcesManager.points500_region, vbom);
					points.setVisible(false);
					spider = new Spider(x, y, vbom, camera, physicsWorld, resourcesManager.spider_region.deepCopy()) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if ((this.getX() - player.getX()) < 854) {
								this.startMoving();
							}
							
							if ((this.getX() - player.getX()) < 350) {
								this.increaseSpiderSpeed();
							}
						};
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								if (spider.getSecondTouch()) {
									resourcesManager.enemiesDeath.play();
									final Sprite spiderRef = this; 
									this.setVisible(false);
									destroySprite(spiderRef);
									points.setPosition(spiderRef.getX(), spiderRef.getY());
									points.setVisible(true);
									final long[] POINTS_ANIMATE = new long[] {75, 75, 250, 75};
									points.animate(POINTS_ANIMATE, 0, 3, false);
									GameScene.this.attachChild(points);
									addToScore(500);
									spider.initializeSecondTouch();
								} else {
									spider.setSecondTouch();
								}
								
							}
							return true;
						};
					};
					levelObject = spider;
					GameScene.this.registerTouchArea(levelObject);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BAT)) {
					final AnimatedSprite points = new AnimatedSprite(x, y + 35, resourcesManager.points500_region, vbom);
					points.setVisible(false);
					bat = new Bat(x, y, vbom, camera, physicsWorld, resourcesManager.bat_region.deepCopy()) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if ((this.getX() - player.getX()) < 400) {
								this.startMoving();
							}
						};
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								if (bat.getSecondTouch()) {
									resourcesManager.enemiesDeath.play();
									final Sprite batRef = this; 
									this.setVisible(false);
									destroySprite(batRef);
									points.setPosition(batRef.getX(), batRef.getY());
									points.setVisible(true);
									final long[] POINTS_ANIMATE = new long[] {75, 75, 250, 75};
									points.animate(POINTS_ANIMATE, 0, 3, false);
									GameScene.this.attachChild(points);
									addToScore(500);
									bat.initializeSecondTouch();
								} else {
									bat.setSecondTouch();
								}
								
							}
							return true;
						};
					};
					levelObject = bat;
					GameScene.this.registerTouchArea(levelObject);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_BEE)) {
					final AnimatedSprite points = new AnimatedSprite(x, y + 35, resourcesManager.points500_region, vbom);
					points.setVisible(false);
					bee = new Bee(x, y, vbom, camera, physicsWorld, resourcesManager.bee_region.deepCopy()) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if ((this.getX() - player.getX()) < 500) {
								this.startMoving();
							}
							if ((this.getX() - player.getX()) < 250) {
								this.increaseBeeSpeed();
							}
						};
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								if (bee.getSecondTouch()) {
									resourcesManager.enemiesDeath.play();
									final Sprite beeRef = this; 
									this.setVisible(false);
									destroySprite(beeRef);
									points.setPosition(beeRef.getX(), beeRef.getY());
									points.setVisible(true);
									final long[] POINTS_ANIMATE = new long[] {75, 75, 250, 75};
									points.animate(POINTS_ANIMATE, 0, 3, false);
									GameScene.this.attachChild(points);
									addToScore(500);
									bee.initializeSecondTouch();
								} else {
									bee.setSecondTouch();
								}
								
							}
							return true;
						};
					};
					levelObject = bee;
					GameScene.this.registerTouchArea(levelObject);
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SIGN)) {
					levelObject = new Sprite(x, y, resourcesManager.sign_region.deepCopy(), vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								engine.runOnUpdateThread(new Runnable() {
									
									@Override
									public void run() {
										GameScene.this.setIgnoreUpdate(true);
								        camera.setChaseEntity(null);
								        firstWindowHelp.setPosition(camera.getCenterX(), camera.getCenterY());
										GameScene.this.attachChild(firstWindowHelp);
									    final Sprite continueHelpButton = new Sprite(560, 45, resourcesManager.continueButton_region, vbom){
									    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
									    		if (pSceneTouchEvent.isActionDown()) {
									    			GameScene.this.detachChild(firstWindowHelp);
									    			GameScene.this.setIgnoreUpdate(false);
									    			camera.setChaseEntity(player);
									    			reloadHud();
									    		}
									    		return true;
									    	};
									    };
									    GameScene.this.registerTouchArea(continueHelpButton);
									    firstWindowHelp.attachChild(continueHelpButton);
									}
								});
								this.setIgnoreUpdate(true);
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_SIGN2)) {
					levelObject = new Sprite(x, y, resourcesManager.sign_region.deepCopy(), vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								engine.runOnUpdateThread(new Runnable() {
									
									@Override
									public void run() {
										GameScene.this.setIgnoreUpdate(true);
								        camera.setChaseEntity(null);
								        secondWindowHelp.setPosition(camera.getCenterX(), camera.getCenterY());
										GameScene.this.attachChild(secondWindowHelp);
									    final Sprite continueHelpButton = new Sprite(560, 45, resourcesManager.continueButton_region, vbom){
									    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
									    		if (pSceneTouchEvent.isActionDown()) {
									    			GameScene.this.detachChild(secondWindowHelp);
									    			GameScene.this.setIgnoreUpdate(false);
									    			camera.setChaseEntity(player);
									    			reloadHud();
									    		}
									    		return true;
									    	};
									    };
									    GameScene.this.registerTouchArea(continueHelpButton);
									    secondWindowHelp.attachChild(continueHelpButton);
									}
								});
								this.setIgnoreUpdate(true);
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDBLUE)) {
					final AnimatedSprite points = new AnimatedSprite(x, y + 35, resourcesManager.points100_region, vbom);
					points.setVisible(false);
					levelObject = new Sprite(x, y, resourcesManager.diamondBlue_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								addToScore(100);
								points.setVisible(true);
								final long[] POINTS_ANIMATE = new long[] {75, 75, 250, 75};
								points.animate(POINTS_ANIMATE, 0, 3, false);
								GameScene.this.attachChild(points);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								addDiamondBlue();
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_POTION)) {
					final AnimatedSprite potionSprite = new AnimatedSprite(x, y + 35, resourcesManager.potionSprite_region, vbom);
					potionSprite.setVisible(false);
					levelObject = new Sprite(x, y, resourcesManager.potion_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								potionSprite.setVisible(true);
								final long[] POTION_ANIMATE = new long[] {75, 75, 250, 75};
								potionSprite.animate(POTION_ANIMATE, 0, 3, false);
								GameScene.this.attachChild(potionSprite);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								player.increaseHP(POTION_RECOVERY);
								increaseHealthBar(POTION_RECOVERY);
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ANTIDOTE)) {
					final AnimatedSprite antidoteSprite = new AnimatedSprite(x, y + 35, resourcesManager.antidoteSprite_region, vbom);
					antidoteSprite.setVisible(false);
					levelObject = new Sprite(x, y, resourcesManager.antidote_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								antidoteSprite.setVisible(true);
								final long[] ANTIDOTE_ANIMATE = new long[] {75, 75, 250, 75};
								antidoteSprite.animate(ANTIDOTE_ANIMATE, 0, 3, false);
								GameScene.this.attachChild(antidoteSprite);
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
					final AnimatedSprite points = new AnimatedSprite(x, y + 35, resourcesManager.points200_region, vbom);
					points.setVisible(false);
					levelObject = new Sprite(x, y, resourcesManager.diamondYellow_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								addToScore(200);
								points.setVisible(true);
								final long[] POINTS_ANIMATE = new long[] {75, 75, 250, 75};
								points.animate(POINTS_ANIMATE, 0, 3, false);
								GameScene.this.attachChild(points);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								addDiamondYellow();
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_DIAMONDRED)) {
					final AnimatedSprite points = new AnimatedSprite(x, y + 35, resourcesManager.points300_region, vbom);
					points.setVisible(false);
					levelObject = new Sprite(x, y, resourcesManager.diamondRed_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								addToScore(300);
								points.setVisible(true);
								final long[] POINTS_ANIMATE = new long[] {75, 75, 250, 75};
								points.animate(POINTS_ANIMATE, 0, 3, false);
								GameScene.this.attachChild(points);
								this.setVisible(false);
								this.setIgnoreUpdate(true);
								addDiamondRed();
							}
						};
					};
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_ROCK)) {
					levelObject = new Sprite(x, y, resourcesManager.rock_region, vbom) {
						@Override
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if ((this.getX() - player.getX()) < 175) {
								final Body rock = physicsWorld.getPhysicsConnectorManager().findBodyByShape(this);
								rock.setType(BodyType.DynamicBody);
							}
						};
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if (pSceneTouchEvent.isActionDown()) {
								final Sprite rockRef = this; 
								this.setVisible(false);
								destroySprite(rockRef);
							}
							return true;
						};
					};
					GameScene.this.registerTouchArea(levelObject);
					final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyType.StaticBody, FIXTURE_DEF);
					body.setUserData("rock");
					body.setFixedRotation(true);
					MassData massData = new MassData();
					massData.mass = 1000f;
					body.setMassData(massData);
					physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false) {
					});
				} else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_TENT)) {
					levelObject = new Sprite(x, y, resourcesManager.tent_region, vbom) {
						protected void onManagedUpdate(float pSecondsElapsed) {
							super.onManagedUpdate(pSecondsElapsed);
							if (player.collidesWith(this)) {
								availablePause = false;
								levelCompleted();
								player.playerStop();
								player.setVisible(false);
								player.setPoisonedStatus(false);
								saveHP("hp", player.getHP());
								saveScore("score", player.getScore());
								saveHighScore("score", player.getScore());
								destroySprite(player);
								levelCompleteWindow.display(countDiamondBlue, countDiamondYellow, countDiamondRed, player.getScore(), GameScene.this, camera);
							    final Sprite continueButton = new Sprite(530, 40, resourcesManager.continueButton_region, vbom){
							    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							    		if (pSceneTouchEvent.isActionDown()) {
							    			saveHP("hp", player.getHP());
							        		saveScore("score", player.getScore());
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
							availablePause = false;
							saveHP("hp", 100);
							saveHighScore("score", player.getScore());
							saveScore("score", 0);
							//destroySprite(player);
							if (!gameOverDisplayed && !levelCompleted) {
								//displayGameOverText();
								engine.runOnUpdateThread(new Runnable() {
									
									@Override
									public void run() {
										GameScene.this.setIgnoreUpdate(true);
								        camera.setChaseEntity(null);
										gameOverWindow.setPosition(camera.getCenterX(), camera.getCenterY());
										GameScene.this.attachChild(gameOverWindow);
									    final Sprite retryButton = new Sprite(355, 45, resourcesManager.retry_button_region, vbom){
									    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
									    		if (pSceneTouchEvent.isActionDown()) {
									    			player.setPoisonedStatus(false);
													resourcesManager.gameMusic.stop();
													myGarbageCollection();
													SceneManager.getInstance().loadGameScene(engine, GameScene.this);
												}
									    		return true;
									    	};
									    };
									    final Sprite quitButton = new Sprite(95, 45, resourcesManager.quit_button_region, vbom){
									    	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
									    		if (pSceneTouchEvent.isActionDown()) {
									        		player.setPoisonedStatus(false);
													resourcesManager.gameMusic.stop();
													myGarbageCollection();
													SceneManager.getInstance().loadMapScene(engine, GameScene.this);
									    		}
									    		return true;
									    	};
									    };
									    GameScene.this.registerTouchArea(retryButton);
									    GameScene.this.registerTouchArea(quitButton);
									    gameOverWindow.attachChild(quitButton);
									    gameOverWindow.attachChild(retryButton);
									}
								});								
							}
							if (getHP() <= 0 && player.getY() > 0) {
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
				
				if (x1.getBody().getUserData().equals("bat") && x2.getBody().getUserData().equals("player")) { 
					resourcesManager.grunt.play();
					player.decreaseHP(25f);
					//1 HP = 2 px
					reduceHealthBar(25f);
					setInactiveBody(x1.getBody());
				}
				
				if (x1.getBody().getUserData().equals("bee") && x2.getBody().getUserData().equals("player")) { 
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
				
				if (x1.getBody().getUserData().equals("spider") && x2.getBody().getUserData().equals("player")) {
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
				
				if (x1.getBody().getUserData().equals("rock") && x2.getBody().getUserData().equals("player")) {
					resourcesManager.grunt.play();
					player.decreaseHP(25f);
					reduceHealthBar(25f);
					//setInactiveBody(x1.getBody());
				}
				
				if (x1.getBody().getUserData().equals("spike") && x2.getBody().getUserData().equals("player")) {
					player.decreaseHP(100f);
					reduceHealthBar(100f);
				}
				
				if (x1.getBody().getUserData().equals("waterPlatform") && x2.getBody().getUserData().equals("player")) {
					engine.registerUpdateHandler(new TimerHandler(0.5f, new ITimerCallback() {
						
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							Log.e("amazonas", "dynamic");
							pTimerHandler.reset();
							engine.unregisterUpdateHandler(pTimerHandler);
							x1.getBody().setType(BodyType.DynamicBody);
						}
					}));
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
        		myGarbageCollection();
        		ArrayList<Integer> completed = MapScene.getCompletedLevels();
        		if ((MapScene.getAvailableLevels() < MapScene.getLastLevel()) && (!completed.contains(level))) {
        			MapScene.increaseAvailableLevels();
        			MapScene.setCompletedLevels(level);
        		}
        		saveAvailableLevels("availableLevels", MapScene.getAvailableLevels());
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
