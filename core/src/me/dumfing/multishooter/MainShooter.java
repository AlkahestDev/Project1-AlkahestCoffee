package me.dumfing.multishooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

import java.util.Scanner;

public class MainShooter extends ApplicationAdapter implements InputProcessor{
	private float scW, scH;
	SpriteBatch batch;
	AssetManager assetManager;
	GameState state;
	Texture loadingIcon1, tuzki, menuImg;
	MultiplayerClient player;
	MultiplayerTools.PlayerSoldier clientSoldier;

	private enum GameState{
		LOADINGGAME, // loading all assets into the assetmanager
		MAINMENU, // Main menu with play, settings, and quit
		ASSIGNTEXTURES, // assigning variables from loaded assets in the assetmanager
		MAINMENUSETTINGS, // settings for the game
		SERVERBROWSER, // browse all active servers on the network
			STARTSERVER, // start your own server on the network
		CONNECTINGTOSERVER, // currently establishing a connection to the server
		CONNECTEDTOSERVER, // might not be used
		PICKINGTEAM, // pick a team to join
		PICKINGLOADOUT, // pick what class you are
		PLAYINGGAME, // playing the game
		ROUNDOVER, // end of round, show scoreboard etc.
		//return to main menu
		QUIT // exiting game
		;

	}
	@Override
	public void create () {
		scW = Gdx.graphics.getWidth();
		scH = Gdx.graphics.getHeight();
		state = GameState.LOADINGGAME;
		Scanner uIn = new Scanner(System.in);
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assetManager.load("tuzki.png",Texture.class);
		assetManager.load("Desktop.jpg",Texture.class);
		loadingIcon1 = new Texture(Gdx.files.internal("tuzki.png"));
		player = new MultiplayerClient();
		player.startClient();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch(state){
			case LOADINGGAME: // we shouldn't ever be going back to LOADINGGAME
				if(assetManager.update()) {
					state = GameState.ASSIGNTEXTURES;
				}
				batch.begin();
					batch.draw(loadingIcon1,scW/2- loadingIcon1.getWidth()/2,scH/2- loadingIcon1.getHeight()/2);
				batch.end();
				break;
			case ASSIGNTEXTURES: // we should only go to ASSIGNTEXTURES once
				tuzki = assetManager.get("tuzki.png");
				menuImg = assetManager.get("Desktop.jpg");
				state = GameState.MAINMENU;
				break;
			case MAINMENU:
				batch.begin();
				batch.draw(menuImg,scW/2-menuImg.getWidth()/2,scH/2-menuImg.getHeight()/2);
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
}
