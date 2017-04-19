package me.dumfing.multishooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

import java.util.Scanner;

public class MainShooter extends ApplicationAdapter {
	private float scW, scH;
	SpriteBatch batch;
	AssetManager assetManager;
	GameState state;
	Texture loadingIcon1, tuzki, menuImg;
	MultiplayerClient player;
	MultiplayerTools.PlayerSoldier clientSoldier;
	private enum GameState{
		LOADINGGAME,
		ASSIGNTEXTURES,
		PICKUSERNAME,
		MAINMENU,
			MAINMENUSETTINGS,
		SERVERBROWSER,
			STARTSERVER,
		CONNECTINGTOSERVER,
		CONNECTEDTOSERVER,
		PICKINGTEAM,
		PICKINGLOADOUT,
		PLAYINGGAME,
		ROUNDOVER,
		//return to main menu
		QUIT
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
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		player.stopClient();
	}
}
