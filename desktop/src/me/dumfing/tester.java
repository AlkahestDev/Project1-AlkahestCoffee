package me.dumfing;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class tester {
    public static void main (String [] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        new LwjglApplication(new fakeMainGame(), config);

        config.width = 1080;
        config.height = 720;

    }
}
