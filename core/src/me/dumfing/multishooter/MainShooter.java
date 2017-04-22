package me.dumfing.multishooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.dumfing.menus.LoadingMenu;
import me.dumfing.menus.MainMenu;
import me.dumfing.menutools.Menu;
import me.dumfing.menutools.MenuTools;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

public class MainShooter extends ApplicationAdapter implements InputProcessor{
	private float scW, scH;
	SpriteBatch batch;
	AssetManager assetManager;
	public static GameState state;
	Texture tuzki, menuImg;
	MultiplayerClient player;
	MultiplayerTools.PlayerSoldier clientSoldier;
	ShapeRenderer sr;
	LoadingMenu loadingMenu;
	Menu gameMain;
	BitmapFontCache bmfc;
	@Override
	public void create () {
		assetManager = new AssetManager();
		queueLoading();
		bmfc = new BitmapFontCache(new BitmapFont());
		sr = new ShapeRenderer();
		gameMain = new MainMenu(bmfc);
		loadingMenu = new LoadingMenu(assetManager);
		setupLoadingMenu(); // loadingmenu is the only one that is setup before anything else is loaded
		scW = Gdx.graphics.getWidth();
		scH = Gdx.graphics.getHeight();
		state = GameState.LOADINGGAME;
		batch = new SpriteBatch();
		player = new MultiplayerClient();
		player.startClient();
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		bmfc.clear(); // clear bitmap font cache because it doesn't clear itself upon drawing (grumble grumble)
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
				}
				break;
			case MAINMENU:
				gameMain.update();
				batch.begin(); // draw menu related things
					gameMain.spriteDraw(batch);
				batch.end();
				sr.begin(ShapeRenderer.ShapeType.Filled); // draw shapes
					gameMain.shapeDraw(sr);
				sr.end();
				batch.begin(); // draw text
					bmfc.draw(batch);
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
		gameMain.setInputProcessor();
		gameMain.addImage(tuzki,Gdx.graphics.getWidth()/2-100,Gdx.graphics.getHeight()/2-20);
		gameMain.setFrameTime(15);
		setupMainMenu();
	}
	public void queueLoading(){ // queue files for assetManager to load
		//Sticking random things to load into the assetmanager to see how long it'll take to load
		assetManager.load("tuzki.png",Texture.class);
		assetManager.load("Desktop.jpg",Texture.class);
		assetManager.load("4k-image-santiago.jpg",Texture.class);
		assetManager.load("4914003-galaxy-wallpaper-png.png",Texture.class);
		assetManager.load("volcano-30238.png",Texture.class);
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
		loadingMenu.setFrameTime(30);
	}
	public void setupMainMenu(){
		MenuTools.Button playButton, settingsButton, quitButton;
		for(int i = 1; i<10; i++){
			Texture tmp = assetManager.get(String.format("L%d.png",i));
			gameMain.addBackground(new TextureRegion(tmp));
		}
		for(int i = 1; i<10; i++){
			Texture tmp = assetManager.get(String.format("R%d.png",i));
			gameMain.addBackground(new TextureRegion(tmp));
		}
		playButton = new MenuTools.Button(0, 6 * (Gdx.graphics.getHeight() / 10), 400, 100, new MenuTools.OnClick() {
			@Override
			public void action() {
				MainShooter.state = GameState.SERVERBROWSER;
				System.out.println("Play!");
			}
		});
		settingsButton = new MenuTools.Button(0, 5 * (Gdx.graphics.getHeight() / 10), 400, 100, new MenuTools.OnClick() {
			@Override
			public void action() {
				MainShooter.state = GameState.MAINMENUSETTINGS;
				System.out.println("Settings");
			}
		});
		Texture santiago, galaxy;
		santiago = assetManager.get("4k-image-santiago.jpg");
		galaxy = assetManager.get("4914003-galaxy-wallpaper-png.png");
		playButton.setPressedTexture(new TextureRegion(santiago));
		playButton.setUnpressedTexture(new TextureRegion(galaxy));
		settingsButton.setPressedTexture(new TextureRegion(santiago));
		settingsButton.setUnpressedTexture(new TextureRegion(galaxy));
		gameMain.setBackground(new TextureRegion(santiago));
		gameMain.addButton(playButton);
		gameMain.addButton(settingsButton);
	}
}
