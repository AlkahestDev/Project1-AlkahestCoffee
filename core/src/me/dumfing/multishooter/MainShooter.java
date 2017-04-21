package me.dumfing.multishooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.dumfing.mainmenu.MainMenu;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

public class MainShooter extends ApplicationAdapter implements InputProcessor{
	private float scW, scH;
	SpriteBatch batch;
	AssetManager assetManager;
	public static GameState state;
	Texture tuzki, menuImg;
	Texture[] loadingFrames;
	MultiplayerClient player;
	MultiplayerTools.PlayerSoldier clientSoldier;
	ShapeRenderer sr;
	MainMenu gameMain;
    int loadingFrame, frameCount;

	@Override
	public void create () {
		sr = new ShapeRenderer();
		gameMain = new MainMenu();
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
		//Sticking random things to load into the assetmanager to see how long it'll take to load
		assetManager.load("tuzki.png",Texture.class);
		assetManager.load("Desktop.jpg",Texture.class);
		assetManager.load("4k-image-santiago.jpg",Texture.class);
		assetManager.load("4914003-galaxy-wallpaper-png.png",Texture.class);
		assetManager.load("volcano-30238.png",Texture.class);
		player = new MultiplayerClient();
		player.startClient();
		Gdx.input.setInputProcessor(this);
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
					gameMain.setInputProcessor();
					gameMain.setBackground(new TextureRegion(menuImg));
					gameMain.addBackground(new TextureRegion(tuzki));
					gameMain.setFrameTime(1);
				}

				batch.begin();
				batch.draw(loadingFrames[loadingFrame],scW/2- loadingFrames[loadingFrame].getWidth()/2,scH-loadingFrames[loadingFrame].getHeight()); // draw tuzki frame at center of screen
				batch.end();
				sr.begin(ShapeRenderer.ShapeType.Filled);
					sr.setColor(Color.WHITE);
					sr.rect(scW/2-200,20,400,30);
					sr.setColor(Color.RED);
					//System.out.println(396f*assetManager.getProgress());
					sr.rect(scW/2-198,22,396f*assetManager.getProgress(),26);
					//sr.circle(scW/2,100,90f*assetManager.getProgress());
				sr.end();
				if(frameCount % 2 == 0) { // change frame at 30fps (render is called at 60hz)
                    loadingFrame = (1 + loadingFrame) % loadingFrames.length; // increase frame by 1, setting it to 0 if it goes over the number of frames
                }
				break;
			case MAINMENU:
				gameMain.update();
				sr.begin(ShapeRenderer.ShapeType.Filled);
					batch.begin();
						gameMain.draw(batch,sr,true);
					batch.end();
				sr.end();
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
