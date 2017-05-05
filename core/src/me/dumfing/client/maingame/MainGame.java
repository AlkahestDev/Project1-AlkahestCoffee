package me.dumfing.client.maingame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import me.dumfing.menus.*;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.PlayerSoldier;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	public static final String versionNumber = "1e-10000000";
	private float scW, scH;
	SpriteBatch batch;
	AssetManager assetManager;
	public static GameState.State state;
	Texture tuzki, menuImg;
	public static MultiplayerClient player; // public to allow any menu to access easily
	public static PlayerSoldier clientSoldier; // public to allow any menu to access easily
	ShapeRenderer shapeRenderer;
	LoadingMenu loadingMenu;
	MainMenu gameMain;
	ConnectingMenu connectingMenu;
	public static ServerBrowser serverBrowser; // static so I can access the serverList from the findServers runnable in MultiplayerClient
	SettingsMenu settingsMenu;
	OrthographicCamera camera;
	Array<BitmapFontCache> fontCaches;
	public static final int DAGGER20 = 0;
	public static final int DAGGER30 = 1;
	public static final int DAGGER40 = 2;
	public static final int DAGGER50 = 3;
	@Override
	public void create () {
		assetManager = new AssetManager();
		queueLoading();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.translate(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
		camera.update();
		fontCaches = new Array<BitmapFontCache>();
		BitmapFont dagger20 = new BitmapFont(Gdx.files.internal("fonts/dagger20.fnt"));
		BitmapFont dagger30 = new BitmapFont(Gdx.files.internal("fonts/dagger30.fnt"));
		BitmapFont dagger40 = new BitmapFont(Gdx.files.internal("fonts/dagger40.fnt"));
		BitmapFont dagger50 = new BitmapFont(Gdx.files.internal("fonts/dagger50.fnt"));
		dagger20.getData().markupEnabled = true;
		dagger30.getData().markupEnabled = true;
		dagger40.getData().markupEnabled = true;
		dagger50.getData().markupEnabled = true;
		fontCaches.add(new BitmapFontCache(dagger20));
		fontCaches.add(new BitmapFontCache(dagger30));
		fontCaches.add(new BitmapFontCache(dagger40));
		fontCaches.add(new BitmapFontCache(dagger50));
		shapeRenderer = new ShapeRenderer();
		gameMain = new MainMenu(fontCaches,assetManager, camera);
		loadingMenu = new LoadingMenu(fontCaches, assetManager, camera);
		connectingMenu = new ConnectingMenu(fontCaches,assetManager,camera);
		serverBrowser = new ServerBrowser(fontCaches,assetManager,camera);
		settingsMenu = new SettingsMenu(fontCaches,assetManager,camera);
		setupLoadingMenu(); // loadingmenu is the only one that is setup before anything else is loaded, background frames are loaded and added to it here
		scW = Gdx.graphics.getWidth();
		scH = Gdx.graphics.getHeight();
		state = GameState.State.LOADINGGAME;
		batch = new SpriteBatch();
		player = new MultiplayerClient();
		clientSoldier = new PlayerSoldier(new Rectangle(0,0,1,2),0,"");
		player.startClient();
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
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
				loadingMenu.draw(batch,shapeRenderer);
				if(loadingMenu.doneLoading()) { // returns true if done loading
					//assets are assigned to variables here
					assignValues();
					gameMain.init();
					serverBrowser.init();
					settingsMenu.init();
					connectingMenu.init();
					player.pingServers();
				}
				break;
			case MAINMENU:
				if(Gdx.input.getInputProcessor() != gameMain ){
					gameMain.setInputProcessor();
				}
				gameMain.update();
				gameMain.standardDraw(batch,shapeRenderer);
				break;
			case MAINMENUSETTINGS:
				if(Gdx.input.getInputProcessor() != settingsMenu){
					settingsMenu.setInputProcessor();
				}
				settingsMenu.update();
				settingsMenu.standardDraw(batch,shapeRenderer);
				break;
			case SERVERBROWSER:
			    if(Gdx.input.getInputProcessor()!=serverBrowser){
			        serverBrowser.setInputProcessor();
			        System.out.println("RUN!");
                }
                serverBrowser.update();
			    serverBrowser.draw(batch,shapeRenderer);
				break;
			case STARTSERVER:
				break;
			case CONNECTINGTOSERVER:
				connectingMenu.update();
				connectingMenu.standardDraw(batch,shapeRenderer);
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
	public void dispose () {
		batch.dispose();
		player.stopClient();
	}
	public void assignValues(){
		tuzki = assetManager.get("tuzki.png");
		menuImg = assetManager.get("Desktop.jpg");

		state = GameState.State.MAINMENU;
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
		for(int i = 1; i<15;i++){
			assetManager.load(String.format("loading/tuzkii/tuzkiii%d.png",i),Texture.class);
		}
	}
	public void setupLoadingMenu(){
		int numFrames = 39;
		for(int i = 0; i<numFrames;i++){
			loadingMenu.addBackground(new TextureRegion(new Texture(Gdx.files.internal(String.format("loading/loadingKnight/loadingKnight%d.png",i)))));
		}
		loadingMenu.setFrameRate(25);
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


	/*public void drawMenu(Menu toDraw){
		batch.begin();
			toDraw.spriteDraw(batch);
		batch.end();
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			toDraw.shapeDraw(shapeRenderer);
		shapeRenderer.end();
		batch.begin();
			for(BitmapFontCache bmfc : fontCaches){
				bmfc.draw(batch);
			}
		batch.end();
	}*/
}
