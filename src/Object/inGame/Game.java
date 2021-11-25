package Object.inGame;


import java.util.ArrayList;
import Object.Player;
import newData.Operation;

public class Game {
    private int gid;
    private String name;
    private static int gameCnt=1;
    private Player whitePlayer;
    private Player blackPlayer;
    private ArrayList<Operation> operationArrayList;
    private int[][] board = new int[8][8];

    public Game(String name, Player whitePlayer, Player blackPlayer) {
        this.name = name;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.gid = gameCnt;
        ++gameCnt;
        this.operationArrayList = new ArrayList<>(0);
    }

    public Game() {
        String name = "Game_" + gid;
        Player white = new Player("White","White");
        Player black = new Player("Black","black");
        new Game(name,white,black);
    }

//    public boolean checkStep(int sid) {
//        for (Step index : this.stepList) {
//            if (sid == index.getSid()){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean addStep(Step step) {
//        if (checkStep(step.getSid())) {
//            return false;
//        }
//        this.stepList.add(step);
//        return true;
//    }

    public int getGid() {
        return this.gid;
    }

    public String getName() {
        return this.name;
    }

//    public ArrayList<Step> getStepList() {
//        return stepList;
//    }

    public static int getGameCnt() {
        return gameCnt;
    }

    public Player getWhitePlayer() {
        return this.whitePlayer;
    }

    public Player getBlackPlayer(){
        return this.blackPlayer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[][] getBoard() {
        return this.board;
    }

//    public String toString() {
//        String steps = "";
//        for (Step temp : stepList) {
//
//        }
//        if (steps.isEmpty() == false) {
//            steps = steps.substring(0, steps.length() - 1);
//        }
//        return "Game: " + this.getName() +
//                ", gid: " + this.getGid() +
//                ", white" + this.getWhitePlayer() +
//                ", black" + this.getBlackPlayer() +
//                ", stepList: [" +  steps +
//                "], board: " + Arrays.deepToString(this.board);
//    }


}

