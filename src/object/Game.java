package object;

import object.inGame.DiskManager;
import newData.intVct;

import java.util.Scanner;

public class Game extends OthelloObject{
    private int gameID;
    private String name;
    private Player whitePlayer;
    private Player blackPlayer;
    private DiskManager Grid;
    private int round;
    private Player winner;
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
            this.Grid.printGrid();
            if(this.Grid.validBP(this.whitePlayer).isEmpty()){} else {
                for (intVct valid : this.Grid.validBP(this.whitePlayer)) {
                    System.out.print("["+valid.r+","+valid.c+"] ");
                }
                while(true) {
//                    intVct pos = new intVct(sc.nextInt(), sc.nextInt());
                    int num = (int)(Math.random()*this.Grid.validBP(this.whitePlayer).size());
                    intVct pos = this.Grid.validBP(this.whitePlayer).get(num);
                    if (this.Grid.checkDisk(pos, this.whitePlayer)) {
                        this.Grid.SetDisk(pos,this.whitePlayer);
                        break;
                    }else {
                        System.out.print("你下你马呢，能下的只有：");
                        for (intVct valid : this.Grid.validBP(this.whitePlayer)) {
                            System.out.print("["+valid.r+","+valid.c+"] ");
                        }
                    }
                }
            }
            this.Grid.printGrid();
            if(this.Grid.validBP(this.blackPlayer).isEmpty()){} else {
                for (intVct valid : this.Grid.validBP(this.blackPlayer)) {
                    System.out.print("["+valid.r+","+valid.c+"] ");
                }
                while(true) {
//                    intVct pos = new intVct(sc.nextInt(), sc.nextInt());
                    int num = (int)(Math.random()*this.Grid.validBP(this.blackPlayer).size());
                    intVct pos = this.Grid.validBP(this.blackPlayer).get(num);
                    if (this.Grid.checkDisk(pos, this.blackPlayer)) {
                        this.Grid.SetDisk(pos,this.blackPlayer);
                        break;
                    }else {
                        System.out.print("你下你马呢，能下的只有：");
                        for (intVct valid : this.Grid.validBP(this.blackPlayer)) {
                            System.out.print("["+valid.r+","+valid.c+"] ");
                        }
                    }
                }
            }
            ++this.round;
            if(this.Grid.validBP(this.whitePlayer).isEmpty()&&this.Grid.validBP(this.blackPlayer).isEmpty()) {
                break;
            }

        }

        winner = this.Grid.checkWinner(this.whitePlayer,this.blackPlayer);
        if (winner == null) {
            System.out.println("Draw");
        }
        else {
            this.winner.winCntPlus(1);
            System.out.println("Winner is " + this.winner.getId());
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

}
