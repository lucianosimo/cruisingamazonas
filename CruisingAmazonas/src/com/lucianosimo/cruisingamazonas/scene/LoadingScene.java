package com.lucianosimo.cruisingamazonas.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import com.lucianosimo.cruisingamazonas.base.BaseScene;
import com.lucianosimo.cruisingamazonas.manager.SceneManager.SceneType;

public class LoadingScene extends BaseScene{

	@Override
	public void createScene() {
		Color greenBackColor = new Color(0.47f,0.73f,0.07f);
		//setBackground(new Background(Color.WHITE));
		setBackground(new Background(greenBackColor));
		attachChild(new Text(400, 240, resourcesManager.font, "Loading.....", vbom));
	}

	@Override
	public void onBackKeyPressed() {
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {
	}

}
