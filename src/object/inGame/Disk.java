package object.inGame;
import component.animation.Animation;
import component.animation.Animator;
import graphics.Sprite;
import object.OthelloObject;
import newData.Vct;
import newData.intVct;

import static stage.StageContainer.BoardSize;
import static stage.StageContainer.DiskSize;

public class Disk extends OthelloObject {
    private int status;
    private boolean onBoard;
    private boolean isFliping;
    private intVct bP;
    private double time;
    private int index;
    private OthelloObject root;
    private Animator amt_y;

    private int SettingState;

    private int FlippingState;

    private boolean afterDrop;

    public static final double DropDiskDuration = 0.2;
    public static final double DropDiskHeight = 20;


    public Disk(intVct bP, int count) {
        super("Disk_" + count);
        this.root = new OthelloObject(this.id + "_root");
        this.addObj(this.root);

        this.onBoard = false;
        this.bP = bP;
        this.status = 0;
        this.Visibility = false;
        this.isFliping = false;
        this.amt_y = new Animator(0);
        this.SettingState = 0;
        this.root.addComponent(this.amt_y);
        this.afterDrop=false;
        this.FlippingState = 0;
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
        if (!this.isFliping) {
            this.FlippingState = 0;
            if (this.status == -1) {
                this.setVisibility(true);
                this.root.setSprite(new Sprite("Black_Disk"));
                this.index = 8;
                this.time = 0;
                this.afterDrop = false;
            } else if (this.status == 1){
                this.setVisibility(true);
                this.root.setSprite(new Sprite("White_Disk"));
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
            if (this.isFliping && this.status == -1) {
                if (this.index > 0) {
                    --index;
                    this.root.setSprite(new Sprite(index + ""));
                } else {
                    this.index = 0;
                    this.isFliping = false;
                    this.status = 1;
                    this.FlippingState = 0;
                }
            }

            if (this.isFliping && this.status == 1) {
                if (this.index < 8) {
                    ++index;
                    this.root.setSprite(new Sprite(index + ""));
                } else {
                    this.index = 8;
                    this.isFliping = false;
                    this.status = -1;
                    this.FlippingState = 0;
                }
            }
        }


        this.root.resizeTo(new Vct(DiskSize,DiskSize));
        this.root.setPosition(0, this.amt_y.val());


    }

    public void DropCompleteCheck(){
        if (SettingState == 1 && this.amt_y.isIdle()) {
            this.dropDiskAnimation(false);
            this.SettingState = 0;
        }
    }

    public void flip() {
        this.isFliping = true;
    }

    public void setOnBoard() {
        this.onBoard = true;
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
            this.amt_y.forceAppend(Animation.GetSmooth(-DropDiskHeight, this.amt_y.val(), DropDiskDuration, 0));
        }
    }


}
