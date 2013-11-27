package com.lucianosimo.cruisingamazonas.object;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.lucianosimo.cruisingamazonas.manager.ResourcesManager;

public class LevelCompleteWindow extends Sprite{
	
	public LevelCompleteWindow(VertexBufferObjectManager pSpriteVertexBufferObject) {
		super(0, 0, 600, 352, ResourcesManager.getInstance().complete_level_window_region, pSpriteVertexBufferObject);		
	}

	 public void display(int countDiamondBlue, int countDiamondYellow, int countDiamondRed, int score, Scene scene, Camera camera)
	    {
		    attachChild(new Text(360, 235, ResourcesManager.getInstance().font, "" + countDiamondBlue, ResourcesManager.getInstance().vbom));
		    attachChild(new Text(360, 175, ResourcesManager.getInstance().font, "" + countDiamondYellow, ResourcesManager.getInstance().vbom));
		    attachChild(new Text(360, 115, ResourcesManager.getInstance().font, "" + countDiamondRed, ResourcesManager.getInstance().vbom));
		    attachChild(new Text(410, 30, ResourcesManager.getInstance().font, "" + score, ResourcesManager.getInstance().vbom));
	        camera.getHUD().setVisible(false);
	        
	        camera.setChaseEntity(null);
	       
	        setPosition(camera.getCenterX(), camera.getCenterY());
	        scene.attachChild(this);
	    }

}
