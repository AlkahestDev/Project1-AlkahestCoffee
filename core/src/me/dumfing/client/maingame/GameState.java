package me.dumfing.client.maingame;

/**
 * Enum for what state the game is in (which menu is currently open, what's currently happening in the game)
 */
public enum GameState{
	LOADINGGAME, // loading all assets into the assetmanager and place them into variables
	MAINMENU, // Main menu with play, settings, and quit
	MAINMENUSETTINGS, // settings for the game
	SERVERBROWSER, // browse all active servers on the network
	STARTSERVER, // start your own server on the network
	CONNECTINGTOSERVER, // currently establishing a connection to the server
	CONNECTEDTOSERVER, // might not be used
	PICKINGTEAM, // pick a team to join
	PICKINGLOADOUT, // pick what class you are
	PLAYINGGAME, // playing the game
	ROUNDOVER, // end of round, show scoreboard etc.
	//return to main menu
	QUIT // exiting game
}
