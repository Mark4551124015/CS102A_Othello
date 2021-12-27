package stage.scene;
import main.mainApp;
import object.OthelloObject;
import stage.GameStage;

import graphics.Shape;
import java.awt.*;

public class Empty extends OthelloObject implements GameStage {
    private double totalTime;
    private Shape empty;

    public Empty() {
        super("scene_empty",null);
        this.empty = new Shape(this.id + "_empty", Color.black, Shape.Type.Rectangle, mainApp.WinSize);
        this.addObj(this.empty);
        this.empty.setPosition(mainApp.WinSize.x/2, mainApp.WinSize.y/2);
        }
    @Override
    public void init() {

    }

    @Override
    public void update(double dt) {
        super.update(dt);
        this.totalTime += dt;
    }

    @Override
    public GameStageID getGameStageID() {
        return GameStageID.Empty;
    }

    public double getTotalTime() {
        return this.totalTime;
    }

}
