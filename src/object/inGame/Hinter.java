package object.inGame;

import component.animation.Animation;
import component.animation.Animator;
import graphics.Image;
import newData.Vct;
import object.OthelloObject;

import static stage.StageContainer.DiskSize;

public class Hinter extends OthelloObject {
    private static int hinterCnt = 0;
    private final Animator amt_a;
    private final Animator amt_s;
    private boolean isEmerged;
    public Hinter(){
        super("Hinter" + hinterCnt,new Image("Hinter"));
        this.setAlpha(0);
        ++hinterCnt;
        this.setSize(new Vct(DiskSize/2, DiskSize/2));
        this.amt_a = new Animator(0);
        this.amt_s = new Animator(0.5);
        this.addComponent(this.amt_a);
        this.addComponent(this.amt_s);
        this.isEmerged = false;
    }

    public void hinted(){
        this.setAlpha(0);
    }
    @Override
    public void update(double dt) {
        this.setAlpha(this.amt_a.val());
        this.setScale(this.amt_s.val(), this.amt_s.val());
        super.update(dt);
    }

    public void Emerge(boolean flag){
        if (flag == true && !this.isEmerged) {
            this.isEmerged =true;
            this.amt_a.forceAppend(Animation.GetTanh(0, 0.5, 0.5, true));
            this.amt_s.forceAppend(Animation.GetTanh(0.5, 1, 0.5, true));
        }
    }
}
