/**
 * @Author: Kaia
 * @FileName: MenuButton.java
 */

package object.GUI.Buttons;

import main.AudioManager;

import graphics.Image;
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

public class ChoosingButton extends ButtonBase {
    private final OthelloObject checker;

    private OthelloObject root;
    private final Text text;

    private boolean hoverState;
    private boolean pressedState;
    private boolean chosen;
    private final String Contains;


    private boolean clicked;


    private static int buttonCnt = 0;


    public ChoosingButton(String id, String choice) {
        super(id+"_"+buttonCnt, new Image("ChoosingButton"));
        ++buttonCnt;
        this.text = new Text(this.id + "_text", choice, new Font("黑体", Font.BOLD, 10));
        this.addObj(this.text);
        this.checker = new OthelloObject(buttonCnt+"_Checker", new Image("Ready"));
        this.addObj(this.checker);
        this.checker.setPosition(120,0);
        this.checker.setVisibility(false);
        this.checker.setScale(0.3);
        this.resizeTo(300,40);
        this.Contains = choice;
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


    public void setChoosingStatus( boolean flag) {
        this.chosen = flag;
        this.checker.setVisibility(flag);

    }

    public boolean isChosen(){
        return this.chosen;
    }

    public String getChoice(){
        if(this.isChosen()){
            return this.Contains;
        }
        else {
            return null;
        }
    }

    @Override
    public void update(double dt) {
        if(!this.Visibility || this.alpha == 0){
            this.setPosition(10000,10000);
            this.active = false;
        } else {
            this.active = true;
        }


        if (this.active) {
            if (isHovering()) {
                if (this.isClicked()) {
                    this.setChoosingStatus(true);
                }
            } else {
                if (this.isClicked()) {
                    this.setChoosingStatus(false);
                }
            }
        }


        super.update(dt);
    }

    @Override
    public AffineTransform render(Graphics2D g2d, AffineTransform parentTransform, double alpha) {
        AffineTransform at = super.render(g2d, parentTransform, alpha);
        return at;
    }

    @Override
    public boolean isHovering() {
        Vct size = this.getSize();
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
        if (button == 1 && this.active) {
            this.clicked = true;
        }
    }



    @Override
    public void onMouseMoved(Vct mousePos) {
    }

    @Override
    public void onMouseWheelMoved(MouseWheelEvent e) {
    }


    @Override
    public void onMouseReleased(MouseEvent e) {
        if (this.active) {
            this.onClicked(e.getButton());
        }
        this.lock = false;
    }




}
