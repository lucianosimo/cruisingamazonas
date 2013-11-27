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
	public Font statusNormalFont;
	public Font statusPoisonedFont;
	
	public ITextureRegion splash_region;
	private BitmapTextureAtlas splashTextureAtlas;
	
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion configure_region;
	public ITextureRegion howtoplay_region;
	public Music menuMusic;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	
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
	
	private BuildableBitmapTextureAtlas gameTextureAtlas;
	
	public ITiledTextureRegion player_region;
	public ITiledTextureRegion venusFlyTraper_region;
	public ITiledTextureRegion snake_region;
	
	public ITextureRegion complete_level_window_region;
	
	public void loadMenuResources() {
		loadMenuGraphics();
		loadMenuAudio();
		loadMenuFonts();
	}
	
	public void loadGameResources() {
		loadGameGraphics();
		loadGameAudio();
		loadGameFonts();
	}

	private void loadGameFonts() {
		
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

	private void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "player.png", 3, 1);
		venusFlyTraper_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "venusFlyTraperTiled.png", 3, 1);
		snake_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTextureAtlas, activity, "snake.png", 3, 1);
		
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
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
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
	
	public static void prepareManager(Engine engine, GameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;		
	}
	
	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
	
	private void loadMenuFonts() {
		FontFactory.setAssetBasePath("font/");
		final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture statusNormalFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		final ITexture statusPoisonedFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "font.ttf", 40, true, Color.WHITE_ARGB_PACKED_INT, 2, Color.BLACK_ARGB_PACKED_INT);
		statusNormalFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), statusNormalFontTexture, activity.getAssets(), "font.ttf", 20, true, Color.GREEN_ARGB_PACKED_INT, 1, Color.BLACK_ARGB_PACKED_INT);
		statusPoisonedFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), statusPoisonedFontTexture, activity.getAssets(), "font.ttf", 20, true, Color.RED_ARGB_PACKED_INT, 1, Color.BLACK_ARGB_PACKED_INT);
		font.load();
		statusNormalFont.load();
		statusPoisonedFont.load();
	}
	
	public void unloadMenuTextures() {
		menuTextureAtlas.unload();
	}
	
	public void loadMenuTextures() {
		menuTextureAtlas.load();
	}
	
	public void unloadGameTextures() {
		
	}
}
