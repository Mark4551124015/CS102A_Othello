package object;

import graphics.Shape;
import net.sf.json.JSONObject;

import static object.Player.playerType.local;

public class Player extends OthelloObject{
    private String playerName;
    private final String username;
    private int winCnt;
    private int played;
    private boolean Ready;
    private int color;
    private int reCalledTime;
    private boolean Cheating;

//    private boolean isOnline;
//    private boolean isLocal;


    private final boolean isCheating;

    public enum playerType {
        local, Internet, AI
    }

    private final playerType type;

    public Player(String username, String playerName, playerType type) {
        super("Player_" + username);
        this.playerName = playerName;
        this.username = username;
        this.Ready = false;
        this.isCheating = false;
        this.color = 0;
        this.winCnt = 0;
        this.played = 0;
        this.reCalledTime = 0;
        this.type = type;
        this.Cheating = false;
    }

    public Player(JSONObject json) {
        this(json.getString("username"), json.getString("playerName"), local );
        this.winCnt = json.getInt("winCnt");
        this.played = json.getInt("roundPlayed");
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

    public void winCntPlus(int a) {
        this.winCnt +=a;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getPlayed() {
        return this.played;
    }

    public playerType getType() {
        return this.type;
    }

    public JSONObject toJson() {
        if (this == null) {
            System.out.println("Failed to handle null Player");
            return null;
        }
        JSONObject jsonPlayer = new JSONObject();
        jsonPlayer.put("username", this.getUsername());
        jsonPlayer.put("playerName", this.getName());
        jsonPlayer.put("roundPlayed", this.getPlayed());
        jsonPlayer.put("winCnt", this.getWinCnt());
        jsonPlayer.put("type", this.getType());
        return jsonPlayer;
    }

    private static playerType fromString(String str) {
        switch (str) {
            case "local":
                return local;
            case "Internet":
                return playerType.Internet;
            case "AI":
                return playerType.AI;
        }
        return null;
    }

    public boolean isLocal() {
        return this.getType() == local;
    }

    public void resetForGame() {
        this.Ready = false;
    }

    public void reCalled(){
        ++this.reCalledTime;
    }

    public int getReCalledTime(){
        return this.reCalledTime;
    }

    public void renewReCalledTime(){
        this.reCalledTime = 0;
    }

    public void setCheating(boolean flag) {
        this.Cheating = flag;
    }
    public boolean isCheating() {
        return this.Cheating;
    }

    public void played(){
        ++this.played;
    }
}

