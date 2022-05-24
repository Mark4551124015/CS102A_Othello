/**
 * @Author: Kaia
 * @FileName: MenuButton.java
 */

package object.GUI.Buttons;

import component.animation.Animation;
import component.animation.Animator;
import graphics.Image;
import graphics.Text;
import input.Controller;
import main.AudioManager;
import newData.Vct;

import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import static input.Controller.MouseKeyStatus;

public class NormalButton extends ButtonBase {

    public static final double PopOutDuration = 0.08;
    public static final double PopInDuration = 0.12;
    public static final Vct NormalButtonSize = new Vct(110,50);
    private static final int ButtonCnt = 0;

    private final Text text;


    private boolean hoverState;
    private boolean pressedState;
    private final Animator amt_v;

    private boolean clicked;


    public NormalButton(String Text) {
        super("NormalButton_" + ButtonCnt, new Image("NormalButton"));
        this.text = new Text(this.id + "_text", Text, new Font("黑体", Font.PLAIN, 18));
        this.addObj(this.text);
        this.resizeTo(NormalButtonSize);
        this.amt_v = new Animator(1.0);
        this.addComponent(this.amt_v);
    }

    public void setFont(Font font) {
        this.text.setFont(font);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setTextColor(Color color) {
        this.text.setColor(color);
    }

    public boolean isClicked() {
        if (this.clicked && this.active) {
            this.clicked = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if(!this.Visibility || this.alpha == 0){
            this.setPosition(10000,10000);
        }


        if (this.active) {
            if (isHovering()) {
                this.toggleHoverZoom(true);
                this.togglePressedZoom(MouseKeyStatus == 1);
            } else {
                this.toggleHoverZoom(false);
            }
        }



        this.setScale(this.amt_v.val(), this.amt_v.val());
    }

    @Override
    public AffineTransform render(Graphics2D g2d, AffineTransform parentTransform, double alpha) {
        AffineTransform at = super.render(g2d, parentTransform, alpha);
        return at;
    }

    @Override
    public boolean isHovering() {
        Vct size = this.getImage().getUnitSize();
        Vct pos = Controller.getMousePos();
        try {
            pos.transform(this.getAbsoluteTransform().createInverse());
        } catch (NoninvertibleTransformException ignored) {
        }
        if (pos.x <= size.x * (-0.5 + 0.105)) {
            return (size.x * (-0.5) <= pos.x && pos.x <= size.x * (-0.5 + 0.105)) && (pos.y >= size.y * (-0.5) && pos.y <= size.y * 0.5) && pos.y <= -((-size.y / (0.105 * size.x)) * (pos.x - (size.x * -0.5)) + size.y * 0.5);
        } else {
            return (size.x * (-0.5 + 0.105) <= pos.x && pos.x <= size.x * 0.5) && (pos.y >= size.y * (-0.5) && pos.y <= size.y * 0.5);
        }
    }

    public void toggleHoverZoom(boolean flag) {
        if (flag && !this.hoverState) {
            this.hoverState = true;
            this.amt_v.forceAppend(Animation.GetSmooth(this.amt_v.val(), 1.024, PopOutDuration, 0));
        } else if (!flag && this.hoverState){
            this.hoverState = false;
            this.amt_v.forceAppend(Animation.GetSmooth(this.amt_v.val(), 0.98, PopOutDuration, 0));
        }
    }

    public void togglePressedZoom(boolean flag) {
        if (flag && !this.pressedState) {
            this.pressedState = true;
            this.amt_v.forceAppend(Animation.GetSmooth(this.amt_v.val(), 0.98, PopOutDuration, 0));

        } else if (!flag && this.pressedState){
            this.pressedState = false;
            this.amt_v.forceAppend(Animation.GetSmooth(this.amt_v.val(), 1.024, PopInDuration, 0));
        }
    }

    @Override
    public void resizeTo(Vct rect) {
        super.resizeTo(rect);
    }

    @Override
    public void resizeTo(double x, double y) {
        super.resizeTo(x, y);
    }

    @Override
    public void onClicked(int button) {
        if (button == 1 && this.isHovering() && this.active) {
            this.clicked = true;
            AudioManager.Play("click");
        }
    }

    @Override
    public void onMouseMoved(Vct mousePos) {

    }

    @Override
    public void onMouseWheelMoved(MouseWheelEvent e) {

    }

}
