package object.inGame;

import component.animation.Animation;
import component.animation.Animator;
import graphics.Sprite;
import newData.Vct;
import object.OthelloObject;

import static input.Controller.MouseKeyStatus;
import static stage.scene.Othello.DiskSize;
import static stage.scene.Othello.mouseIsOnboard;

public class BoardIndex extends OthelloObject {

    public static final double PopOutDuration = 0.05;
    public static final double PopInDuration = 0.05;
    public static final double TraceDuration = 0.05;

    private boolean pressedState;

    private Animator amt_hx;
    private Animator amt_hy;
    private Animator amt_v;
    private Animator amt_a;

    public BoardIndex(){
        super("boardIndex",new Sprite("BoardIndex"));
        this.resizeTo(new Vct(DiskSize, DiskSize));
        this.setVisibility(false);
        this.amt_hx = new Animator(0);
        this.amt_hy = new Animator(0);
        this.amt_v = new Animator(1.0);
        this.amt_a = new Animator(0);
        this.addComponent(this.amt_hx);
        this.addComponent(this.amt_hy);
        this.addComponent(this.amt_v);
        this.addComponent(this.amt_a);
    }


    public void togglePressedZoom(boolean flag) {
        if (flag && !this.pressedState) {
            this.pressedState = true;
            this.amt_v.forceAppend(Animation.GetTanh(this.amt_v.val(), 0.86, PopOutDuration, true));

        } else if (!flag && this.pressedState){
            this.pressedState = false;
            this.amt_v.forceAppend(Animation.GetTanh(this.amt_v.val(), 1.0, PopInDuration, false));
        }
    }

    @Override
    public void update(double dt) {

        if (mouseIsOnboard) {
            this.amt_a.forceAppend(Animation.GetTanh(this.amt_a.val(), 0.8, PopOutDuration, true));
            if (MouseKeyStatus==1) {
                this.togglePressedZoom(true);
            } else {
                this.togglePressedZoom(false);
            }
        }  else {
            this.amt_a.forceAppend(Animation.GetTanh(this.amt_a.val(), 0.2, TraceDuration, true));
        }

        this.setPosition(this.amt_hx.val(), this.amt_hy.val());
        this.setScale(this.amt_v.val(), this.amt_v.val());
        this.setAlpha(this.amt_a.val());
        super.update(dt);
    }

    public void traceMouse(Vct position) {
        this.amt_hx.forceAppend(Animation.GetTanh(this.amt_hx.val(), position.x, TraceDuration, true));
        this.amt_hy.forceAppend(Animation.GetTanh(this.amt_hy.val(), position.y, TraceDuration, true));
    }




}
