package Object.inGame;

import newData.*;
import Object.*;
import java.util.ArrayList;

public class DiskManager extends OthelloObject {
    private Disk[][] Disks = new Disk[8][8];


    //创建64个棋
    public DiskManager(){
        super("DiskManager");
        initBoard();
    }

    public void initBoard(){
        int index = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                this.Disks[r][c] = new Disk(new intVct(r,c),index);
                ++index;
            }
        }
        this.Disks[3][4].setVisibility(true);
        this.Disks[3][4].setStatus(-1);
        this.Disks[4][3].setVisibility(true);
        this.Disks[4][3].setStatus(-1);

        this.Disks[3][3].setVisibility(true);
        this.Disks[3][3].setStatus(1);
        this.Disks[4][4].setVisibility(true);
        this.Disks[4][4].setStatus(1);


    }

    public void renewBoard(){
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                this.Disks[r][c].setStatus(0);
                this.Disks[r][c].setVisibility(false);
            }
        }
        this.Disks[3][4].setVisibility(true);
        this.Disks[3][4].setStatus(-1);
        this.Disks[4][3].setVisibility(true);
        this.Disks[4][3].setStatus(-1);

        this.Disks[3][3].setVisibility(true);
        this.Disks[3][3].setStatus(1);
        this.Disks[4][4].setVisibility(true);
        this.Disks[4][4].setStatus(1);

    }

    public void flipDisks(intVct bP) {
        this.Disks[bP.r][bP.c].flip();
    }



    //检查某个位置可否下棋子
    public boolean checkDisk(intVct bP, Player player) {
        for (intVct bp : validBP(player)) {
            if (bP.r == bp.r && bP.c == bp.c) {
                return true;
            }
        }
        return false;
    }

    //检查玩家可落子的地方
    public ArrayList<intVct> validBP(Player player) {
        ArrayList<intVct> validBP = new ArrayList<>();
        ArrayList<intVct> playerDisks = new ArrayList<>();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c<8; c++) {
                if (this.Disks[r][c].getVisbility() && this.Disks[r][c].getStatus() == player.getColor()){
                    playerDisks.add(new intVct(r,c));
                }
            }
        }   //获得所有当前玩家的棋子位置
        for (intVct bp : playerDisks) {
            //向上check
            if ( bp.r<=1){
            }else if (this.Disks[bp.r - 1][bp.c].getStatus() == player.getColor()*-1 && this.Disks[bp.r - 1][bp.c].getVisbility() && !this.Disks[bp.r - 2][bp.c].getVisbility()) {
                validBP.add(new intVct(bp.r-2,bp.c));
            }

            //向左check
            if ( bp.c<=1){
            }else if (this.Disks[bp.r][bp.c-1].getStatus() == player.getColor()*-1 && this.Disks[bp.r][bp.c - 1].getVisbility() && !this.Disks[bp.r][bp.c - 2].getVisbility()) {
                validBP.add(new intVct(bp.r,bp.c-2));
            }

            //向下Check
            if ( bp.r>=6){
            }else if (this.Disks[bp.r + 1][bp.c].getStatus() == player.getColor()*-1 && this.Disks[bp.r + 1][bp.c].getVisbility() && !this.Disks[bp.r + 2][bp.c].getVisbility()) {
                validBP.add(new intVct(bp.r+2,bp.c));
            }

            //向右check
            if ( bp.c>=6){
            }else if (this.Disks[bp.r][bp.c+1].getStatus() == player.getColor()*-1 && this.Disks[bp.r][bp.c + 1].getVisbility() && !this.Disks[bp.r][bp.c + 2].getVisbility()) {
                validBP.add(new intVct(bp.r,bp.c+2));
            }

            //左上check
            if ( bp.r<=1 || bp.c<=1){
            }else if (this.Disks[bp.r-1][bp.c-1].getStatus() == player.getColor()*-1 && this.Disks[bp.r - 1][bp.c - 1].getVisbility() && !this.Disks[bp.r - 2][bp.c - 2].getVisbility()) {
                validBP.add(new intVct(bp.r-2,bp.c-2));
            }

            //左下check
            if ( bp.r>=6 || bp.c<=1){
            }else if (this.Disks[bp.r+1][bp.c-1].getStatus() == player.getColor()*-1 && this.Disks[bp.r + 1][bp.c - 1].getVisbility() && !this.Disks[bp.r + 2][bp.c - 2].getVisbility()) {
                validBP.add(new intVct(bp.r+2,bp.c-2));
            }

            //右下check
            if ( bp.r>=6 || bp.c>=6){
            }else if (this.Disks[bp.r+1][bp.c+1].getStatus() == player.getColor()*-1 && this.Disks[bp.r + 1][bp.c + 1].getVisbility() && !this.Disks[bp.r + 2][bp.c + 2].getVisbility()) {
                validBP.add(new intVct(bp.r+2,bp.c+2));
            }

            //右上check
            if ( bp.r<=1 || bp.c>=6){
            }else if (this.Disks[bp.r-1][bp.c+1].getStatus() == player.getColor()*-1 && this.Disks[bp.r - 1][bp.c + 1].getVisbility() && !this.Disks[bp.r - 2][bp.c + 2].getVisbility()) {
                validBP.add(new intVct(bp.r-2,bp.c+2));
            }





        } //获得所有当前玩家可以下子的位置列表
        validBP = cleanArray(validBP);
        return validBP;
    }

    //玩家落子
    public boolean SetDisk(intVct bp, Player player) {
            if (checkDisk(bp, player)) {
                forceSetDisk(bp, player.getColor());
                this.Disks[bp.r][bp.c].setVisibility(true);

                //向上check
                if ( bp.r<=1){
                }else if (this.Disks[bp.r - 1][bp.c].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r - 1][bp.c].getVisbility()
                        && this.Disks[bp.r - 2][bp.c].getStatus() == player.getColor()
                        && this.Disks[bp.r - 2][bp.c].getVisbility()) {
                    flipDisks(new intVct(bp.r - 1,bp.c));
                }

                //向左check
                if ( bp.c<=1){
                }else if (this.Disks[bp.r][bp.c-1].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r][bp.c - 1].getVisbility()
                        && this.Disks[bp.r][bp.c-2].getStatus() == player.getColor()
                        && this.Disks[bp.r][bp.c - 2].getVisbility()) {
                    flipDisks(new intVct(bp.r,bp.c -1));
                }

                //向下Check
                if ( bp.r>=6){
                }else if (this.Disks[bp.r + 1][bp.c].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r + 1][bp.c].getVisbility()
                        && this.Disks[bp.r + 2][bp.c].getStatus() == player.getColor()
                        && this.Disks[bp.r + 2][bp.c].getVisbility()) {
                    flipDisks(new intVct(bp.r + 1,bp.c));
                }

                //向右check
                if ( bp.c>=6){
                }else if (this.Disks[bp.r][bp.c+1].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r][bp.c + 1].getVisbility()
                        && this.Disks[bp.r][bp.c+2].getStatus() == player.getColor()
                        && this.Disks[bp.r][bp.c + 2].getVisbility()) {
                    flipDisks(new intVct(bp.r,bp.c + 1));
                }

                //左上check
                if ( bp.r<=1 || bp.c<=1){
                }else if (this.Disks[bp.r-1][bp.c-1].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r - 1][bp.c - 1].getVisbility()
                        && this.Disks[bp.r-2][bp.c-2].getStatus() == player.getColor()
                        && this.Disks[bp.r - 2][bp.c - 2].getVisbility()) {
                    flipDisks(new intVct(bp.r - 1,bp.c-1));
                }

                //左下check
                if ( bp.r>=6 || bp.c<=1){
                }else if (this.Disks[bp.r+1][bp.c-1].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r + 1][bp.c - 1].getVisbility()
                        && this.Disks[bp.r+2][bp.c-2].getStatus() == player.getColor()
                        && this.Disks[bp.r + 2][bp.c - 2].getVisbility()) {
                    flipDisks(new intVct(bp.r + 1,bp.c-1));
                }

                //右下check
                if ( bp.r>=6 || bp.c>=6){
                }else if (this.Disks[bp.r+1][bp.c+1].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r + 1][bp.c + 1].getVisbility()
                        && this.Disks[bp.r+2][bp.c+2].getStatus() == player.getColor()
                        && this.Disks[bp.r + 2][bp.c + 2].getVisbility()) {
                    flipDisks(new intVct(bp.r + 1,bp.c+1));
                }

                //右上check
                if ( bp.r<=1 || bp.c>=6){
                }else if (this.Disks[bp.r-1][bp.c+1].getStatus() == player.getColor()*-1
                        && this.Disks[bp.r - 1][bp.c + 1].getVisbility()
                        && this.Disks[bp.r-2][bp.c+2].getStatus() == player.getColor()
                        && this.Disks[bp.r - 2][bp.c + 2].getVisbility()) {
                    flipDisks(new intVct(bp.r - 1,bp.c+1));
                }
                return true;
            }
            return false;
    }


//    //换边
//    public void roundsTurn(Player toPlayer) {
//        for (int r = 0; r < 8; r++) {
//            for (int c = 0; c < 8; c++) {
//                if(!this.Disks[r][c].getVisbility()){
//                    this.Disks[r][c].setStatus(toPlayer.getColor());
//                }
//            }
//        }
//    }

    //设置某个子
    public void forceSetDisk(intVct bP, int status) {
        this.Disks[bP.r][bP.c].setOnBoard();
        this.Disks[bP.r][bP.c].setStatus(status);
    }

    //获取某个子的状态
    public int getDiskstatus(intVct bP) {
        return this.Disks[bP.r][bP.c].getStatus();
    }

    //打印棋盘
    public void printGrid() {
        System.out.println();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (this.Disks[r][c].getVisbility()){
                    if (this.getDiskstatus(new intVct(r, c))==-1) {
                        System.out.print(" "+ -1);
                    }else{
                        System.out.print("  "+1);
                    }
                } else {
                    System.out.print("  "+0);
                }
            }
            System.out.println();
        }
    }

    public ArrayList cleanArray(ArrayList<intVct> object) {
        for (int a = 0; a < object.size(); a++) {
            for (int b = 0; b < a; b++) {
                if (object.get(b).r == object.get(a).r && object.get(b).c == object.get(a).c) {
                    object.remove(b);
                    --a;
                }
            }
        }
        return object;
    }



}
