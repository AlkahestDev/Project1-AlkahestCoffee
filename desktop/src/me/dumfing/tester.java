package me.dumfing;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class tester {
    public static void main (String [] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        new LwjglApplication(new fakeMainGame(), config);

        config.width = 1080;
        config.height = 720;

        boolean [] collisions = new boolean[4]; // Keeps tracks of all current collision. 1-Top, 2-Down, 3-Left, 4-Right

        System.out.println(collisions[3]);

    }
}
