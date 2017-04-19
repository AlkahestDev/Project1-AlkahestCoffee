package me.dumfing.multiplayerTools;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

/**
 * Created by dumpl on 4/18/2017.
 */
public class MainServer {
    private Server server;
    private boolean isRunning;
    public MainServer(int pTCP, int pUDP){
        this.server = new Server();
        MultiplayerTools.register(this.server);
        try {
            this.server.bind(pTCP,pUDP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public MainServer(){
        this.server = new Server();
        MultiplayerTools.register(this.server);
        try {
            this.server.bind(MultiplayerTools.TCPPORT,MultiplayerTools.UDPPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.server.addListener(new Listener.ThreadedListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                System.out.printf("Client Connected! %d\n",connection.getID());
                super.connected(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.printf("Client Disconnected %d\n",connection.getID());
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                super.received(connection, o);
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }
        }));
    }
    public void start(){
        this.server.start();
        this.isRunning = true;
    }
    public void stop(){
        this.server.stop();
        this.isRunning = false;
    }
    public void updateClients(){ // send info about worldstate to clients

    }
    public boolean running(){
        return this.isRunning;
    }
}
