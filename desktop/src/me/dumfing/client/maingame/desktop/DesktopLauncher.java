package me.dumfing.client.maingame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.dumfing.client.maingame.MainGame;

public class DesktopLauncher {
	public static void main (String [] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1800;
		config.height = 900;
		new LwjglApplication(new MainGame(), config);
	}
}
