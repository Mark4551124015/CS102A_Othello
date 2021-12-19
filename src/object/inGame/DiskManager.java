package object.inGame;

import graphics.Sprite;
import newData.Vct;
import newData.direction;
import newData.intVct;
import object.*;

import java.util.ArrayList;

import static object.Game.switchRound;
import static stage.scene.Othello_Local.DiskSize;

public class DiskManager extends OthelloObject {
    public Disk[][] Disks = new Disk[8][8];
    private direction direction = new direction();

    //创建64个棋
    public DiskManager(){
        super("DiskManager",null);
        initBoard();
    }

    public void initBoard(){
        int index = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                this.Disks[r][c] = new Disk(new intVct(r,c),index);
                this.addObj(this.Disks[r][c]);
                this.Disks[r][c].resizeTo(new Vct(DiskSize,DiskSize));
                System.out.println(DiskSize);
                this.Disks[r][c].setPosition(new intVct(r,c).toPosition());
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
                if (this.Disks[r][c].getVisibility() && this.Disks[r][c].getStatus() == player.getColor()){
                    playerDisks.add(new intVct(r,c));
                }
            }
        }   //获得所有当前玩家的棋子位置
        for (intVct bp : playerDisks) {
            for (intVct index : check(bp, player, "valid")){
                validBP.add(index);
            }
        }
        validBP = cleanArray(validBP);
        return validBP;
    }

    //玩家落子
    public boolean SetDisk(intVct bp, Player player) {
            if (checkDisk(bp, player)) {
                forceSetDisk(bp, player.getColor());
                //落子成功
                System.out.print("落子成功");
                switchRound();
                this.Disks[bp.r][bp.c].setVisibility(true);
                for (intVct index : check(bp, player, "flip")){
                    flipDisks(index);
                }
                return true;
            }
            return false;
    }

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
                if (this.Disks[r][c].getVisibility()){
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

   //清理重复的项
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

    //查找
    public ArrayList<intVct> check(intVct disk, Player player, String operation){
        ArrayList<intVct> num = new ArrayList<>();
        //查找目的是翻棋子
        if(operation == "flip") {
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
        else if(operation == "valid") {
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
        return num;
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

    //同步棋盘


    @Override
    public void update(double dt) {
        for (int r=0; r<8; r++) {
            for (int c=0; c<8; c++) {
                if (this.Disks[r][c].getStatus() == 1) {
                    this.Disks[r][c].setSprite(new Sprite("White_Disk"));
                } else if (this.Disks[r][c].getStatus() == -1) {
                    this.Disks[r][c].setSprite(new Sprite("Black_Disk"));
                }
            }
        }

        super.update(dt);

    }


}
