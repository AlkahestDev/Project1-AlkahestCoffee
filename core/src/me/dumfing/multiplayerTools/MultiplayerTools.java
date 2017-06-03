package me.dumfing.multiplayerTools;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Objects that will be sent between the client and server as well as useful variables like ports<br>
 * All classes that the server will send will be prefixed with <b>Server</b> and all classes that the client will send will be prefixed with <b>Client</b>
 */
public class MultiplayerTools {
    public static final int UDPPORT = 19815;
    public static final int TCPPORT = 19816;
    public static final float GRAVITY = -0.05f;
    public static final float WALKSPEED = 0.22f;
    public static final float JUMPPOWER = 0.8f;
    public static final int REDTEAM = 0;
    public static final int BLUTEAM = 1;
    public static class Keys{
        public static final int W = 0;
        public static final int A = 1;
        public static final int S = 2;
        public static final int D = 3;
        public static final int LMB = 4;
        public static final int RMB = 5;
        public static final int SPACE = 6;
        public static final int SHIFT = 7;
        public static final int CONTROL = 8;
        public static final int ANGLE = 9;
    }
    public static void register(EndPoint endpoint){
        Kryo serializer = endpoint.getKryo();
        //Can't register Connection so will have to switch with Integer
        serializer.register(HashMap.class);
        serializer.register(LinkedList.class);
        serializer.register(Rectangle.class);
        serializer.register(PlayerSoldier.class);
        serializer.register(Projectile.class);
        serializer.register(ServerProjectilePositions.class);
        serializer.register(ClientControlObject[].class);
        serializer.register(ClientControlObject.class);
        serializer.register(ClientKeysUpdate.class);
        serializer.register(ClientPickedLoadout.class);
        serializer.register(ClientInfoRequest.class);
        serializer.register(ClientConnectionRequest.class);
        serializer.register(ClientPickedTeam.class);
        serializer.register(ClientSentChatMessage.class);
        serializer.register(ClientKeysUpdate.class);
        serializer.register(ServerSummary.class);
        serializer.register(ServerResponse.class);
        serializer.register(ServerResponse.ResponseCode.class);
        serializer.register(ServerDetailedSummary.class);
        serializer.register(ServerGameCountdown.class);
        serializer.register(ServerSentChatMessage.class);
        serializer.register(ServerPlayerPositions.class);
        serializer.register(ServerGameStarted.class);
        serializer.register(ServerNotifyGame.class);
    }

    /**
     * An object that can either hold a boolean or float for a key's state or the mouse's angle
     */
    public static class ClientControlObject{
        //object types
        //0 - not assigned
        //1 - boolean
        //2 - float
        float angle = 0;
        boolean isDown = false;
        int type = 0;
        public ClientControlObject(){}
        public ClientControlObject(float angle){
            this.angle = angle;
            this.type = 2;
        }
        public ClientControlObject(boolean isDown){
            this.isDown = isDown;
            this.type = 1;
        }
        public float getAngle(){
            return angle;
        }
        public boolean getIsDown(){
            return isDown;
        }
        public int getType(){
            return type;
        }
    }
    /**
     *
     */
    public static class ClientKeysUpdate{
        ClientControlObject[] keys;
        public ClientKeysUpdate(){}
        public ClientKeysUpdate(ClientControlObject[] keys){
            this.keys = keys;
        }

        public ClientControlObject[] getKeys() {
            return keys;
        }
    }
    /**
     * sent to a client to tell them that they can start playing the game
     */
    public static class ServerNotifyGame{
        private int worldNum;
        public ServerNotifyGame(){

        }
        public ServerNotifyGame(int worldNum){
            this.worldNum = worldNum;
        }
        public int getWorldNum() {
            return worldNum;
        }
    }
    public static class ClientPickedLoadout{
        int loadout;
        //0 is knight
        //1 is archer
        //2 is tank
        public ClientPickedLoadout(){

        }

        public ClientPickedLoadout(int loadout) {
            this.loadout = loadout;
        }

        public int getLoadout() {
            return loadout;
        }
    }
    public static class ServerGameStarted{
        public ServerGameStarted(){}
    }
    public static class ServerPlayerPositions{
        HashMap<Integer, PlayerSoldier> players;
        public ServerPlayerPositions(){
        }
        public ServerPlayerPositions(HashMap<Integer, PlayerSoldier> players){
            this.players = players;
        }

        public HashMap<Integer, PlayerSoldier> getPlayers() {
            return players;
        }
    }
    public static class ServerProjectilePositions{
        private LinkedList<Projectile> projectiles;

        public ServerProjectilePositions() {
        }

        public ServerProjectilePositions(LinkedList<Projectile> projectiles) {
            this.projectiles = projectiles;
        }

        public LinkedList<Projectile> getProjectiles() {
            return projectiles;
        }
    }
    /**
     * Sent by the server to tell all clients that it's received a message
     */
    public static class ServerSentChatMessage{
        String message;
        public ServerSentChatMessage() {
        }

        /**
         * Sends the message to all clients, uses a hashmap of all players and a connection so that it'll automatically generate a message with the user's name and the message
         * without needing you to insert it yourself
         * @param message The message to be sent
         * @param sender The connection of the person who sent it
         * @param players All the connections and their respective playersoldiers
         */
        public ServerSentChatMessage(String message, Connection sender, HashMap<Integer, PlayerSoldier> players) {
            this.message = String.format("[WHITE]%s[GRAY]:  [BLACK]%s",players.get(sender.getID()),message);
        }

        /**
         * Similar to ServerSentChatMessage but with an Object so you don't have to convert it yourself
         * @param messageIn Supposed to be a ClientSentChatMessage object, will throw an error otherwise
         * @param sender The connection for the person who's sending the message
         * @param players The hashmap of all players connected
         */
        public ServerSentChatMessage(Object messageIn, Connection sender, HashMap<Integer, PlayerSoldier> players){
            if(messageIn instanceof  ClientSentChatMessage){
                this.message = String.format("[WHITE]%s[GRAY]:  [BLACK]%s",players.get(sender.getID()).getName(),((ClientSentChatMessage) messageIn).getMessage());
            }
            else{
                throw new ClassCastException("Object must be an instance of a ClientSentChatMessage");
            }
        }
    }
    /**
     * Sent by the client to tell the server that they've sent a message
     */
    public static class ClientSentChatMessage{
        String message;
        public ClientSentChatMessage(){

        }
        public ClientSentChatMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
    public static class ClientPickedTeam{
        //Red is 0 Blue is 1
        int picked; //to prevent creating too many unnecessary enums, I'm using an int here
        public ClientPickedTeam(){

        }
        public ClientPickedTeam(int picked){
            this.picked = picked;
        }

        public int getPicked() {
            return picked;
        }
    }
    /**
     * used in the lobby when the server is counting down to the actual game
     */
    public static class ServerGameCountdown{
        int seconds;
        public ServerGameCountdown(){

        }

        public ServerGameCountdown(int seconds) {
            this.seconds = seconds;
        }
        public int getSeconds(){
            return this.seconds;
        }
    }
    /**
     * TODO: determine what info should be sent in the detailed server summary
     */
    public static class ServerDetailedSummary {
        int rTeam, bTeam, rMax,bMax;
        HashMap<Integer, PlayerSoldier> people;
        public ServerDetailedSummary(){

        }
        public ServerDetailedSummary(int redTeam, int blueTeam,HashMap<Integer, PlayerSoldier> people){
            this.rTeam = redTeam;
            this.bTeam = blueTeam;
            this.bMax = people.size()/2;
            this.rMax = people.size() - bMax;
            this.people = people;
        }
    }
    /**
     * The response from the server for if the client has connected as a client or if they aren't allowed on
     */
    public static class ServerResponse{
        public enum ResponseCode{
            CLIENTCONNECTED,
            SERVERFULL
        }
        ResponseCode response;
        public ServerResponse(){}
        public ServerResponse(ResponseCode response){
            this.response = response;
        }
    }
    /**
     * Sent by the client to tell the server they want basic info about the server
     */
    public static class ClientInfoRequest {
        public ClientInfoRequest(){

        }
    }

    /**
     * Contains the ping, amount of people on, max people on, and server's name
     */
    public static class ServerSummary{
        public int num, max, ping;
        public String serverName;
        public ServerSummary(){

        }
        public ServerSummary(int numPlayers, int maxPlayers, int ping, String serverName){
            this.num = numPlayers;
            this.max = maxPlayers;
            this.ping = ping;
            this.serverName = serverName;
        }
        public String toString(){
            return String.format("%20s %d/%d %d",serverName.substring(0,Math.min(20,serverName.length())),num,max,ping);
        }
    }

    /**
     * Sent by the client when they wish to connect to the server to play
     */
    public static class ClientConnectionRequest {
        public String playerName;
        public ClientConnectionRequest(){
        }
        public ClientConnectionRequest(String playerName){
            this.playerName = playerName;
        }
    }
    public static class RequestWorld{
        public RequestWorld(){
        }
    }
    public static class WorldInfo{
        HashMap<Connection, PlayerSoldier> world;
        public WorldInfo(HashMap<Connection, PlayerSoldier> world){
            this.world = world;
        }
    }
}
