package me.dumfing.server;
//FILENAME
//Aaron Li  5/30/2017
//EXPLAIN

public class ServerEvent {
    public enum EventType{
        PLAYERCONNECTED,
        PLAYERPICKEDTEAM,
        PLAYERPICKEDCLASS
    }
    private Integer connectionID;
    private EventType eventType;

    public ServerEvent(EventType evtType, Integer connectionID) {
        this.eventType = evtType;
        this.connectionID = connectionID;
    }

    public Integer getConnectionID() {
        return connectionID;
    }

    public EventType getEventType() {
        return eventType;
    }
}
