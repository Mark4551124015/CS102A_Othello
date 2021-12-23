package stage.transition;

import component.animation.Animation;
import component.animation.Animator;
import graphics.Image;
import main.mainApp;
import object.OthelloObject;

import java.awt.*;

public class OthelloTransition implements Transition{

    private boolean active;
    private Animator alphaAnimator;
    private Animator left;
    private Animator right;
    private OthelloObject TransitionLeft;
    private OthelloObject TransitionRight;
    private OthelloObject TransitionLOGO;



    public OthelloTransition(double fadeTime, double delay, TransitionType type){
        this.TransitionLeft = new OthelloObject("Transition-left", new Image("Transition-left"));
        this.TransitionRight = new OthelloObject("Transition-right", new Image("Transition-right"));
        this.TransitionLOGO = new OthelloObject("Transition-LOGO", new Image("LOGO_Big"));

        if (type == TransitionType.OUT) {
            this.TransitionLeft.setAlpha(1);
            this.TransitionRight.setAlpha(1);
            this.TransitionLeft.setPosition((mainApp.Width / 2.0) - 1500, mainApp.Height / 2.0);
            this.TransitionRight.setPosition((mainApp.Width / 2.0) + 1500, mainApp.Height / 2.0);
            this.TransitionLOGO.setPosition(mainApp.Width / 2.0, mainApp.Height / 2.0);


            this.alphaAnimator = new Animator(0);
            this.left = new Animator((mainApp.Width / 2.0) - 1500);
            this.right = new Animator((mainApp.Width / 2.0) + 1500);

            this.left.append(Animation.GetSmooth((mainApp.Width / 2.0) - 1500, (mainApp.Width / 2.0) , fadeTime, delay));
            this.right.append(Animation.GetSmooth((mainApp.Width / 2.0) + 1500, (mainApp.Width / 2.0) , fadeTime, delay));

            this.alphaAnimator.append(Animation.GetSmooth(0, 1, fadeTime, delay));
        }


        if (type == TransitionType.IN) {
            this.TransitionLeft.setAlpha(1);
            this.TransitionRight.setAlpha(1);
            this.TransitionLeft.setPosition((mainApp.Width / 2.0), mainApp.Height / 2.0);
            this.TransitionRight.setPosition((mainApp.Width / 2.0), mainApp.Height / 2.0);
            this.TransitionLOGO.setPosition(mainApp.Width / 2.0, mainApp.Height / 2.0);


            this.alphaAnimator = new Animator(0);
            this.left = new Animator((mainApp.Width / 2.0));
            this.right = new Animator((mainApp.Width / 2.0));

            this.left.append(Animation.GetSmooth((mainApp.Width / 2.0),(mainApp.Width / 2.0) + 1500 , fadeTime, delay));
            this.right.append(Animation.GetSmooth((mainApp.Width / 2.0), (mainApp.Width / 2.0) - 1500 , fadeTime, delay));

            this.alphaAnimator.append(Animation.GetSmooth(1, 0, fadeTime, delay));
        }
        this.TransitionLOGO.setScale((mainApp.Width)/this.TransitionLOGO.getSprite().getUnitSize().x);

        this.right.setActive(false);
        this.left.setActive(false);
        this.alphaAnimator.setActive(false);
        this.active = false;
    }



    public enum TransitionType{
        IN,OUT
    }
    @Override
    public void init() {
        this.active = true;
        this.right.setActive(true);
        this.left.setActive(true);
        this.alphaAnimator.setActive(true);
    }

    @Override
    public void update(double dt) {
        if (!this.active) {
            return;
        }
        this.alphaAnimator.update(dt);
        this.left.update(dt);
        this.right.update(dt);
        this.TransitionLOGO.setAlpha(this.alphaAnimator.val());


        this.TransitionLeft.setPosition(this.left.val(), mainApp.Height / 2.0);
        this.TransitionRight.setPosition(this.right.val(), mainApp.Height / 2.0);
    }

    @Override
    public void render(Graphics2D g2d) {
        if (!this.active)
            return;
        this.TransitionLeft.render(g2d);
        this.TransitionRight.render(g2d);
        this.TransitionLOGO.render(g2d);

    }

    @Override
    public boolean isFinished() {
        return this.alphaAnimator.isIdle();
    }
}
