package me.dumfing.multishooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

import java.util.Scanner;

public class MainShooter extends ApplicationAdapter implements InputProcessor{
	private float scW, scH;
	SpriteBatch batch;
	AssetManager assetManager;
	GameState state;
	Texture tuzki, menuImg;
	Texture[] loadingFrames;
	MultiplayerClient player;
	MultiplayerTools.PlayerSoldier clientSoldier;
    int loadingFrame, frameCount;

	private enum GameState{
		LOADINGGAME, // loading all assets into the assetmanager and place them into variables
		MAINMENU, // Main menu with play, settings, and quit
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
	    loadingFrame = frameCount = 0;
	    int numFrames = 14;
	    loadingFrames = new Texture[numFrames];
	    for(int i = 1; i<numFrames+1;i++){
	        loadingFrames[i-1] = new Texture(Gdx.files.internal(String.format("loading/tuzkiii%d.png",i)));
        }
		scW = Gdx.graphics.getWidth();
		scH = Gdx.graphics.getHeight();
		state = GameState.LOADINGGAME;
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assetManager.load("tuzki.png",Texture.class);
		assetManager.load("Desktop.jpg",Texture.class);

		player = new MultiplayerClient();
		player.startClient();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		switch(state){
			case LOADINGGAME: // we shouldn't ever be going back to LOADINGGAME
                //LOADINGGAME just needs to call assetManager.update(), then assign the values
				if(assetManager.update()) { // returns true if done loading
                    //assets are assigned to variables here
                    tuzki = assetManager.get("tuzki.png");
                    menuImg = assetManager.get("Desktop.jpg");
					state = GameState.MAINMENU;
				}

				batch.begin();
					batch.draw(loadingFrames[loadingFrame],scW/2- loadingFrames[loadingFrame].getWidth()/2,scH/2- loadingFrames[loadingFrame].getHeight()/2); // draw tuzki frame at center of screen
				batch.end();
				if(frameCount % 2 == 0) { // change frame at 30fps (render is called at 60hz)
                    loadingFrame = (1 + loadingFrame) % loadingFrames.length; // increase frame by 1, setting it to 0 if it goes over the number of frames
                }
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
        frameCount = (1 + frameCount) % Integer.MAX_VALUE; // increase frameCount by 1, setting it to 0 if it is above integer.max_value
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
