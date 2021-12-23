package stage;

import object.GUI.Buttons.ButtonBase;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public interface GameStage {
    void init();
    void update(double dt);
    AffineTransform render(Graphics2D g2d);
    GameStageID getGameStageID();
    public enum GameStageID {
        Empty,Launching,Login,Matching,Othello,Othello_AI,Othello_Local,Demo,Lobby
    }
}
