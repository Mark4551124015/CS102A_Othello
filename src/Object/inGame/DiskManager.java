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

    //检查某个位置可否下棋子
    public boolean checkDisk(intVct bP, int status) {
        if (this.Disks[bP.r][bP.c] != null) {
            return false;
        }if (this.Disks[][])
    }

    //检查玩家可落子的地方
    public ArrayList validBP(intVct bP, int status) {
        ArrayList<intVct> validBP = new ArrayList<>(0);

        if() {}
    }

    //换边
    public void roundsTurn(int toPlayer) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if(!this.Disks[r][c].isOnBoard()){
                    this.Disks[r][c].setStatus(toPlayer);
                }
            }
        }
    }

    //设置某个子
    public void setDisk(intVct bP, int status) {
        this.Disks[bP.r][bP.c].setOnBoard();
        this.Disks[bP.r][bP.c].setStatus(status);
    }

    //获取某个子的状态
    public int getDiskStatus(intVct bP) {
        return this.Disks[bP.r][bP.c].getStatus();
    }

    //打印棋盘
    public void printGrid() {
        System.out.println();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (this.Disks[r][c].getVisbility()){
                    System.out.print(this.getDiskStatus(new intVct(r, c)));
                } else {
                    System.out.print(0);
                }
                System.out.print("  ");
            }
            System.out.println();
        }
    }

    public void dropDisk(intVct bP, Player player) {
        setDisk(bP,player.getColor());

    }



}
