package com.lucianosimo.cruisingamazonas.scene;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.lucianosimo.cruisingamazonas.base.BaseScene;
import com.lucianosimo.cruisingamazonas.manager.SceneManager;
import com.lucianosimo.cruisingamazonas.manager.SceneManager.SceneType;

public class MapScene extends BaseScene implements IOnMenuItemClickListener{
	
	private MenuScene menuChildScene;
	private final int LEVEL_1 = 1;
	private final int LEVEL_2 = 2;
	private final int LEVEL_3 = 3;
	private final int LEVEL_4 = 4;
	private static int nextLevel;
	private static int availableLevels = 1;
	private static int lastLevel = 4;
	private static ArrayList<Integer> completedLevels = new ArrayList<Integer>();
	private int buttonX;
	private int buttonY;

	@Override
	public void createScene() {
		loadSavedPreferences();
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMenuScene(engine);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MAP;
	}

	@Override
	public void disposeScene() {
	}
	
	public void createBackground() {
		float screenWidth = resourcesManager.camera.getWidth();
		float screenHeight = resourcesManager.camera.getHeight();
		attachChild(new Sprite(screenWidth/2, screenHeight/2, resourcesManager.map_background_region, vbom){
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}
	
	private void createMenuChildScene() {
		float screenWidth = resourcesManager.camera.getWidth();
		float screenHeight = resourcesManager.camera.getHeight();
		
		final IMenuItem[] levelButtons = new IMenuItem[availableLevels];
		buttonX = -460;
		buttonY = -220;

		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(screenWidth/2, screenHeight/2);
		
		for (int i = 0; i < availableLevels; i++) {
			levelButtons[i] = new ScaleMenuItemDecorator(new SpriteMenuItem(i + 1, resourcesManager.level_indicator_region, vbom), 1.2f, 1);
			menuChildScene.addMenuItem(levelButtons[i]);
			
		}
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
		for (int i = 0; i < availableLevels; i++) {
			buttonX = buttonX + 90;
			buttonY = buttonY + 20;
			levelButtons[i].setPosition(buttonX, buttonY);
		}
		
		menuChildScene.setOnMenuItemClickListener(this);
		setChildScene(menuChildScene);
	}
	
	public static int getNextLevel() {
		return nextLevel;
	}
	
	public static int getLastLevel() {
		return lastLevel;
	}
	
	public static void setAvailableLevels(int levels) {
		availableLevels = levels;
	}
	
	public static int getAvailableLevels() {
		return availableLevels;
	}
	
	public static void increaseAvailableLevels() {
		availableLevels++;
	}
	
	public static void setCompletedLevels(int level) {
		if (!completedLevels.contains(level)) {
			completedLevels.add(level);
		}
	}
	
	public static ArrayList<Integer> getCompletedLevels() {
		return completedLevels;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,	float pMenuItemLocalX, float pMenuItemLocalY) {
		resourcesManager.menuMusic.stop();
		switch (pMenuItem.getID()) {
			case LEVEL_1:
				nextLevel = LEVEL_1;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case LEVEL_2:
				nextLevel = LEVEL_2;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case LEVEL_3:
				nextLevel = LEVEL_3;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case LEVEL_4:
				nextLevel = LEVEL_4;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			default:
				return false;
		}
	}
	
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		int available = sharedPreferences.getInt("availableLevels", 1);
		setAvailableLevels(available);
		for (int i = 1; i < availableLevels; i++) {
			setCompletedLevels(i);
		}
	}

}
