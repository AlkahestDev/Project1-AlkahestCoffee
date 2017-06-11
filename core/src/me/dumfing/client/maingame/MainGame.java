package me.dumfing.client.maingame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.menus.*;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.PlayerSoldier;
import me.dumfing.multiplayerTools.WorldMap;

import java.util.HashMap;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	public static final String versionNumber = "1e-10000000";
	private float scW, scH;
	//private Viewport viewport;
	SpriteBatch batch, uiBatch; //uiBatch and uiShapeRenderer are not attached to the camera and can be used for the ui
	AssetManager assetManager;
	public static GameState.State state;
	Texture tuzki, menuImg;
	public static MultiplayerClient client; // public to allow any menu to access easily
	public static PlayerSoldier clientSoldier; // public to allow any menu to access easily
	ShapeRenderer shapeRenderer, uiShapeRenderer;
	LoadingMenu loadingMenu;
	MainMenu gameMain;
	ConnectingMenu connectingMenu;
	ClientPickingInfoMenu pickingInfoMenu;
	ClientLobbyMenu lobbyMenu;
	public static ServerBrowser serverBrowser; // static so I can access the serverList from the findServers runnable in MultiplayerClient
	SettingsMenu settingsMenu;
	OrthographicCamera camera;
	UniversalClientMenu menu;
	Array<BitmapFontCache> fontCaches;
	boolean zoomedIn = false;
	public static final int DAGGER20 = 0;
	public static final int DAGGER30 = 1;
	public static final int DAGGER40 = 2;
	public static final int DAGGER50 = 3;
	public static final int DAGGER20S = 4;
	public static WorldMap[] worldMaps;
	public static final int DEBUGWORLD = 0;
	ClientGameInstance gameInstance;
	@Override
	public void create () {
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("mouseCursorTemp.png")),0,0));
        assetManager = new AssetManager();
        queueLoading();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camera.translate(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        camera.update();
        //viewport = new FitViewport(1920, 1080, camera);
        fontCaches = new Array<BitmapFontCache>();
        BitmapFont dagger20 = new BitmapFont(Gdx.files.internal("fonts/dagger20.fnt"));
		BitmapFont dagger30 = new BitmapFont(Gdx.files.internal("fonts/dagger30.fnt"));
		BitmapFont dagger40 = new BitmapFont(Gdx.files.internal("fonts/dagger40.fnt"));
		BitmapFont dagger50 = new BitmapFont(Gdx.files.internal("fonts/dagger50.fnt"));
		BitmapFont dagger20Small = new BitmapFont(Gdx.files.internal("fonts/dagger20.fnt"));
		dagger20.getData().markupEnabled = true;
		dagger30.getData().markupEnabled = true;
		dagger40.getData().markupEnabled = true;
		dagger50.getData().markupEnabled = true;
		dagger20Small.getData().markupEnabled = true;
		dagger20Small.getData().setScale(0.07f);
		fontCaches.add(new BitmapFontCache(dagger20));
		fontCaches.add(new BitmapFontCache(dagger30));
		fontCaches.add(new BitmapFontCache(dagger40));
		fontCaches.add(new BitmapFontCache(dagger50));
		fontCaches.add(new BitmapFontCache(dagger20Small));
		shapeRenderer = new ShapeRenderer();
		uiShapeRenderer = new ShapeRenderer();
		gameMain = new MainMenu(fontCaches,assetManager, camera);
		loadingMenu = new LoadingMenu(fontCaches, assetManager, camera);
		connectingMenu = new ConnectingMenu(fontCaches,assetManager,camera);
		serverBrowser = new ServerBrowser(fontCaches,assetManager,camera);
		settingsMenu = new SettingsMenu(fontCaches,assetManager,camera);
		pickingInfoMenu = new ClientPickingInfoMenu(fontCaches,assetManager,camera);
		lobbyMenu = new ClientLobbyMenu(fontCaches,assetManager,camera);
		setupLoadingMenu(); // loadingmenu is the only one that is setup before anything else is loaded, background frames are loaded and added to it here
		scW = Gdx.graphics.getWidth();
		scH = Gdx.graphics.getHeight();
		//System.out.println(new Pixmap(Gdx.files.internal("pixmapTest.png")).getPixel(0,0)>>8);//since gimp doesn't do alpha in a friendly way, I'll bitshift right 8 bits to ignore alpha
		state = GameState.State.LOADINGGAME;
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		client = new MultiplayerClient();
		clientSoldier = new PlayerSoldier(new Rectangle(0,0,1,2),0);
		client.startClient();
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
		menu = new UniversalClientMenu(fontCaches,assetManager,camera);
		Gdx.input.setInputProcessor(this);

		//offline setup things
		clientSoldier.setPos(1,6);
		clientSoldier.setCurrentClass(1);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for(BitmapFontCache bmfc : fontCaches) {
			bmfc.clear(); // clear bitmap font cache because it doesn't clear itself upon drawing (grumble grumble)
		}
		if(state == GameState.State.PLAYINGGAME || state == GameState.State.OFFLINEDEBUG){
			if(!zoomedIn){
				System.out.println("CameraZoom "+camera.zoom);
				System.out.println("zoomin");
				zoomCamera(0.025f);
				zoomedIn = true;
			}
		}
		else{
			if(zoomedIn){
				camera.position.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2,0);
				zoomCamera(1);
				//camera.update();
				//shapeRenderer.setProjectionMatrix(camera.combined);
				//batch.setProjectionMatrix(camera.combined);
				zoomedIn = false;
			}
		}
		switch(state){
			case LOADINGGAME: // we shouldn't ever be going back to LOADINGGAME
                //LOADINGGAME just needs to call assetManager.update(), then assign the values
				loadingMenu.update();
				loadingMenu.draw(uiBatch,uiShapeRenderer);
				if(loadingMenu.doneLoading()) { // returns true if done loading
					//assets are assigned to variables here
					assignValues();
					gameMain.init();
					serverBrowser.init();
					settingsMenu.init();
					connectingMenu.init();
					pickingInfoMenu.init(client);
					lobbyMenu.init(client);
					//menu.init();
					createWorlds();
					client.pingServers();
					//state = GameState.State.OFFLINEDEBUG;

				}
				break;
			case MAINMENU:
				if(Gdx.input.getInputProcessor() != gameMain){
					gameMain.setInputProcessor();
				}
				/*
				if(Gdx.input.getInputProcessor() != menu ){
					menu.setInputProcessor();
				}
				menu.update();
				menu.standardDraw(batch,shapeRenderer);*/
				gameMain.update();
				gameMain.standardDraw(uiBatch,uiShapeRenderer);
				break;
			case MAINMENUSETTINGS:
				if(Gdx.input.getInputProcessor() != settingsMenu){
					settingsMenu.setInputProcessor();
				}
				settingsMenu.update();
				settingsMenu.standardDraw(uiBatch,uiShapeRenderer);
				break;
			case SERVERBROWSER:
			    if(Gdx.input.getInputProcessor()!=serverBrowser){
			        serverBrowser.setInputProcessor();
			        System.out.println("RUN!");
                }
                serverBrowser.update();
			    serverBrowser.draw(uiBatch,uiShapeRenderer);
				break;
			case STARTSERVER:
				break;
			case CONNECTINGTOSERVER:
				connectingMenu.update();
				connectingMenu.standardDraw(uiBatch,uiShapeRenderer);
				break;
			case GAMELOBBY:
				if(Gdx.input.getInputProcessor() != lobbyMenu){
					lobbyMenu.setInputProcessor();
				}
				lobbyMenu.update(client);
				lobbyMenu.draw(uiBatch,uiShapeRenderer);
				break;
			case PICKINGINFO:
				if(Gdx.input.getInputProcessor() != pickingInfoMenu){
					pickingInfoMenu.setInputProcessor();
				}
				pickingInfoMenu.updateTeamNumbers(client.getRedTeam(), client.getBlueTeam(), client.getrLimit(), client.getbLimit());
				pickingInfoMenu.update();
				pickingInfoMenu.standardDraw(uiBatch,uiShapeRenderer);
				break;
			case PLAYINGGAME:
				//camera.setToOrtho(true,900,450);
				if(Gdx.input.getInputProcessor() != gameInstance){
					gameInstance = new ClientGameInstance(client, client.getPlayers(),camera,assetManager,fontCaches);
					gameInstance.pickWorld(DEBUGWORLD);
					Gdx.input.setInputProcessor(gameInstance);
				}
				gameInstance.update();
				shapeRenderer.setColor(Color.BLUE);
				PlayerSoldier clientSoldierTemp = gameInstance.getPlayer(client.getConnectionID());
				//System.out.println("before "+clientSoldierTemp.getX());
				float deltaX = camera.position.x-clientSoldierTemp.getX();
				float deltaY = camera.position.y-clientSoldierTemp.getY();
				if(deltaX<-3){ // player on right side of camera
					//camera.position.x = clientSoldierTemp.getX()-5;
					if(Math.abs(deltaX)<6){
						camera.position.x = clientSoldierTemp.getX()-3;
					}
					else {
						camera.position.x += Math.abs(deltaX) / 5f;
					}
				}
				else if(deltaX>3){
					//camera.position.x = clientSoldierTemp.getX()+5;//-=Math.abs(deltaX)/5f;
					if(Math.abs(deltaX)<6){
						camera.position.x = clientSoldierTemp.getX()+3;
					}
					else {
						camera.position.x -= Math.abs(deltaX / 5f);
					}
				}
				if(deltaY<-3f){
					camera.position.y+=Math.abs(deltaY)/5f;//Math.min(Math.abs(deltaY)/5f,Math.abs(deltaY));
				}
				else if(deltaY>3f){
					camera.position.y-=Math.abs(deltaY)/5f;//Math.min(Math.abs(deltaY)/5f,Math.abs(deltaY));
				}
				//camera.position.x = clientSoldierTemp.getX();
				//System.out.println("camera update");
				//System.out.println(camera.position.x-clientSoldierTemp.getX()+" "+deltaX);
				camera.update();
				shapeRenderer.setProjectionMatrix(camera.combined);
				batch.setProjectionMatrix(camera.combined);
				gameInstance.draw(batch,shapeRenderer,uiBatch,uiShapeRenderer);
				//System.out.println("after "+clientSoldierTemp.getX());
				break;
			case ROUNDOVER:
				break;
			case OFFLINEDEBUG:
				//camera.setToOrtho(true,900,450);
				if(Gdx.input.getInputProcessor() != gameInstance){
					HashMap<Integer, PlayerSoldier> temp = new HashMap<Integer, PlayerSoldier>();
					temp.put(0,clientSoldier);
					gameInstance = new ClientGameInstance(temp,camera,assetManager,fontCaches);
					gameInstance.pickWorld(DEBUGWORLD);
					Gdx.input.setInputProcessor(gameInstance);
				}
				gameInstance.update();
				shapeRenderer.setColor(Color.BLUE);
				 clientSoldierTemp = gameInstance.getPlayer(client.getConnectionID());
				//System.out.println("before "+clientSoldierTemp.getX());
				deltaX = camera.position.x-clientSoldierTemp.getX();
				deltaY = camera.position.y-clientSoldierTemp.getY();
				if(deltaX<-3){ // player on right side of camera
					//camera.position.x = clientSoldierTemp.getX()-5;
					if(Math.abs(deltaX)<6){
						camera.position.x = clientSoldierTemp.getX()-3;
					}
					else {
						camera.position.x += Math.abs(deltaX) / 5f;
					}
				}
				else if(deltaX>3){
					//camera.position.x = clientSoldierTemp.getX()+5;//-=Math.abs(deltaX)/5f;
					if(Math.abs(deltaX)<6){
						camera.position.x = clientSoldierTemp.getX()+3;
					}
					else {
						camera.position.x -= Math.abs(deltaX / 5f);
					}
				}
				if(deltaY<-3f){
					camera.position.y+=Math.abs(deltaY)/5f;//Math.min(Math.abs(deltaY)/5f,Math.abs(deltaY));
				}
				else if(deltaY>3f){
					camera.position.y-=Math.abs(deltaY)/5f;//Math.min(Math.abs(deltaY)/5f,Math.abs(deltaY));
				}
				//camera.position.x = clientSoldierTemp.getX();
				//System.out.println("camera update");
				//System.out.println(camera.position.x-clientSoldierTemp.getX()+" "+deltaX);
				camera.update();
				shapeRenderer.setProjectionMatrix(camera.combined);
				batch.setProjectionMatrix(camera.combined);
				gameInstance.draw(batch,shapeRenderer,uiBatch,uiShapeRenderer);
				//System.out.println("after "+clientSoldierTemp.getX());
				break;
			case QUIT:
				Gdx.app.exit();
				break;
		}
	}

	@Override
	public void resize(int width, int height){
		// Called when the viewport is scaled
		//viewport.update(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		client.stopClient();
	}
	public void assignValues(){
		tuzki = assetManager.get("tuzki.png");
		menuImg = assetManager.get("simpleBG.png");

		state = GameState.State.MAINMENU;
	}
	public void queueLoading(){ // queue files for assetManager to load
		//Sticking random things to load into the assetmanager to see how long it'll take to load
		assetManager.load("tuzki.png",Texture.class);
		/*assetManager.load("Desktop.jpg",Texture.class);
		assetManager.load("4k-image-santiago.jpg",Texture.class);
		assetManager.load("4914003-galaxy-wallpaper-png.png",Texture.class);
		assetManager.load("volcano-30238.png",Texture.class);
		//assetManager.load("fonts/dagger40.fnt",BitmapFont.class);*/
		assetManager.load("simpleBG.png",Texture.class);
		assetManager.load("simpleBGB.png",Texture.class);
		assetManager.load("menubackdrops/canvas.png",Texture.class);
		assetManager.load("pixmapTest.png",Texture.class);
		assetManager.load("pixmapVisual.png",Texture.class);
		assetManager.load("projectiles/arrow.png",Texture.class);
		assetManager.load("redArrow.png",Texture.class);
		assetManager.load("blueArrow.png",Texture.class);
		assetManager.load("cloudTemp.png",Texture.class);
		assetManager.load("fgTemp.png",Texture.class);
		for(int i = 1; i<10; i++){
			assetManager.load(String.format("archive/L%d.png",i),Texture.class);
			assetManager.load(String.format("archive/R%d.png",i),Texture.class);
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

	private void zoomCamera(float amount){
		camera.zoom =  amount;//MathUtils.clamp(amount, 0.1f,100);// 100/camera.viewportWidth);
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
	}
	public void createWorlds(){
		WorldMap debugWorld = new WorldMap(MenuTools.mGTR("pixmapTest.png",assetManager),MenuTools.mGTR("pixmapVisual.png",assetManager));
		debugWorld.addBackground(MenuTools.mGTR("cloudTemp.png",assetManager));
		debugWorld.addForeground(MenuTools.mGTR("fgTemp.png",assetManager));
		worldMaps = new WorldMap[]{debugWorld};
	}

}
