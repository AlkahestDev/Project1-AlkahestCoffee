package me.dumfing.client.maingame;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Enum for what state the game is in (which menu is currently open, what's currently happening in the game)
 */
public class GameState{
	public static HashSet<State> ONLINESTATES= new HashSet<State>(Arrays.asList(new State[]{
			State.CONNECTINGTOSERVER,
			State.CONNECTEDTOSERVER,
			State.PICKINGTEAM,
			State.PICKINGLOADOUT,
			State.PLAYINGGAME,
			State.ROUNDOVER,
			State.GAMELOBBY
	})); // HashSet of all states that involve online interaction with the server
	public enum State{
		LOADINGGAME, // loading all assets into the assetmanager and place them into variables
		MAINMENU, // Main menu with play, settings, and quit
		MAINMENUSETTINGS, // settings for the game
		SERVERBROWSER, // browse all active servers on the network
		STARTSERVER, // start your own server on the network
		CONNECTINGTOSERVER, // currently establishing a connection to the server
		CONNECTEDTOSERVER, // might not be used
		GAMELOBBY,
		PICKINGTEAM, // pick a team to join
		PICKINGLOADOUT, // pick what class you are
		PLAYINGGAME, // playing the game
		ROUNDOVER, // end of round, show scoreboard etc.
		//return to main menu
		QUIT, // exiting game
	}
}
