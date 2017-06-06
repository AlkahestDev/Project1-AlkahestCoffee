package me.dumfing.client.maingame.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.dumfing.client.maingame.MainGame;

public class DesktopLauncher {
	public static void main (String [] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="Alkahest Coffee Corporation";
        /*config.fullscreen = true;
        config.width=999999;
        config.height=999999;*/
        config.width=1280;
        config.height=720;
        config.resizable=false;
        config.addIcon("badAlkahest.png", Files.FileType.Internal);
        new LwjglApplication(new MainGame(), config);

	}
}
