package me.dumfing.client.maingame.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.dumfing.client.maingame.MainGame;

public class DesktopLauncher {
	public static void main (String [] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = false;
		config.title="Alkahest Coffee Corporation";
		config.width=99; //TODO: make a better way of fullscreening : i made one in fakeMainGame, but the hitboxes dont work :/
		config.height=99;
        new LwjglApplication(new MainGame(), config);

		config.addIcon("badAlkahest.png", Files.FileType.Internal);

	}
}
