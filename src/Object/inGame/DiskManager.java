package Object.inGame;

import newData.*;
import Object.*;
import java.util.ArrayList;

public class DiskManager extends OthelloObject {
    private Disk[][] Disks = new Disk[8][8];

    //创建64个棋
    public DiskManager(){
        super("DiskManager");
        int index = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                this.Disks[r][c] = new Disk(new intVct(r,c),index);
                ++index;
            }
        }
    }

    public void flipDisks(intVct bP) {
        this.Disks[bP.r][bP.c].flip();
    }


    //换边
    public void roundsTurn(int toPlayer) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if(this.Disks[r][c].isonBoard() == false){
                    this.Disks[r][c].setStatus(toPlayer);
                }
            }
        }
    }

    //落子
    public void setonDisk(intVct bP) {
        this.Disks[bP.r][bP.c].setOnBoard();
    }

    public int getDiskstatus(intVct bP) {
        return this.Disks[bP.r][bP.c].getStatus();
    }

    //打印棋盘
    public void printGrid() {
        System.out.println();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (this.Disks[r][c].getVisbility() == true){
                    System.out.print(this.getDiskstatus(new intVct(r, c)));
                } else {
                    System.out.print(0);
                }
                System.out.print("  ");
            }
            System.out.println();
        }
    }

}
