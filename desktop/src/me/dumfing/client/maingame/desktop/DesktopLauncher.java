package me.dumfing.client.maingame.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.dumfing.client.maingame.MainGame;

public class DesktopLauncher {
	public static void main (String [] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = true;
		config.title="Alkahest Coffee Corporation";
		config.width=999999; //TODO: make a better way of fullscreening
		config.height=999999;
        new LwjglApplication(new MainGame(), config);
	}
}
