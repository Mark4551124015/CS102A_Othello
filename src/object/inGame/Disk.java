package object.inGame;
import component.animation.Animation;
import component.animation.Animator;
import graphics.Image;
import object.OthelloObject;
import newData.Vct;
import newData.intVct;

import static stage.StageContainer.DiskSize;

public class Disk extends OthelloObject {
    private int status;
    private boolean onBoard;
    private boolean isFlipping;
    private intVct bP;
    private double time;
    private int index;
    private OthelloObject root;
    private Animator amt_y;
    private Animator amt_a;

    private int SettingState;

    private int FlippingState;

    private boolean afterDrop;

    public static final double DropDiskDuration = 0.2;
    public static final double DropDiskHeight = 20;

    private static int DiskCnt = 0;


    public Disk(intVct bP) {
        super("Disk_" + DiskCnt);
        this.root = new OthelloObject(this.id + "_root");
        this.addObj(this.root);

        this.onBoard = false;
        this.bP = bP;
        this.status = 0;
        this.Visibility = false;
        this.isFlipping = false;
        this.amt_y = new Animator(0);
        this.amt_a = new Animator(1);
        this.SettingState = 0;
        this.root.addComponent(this.amt_y);
        this.root.addComponent(this.amt_a);
        this.afterDrop=false;
        this.FlippingState = 0;

        ++DiskCnt;
    }

//    public Disk(Vct position, int status) {
//        super("Disk_inHand");
//        this.onBoard = false;
//        this.size = new Vct(boardSize/8.1 , boardSize/8.1);
//        this.status = status;
//    }

    @Override
    public void update(double dt) {
        flipCheck(dt);
        this.DropCompleteCheck();
        this.root.setPosition(0, this.amt_y.val());
        super.update(dt);
    }

    public void flipCheck(double dt) {
        if (!this.isFlipping) {
            this.FlippingState = 0;
            if (this.status == -1) {
                this.setVisibility(true);
                this.index = 8;
                this.time = 0;
                this.afterDrop = false;
            } else if (this.status == 1){
                this.setVisibility(true);
                this.index = 0;
                this.time = 0;
                this.afterDrop = false;
            }
        } else {
            this.FlippingState = 1;
        }
        this.time += dt;
        if (this.time > 0.3) {
            this.afterDrop = true;
        }


        if (this.time * 40 > 1 && this.afterDrop) {
            this.time = 0;
            if (this.isFlipping && this.status == -1) {
                if (this.index > 0) {
                    --index;
                    this.root.setSprite(new Image(index + ""));
                } else {
                    this.status = 1;
                    this.index = 0;
                    this.isFlipping = false;
                    this.FlippingState = 0;
                }
            }

            if (this.isFlipping && this.status == 1) {
                if (this.index < 8) {
                    ++index;
                    this.root.setSprite(new Image(index + ""));
                } else {
                    this.status = -1;
                    this.index = 8;
                    this.isFlipping = false;
                    this.FlippingState = 0;
                }
            }
        }


        this.root.resizeTo(new Vct(DiskSize,DiskSize));
        this.root.setPosition(0, this.amt_y.val());


    }



    public void DropCompleteCheck(){
        if (SettingState == 1 && this.amt_y.isIdle()) {
            this.SettingState = 0;
            if (this.getStatus() == 0) {
                this.setVisibility(false);
            }
        }
    }

    public int getSttingState() {
        return this.SettingState;
    }



    public void flip() {
        this.isFlipping = true;
    }

    public void simpleFlip() {
        this.setStatus(this.status*-1);
    }


    public OthelloObject getRoot(){
        return this.root;
    }

    public intVct getBP() {
        return this.bP;
    }

    public void recall() {
        this.dropDiskAnimation(false);
        this.setStatus(0);
    }

    public void setOnBoard() {
        this.onBoard = true;
        this.setVisibility(true);
        this.dropDiskAnimation(true);
    }

    public boolean isOnBoard() {
        return this.onBoard;
    }

    public int getStatus() {
        return this.status;
    }

    public int getFlippingState() {
        return this.FlippingState;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void dropDiskAnimation(boolean flag) {
        if (flag && this.SettingState !=1) {
            this.SettingState = 1;
            this.amt_y.forceAppend(Animation.GetSmooth(-DropDiskHeight, 0, DropDiskDuration, 0));
        }
        if (!flag && this.SettingState !=1) {
            this.SettingState = 1;
            this.amt_y.forceAppend(Animation.GetSmooth(0,-DropDiskHeight, DropDiskDuration, 0));
        }
    }





}
