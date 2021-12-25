package object.GUI;

import graphics.Image;
import graphics.Text;
import input.Controller;
import main.AudioManager;
import newData.Vct;
import object.GUI.Buttons.ButtonBase;
import object.GUI.Buttons.NormalButton;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class InputBox extends ButtonBase {

    private Font TypeInFont;
    private Color backGroundColor;
    private String Result;
    private boolean isSelect;
    private boolean submitted;


    private static int inputCnt=0;
    private boolean active;
    private final static Color Text_color = new Color(0x1A1A1A) ;
    private final static Color Default_Color = new Color(0x98A4A1A1, true) ;
    private final static Font Text_Font = new Font("黑体", Font.BOLD, 15);

    private int Max;

    private StringBuilder stringBuilder;
    private Text text;
    private String defaultStr;

    public InputBox(Vct size, String hint, int Max) {
        super("InputBox_"+inputCnt);
        ++inputCnt;
        this.isSelect = false;
        this.stringBuilder = new StringBuilder();
        this.Max = Max;
        this.resizeTo(size);
        this.setImage(new Image("InputBG"));

        this.text = new Text(this.id + "_text_", "", Text_Font);
        this.defaultStr = hint;
        this.text.setText(hint);
        this.text.setColor(new Color(0x98A4A4A4, true));
        this.addObj(text);
        this.active = true;
    }

    public void setTypeInFont(Font font){
        this.text.setFont(font);
    }

    public void clear() {
        this.stringBuilder.delete(0, this.stringBuilder.length());
    }

    public void setSelect(boolean flag) {
        this.isSelect = flag;
    }

    public String getResult() {
        if (this.submitted) {
            this.submitted = false;
            return this.Result;
        }
        return null;
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if (this.isHovering() && this.active){
            this.onClicked(e.getButton());
        }
    }

    @Override
    public void onClicked(int button) {
        if (button == 1 && this.Visibility) {
            this.isSelect=true;
        }
        AudioManager.Play("click");
    }

    @Override
    public AffineTransform render(Graphics2D g2d, AffineTransform parentTransform, double alpha) {
        AffineTransform at = super.render(g2d, parentTransform, alpha);
        return at;
    }


    @Override
    public void onMouseWheelMoved(MouseWheelEvent e) {
    }

    @Override
    public void onKeyTyped(KeyEvent e) {
        super.onKeyPressed(e);
        if (this.isSelect) {
            if ((int)e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                if (this.stringBuilder.length() > 0)
                    this.stringBuilder.deleteCharAt(this.stringBuilder.length() - 1);
            } else if ((int)e.getKeyChar() == KeyEvent.VK_ENTER) {
                AudioManager.PlayWithVolume("type", 0.5, 0);
                this.submit();
            } else {
                AudioManager.PlayWithVolume("type", 0.5, 0);
                if (this.stringBuilder.length() < this.Max)
                    this.stringBuilder.append(e.getKeyChar());
            }
        }
    }

    public void submit(){
        this.submitted = true;
        this.Result =this.stringBuilder.toString();
    }

    public boolean getStatus(){
        return this.submitted;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if (this.stringBuilder.length() > 0 && !this.submitted) {
            this.text.setText(this.stringBuilder.toString());
            this.text.setColor(Text_color);
        } else if (this.submitted) {
            this.text.setColor(Default_Color);
        } else {
            this.text.setText(this.defaultStr);
            this.text.setColor(Default_Color);
        }
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


    public void setActive(boolean flag) {
        this.active = flag;
    }


    @Override
    public void onMouseMoved(Vct mousePos) {

    }

    @Override
    public void resizeTo(Vct rect) {
        super.resizeTo(rect);
    }

    @Override
    public void resizeTo(double x, double y) {
        super.resizeTo(x, y);
    }

}
