package stage;

import java.awt.*;
import java.awt.geom.AffineTransform;

public interface GameStage {
    void init();
    void update(double dt);
    AffineTransform render(Graphics2D g2d);
    GameStageID getGameStageID();
    public enum GameStageID {
        Empty,Launching,Matching,Othello,Othello_Local,Demo,Lobby
    }
}
