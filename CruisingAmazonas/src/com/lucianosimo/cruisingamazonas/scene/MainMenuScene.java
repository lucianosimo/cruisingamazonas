package com.lucianosimo.cruisingamazonas.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.align.HorizontalAlign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.lucianosimo.cruisingamazonas.base.BaseScene;
import com.lucianosimo.cruisingamazonas.manager.SceneManager;
import com.lucianosimo.cruisingamazonas.manager.SceneManager.SceneType;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{
	
	private MenuScene menuChildScene;
	private Text highScoreText;
	private final int MENU_PLAY = 0;
	private final int MENU_CONFIGURE = 1;
	private final int MENU_RATEUS = 2;

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
		resourcesManager.menuMusic.play();
	}

	@Override
	public void onBackKeyPressed() {
		resourcesManager.menuMusic.stop();
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
	}
	
	public void createBackground() {
		float screenWidth = resourcesManager.camera.getWidth();
		float screenHeight = resourcesManager.camera.getHeight();
		attachChild(new Sprite(screenWidth/2, screenHeight/2, resourcesManager.menu_background_region, vbom){
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
		
		highScoreText = new Text(20, 430, resourcesManager.highScorefont, "High Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
		loadSavedPreferences();

		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(screenWidth/2, screenHeight/2);
		
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
		final IMenuItem configureMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_CONFIGURE, resourcesManager.configure_region, vbom), 1.2f, 1);
		final IMenuItem rateusMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_RATEUS, resourcesManager.rateus_region,vbom), 1, 1);
		
		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(configureMenuItem);
		menuChildScene.addMenuItem(rateusMenuItem);
		menuChildScene.attachChild(highScoreText);
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
		playMenuItem.setPosition(0, 80);
		configureMenuItem.setPosition(0, -30);
		rateusMenuItem.setPosition(-325, -120);
		highScoreText.setPosition(0, -145);
		
		menuChildScene.setOnMenuItemClickListener(this);
		setChildScene(menuChildScene);
	}
	
	private void loadSavedPreferences() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
		int score = sharedPreferences.getInt("highScore", 0);
		highScoreText.setText("High Score: " + score);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,	float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {
			case MENU_PLAY:
				SceneManager.getInstance().loadMapScene(engine, this);
				return true;
			case MENU_CONFIGURE:
				SceneManager.getInstance().loadConfigureScene(engine);
				return true;
			case MENU_RATEUS:
				activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.lucianosimo.dolar")));
				return true;
			default:
				return false;
		}
	}

}
