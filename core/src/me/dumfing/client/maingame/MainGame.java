package me.dumfing.client.maingame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.menus.*;
import me.dumfing.multiplayerTools.*;

import java.util.HashMap;

public class MainGame extends ApplicationAdapter implements InputProcessor{
	public static final String versionNumber = "1.0";
	private float scW, scH;
	SpriteBatch batch, uiBatch; //uiBatch and uiShapeRenderer are not attached to the camera and can be used for the ui
	AssetManager assetManager;
	public static GameState.State state;
	public static MultiplayerClient client; // public to allow any menu to access easily
	public static PlayerSoldier clientSoldier; // public to allow any menu to access easily
	ShapeRenderer shapeRenderer, uiShapeRenderer;
	LoadingMenu loadingMenu;
	MainMenu gameMain;
	ConnectingMenu connectingMenu;
	ClientPickingInfoMenu pickingInfoMenu;
	ClientLobbyMenu lobbyMenu;
	public static ServerBrowser serverBrowser; // static so I can access the serverList from the findServers runnable in MultiplayerClient
	OrthographicCamera camera;
	UniversalClientMenu menu;
	Array<BitmapFontCache> fontCaches;
	public static final int DAGGER20 = 0;
	public static final int DAGGER30 = 1;
	public static final int DAGGER40 = 2;
	public static final int DAGGER50 = 3;
	public static WorldMap[] worldMaps;
	public static final int DEBUGWORLD = 0;
	ClientGameInstance gameInstance;
	ParticleEffect bloodEffect;
	public static ParticleEffectPool bloodEffectPool;
	public static boolean gameStarted = false;
	public static String gpuName;
	public static ParticleEffect redFlagCap;
	public static ParticleEffect bluFlagCap;
	public static Sound flagCap, arrowHit, swordHit;
	public static TextureRegion backPres,backUn,background,bigButtonPress,bigButtonUn,refreshPress,refreshUn,bowSill,swordSill;
	public static final float DEFAULT_CAMERA_ZOOM = 0.025f;
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

		shapeRenderer = new ShapeRenderer();
		uiShapeRenderer = new ShapeRenderer();
		gameMain = new MainMenu(fontCaches,assetManager, camera);
		loadingMenu = new LoadingMenu(fontCaches, assetManager, camera);
		connectingMenu = new ConnectingMenu(fontCaches,assetManager,camera);
		serverBrowser = new ServerBrowser(fontCaches,assetManager,camera);
		pickingInfoMenu = new ClientPickingInfoMenu(fontCaches,assetManager,camera);
		lobbyMenu = new ClientLobbyMenu(fontCaches,assetManager,camera);
		//setupLoadingMenu(); // loadingmenu is the only one that is setup before anything else is loaded, background frames are loaded and added to it here
		scW = Gdx.graphics.getWidth();
		scH = Gdx.graphics.getHeight();
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
		clientSoldier.setPos(16,7);
		clientSoldier.setCurrentClass(0);
		clientSoldier.setTeam(0);
		gpuName = Gdx.gl.glGetString(GL20.GL_RENDERER);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(169 / 255f, 223 / 255f, 229 / 255f, 1); // using sky blue as the fill colour
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT );
		for(BitmapFontCache bmfc : fontCaches) {
			bmfc.clear(); // clear bitmap font cache because it doesn't clear itself upon drawing (grumble grumble)
		}
		if(state == GameState.State.PLAYINGGAME || state == GameState.State.OFFLINEDEBUG) { // if the mode requires the camera to be moved
            if (gameInstance == null || gameInstance.clientSoldier().getKeysHeld()[MultiplayerTools.Keys.RMB] == null) { // what to do if nothing else is ready for the camera
                zoomCamera(DEFAULT_CAMERA_ZOOM);
            } else if (gameInstance.clientSoldier() != null && gameInstance.clientSoldier().getCurrentClass() == PlayerSoldier.ARCHER && gameInstance.clientSoldier().getKeysHeld()[MultiplayerTools.Keys.RMB].getIsDown()) { // if the player exists and the right mouse button is pressed
                camera.translate(gameInstance.clientSoldier().getFacingDirection() == 0 ? -7 : 7, 0); // translate the camera left or right based on the player's facing direction
                zoomCamera(DEFAULT_CAMERA_ZOOM*2); // zoom camera out (currently 2x)
            } else if (gameInstance.clientSoldier() != null && !gameInstance.clientSoldier().getKeysHeld()[MultiplayerTools.Keys.RMB].getIsDown()) { // if the mouse isn't down
                zoomCamera(DEFAULT_CAMERA_ZOOM); // zoom in to regular area
            }
        }
		switch(state){
			case LOADINGGAME: // we shouldn't ever be going back to LOADINGGAME
                //LOADINGGAME just needs to call assetManager.update(), then assign the values
				loadingMenu.update();
				loadingMenu.draw(uiBatch,uiShapeRenderer);
				if(loadingMenu.doneLoading()) { // when all assets are loaded
					//assets are assigned to variables here
                    loadFonts();
					assignValues();
					gameMain.init();
					serverBrowser.init();
					connectingMenu.init();
					pickingInfoMenu.init(client);
					lobbyMenu.init(client);
                    AnimationManager.init(assetManager);
					createWorlds();
					client.pingServers();
					//state = GameState.State.OFFLINEDEBUG; //uncomment to go straight to offline mode

				}
				break;
			case MAINMENU:
				if(Gdx.input.getInputProcessor() != gameMain){ //set this to the input processor if it isn't already
					gameMain.setInputProcessor();
				}
				gameMain.update();
				gameMain.standardDraw(uiBatch,uiShapeRenderer);
				break;
			case SERVERBROWSER:
			    if(Gdx.input.getInputProcessor()!=serverBrowser){
			        serverBrowser.setInputProcessor();
			        gameInstance = null; // the game goes back to the server browser when disconnected. Here it is set to null for the next game
                }
                serverBrowser.update();
			    serverBrowser.draw(uiBatch,uiShapeRenderer);
				break;
			case CONNECTINGTOSERVER: // this is shown if the game fails to connect
				connectingMenu.update();
				connectingMenu.standardDraw(uiBatch,uiShapeRenderer);
				break;
			case GAMELOBBY: // the lobby the players wait in while waiting for more people to connect
				if(Gdx.input.getInputProcessor() != lobbyMenu){
					lobbyMenu.setInputProcessor();
				}
				lobbyMenu.update(client);
				lobbyMenu.draw(uiBatch,uiShapeRenderer);
				break;
			case PICKINGINFO: // picking loadout and team
				if(Gdx.input.getInputProcessor() != pickingInfoMenu){
				    if(Gdx.input.getInputProcessor() != gameInstance){
                        pickingInfoMenu.resetButtonPosses();
                    }
					pickingInfoMenu.setInputProcessor();
				}
				pickingInfoMenu.updateTeamNumbers(client.getRedTeam(), client.getBlueTeam(), client.getrLimit(), client.getbLimit());
				pickingInfoMenu.update();
				pickingInfoMenu.standardDraw(uiBatch,uiShapeRenderer);
				break;
			case PLAYINGGAME: // in the actual game
				if(Gdx.input.getInputProcessor() != gameInstance){
				    if(!gameStarted) { // only runs if the game has stopped (not when changing classes, etc.)
                        gameInstance = new ClientGameInstance(client, client.getPlayers(), camera, assetManager, fontCaches);
                        gameInstance.pickWorld(DEBUGWORLD);
                    }
                    gameStarted = true;
					Gdx.input.setInputProcessor(gameInstance);
				}
				gameInstance.update();
				PlayerSoldier clientSoldierTemp = gameInstance.getPlayer(client.getConnectionID());
				moveCamera(clientSoldierTemp);
				camera.update();
				shapeRenderer.setProjectionMatrix(camera.combined);
				batch.setProjectionMatrix(camera.combined);
				gameInstance.draw(batch,shapeRenderer,uiBatch,uiShapeRenderer);
				break;
			case OFFLINEDEBUG: // offline mode, doesn't listen to the playerClient
				if(Gdx.input.getInputProcessor() != gameInstance){
				    if(!gameStarted) {
                        HashMap<Integer, PlayerSoldier> temp = new HashMap<Integer, PlayerSoldier>();
                        temp.put(0, clientSoldier);
                        gameInstance = new ClientGameInstance(temp, camera, assetManager, fontCaches);
                        gameInstance.pickWorld(DEBUGWORLD);
                    }
                    gameStarted = true;
					Gdx.input.setInputProcessor(gameInstance);
				}
				gameInstance.update();
                clientSoldierTemp = gameInstance.getPlayer(0);
				moveCamera(clientSoldierTemp);
				camera.update();
				shapeRenderer.setProjectionMatrix(camera.combined);
				batch.setProjectionMatrix(camera.combined);
				gameInstance.draw(batch,shapeRenderer,uiBatch,uiShapeRenderer);
				break;
			case QUIT:
				Gdx.app.exit();
				break;
		}
	}

	/**
	 * Handles camera positioning
	 * @param clientSoldierTemp The PlayerSoldier for the camera to focus on
	 */
    private void moveCamera(PlayerSoldier clientSoldierTemp){
		float deltaX = camera.position.x-clientSoldierTemp.getX();
		float deltaY = camera.position.y-clientSoldierTemp.getY();
        if(deltaX<-3){ // player on right side of camera
            if(Math.abs(deltaX)<6){
                camera.position.x = clientSoldierTemp.getX()-3;
            }
            else {
                camera.position.x += Math.abs(deltaX) / 5f;
            }
        }
        else if(deltaX>3){
            if(Math.abs(deltaX)<6){
                camera.position.x = clientSoldierTemp.getX()+3;
            }
            else {
                camera.position.x -= Math.abs(deltaX / 5f);
            }
        }
        if(deltaY<-3f){
            camera.position.y+=Math.abs(deltaY)/5f;
        }
        else if(deltaY>3f){
            camera.position.y-=Math.abs(deltaY)/5f;
        }
    }
	@Override
	public void dispose () {
		batch.dispose();
		client.stopClient();
	}

	/**
	 * this prevents having to access the assetmanager to construct textureRegions all the time
	 */
	public void assignValues(){
        bloodEffect = assetManager.get("Particles/hitBlood");
        bloodEffectPool = new ParticleEffectPool(bloodEffect,4,8); // the max capacity only needs to be 4 (8 players, 4 attacking at a time) but there are enough effects for everyone to bleed
        redFlagCap = assetManager.get("Particles/RedFlagCap");
        bluFlagCap = assetManager.get("Particles/BluFlagCap");
        flagCap = assetManager.get("sounds/flagCap.wav");
        swordHit = assetManager.get("sounds/swordHit.mp3");
        arrowHit = assetManager.get("sounds/arrowHit.mp3");
        backPres = new TextureRegion((Texture) assetManager.get("Menu/BackPres.png"));
        backUn = new TextureRegion((Texture) assetManager.get("Menu/BackUn.png"));
        background  = new TextureRegion((Texture) assetManager.get("Menu/bckgrndCropped.png"));
        bigButtonPress = new TextureRegion((Texture) assetManager.get("Menu/BigButtonPres.png"));
        bigButtonUn = new TextureRegion((Texture) assetManager.get("Menu/BigButtonUn.png"));
        refreshPress = new TextureRegion((Texture) assetManager.get("Menu/RefreshPres.png"));
        refreshUn = new TextureRegion((Texture) assetManager.get("Menu/RefreshUn.png"));
        bowSill = new TextureRegion((Texture) assetManager.get("Menu/BowSillo.png"));
        swordSill = new TextureRegion((Texture) assetManager.get("Menu/SwordSilo.png"));
		state = GameState.State.MAINMENU;
	}

	/**
	 * All assets to be used will be loaded here
	 */
	private void queueLoading(){ // queue files for assetManager to load
		assetManager.load("Menu/bckgrndCropped.png",Texture.class);
		assetManager.load("Menu/BigButtonUn.png",Texture.class);
		assetManager.load("Menu/BigButtonPres.png",Texture.class);
		assetManager.load("Menu/BackPres.png",Texture.class);
		assetManager.load("Menu/BackUn.png",Texture.class);
		assetManager.load("Menu/RefreshPres.png",Texture.class);
		assetManager.load("Menu/RefreshUn.png",Texture.class);
		assetManager.load("Menu/BowSillo.png",Texture.class);
		assetManager.load("Menu/SwordSilo.png",Texture.class);
		assetManager.load("pixmapTest.png",Texture.class);
		assetManager.load("pixmapVisual.png",Texture.class);
		assetManager.load("pixmapBackground.png",Texture.class);
		assetManager.load("bgMedium.png",Texture.class);
		assetManager.load("projectiles/arrow.png",Texture.class);
		assetManager.load("redArrow.png",Texture.class);
		assetManager.load("blueArrow.png",Texture.class);
		assetManager.load("cloudTemp.png",Texture.class);
		assetManager.load("SpriteSheets/KnightSprites.atlas", TextureAtlas.class);
		assetManager.load("SpriteSheets/ArcherSprites.atlas",TextureAtlas.class);
		assetManager.load("fonts/dagger20.fnt",BitmapFont.class);
		assetManager.load("fonts/dagger30.fnt",BitmapFont.class);
		assetManager.load("fonts/dagger40.fnt",BitmapFont.class);
		assetManager.load("fonts/dagger50.fnt",BitmapFont.class);
		assetManager.load("SpriteSheets/FlagSprites.atlas",TextureAtlas.class);
		assetManager.load("Particles/RedFlagCap",ParticleEffect.class);
		assetManager.load("Particles/BluFlagCap",ParticleEffect.class);
		assetManager.load("Particles/hitBlood",ParticleEffect.class);
		assetManager.load("sounds/flagCap.wav",Sound.class);
		assetManager.load("sounds/swordHit.mp3",Sound.class);
		assetManager.load("sounds/arrowHit.mp3",Sound.class);
		for(int i = 1; i<10; i++){
			assetManager.load(String.format("archive/L%d.png",i),Texture.class);
			assetManager.load(String.format("archive/R%d.png",i),Texture.class);
		}
		for(int i = 1; i<15;i++){
			assetManager.load(String.format("loading/tuzkii/tuzkiii%d.png",i),Texture.class);
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

	private void zoomCamera(float amount){
		camera.zoom =  amount;//MathUtils.clamp(amount, 0.1f,100);// 100/camera.viewportWidth);
		camera.update();
		shapeRenderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
	}

	/**
	 * Takes the required assets and makes them into a worldMap
	 */
	private void createWorlds(){
		WorldMap world1 = new WorldMap(MenuTools.mGTR("pixmapTest.png",assetManager),MenuTools.mGTR("pixmapVisual.png",assetManager));
		world1.addBackground(MenuTools.mGTR("cloudTemp.png",assetManager));
		world1.addBackgroundClose(MenuTools.mGTR("pixmapBackground.png",assetManager));
		world1.addBackgroundMedium(MenuTools.mGTR("bgMedium.png",assetManager));
		worldMaps = new WorldMap[]{world1};
	}

	/**
	 * gets the fonts and applies the desired settings for them<br>
	 *     fonts must have a bit depth of 32 to be colourable
	 */
	public void loadFonts(){
        BitmapFont dagger20 = assetManager.get("fonts/dagger20.fnt",BitmapFont.class);
        BitmapFont dagger30 = assetManager.get("fonts/dagger30.fnt",BitmapFont.class);
        BitmapFont dagger40 = assetManager.get("fonts/dagger40.fnt",BitmapFont.class);
        BitmapFont dagger50 = assetManager.get("fonts/dagger50.fnt",BitmapFont.class);
        dagger20.getData().markupEnabled = true;
        dagger30.getData().markupEnabled = true;
        dagger40.getData().markupEnabled = true;
        dagger50.getData().markupEnabled = true;
        fontCaches.add(new BitmapFontCache(dagger20));
        fontCaches.add(new BitmapFontCache(dagger30));
        fontCaches.add(new BitmapFontCache(dagger40));
        fontCaches.add(new BitmapFontCache(dagger50));
    }

}
