package object;

import main.AttentionManager;
import net.sf.json.JSONObject;
import newData.Operation;
import object.inGame.DiskManager;
import newData.intVct;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static newData.Operation.Operation_Type.*;
import static object.PlayerManager.Competitor;
import static object.PlayerManager.User;
import static util.Tools.saveDataToFile;

public class Game extends OthelloObject{
    private String name;
    private Player whitePlayer;
    private Player blackPlayer;
    private DiskManager Grid;
    private int round;
    private Player winner = null;
    private static int CurrentSide;


    public static final String gamePath = "games/";

    public static final String gameInfoName = "gameInfo";


    Scanner sc = new Scanner(System.in);


    public Game(Player white, Player black){
        super("Game", null);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        this.name = "Game_"+formatter.format(date);
        this.whitePlayer=white;
        this.blackPlayer=black;
        this.Grid = new DiskManager();
        this.addObj(Grid);
        this.whitePlayer.setColor(1);
        this.blackPlayer.setColor(-1);
        this.CurrentSide = 1;
    }


    public String getName() {
        return this.name;
    }

    public void start() {
        this.round = 1;
    }

    public Player getWinner(){
        return this.winner;
    }

    public void renew(){
        this.Grid.renewBoard();
    }

    public void update(double dt) {
        super.update(dt);

        if ((this.winner == null || !this.gameEnd() )) {
            if (this.Grid.validBP(this.whitePlayer).isEmpty()) {
                this.CurrentSide = -1;
            }
            if (this.Grid.validBP(this.blackPlayer).isEmpty()) {
                this.CurrentSide = 1;
            }
        }

    }

    public DiskManager getGrid(){
        return this.Grid;
    }

    public boolean gameEnd() {
        if(this.Grid.validBP(this.whitePlayer).isEmpty() && this.Grid.validBP(this.blackPlayer).isEmpty()) {
            return true;
        }
        return false;
    }

    public static void switchRound() {
        CurrentSide *= -1;
    }

    public void gameEnding(){
        if (this.winner == null) {
            System.out.println("Draw");
        }
        else {
            this.winner.winCntPlus(1);
            System.out.println("Winner is " + this.winner.getId());
        }
    }

    public int getCurrentSide() {
        return this.CurrentSide;
    }

    public Player getPlayer(int a){
        if ( a == 1) {
            return this.whitePlayer;
        }
        if ( a == -1) {
            return this.blackPlayer;
        }
        return null;
    }

    private JSONObject gameInfoToJson() {
        JSONObject gameInfo = new JSONObject();
        gameInfo.put("white_Player", this.whitePlayer.getUsername());
        gameInfo.put("black_Player", this.blackPlayer.getUsername());
        gameInfo.put("Name", this.name);
        return gameInfo;
    }

    public void setName(String name){
        this.name = name;
    }

    public void save() {
        saveDataToFile(gamePath+this.getName()+"/" + gameInfoName,gameInfoToJson().toString());
    }

    public void setWinner(Player player) {
        this.winner = player;
    }

    public void loadGameInfo(JSONObject json) {
        this.name = json.getString("Name");
        if (User.getUsername() == json.getString("white_Player") ){
            this.whitePlayer = User;
            this.blackPlayer = Competitor;
        } else {
            this.whitePlayer = Competitor;
            this.blackPlayer = User;
        }
    }


}
