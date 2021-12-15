package object;

import graphics.Shape;

public class Player extends OthelloObject{
    private String playerID;
    private String playerName;
    private String username;
    private static int winCnt;
    private static int played;
    private boolean Active;
    private boolean Ready;
    private int color;

//    private boolean isOnline;
//    private boolean isLocal;


    private int Score;
    private boolean isCheating;
    public static enum playerType {
        local, Internet, AI
    }

    private playerType type;

    public Player(String playerID, String playerName, playerType type) {
        super("Player_" + playerName);
        this.playerName = playerName;
        this.playerID = playerID;
        this.Ready = false;
        this.Active = false;
        this.isCheating = false;
        this.color = 0;
        this.winCnt = 0;
        this.played = 0;
        this.type = type;

    }

    public boolean getReady() {
        return this.Ready;
    }

    public void setReady(boolean bool) {
        this.Ready = bool;
    }

    public String getName() {
        return this.playerName;
    }

    public void reName(String name) {
        this.playerName = name;
    }

    public String getUsername() {
        return this.username;
    }

    public int getColor() {
        return this.color;
    }

    public int getWinCnt(){
        return winCnt;
    }

    public void winCntplus(int a) {
        this.winCnt +=a;
    }

    public void setColor(int color) {
        this.color = color;
    }





}
