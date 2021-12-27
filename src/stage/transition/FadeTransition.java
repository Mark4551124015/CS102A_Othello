
package stage.transition;

import component.animation.Animation;
import component.animation.Animator;
import graphics.Shape;
import main.mainApp;

import java.awt.*;

public class FadeTransition implements Transition {

    private boolean active;
    private Animator alphaAnimator;
    private Shape pc;


    public FadeTransition(Color color, double fadeTime, double delay, FadeType type) {
        this.pc = new Shape("bg", color, Shape.Type.Rectangle, mainApp.WinSize);
        if (type == FadeType.IN) {
            this.pc.setAlpha(1);
            this.alphaAnimator = new Animator(1);
            this.alphaAnimator.append(Animation.GetTanh(1, 0, fadeTime, false, delay));
        }

        if (type == FadeType.OUT) {
            this.pc.setAlpha(0);
            this.alphaAnimator = new Animator(0);
            this.alphaAnimator.append(Animation.GetTanh(0, 1, fadeTime, false, delay));
        }


        this.pc.setPosition(mainApp.Width / 2.0, mainApp.Height / 2.0);
        this.alphaAnimator.setActive(false);
        this.active = false;
    }



    @Override
    public void init() {
        this.active = true;
        this.alphaAnimator.setActive(true);
    }

    @Override
    public void update(double dt) {
        if (!this.active)
            return;
        this.alphaAnimator.update(dt);
        this.pc.setAlpha(this.alphaAnimator.val());
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!this.active)
            return;
        this.pc.render(g2d);
    }

    @Override
    public boolean isFinished() {
        return this.alphaAnimator.isIdle();
    }

    public enum FadeType {
        IN,OUT
    }

}
