package me.dumfing.server.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.dumfing.server.CoffeeServer;

public class ServerDesktop {
    public static void main(String[]args){
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new CoffeeServer(),config);
        config.resizable=false;
    }
}
