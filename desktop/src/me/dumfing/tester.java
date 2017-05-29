package me.dumfing;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class tester {
    public static void main (String [] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1800;
        config.height = 900;
        new LwjglApplication(new fakeMainGame(), config);
    }
}
