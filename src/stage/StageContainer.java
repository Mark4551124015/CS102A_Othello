package stage;

import main.AttentionManager;
import main.mainApp;
import stage.scene.Empty;
import stage.transition.EmptyTransition;
import stage.transition.Transition;
import graphics.Camera;

import javax.swing.*;
import java.awt.*;

public class StageContainer extends JPanel {


    private GameStage currentStage;
    private GameStage preventStage;
    private Transition enterTransition;
    private Transition leaveTransition;

    public StageContainer() {
        this.setPreferredSize(new Dimension(mainApp.Width, mainApp.Height));


        this.preventStage = null;
        this.currentStage = new Empty();


    }

    public GameStage getCurrentStage() {
        return this.currentStage;
    }

    public void render(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        if (this.preventStage != null)
            this.preventStage.render(g2d);
        else
            this.currentStage.render(g2d);

        if (this.leaveTransition != null)
            this.leaveTransition.render(g2d);
        if (this.enterTransition != null)
            this.enterTransition.render(g2d);
        AttentionManager.render(g2d);
    }


    public void update(double dt) {
        Camera.update(dt);
        AttentionManager.update(dt);

        if (this.leaveTransition != null) {
            if (this.leaveTransition.isFinished()) {
                this.leaveTransition = null;
                this.preventStage = null;
                if (this.enterTransition != null)
                    this.enterTransition.init();
            } else {
                this.leaveTransition.update(dt);
            }
        }
        if (this.enterTransition != null) {
            if (this.enterTransition.isFinished()) {
                this.enterTransition = null;
            } else {
                this.enterTransition.update(dt);
            }
        }

        if (this.preventStage != null)
            this.preventStage.update(dt);
        else
            this.currentStage.update(dt);
    }

    public void enterStage(GameStage nextStage, Transition leaveTransition, Transition enterTransition) {
        if (nextStage == null) {
            nextStage = new Empty();
        }
        nextStage.init();
        this.preventStage = this.currentStage;
        this.currentStage = nextStage;
        if (leaveTransition == null)
            leaveTransition = new EmptyTransition();
        if (enterTransition == null)
            enterTransition = new EmptyTransition();
        this.leaveTransition = leaveTransition;
        this.leaveTransition.init();
        this.enterTransition = enterTransition;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.render((Graphics2D)g);
    }
}
