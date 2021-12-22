package object.inGame;

import newData.direction;
import newData.intVct;
import object.OthelloObject;
import object.Player;
import util.Tools;

import java.util.ArrayList;

import static util.Tools.cleanArray;

public class DiskManager_AI extends OthelloObject {
    private Disk[][] Disks = new Disk[8][8];
    private final direction direction = new direction();
    private Disk lastDisk;
    private ArrayList<intVct> lastFlippedDisks;
    private static int DMCnt=0;

    //创建64个棋
    public DiskManager_AI(){
        super("DiskManager_AI_"+DMCnt,null);
        initBoard();
        ++DMCnt;
    }

    public void initBoard(){
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                this.Disks[r][c] = new Disk(new intVct(r,c));
                this.addObj(this.Disks[r][c]);
            }
        }

        this.Disks[3][4].setStatus(-1);

        this.Disks[4][3].setStatus(-1);


        this.Disks[3][3].setStatus(1);

        this.Disks[4][4].setStatus(1);

        this.lastFlippedDisks = new ArrayList<>(0);

    }

    public void renewBoard(){
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                this.Disks[r][c].setStatus(0);
            }
        }
        this.Disks[3][4].setStatus(-1);
        this.Disks[4][3].setStatus(-1);
        this.Disks[3][3].setStatus(1);
        this.Disks[4][4].setStatus(1);

    }

    public void flipDisk(intVct bP) {
        this.Disks[bP.r][bP.c].simpleFlip();
    }

    public void recallDisk(intVct bP){
        this.Disks[bP.r][bP.c].setStatus(0);
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
                if (this.Disks[r][c].getVisibility() && this.Disks[r][c].getStatus() == player.getColor()){
                    playerDisks.add(new intVct(r,c));
                }
            }
        }   //获得所有当前玩家的棋子位置
        for (intVct bp : playerDisks) {
            for (intVct index : check(bp, player, checkPurpose_AI.valid)){
                validBP.add(index);
            }
        }
        cleanArray(validBP);
        return validBP;
    }

    //玩家落子
    public boolean SetDisk(intVct bp, Player player) {
        this.lastFlippedDisks = new ArrayList<>(0);
        this.lastDisk = Disks[bp.r][bp.c];
        if (checkDisk(bp, player)) {
                forceSetDisk(bp, player.getColor());
                for (intVct index : check(bp, player, checkPurpose_AI.Flip)) {
                    this.lastFlippedDisks.add(index);
                    this.flipDisk(index);
                }
                return true;
            }
            return false;
    }

    //设置某个子
    public void forceSetDisk(intVct bP, int status) {
        this.Disks[bP.r][bP.c].setOnBoard();
        this.Disks[bP.r][bP.c].setStatus(status);
        this.Disks[bP.r][bP.c].setVisibility(true);
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
                if (this.Disks[r][c].getVisibility()){
                    if (this.getDiskStatus(new intVct(r, c))==-1) {
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

    //查找
    public ArrayList<intVct> check(intVct disk, Player player, checkPurpose_AI purpose){
        ArrayList<intVct> num = new ArrayList<>();
        //查找目的是翻棋子
        if(purpose == checkPurpose_AI.Flip) {
            int alongStatus = player.getColor()*-1;
            int targetStatus = player.getColor();
            for (intVct index : direction.list) {
                int k = 1; //每个方向k初始值都是1
                while (true) {
                    if(        disk.r + k * index.r >= 0
                            && disk.r + k * index.r <= 7
                            && disk.c + k * index.c >= 0
                            && disk.c + k * index.c <= 7
                    ){
                        if (this.Disks[disk.r + k * index.r][disk.c + k * index.c].getStatus() == targetStatus) {
                            break;  //找到目标颜色跳出循环
                        } else
                        if (this.Disks[disk.r + k * index.r][disk.c + k * index.c].getStatus() == alongStatus) {
                            ++k;   //如果碰到的是可以继续探索的颜色，让k+1
                        } else {
                            k = 1;   //碰到的既不是目标颜色 也不是可追踪颜色，跳出循环放弃方向
                            break;
                        }
                    } else {
                        k = 1;
                        break; //在找到目标颜色之前越界，直接跳出循环，放弃这个方向
                    }
                }
                //返回沿途坐标
                for (int i = 1; i < k; i++) {
                    num.add(new intVct(disk.r + i * index.r, disk.c + i * index.c));
                }

            }
        }
        //查找目的是找合理的落子点
        else if(purpose == checkPurpose_AI.valid) {
            int alongStatus = player.getColor()*-1;
            int targetStatus = 0;
            for (intVct index : direction.list) {
                int k = 1; //每个方向k初始值都是1
                while (true) {
                    if(        disk.r + k * index.r >= 0
                            && disk.r + k * index.r <= 7
                            && disk.c + k * index.c >= 0
                            && disk.c + k * index.c <= 7
                    ){
                        if (this.Disks[disk.r + k * index.r][disk.c + k * index.c].getStatus() == targetStatus) {
                            break;  //找到目标颜色跳出循环
                        } else
                        if (this.Disks[disk.r + k * index.r][disk.c + k * index.c].getStatus() == alongStatus) {
                            ++k;   //如果碰到的是可以继续探索的颜色，让k+1
                        } else {
                            k = 1;   //碰到的既不是目标颜色 也不是可追踪颜色，跳出循环放弃方向
                            break;
                        }
                    } else {
                        k = 1;
                        break; //在找到目标颜色之前越界，直接跳出循环，放弃这个方向
                    }
                }
                //如果k大于1添加最终坐标(不大于1 说明上来就碰到了空格)
                if (k > 1) {
                    num.add(new intVct(disk.r + k * index.r, disk.c + k * index.c));
                }
            }
        }
        Tools.cleanArray(num);
        return num;
    }

    public enum checkPurpose_AI {
        Flip,valid
    }

    //查找胜者
    public Player checkWinner(Player a, Player b) {
        int diskCnt = 0 ;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c<8; c++) {
                diskCnt += this.Disks[r][c].getStatus();
            }
        }
        if (diskCnt * a.getColor() > 0) {
            return a;
        }
        if (diskCnt * b.getColor() > 0) {
            return b;
        }else{
            return null;
        }
    }

    public void recallLastDisk(){
        this.recallDisk(this.lastDisk.getBP());
        for (intVct index : this.lastFlippedDisks) {
            this.flipDisk(index);
        }
        this.lastFlippedDisks.clear();
    }

    public Disk getLastDisk(){
        return this.lastDisk;
    }

    public void loadDisks(Disk[][] disks){
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c<8; c++) {
                this.Disks[r][c] = disks[r][c];
            }
        }
    }


}
