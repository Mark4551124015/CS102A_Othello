package object;

import net.sf.json.JSONObject;
import newData.intVct;
import object.inGame.DiskManager;
import object.inGame.Hinter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static main.PlayerManager.Competitor;
import static main.PlayerManager.User;
import static object.inGame.DiskManager.ReadyForNextOperation;
import static util.Tools.saveDataToFile;

public class Game extends OthelloObject{
    private String name;
    private Player whitePlayer;
    private Player blackPlayer;
    private final boolean Hint;
//    private boolean GameEnd;
    private int GameEndingState;
    private final DiskManager Grid;
    private int round;
    private static boolean hinted;
    private Player winner = null;
    private static int CurrentSide;
    static ArrayList<Hinter> hinters;

    public static final String gamePath = "games/";

    public static final String gameInfoName = "gameInfo";

    public Game(Player white, Player black){
        super("Game", null);
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'_'HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        hinters = new ArrayList<>();
        this.name = "Game_"+formatter.format(date);
        this.setId(this.name);
        this.whitePlayer=white;
        this.whitePlayer.setColor(1);
        this.blackPlayer=black;
        this.blackPlayer.setColor(-1);
        this.Grid = new DiskManager();
        this.addObj(Grid);

        this.Hint = true;
//        this.GameEnd = false;
        this.GameEndingState = 0;
        hinted = false;
        CurrentSide = 1;
        User.renewReCalledTime();
        Competitor.renewReCalledTime();
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
        User.renewReCalledTime();
        Competitor.renewReCalledTime();

        this.setHinted(false);
        CurrentSide = 1;
    }

    public void update(double dt) {
        super.update(dt);
        if (this.GameEndingState ==0) {
            this.Hint();

            if ((this.winner == null && !this.checkGameEnd() && this.GameEndingState == 0)) {
                if (this.Grid.validBP(this.whitePlayer).isEmpty()) {
                    CurrentSide = -1;
                }
                if (this.Grid.validBP(this.blackPlayer).isEmpty()) {
                    CurrentSide = 1;
                }
            }
            if(checkGameEnd()) {
            }
        }

    }

    public DiskManager getGrid(){
        return this.Grid;
    }

    public boolean checkGameEnd() {
        if(this.Grid.validBP(this.whitePlayer).isEmpty() && this.Grid.validBP(this.blackPlayer).isEmpty()) {
            this.GameEndingState = 1;
            return true;
        }
        return false;
    }

    public static void switchRound() {
        CurrentSide *= -1;
        hinted=false;
    }



    public int getCurrentSide() {
        return CurrentSide;
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

        File file = new File("./save/" + gamePath+this.getName());
        if (!file.exists()) {
            file.mkdir();
        }
        saveDataToFile(gamePath + this.getName()+"/" + gameInfoName,gameInfoToJson().toString());
    }

    private void Hint() {
        if (hinted) {
            return;
        }
        if (ReadyForNextOperation != 0) {
            for (Hinter index : hinters) {
                index.setVisibility(false);

            }
            return;
        }
        if (this.getPlayer(getCurrentSide()).getUsername().equals("AI")) {
            for (Hinter index : hinters) {
                index.setVisibility(false);
            }
            hinters.clear();
            return;
        }
        if (this.Hint && !hinted) {
            for (Hinter index : hinters) {
                index.setVisibility(false);
            }
            hinters.clear();
            hinted = true;
            for (intVct vct : this.getGrid().validBP(getPlayer(getCurrentSide()))) {
                Hinter hinter = new Hinter();
                hinter.setPosition(this.Grid.Disks[vct.r][vct.c].getTrans().position);
                hinters.add(hinter);
            }
            for (Hinter index : hinters) {
                this.Grid.addObj(index);
                index.Emerge(true);
            }
        }

    }

    public int getBlackCnt() {
        return this.Grid.getBlackCnt();
    }

    public int getWhiteCnt() {
        return this.Grid.getWhiteCnt();
    }

    public void setWinner(Player player) {
        this.winner = player;
    }

    public void loadGameInfo(JSONObject json) {
        this.name = json.getString("Name");
        if (User.getUsername().equals(json.getString("white_Player"))){
            this.whitePlayer = User;
            this.blackPlayer = Competitor;
            User.setColor(1);
            Competitor.setColor(-1);
        } else {
            this.whitePlayer = Competitor;
            this.blackPlayer = User;
            User.setColor(-1);
            Competitor.setColor(1);
        }
    }

    public void playerRecall(Player player) {
        if (player.getReCalledTime() >= 3) {
            System.out.println("Recalled Too much");
            return;
        }
        if (this.Grid.getLastDisk() != null) {
            if (player.getColor() == this.Grid.getLastDisk().getStatus()) {
                this.Grid.recallLastDisk();
                player.reCalled();
                CurrentSide = player.getColor();
                return;
            }
        }
        System.out.println("Too late");

    }

    public void setHinted(boolean flag) {
        hinted = flag;
    }

    public void endTheGame() {
        this.GameEndingState = 1;
        for (Hinter index : hinters) {
            index.setVisibility(false);
            index.detachParentObj();
        }
        hinters.clear();
    }

//    public boolean isGameEnd(){
//        return this.GameEnd;
//    }

    public int getGameEndingState(){
        return this.GameEndingState;
    }
    public void setGameEndingState(int a) {
        this.GameEndingState = a;
    }
    public void setCurrentPlayer(int a){
        CurrentSide = a;
    }
}
