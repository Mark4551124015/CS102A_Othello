package object.GUI.Buttons;

import graphics.Image;
import input.Controller;
import input.InputCallback;
import object.OthelloObject;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public abstract class ButtonBase extends OthelloObject implements InputCallback {
    // whether the hover component.animation will step on
    protected boolean active;
    protected boolean lock;

    public ButtonBase(String id, Image image) {
        super(id, image);
        Controller.registerCallback(this);
        this.active = false;
    }

    public abstract boolean isHovering();

    public abstract void onClicked(int button);

    public void setActive(boolean flag) {
        this.active = flag;
    }


    @Override
    public void onMousePressed(MouseEvent e) {
        if (this.isHovering() && this.active) {
            // System.out.println("[RectButton] Being Clicked!");
            this.lock = true;
        }
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if (this.isHovering() && this.active) {
            // System.out.println("[RectButton] Being Clicked!");
            this.onClicked(e.getButton());
        }
        this.lock = false;
    }

    @Override
    public void onKeyTyped(KeyEvent e) {

    }

    @Override
    public void onKeyPressed(KeyEvent e) {

    }

    @Override
    public void onKeyReleased(KeyEvent e) {

    }

}
