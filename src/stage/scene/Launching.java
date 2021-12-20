package stage.scene;

import graphics.Shape;
import main.mainApp;
import object.OthelloObject;
import stage.GameStage;
import java.awt.*;

public class Launching extends OthelloObject implements GameStage {
    private double totalTime;
    private Shape empty;

    public Launching() {
        super("scene_empty");

        }
    @Override
    public void init() {
        this.empty = new Shape(this.id + "_empty", Color.black, Shape.Type.Rect, mainApp.WinSize);
        this.addObj(this.empty);
        this.empty.setPosition(mainApp.WinSize.x/2, mainApp.WinSize.y/2);
        this.empty.resizeTo(mainApp.WinSize.x,mainApp.WinSize.y);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        this.totalTime += dt;
    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Launching;
    }

    public double getTotalTime() {
        return this.totalTime;
    }
}
