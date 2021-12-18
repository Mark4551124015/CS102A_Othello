package object;

import main.AttentionManager;
import newData.Operation;
import object.inGame.DiskManager;
import newData.intVct;

import java.util.ArrayList;
import java.util.Scanner;

import static newData.Operation.Operation_Type.*;

public class Game extends OthelloObject{
    private int gameID;
    private String name;
    private Player whitePlayer;
    private Player blackPlayer;
    private DiskManager Grid;
    private int round;
    private Player winner = null;
    private ArrayList<Operation> operationList;
    private int CurrentPlayer;

    Scanner sc = new Scanner(System.in);


    public Game(String name,Player white, Player black){
        super("Game_"+name, null);
        this.whitePlayer=white;
        this.blackPlayer=black;
        this.Grid = new DiskManager();
        this.addObj(Grid);
    }

    public void start() {
        this.round = 1;


        while (true) {

            //白先

            if(this.Grid.validBP(this.whitePlayer).isEmpty()){
                this.CurrentPlayer = -1;
            } else {
                this.CurrentPlayer = 1;
            }

            //检查胜者
            if (this.winner != null) {
                break;
            }
            if (this.winnerCheck()) {
                break;
            }

            if(this.Grid.validBP(this.blackPlayer).isEmpty()){
                this.CurrentPlayer = 1;
            } else {
                this.CurrentPlayer = -1;
            }

            ++this.round;
            if (this.winner != null) {
                break;
            }
            if (this.winnerCheck()) {
                break;
            }

        }

        //没有胜者就计算棋盘
        if (!this.winnerCheck()) {
            this.winner = this.Grid.checkWinner(this.whitePlayer, this.blackPlayer);
        }





    }

    public Player getWinner(){
        return this.winner;
    }

    public void renew(){
        this.Grid.renewBoard();
    }

    public void update(double dt) {
        super.update(dt);

    }

    public void OperationHandler(Operation operation) {
        Player operator = null;
        if (this.whitePlayer.getUsername() == operation.Operator) {
            operator = this.whitePlayer;
        }
        if (this.blackPlayer.getUsername() == operation.Operator) {
            operator = this.blackPlayer;
        }


        if (operation.type == Surrender) {
            if (operator.getColor() == 1) {
                this.winner = this.blackPlayer;
            } else if (operator.getColor() == -1) {
                this.winner = this.whitePlayer;
            }
            operationList.add(operation);
            switchRound();
        }

        if (operator.getColor() == CurrentPlayer) {

            if (operation.type == SetDisk) {
                this.Grid.SetDisk(operation.position, operator);
                operationList.add(operation);
                switchRound();
            }

            if (operation.type == MadeInHeaven) {
                this.Grid.forceSetDisk(operation.position, operator.getColor());
                operationList.add(operation);
            }

        } else {
            AttentionManager.showWarnMessage("Not you round");
        }




    }

    public DiskManager getGrid(){
        return this.Grid;
    }

    public boolean winnerCheck() {
        if(this.Grid.validBP(this.whitePlayer).isEmpty()&&this.Grid.validBP(this.blackPlayer).isEmpty()) {
            return true;
        }
        return false;
    }

    public void switchRound() {
        CurrentPlayer *= -1;
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

}
