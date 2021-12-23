package stage.scene;

import component.animation.Animation;
import component.animation.Animator;
import graphics.Image;
import graphics.Shape;
import main.mainApp;
import object.OthelloObject;
import stage.GameStage;
import java.awt.*;

public class Launching extends OthelloObject implements GameStage {
    private double totalTime;
    private Shape empty;
    private OthelloObject logo;
    private Animator amt_logo_scale_1;
    private Animator amt_logo_scale_2;


    public Launching() {
        super("Scene_Launching_First");
    }

    @Override
    public void init() {
        this.empty = new Shape(this.id + "_empty", Color.black, Shape.Type.Rect, mainApp.WinSize);
        this.addObj(this.empty);
        this.empty.setPosition(mainApp.WinSize.x/2, mainApp.WinSize.y/2);
        this.empty.resizeTo(mainApp.WinSize.x,mainApp.WinSize.y);
        this.logo = new OthelloObject("lunching_Logo", new Image("LOGO_Big"));
        this.empty.addObj(this.logo);
        this.logo.resizeTo(0.35);
        this.amt_logo_scale_1 = new Animator(1.0);
        this.logo.addComponent(this.amt_logo_scale_1);
        this.amt_logo_scale_1.append(Animation.GetLinear(1.0, 1.5, 20));
        this.totalTime = 0;
    }

    @Override
    public void update(double dt) {

        this.totalTime += dt;
        if (totalTime < 0.3) {
            this.logo.setScale(this.amt_logo_scale_1.val());
        }
        super.update(dt);
    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Launching;
    }

    public double getTotalTime() {
        return this.totalTime;
    }
}
