package me.dumfing.maingame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.menus.LoadingMenu;
import me.dumfing.menus.MainMenu;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

import java.util.HashMap;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	private float scW, scH;
	SpriteBatch batch;
	AssetManager assetManager;
	public static GameState state;
	Texture tuzki, menuImg;
	public static MultiplayerClient player; // public to allow any menu to access easily
	public static MultiplayerTools.PlayerSoldier clientSoldier; // public to allow any menu to access easily
	ShapeRenderer sr;
	LoadingMenu loadingMenu;
	MainMenu gameMain;
	Array<BitmapFontCache> fontCaches;
	public static final int DAGGER40 = 0;
	public static final int DAGGER20 = 1;
	@Override
	public void create () {
		assetManager = new AssetManager();
		queueLoading();
		fontCaches = new Array<BitmapFontCache>();
		BitmapFont dagger40 = new BitmapFont(Gdx.files.internal("fonts/dagger40.fnt"));
		BitmapFont dagger20 = new BitmapFont(Gdx.files.internal("fonts/dagger20.fnt"));
		dagger40.getData().markupEnabled = true;
		dagger20.getData().markupEnabled = true;
		fontCaches.add(new BitmapFontCache(dagger40));
		fontCaches.add(new BitmapFontCache(dagger20));
		sr = new ShapeRenderer();
		gameMain = new MainMenu(fontCaches,assetManager);
		loadingMenu = new LoadingMenu(fontCaches, assetManager);
		setupLoadingMenu(); // loadingmenu is the only one that is setup before anything else is loaded, background frames are loaded and added to it here
		scW = Gdx.graphics.getWidth();
		scH = Gdx.graphics.getHeight();
		state = GameState.LOADINGGAME;
		batch = new SpriteBatch();
		player = new MultiplayerClient();
		clientSoldier = new MultiplayerTools.PlayerSoldier(0,0,0,"");
		player.startClient();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for(BitmapFontCache bmfc : fontCaches) {
			bmfc.clear(); // clear bitmap font cache because it doesn't clear itself upon drawing (grumble grumble)
		}
		switch(state){
			case LOADINGGAME: // we shouldn't ever be going back to LOADINGGAME
                //LOADINGGAME just needs to call assetManager.update(), then assign the values
				loadingMenu.update();
				sr.begin(ShapeRenderer.ShapeType.Filled);
					batch.begin();
						loadingMenu.draw(batch,sr);
					batch.end();
				sr.end();
				if(loadingMenu.doneLoading()) { // returns true if done loading
					//assets are assigned to variables here
					assignValues();
					gameMain.init(assetManager);
				}
				break;
			case MAINMENU:
				if(Gdx.input.getInputProcessor() != gameMain ){
					gameMain.setInputProcessor();
				}
				gameMain.update();
				batch.begin(); // draw menu related things
					gameMain.spriteDraw(batch);
				batch.end();
				sr.begin(ShapeRenderer.ShapeType.Filled); // draw shapes
					gameMain.shapeDraw(sr);
				sr.end();
				batch.begin(); // draw text
					for(BitmapFontCache bmfc : fontCaches){
						bmfc.draw(batch);
					}
				batch.end();
				break;
			case MAINMENUSETTINGS:
				break;
			case SERVERBROWSER:
				break;
			case STARTSERVER:
				break;
			case CONNECTINGTOSERVER:
				break;
			case PICKINGTEAM:
				break;
			case PICKINGLOADOUT:
				break;
			case PLAYINGGAME:
				break;
			case ROUNDOVER:
				break;
			case QUIT:
				break;
		}
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void dispose () {
		batch.dispose();
		player.stopClient();
	}
	public void assignValues(){
		tuzki = assetManager.get("tuzki.png");
		menuImg = assetManager.get("Desktop.jpg");

		state = GameState.MAINMENU;
	}
	public void queueLoading(){ // queue files for assetManager to load
		//Sticking random things to load into the assetmanager to see how long it'll take to load
		assetManager.load("tuzki.png",Texture.class);
		assetManager.load("Desktop.jpg",Texture.class);
		assetManager.load("4k-image-santiago.jpg",Texture.class);
		assetManager.load("4914003-galaxy-wallpaper-png.png",Texture.class);
		assetManager.load("volcano-30238.png",Texture.class);
		//assetManager.load("fonts/dagger40.fnt",BitmapFont.class);
		assetManager.load("menubackdrops/canvas.png",Texture.class);
		for(int i = 1; i<10; i++){
			assetManager.load(String.format("L%d.png",i),Texture.class);
			assetManager.load(String.format("R%d.png",i),Texture.class);
		}
	}
	public void setupLoadingMenu(){
		int numFrames = 39;
		for(int i = 0; i<numFrames;i++){
			loadingMenu.addBackground(new TextureRegion(new Texture(Gdx.files.internal(String.format("loading/loadingKnight/loadingKnight%d.png",i)))));
		}
		loadingMenu.setFrameTime(25);
	}
}
