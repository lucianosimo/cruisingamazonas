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

public class ResourcesManager {

	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Engine engine;
	public BoundCamera camera;
	public GameActivity activity;
	public VertexBufferObjectManager vbom;
	public Font font;
	public Font statusFont;
	
	//Splash items
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	
	//Menu items
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion configure_region;
	public ITextureRegion howtoplay_region;
	public Music menuMusic;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
	//Map items
	public ITextureRegion map_background_region;
	public ITextureRegion level_indicator_region;
	private BuildableBitmapTextureAtlas mapTextureAtlas;
	
	//Game items
	public Music gameMusic;
	public Sound grunt;
	public ITextureRegion landPlatformShort_region;
	public ITextureRegion landPlatform_region;
	public ITextureRegion landPlatformLong_region;
	public ITextureRegion airPlatform_region;
	public ITextureRegion airPlatformLong_region;
	public ITextureRegion background_region;
	public ITextureRegion diamondBlue_region;
	public ITextureRegion diamondYellow_region;
	public ITextureRegion diamondRed_region;
	public ITextureRegion potion_region;
	public ITextureRegion antidote_region;
	public ITextureRegion tent_region;
	public ITextureRegion healthBarBackground_region;
	public ITextureRegion statusBarBackground_region;
	public ITextureRegion continueButton_region;
	public ITextureRegion lightHalo_region;
	public ITextureRegion darkBackground_region;
	public ITextureRegion brick_region;
	public ITextureRegion jumpButton_region;
	
	public ITiledTextureRegion rain_region;
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	private BitmapTextureAtlas darkTextureAtlas;
	
	public ITiledTextureRegion player_region;
	public ITiledTextureRegion venusFlyTraper_region;
	public ITiledTextureRegion snake_region;
	
	public ITextureRegion complete_level_window_region;
	
	//Splash Methods
	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 229, 260, TextureOptions.BILINEAR);
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
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture statusNormalFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 40, true, Color.WHITE_ARGB_PACKED_INT, 2, Color.BLACK_ARGB_PACKED_INT);
		statusFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), statusNormalFontTexture, activity.getAssets(), "font.ttf", 20, true, Color.WHITE_ARGB_PACKED_INT, 1, Color.BLACK_ARGB_PACKED_INT);
		font.load();
		statusFont.load();
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
		//activity.getMusicManager().remove(menuMusic);
		//System.gc();
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
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		darkTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 2, 2, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
		venusFlyTraper_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "venusFlyTraperTiled.png", 3, 1);
		snake_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "snake.png", 3, 1);
		rain_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "rain.png", 2, 1);
		
		darkBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(darkTextureAtlas, activity, "darkBackground.png", 0, 0);
		darkBackground_region.setTextureWidth(10000);
		darkBackground_region.setTextureHeight(600);
		
		jumpButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "jumpButton.png");
		brick_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "brick.png");
		lightHalo_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "lightHalo.png");		
		background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "background.png");
		landPlatform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "landPlatform.png");
		landPlatformLong_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "landPlatformLong.png");
		landPlatformShort_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "landPlatformShort.png");
		airPlatform_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "airPlatform.png");
		airPlatformLong_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "airPlatformLong.png");
		diamondBlue_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "diamondBlue.png");
		diamondYellow_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "diamondYellow.png");
		diamondRed_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "diamondRed.png");
		potion_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "potion.png");
		antidote_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "antidote.png");
		tent_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "tent.png");
		healthBarBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "healthBarBackground.png");
		statusBarBackground_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "statusBarBackground.png");
		
		complete_level_window_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "levelCompleteWindow.png");
		continueButton_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "continueButton.png");
		try {
			this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.gameTextureAtlas.load();
			this.darkTextureAtlas.load();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void unloadGameAudio() {
		gameMusic.stop();
		grunt.stop();
		//gameMusic.release();
		//grunt.release();
		//activity.getMusicManager().remove(gameMusic);
		//activity.getSoundManager().remove(grunt);
		//System.gc();
	}
	
	private void loadGameFonts() {
		
	}
	
	public void unloadGameTextures() {
		gameTextureAtlas.unload();
		darkTextureAtlas.unload();
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

}
