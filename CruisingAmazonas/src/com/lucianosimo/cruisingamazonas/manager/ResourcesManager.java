package com.lucianosimo.cruisingamazonas.manager;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import com.lucianosimo.cruisingamazonas.GameActivity;
import com.lucianosimo.cruisingamazonas.scene.MapScene;

public class ResourcesManager {

	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public BoundCamera camera;
	public GameActivity activity;
	public VertexBufferObjectManager vbom;
	public Font font;
	public Font statusFont;
	public Font highScorefont;
	
	//Splash items
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	
	//Menu items
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion configure_region;
	public ITextureRegion howtoplay_region;
	public ITextureRegion rateus_region;
	public ITextureRegion continueButtonMenu_region;
	public Music menuMusic;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
	//Configure items
	public ITextureRegion configure_background_region;
	public ITextureRegion delete_region;
	private BuildableBitmapTextureAtlas configureTextureAtlas;
	
	//Map items
	public ITextureRegion map_background_region;
	public ITextureRegion level_indicator_region;
	private BuildableBitmapTextureAtlas mapTextureAtlas;
	
	//Game items
	public Music gameMusic;
	public Sound grunt;
	public Sound enemiesDeath;
	
	//Platforms
	public ITextureRegion landPlatformShort_region;
	public ITextureRegion landPlatform_region;
	public ITextureRegion landPlatformLong_region;
	public ITextureRegion airPlatform_region;
	public ITextureRegion airPlatformLong_region;
	public ITextureRegion bridge_region;
	public ITextureRegion darkEarthPlatform_region;
	public ITextureRegion darkEarthPlatformShort_region;
	public ITextureRegion darkEarthPlatformLong_region;
	public ITextureRegion earthPlatform_region;
	public ITextureRegion earthPlatformShort_region;
	public ITextureRegion earthPlatformLong_region;
	public ITextureRegion grassHalfLeft_region;	
	public ITextureRegion grassHalfRight_region;
	public ITextureRegion water_region;
	public ITextureRegion waterPlatform_region;
	
	//Decoration
	public ITextureRegion bush_region;
	public ITextureRegion mushroomBrown_region;
	public ITextureRegion mushroomRed_region;
	public ITextureRegion plant_region;
	public ITextureRegion rockDeco_region;
	public ITextureRegion tree_region;
	
	//Backgrounds
	public ITextureRegion darkBackground_region;
	public ITextureRegion background_region;
	
	//Objects
	public ITextureRegion diamondBlue_region;
	public ITextureRegion diamondYellow_region;
	public ITextureRegion diamondRed_region;
	public ITextureRegion potion_region;
	public ITextureRegion antidote_region;
	public ITextureRegion rock_region;
	public ITextureRegion box_region;
	public ITextureRegion box_position_region;
	public ITextureRegion spike_region;
	public ITextureRegion torch_region;
	
	//Others
	public ITextureRegion tent_region;
	public ITextureRegion sign_region;
	public ITextureRegion healthBarBackground_region;
	public ITextureRegion statusBarBackground_region;
	public ITextureRegion continueButton_region;
	public ITextureRegion lightHalo_region;
	public ITextureRegion torch_lightHalo_region;
	public ITextureRegion jumpButton_region;
	public ITextureRegion shortJumpButton_region;
	public ITextureRegion complete_level_window_region;
	public ITextureRegion pause_window_region;
	public ITextureRegion quit_button_region;
	public ITextureRegion continue_pause_button_region;
	public ITextureRegion game_over_window_region;
	public ITextureRegion retry_button_region;
	
	
	//Animated
	public ITiledTextureRegion player_region;
	public ITiledTextureRegion points100_region;
	public ITiledTextureRegion points200_region;
	public ITiledTextureRegion points300_region;
	public ITiledTextureRegion points500_region;
	public ITiledTextureRegion antidoteSprite_region;
	public ITiledTextureRegion potionSprite_region;
	public ITiledTextureRegion bat_region;
	public ITiledTextureRegion bee_region;
	public ITiledTextureRegion snake_region;
	public ITiledTextureRegion spider_region;
	
	//Help windows
	public ITextureRegion first_help_window_region;
	public ITextureRegion second_help_window_region;
	public ITextureRegion third_help_window_region;
	public ITextureRegion fourth_help_window_region;
	public ITextureRegion fifth_help_window_region;
	public ITextureRegion creditsWindow_region;
	
	//Game Textures
	private BuildableBitmapTextureAtlas decoTextureAtlas;
	private BuildableBitmapTextureAtlas animatedTextureAtlas;
	private BuildableBitmapTextureAtlas backgroundTextureAtlas;
	private BuildableBitmapTextureAtlas platformsTextureAtlas;
	private BuildableBitmapTextureAtlas objectsTextureAtlas;
	private BuildableBitmapTextureAtlas hudTextureAtlas;
	private BuildableBitmapTextureAtlas othersTextureAtlas;
	private BuildableBitmapTextureAtlas gameOverTextureAtlas;
	private BuildableBitmapTextureAtlas completeWindowTextureAtlas;
	private BuildableBitmapTextureAtlas firstHelpWindowTextureAtlas;
	private BuildableBitmapTextureAtlas secondHelpWindowTextureAtlas;
	private BuildableBitmapTextureAtlas thirdHelpWindowTextureAtlas;
	private BuildableBitmapTextureAtlas fourthHelpWindowTextureAtlas;
	private BuildableBitmapTextureAtlas fifthHelpWindowTextureAtlas;
	private BitmapTextureAtlas darkBackgroundTextureAtlas;
	private BuildableBitmapTextureAtlas creditsTextureAtlas;
	
	
	//Splash Methods
	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 500, 400, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
	}
	
	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}
	
	//Menu methods
	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}

	private void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu_background.png");
		play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
		configure_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "configure.png");
		howtoplay_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "howtoplay.png");
		rateus_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "rateUs.png");
		continueButtonMenu_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "continueButton.png");
		try {
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			org.andengine.util.debug.Debug.e(e);
		}
	}
	
	private void loadMenuAudio() {
		try {
			MusicFactory.setAssetBasePath("music/menu/");
			menuMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "menuMusic.mp3");
			menuMusic.setLooping(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture highScoreTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture statusNormalFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "jungle4.ttf", 35, true, Color.GREEN_ARGB_PACKED_INT, 1f, Color.BLACK_ARGB_PACKED_INT);
		highScorefont = FontFactory.createStrokeFromAsset(activity.getFontManager(), highScoreTexture, activity.getAssets(), "jungle4.ttf", 37, true, Color.GREEN_ARGB_PACKED_INT, 0.1f, Color.BLACK_ARGB_PACKED_INT);
        statusFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), statusNormalFontTexture, activity.getAssets(), "jungle4.ttf", 18, true, Color.WHITE_ARGB_PACKED_INT, 1f, Color.BLACK_ARGB_PACKED_INT);
		font.load();
		statusFont.load();
		highScorefont.load();
	}
	
	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}
	
	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}
	
	public void unloadMenuAudio() {
		menuMusic.stop();
		//menuMusic.release();
		activity.getMusicManager().remove(menuMusic);
		System.gc();
	}
	
	//Map methods
	public void loadMapResources() {
		loadMapGraphics();
	}
	
	private void loadMapGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/map/");
		mapTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		map_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity, "map.png");
		level_indicator_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mapTextureAtlas, activity, "levelIndicator.png");
		try {
			this.mapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.mapTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			org.andengine.util.debug.Debug.e(e);
		}
	}
	
	public void unloadMapTextures() {
		mapTextureAtlas.unload();
	}
	
	//Game Methods
	public void loadGameResources() {
		loadGameGraphics();
		loadGameAudio();
		loadGameFonts();
	}
	
	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		decoTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 400, 400, TextureOptions.BILINEAR);
		animatedTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 400, 400, TextureOptions.BILINEAR);
		backgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 800, 480, TextureOptions.BILINEAR);
		platformsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 500, 400, TextureOptions.DEFAULT);
		objectsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 200, 200, TextureOptions.BILINEAR);
		hudTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 300, 200, TextureOptions.BILINEAR);
		othersTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 512, 750, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		gameOverTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 450, 450, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		completeWindowTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 600, 512, TextureOptions.BILINEAR);
		if (MapScene.getNextLevel() == 1) {
			firstHelpWindowTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 600, 320, TextureOptions.BILINEAR);
			secondHelpWindowTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 600, 320, TextureOptions.BILINEAR);
			first_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(firstHelpWindowTextureAtlas, activity, "firstHelpWindow.png");
			second_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(secondHelpWindowTextureAtlas, activity, "secondHelpWindow.png");
		}
		if (MapScene.getNextLevel() == 2) {
			thirdHelpWindowTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 600, 320, TextureOptions.BILINEAR);
			third_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(thirdHelpWindowTextureAtlas, activity, "thirdHelpWindow.png");
		}
		if (MapScene.getNextLevel() == 3) {
			fourthHelpWindowTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 600, 320, TextureOptions.BILINEAR);
			fourth_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fourthHelpWindowTextureAtlas, activity, "fourthHelpWindow.png");
		}
		if (MapScene.getNextLevel() == 5) {
			fifthHelpWindowTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 600, 320, TextureOptions.BILINEAR);
			fifth_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fifthHelpWindowTextureAtlas, activity, "fifthHelpWindow.png");
		}
		if (MapScene.getNextLevel() == 8) {
			creditsTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 600, 320, TextureOptions.BILINEAR);
			creditsWindow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(creditsTextureAtlas, activity, "creditsWindow.png");
		}
		
				
		//Animated Sprites
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "player.png", 6, 1);
		bat_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "bat.png", 2, 1);
		bee_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "bee.png", 2, 1);
		snake_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "snake.png", 2, 1);
		spider_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "spider.png", 3, 1);
		points100_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "100.png", 4, 1);
		points200_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "200.png", 4, 1);
		points300_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "300.png", 4, 1);
		points500_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "500.png", 4, 1);
		antidoteSprite_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "antidoteSprite.png", 4, 1);
		potionSprite_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(animatedTextureAtlas, activity, "potionSprite.png", 4, 1);
		
		//Backgrounds
		background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backgroundTextureAtlas, activity, "background.png");
		if (MapScene.getNextLevel() == 4) {
			darkBackgroundTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2, 2, TextureOptions.REPEATING_BILINEAR);
			darkBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(darkBackgroundTextureAtlas, activity, "darkBackground.png", 0, 0);
			darkBackground_region.setTextureWidth(10000);
			darkBackground_region.setTextureHeight(600);
		} else if (MapScene.getNextLevel() == 8) {
			darkBackgroundTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2, 2, TextureOptions.REPEATING_BILINEAR);
			darkBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(darkBackgroundTextureAtlas, activity, "darkBackground.png", 0, 0);
			darkBackground_region.setTextureWidth(15000);
			darkBackground_region.setTextureHeight(600);
		}
		
		
		//Platforms
		landPlatform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "landPlatform.png");
		landPlatformLong_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "landPlatformLong.png");
		landPlatformShort_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "landPlatformShort.png");
		airPlatform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "airPlatform.png");
		airPlatformLong_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "airPlatformLong.png");
		bridge_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "bridge.png");;
		darkEarthPlatform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "darkEarthPlatform.png");;
		darkEarthPlatformShort_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "darkEarthPlatformShort.png");
		darkEarthPlatformLong_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "darkEarthPlatformLong.png");
		earthPlatform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "earthPlatform.png");
		earthPlatformShort_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "earthPlatformShort.png");
		earthPlatformLong_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "earthPlatformLong.png");
		grassHalfLeft_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "grassHalfLeft.png");	
		grassHalfRight_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "grassHalfRight.png");
		water_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "water.png");
		waterPlatform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(platformsTextureAtlas, activity, "waterPlatform.png");
		
		//Objects
		rock_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "rock.png");
		box_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "box.png");
		box_position_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "boxPosition.png");
		spike_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "spikes.png");
		torch_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "torch.png");
		diamondBlue_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "diamondBlue.png");
		diamondYellow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "diamondYellow.png");
		diamondRed_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "diamondRed.png");
		potion_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "potion.png");
		antidote_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(objectsTextureAtlas, activity, "antidote.png");
		
		//Decoration
		bush_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(decoTextureAtlas, activity, "bush.png");
		mushroomBrown_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(decoTextureAtlas, activity, "mushroomBrown.png");
		mushroomRed_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(decoTextureAtlas, activity, "mushroomRed.png");
		plant_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(decoTextureAtlas, activity, "plant.png");
		rockDeco_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(decoTextureAtlas, activity, "rockDeco.png");
		tree_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(decoTextureAtlas, activity, "tree.png");
		
		//HUD
		jumpButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "jumpButton.png");
		shortJumpButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "shortJumpButton.png");
		healthBarBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "healthBarBackground.png");
		statusBarBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hudTextureAtlas, activity, "statusBarBackground.png");
				
		//Others
		lightHalo_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(othersTextureAtlas, activity, "lightHalo.png");
		torch_lightHalo_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(othersTextureAtlas, activity, "torchLightHalo.png");
		tent_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(othersTextureAtlas, activity, "tent.png");
		sign_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(othersTextureAtlas, activity, "sign.png");
		pause_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(othersTextureAtlas, activity, "pauseWindow.png");
		quit_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(othersTextureAtlas, activity, "quitButton.png");
		continue_pause_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(othersTextureAtlas, activity, "continuePauseButton.png");
		retry_button_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameOverTextureAtlas, activity, "retry.png");
		game_over_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameOverTextureAtlas, activity, "gameOverWindow.png");
		
		//Help Windows
		//first_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(firstHelpWindowTextureAtlas, activity, "firstHelpWindow.png");
		//second_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(secondHelpWindowTextureAtlas, activity, "secondHelpWindow.png");
		//third_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(thirdHelpWindowTextureAtlas, activity, "thirdHelpWindow.png");
		//fourth_help_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fourthHelpWindowTextureAtlas, activity, "fourthHelpWindow.png");
		
		//Complete window
		complete_level_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(completeWindowTextureAtlas, activity, "levelCompleteWindow.png");
		continueButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(completeWindowTextureAtlas, activity, "continueButton.png");
		
		try {
			this.decoTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.animatedTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.backgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.platformsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.objectsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.hudTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.othersTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.gameOverTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.completeWindowTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			if (MapScene.getNextLevel() == 1) {
				this.firstHelpWindowTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.secondHelpWindowTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.firstHelpWindowTextureAtlas.load();
				this.secondHelpWindowTextureAtlas.load();
			}
			if (MapScene.getNextLevel() == 2) {
				this.thirdHelpWindowTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.thirdHelpWindowTextureAtlas.load();
			}
			if (MapScene.getNextLevel() == 3) {
				this.fourthHelpWindowTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.fourthHelpWindowTextureAtlas.load();
				
			}
			if (MapScene.getNextLevel() == 4) {
				this.darkBackgroundTextureAtlas.load();	
			}
			if (MapScene.getNextLevel() == 8) {
				this.creditsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.creditsTextureAtlas.load();
				this.darkBackgroundTextureAtlas.load();	
			}
			if (MapScene.getNextLevel() == 5) {
				this.fifthHelpWindowTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
				this.fifthHelpWindowTextureAtlas.load();
				
			}
			this.decoTextureAtlas.load();
			this.animatedTextureAtlas.load();
			this.backgroundTextureAtlas.load();
			this.platformsTextureAtlas.load();
			this.objectsTextureAtlas.load();
			this.hudTextureAtlas.load();
			this.othersTextureAtlas.load();
			this.gameOverTextureAtlas.load();
			this.completeWindowTextureAtlas.load();
			
			
			
			
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadGameAudio() {
		MusicFactory.setAssetBasePath("music/game/");
		SoundFactory.setAssetBasePath("sound/game/");
		try {
			gameMusic = MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "gameMusic.mp3");
			gameMusic.setLooping(true);
			grunt = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "grunt.mp3");
			enemiesDeath = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "enemiesDeath.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unloadGameAudio() {
		gameMusic.stop();
		grunt.stop();
		enemiesDeath.stop();
		//gameMusic.release();
		//grunt.release();
		activity.getMusicManager().remove(gameMusic);
		activity.getSoundManager().remove(grunt);
		activity.getSoundManager().remove(enemiesDeath);
		System.gc();
	}
	
	private void loadGameFonts() {
		
	}
	
	public void unloadGameTextures() {
		this.decoTextureAtlas.unload();
		this.animatedTextureAtlas.unload();
		this.backgroundTextureAtlas.unload();
		this.platformsTextureAtlas.unload();
		this.objectsTextureAtlas.unload();
		this.hudTextureAtlas.unload();
		this.othersTextureAtlas.unload();
		this.gameOverTextureAtlas.unload();
		this.completeWindowTextureAtlas.unload();
		//this.darkBackgroundTextureAtlas.unload();
		if (MapScene.getNextLevel() == 1) {
			this.firstHelpWindowTextureAtlas.unload();
			this.secondHelpWindowTextureAtlas.unload();
		}
		if (MapScene.getNextLevel() == 2) {
			this.thirdHelpWindowTextureAtlas.unload();
		}
		if (MapScene.getNextLevel() == 3) {
			this.fourthHelpWindowTextureAtlas.unload();
			
		}
		if (MapScene.getNextLevel() == 4) {
			this.darkBackgroundTextureAtlas.unload();
		}
		if (MapScene.getNextLevel() == 8) {
			this.darkBackgroundTextureAtlas.unload();
			this.creditsTextureAtlas.unload();
		}
		if (MapScene.getNextLevel() == 5) {
			this.fifthHelpWindowTextureAtlas.unload();
			
		}
		/*this.firstHelpWindowTextureAtlas.unload();
		this.secondHelpWindowTextureAtlas.unload();
		this.thirdHelpWindowTextureAtlas.unload();
		this.fourthHelpWindowTextureAtlas.unload();*/
	}
	
	//Manager Methods
	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;		
	}
	
	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
	
	//Menu methods
	public void loadConfigureResources() {
		loadConfigureGraphics();
		loadMenuFonts();
	}

	private void loadConfigureGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		configureTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		configure_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(configureTextureAtlas, activity, "configure_background.png");
		delete_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(configureTextureAtlas, activity, "delete.png");
		try {
			this.configureTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.configureTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			org.andengine.util.debug.Debug.e(e);
		}
	}
	
	public void unloadConfigureTextures() {
		configureTextureAtlas.unload();
	}
	
	public void unloadConfigureAudio() {
		menuMusic.stop();
		activity.getMusicManager().remove(menuMusic);
		System.gc();
	}


}

