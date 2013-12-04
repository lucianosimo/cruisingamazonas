package com.lucianosimo.cruisingamazonas.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.lucianosimo.cruisingamazonas.base.BaseScene;
import com.lucianosimo.cruisingamazonas.manager.SceneManager;
import com.lucianosimo.cruisingamazonas.manager.SceneManager.SceneType;

public class MapScene extends BaseScene implements IOnMenuItemClickListener{
	
	private MenuScene menuChildScene;
	private final int LEVEL_ONE = 1;
	private final int LEVEL_TWO = 2;
	private final int LEVEL_THREE = 3;
	private static int nextLevel;

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
		resourcesManager.menuMusic.play();
	}

	@Override
	public void onBackKeyPressed() {
		resourcesManager.menuMusic.stop();
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

		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(screenWidth/2, screenHeight/2);
		
		final IMenuItem levelOne = new ScaleMenuItemDecorator(new SpriteMenuItem(LEVEL_ONE, resourcesManager.level_indicator_region, vbom), 1.2f, 1);
		final IMenuItem levelTwo = new ScaleMenuItemDecorator(new SpriteMenuItem(LEVEL_TWO, resourcesManager.level_indicator_region, vbom), 1.2f, 1);
		final IMenuItem levelThree = new ScaleMenuItemDecorator(new SpriteMenuItem(LEVEL_THREE, resourcesManager.level_indicator_region, vbom), 1.2f, 1);
		
		menuChildScene.addMenuItem(levelOne);
		menuChildScene.addMenuItem(levelTwo);
		menuChildScene.addMenuItem(levelThree);
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
		levelOne.setPosition(-370, -200);
		levelTwo.setPosition(-287, -180);
		levelThree.setPosition(-240, -160);
		
		menuChildScene.setOnMenuItemClickListener(this);
		setChildScene(menuChildScene);
	}
	
	public static int getNextLevel() {
		return nextLevel;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,	float pMenuItemLocalX, float pMenuItemLocalY) {
		resourcesManager.menuMusic.stop();
		switch (pMenuItem.getID()) {
			case LEVEL_ONE:
				nextLevel = LEVEL_ONE;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case LEVEL_TWO:
				nextLevel = LEVEL_TWO;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			case LEVEL_THREE:
				nextLevel = LEVEL_THREE;
				SceneManager.getInstance().loadGameScene(engine);
				return true;
			default:
				return false;
		}
	}

}
