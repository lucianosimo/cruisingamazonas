package com.lucianosimo.cruisingamazonas;

import java.io.IOException;
import com.chartboost.sdk.*;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.view.KeyEvent;

import com.lucianosimo.cruisingamazonas.manager.ResourcesManager;
import com.lucianosimo.cruisingamazonas.manager.SceneManager;

public class GameActivity extends BaseGameActivity{

	private BoundCamera camera;
	private ResourcesManager resourcesManager;
	
	private Chartboost cb;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// Configure Chartboost
		this.cb = Chartboost.sharedChartboost();
		String appId = "52c5ae739ddc3561b816facf";
		String appSignature = "141edd3649126793164ef409e57401e2053a6e24";
		this.cb.onCreate(this, appId, appSignature, null);
		
		camera = new BoundCamera(0, 0, 854, 480);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(854, 480), this.camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        this.cb.onStart(this);
        // Notify the beginning of a user session. Must not be dependent on user actions or any prior network requests.
        this.cb.startSession();

    }   
	
	@Override
	protected void onStop() {
	    super.onStop();
	    this.cb.onStop(this);
	}

	@Override
	public void onBackPressed() {
	    // If an interstitial is on screen, close it. Otherwise continue as normal.
	    if (this.cb.onBackPressed())
	        return;
	            else
	        super.onBackPressed();
	}
	
	public void showAd() {
		this.cb.showInterstitial(); 
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)	throws IOException {
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager());
		resourcesManager = ResourcesManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)	throws IOException {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);		
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
		        // Show an interstitial
				mEngine.unregisterUpdateHandler(pTimerHandler);
				SceneManager.getInstance().createMenuScene();
			}
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}
	
	@Override
	protected void onDestroy() {
		this.cb.onDestroy(this);
		super.onDestroy();
		System.exit(0);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
		}
		return false;
	}

}
