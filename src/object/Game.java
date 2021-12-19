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
    private static int CurrentSide;

    Scanner sc = new Scanner(System.in);


    public Game(String name,Player white, Player black){
        super("Game_"+name, null);
        this.whitePlayer=white;
        this.blackPlayer=black;
        this.Grid = new DiskManager();
        this.addObj(Grid);
        this.whitePlayer.setColor(1);
        this.blackPlayer.setColor(-1);
        this.operationList = new ArrayList<>(0);
        this.CurrentSide = 1;
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
            this.operationList.add(operation);

        } else if (operator.getColor() == this.CurrentSide) {
            if (operation.type == SetDisk) {
                this.Grid.SetDisk(operation.position, operator);
                this.operationList.add(operation);
            }

            if (operation.type == MadeInHeaven) {
                this.Grid.forceSetDisk(operation.position, operator.getColor());
                this.operationList.add(operation);
            }

        } else if (operator.getColor() != this.CurrentSide){
            AttentionManager.showWarnMessage("Not you round");
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
        System.out.print(CurrentSide);
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

}
