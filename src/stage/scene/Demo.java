package stage.scene;

import main.mainApp;
import stage.GameStage;
import object.OthelloObject;
import graphics.Sprite;

public class Demo extends OthelloObject implements GameStage {
    public OthelloObject panel;
    public OthelloObject test;
    private double totalTime;

    public Demo() {
        super("Scene_Demo");
    }

    public void init() {
        this.panel = new OthelloObject("panel");
        this.addObj(this.panel);
        this.test = new OthelloObject("test", new Sprite("test"));
        this.panel.addObj(this.test);
        this.panel.setPosition(mainApp.WinSize.x/2,mainApp.WinSize.y/2);
        this.test.resizeTo(100,100);
        this.test.setAngle(-90);
        this.totalTime = 0;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        this.totalTime += dt;
        this.test.setPosition(10*this.totalTime* Math.cos(this.totalTime*2*3.14), 10*this.totalTime * Math.sin(2*3.14*this.totalTime));
        this.test.rotate(-dt*360);
    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Demo;
    }

}
