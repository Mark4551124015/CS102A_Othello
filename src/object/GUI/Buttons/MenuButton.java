/**
 * @Author: Kaia
 * @FileName: MenuButton.java
 */

package object.GUI.Buttons;

import main.AudioManager;
import component.animation.Animation;
import component.animation.Animator;

import graphics.Sprite;
import graphics.Text;
import input.Controller;
import newData.Vct;
import object.OthelloObject;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import static input.Controller.MouseKeyStatus;

public class MenuButton extends ButtonBase {

    public static final double PopOutDuration = 0.08;
    public static final double PopInDuration = 0.12;


    private OthelloObject root;
    private Text text;

    private OthelloObject base;
    private OthelloObject surface;

    private boolean hoverState;
    private boolean pressedState;

    private Animator amt_h;
    private Animator amt_v;
    private Animator amt_a;

    private boolean clicked;


    public MenuButton(String id, Sprite sprite) {
        super(id, null);

        this.root = new OthelloObject(this.id + "_root");
        this.addObj(this.root);

        this.base = new OthelloObject(this.id + "_base", new Sprite("popo"));
        this.base.setColor(new Color(255, 255, 255, 20));
        this.base.resizeTo(sprite.getUnitSize());
        this.root.addObj(this.base);

        this.surface = new OthelloObject(this.id + "_surface", sprite);
        this.surface.setColor(Color.white);
        this.root.addObj(this.surface);


        this.text = new Text(this.id + "_text", "", new Font("黑体", Font.BOLD, 50));
        this.root.addObj(this.text);


        this.amt_h = new Animator(0);
        this.amt_v = new Animator(1.0);
        this.amt_a = new Animator(0);
        this.addComponent(this.amt_h);
        this.addComponent(this.amt_v);
        this.addComponent(this.amt_a);
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
        if (this.clicked) {
            this.clicked = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if (!this.getVisibility()) {
            this.setActive(false);
        } else {
            this.setActive(true);
        }
        if (this.active) {
            if (isHovering()) {
                this.toggleHoverZoom(true);
                if (MouseKeyStatus==1) {
                    this.togglePressedZoom(true);
                    this.lock =true;
                }
            } else {
                if (MouseKeyStatus == 0 && !this.lock) {
                    this.togglePressedZoom(false);
                    this.toggleHoverZoom(false);
                }
            }
        }

        this.root.setPosition(this.amt_h.val(), 0);
        this.setScale(this.amt_v.val(), this.amt_v.val());
        this.surface.setAlpha(this.amt_a.val());
    }

    @Override
    public AffineTransform render(Graphics2D g2d, AffineTransform parentTransform, double alpha) {
        AffineTransform at = super.render(g2d, parentTransform, alpha);
        return at;
    }

    @Override
    public boolean isHovering() {
        Vct size = this.base.getSprite().getUnitSize();
        Vct pos = Controller.getMousePos();
        try {
            pos.transform(this.getAbsoluteTransform().createInverse());
        } catch (NoninvertibleTransformException ignored) {
        }
        if (pos.x <= size.x * (-0.5 + 0.105)) {
            if ((size.x * (-0.5) <= pos.x && pos.x <= size.x * (-0.5 + 0.105)) && (pos.y >= size.y * (-0.5) && pos.y <= size.y * 0.5) && pos.y <= -((-size.y / (0.105 * size.x)) * (pos.x - (size.x * -0.5)) + size.y * 0.5)) {
                return true;
            }
        } else {
            if ((size.x * (-0.5 + 0.105) <= pos.x && pos.x <= size.x * 0.5) && (pos.y >= size.y * (-0.5) && pos.y <= size.y * 0.5)) {
                return true;
            }
        }
        return false;
    }

    public void toggleHoverZoom(boolean flag) {
        if (flag && !this.hoverState) {
            this.hoverState = true;
            this.amt_h.forceAppend(Animation.GetTanh(this.amt_h.val(), this.base.getSprite().getUnitSize().x * 0.2, PopOutDuration, true));
            this.amt_v.forceAppend(Animation.GetTanh(this.amt_v.val(), 1.05, PopOutDuration, true));
            this.amt_a.forceAppend(Animation.GetTanh(this.amt_a.val(), 1, PopOutDuration, true));
        } else if (!flag && this.hoverState){
            this.hoverState = false;
            this.amt_h.forceAppend(Animation.GetTanh(this.amt_h.val(), 0, PopInDuration, false));
            this.amt_v.forceAppend(Animation.GetTanh(this.amt_v.val(), 1.0, PopInDuration, false));
            this.amt_a.forceAppend(Animation.GetTanh(this.amt_a.val(), 0, PopInDuration, false));
        }
    }

    @Override
    public void resizeTo(Vct rect) {
        super.resizeTo(rect);
        this.base.resizeTo(rect);
        this.surface.resizeTo(rect);
    }

    @Override
    public void resizeTo(double x, double y) {
        super.resizeTo(x, y);
        this.base.resizeTo(x, y);
        this.surface.resizeTo(x, y);
    }



    @Override
    public void onClicked(int button) {
        if (button == 1 && this.isHovering() && this.active ) {
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

    public OthelloObject getBase() {
        return this.base;
    }


    public void togglePressedZoom(boolean flag) {
        if (flag && !this.pressedState) {
            this.pressedState = true;
            this.amt_v.forceAppend(Animation.GetTanh(this.amt_v.val(), 0.95, PopOutDuration, true));

        } else if (!flag && this.pressedState){
            this.pressedState = false;
            this.amt_v.forceAppend(Animation.GetTanh(this.amt_v.val(), 1.0, PopInDuration, false));
        }
    }



}
