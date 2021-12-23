package object.GUI;

import component.animation.Animator;
import graphics.Text;
import input.InputCallback;
import newData.Vct;
import object.OthelloObject;

import java.awt.*;

public class Input extends OthelloObject {

    private Font TypeInFont;
    private Color backGroundColor;
    private String disPlay;
    private boolean isSelect;
    private boolean submitted;

    private static int inputCnt=0;
    private boolean active;
    private int Min;
    private int Max;

    private StringBuilder stringBuilder;
    private Text text;
    private String defaultStr;

    public Input(Vct size, String hint, int Min, int Max) {
        super("InputBox_"+inputCnt);
        ++inputCnt;
        this.isSelect = false;
        this.stringBuilder = new StringBuilder();
        this.Min = Min;
        this.Max = Max;

        this.text = new Text(this.id + "_text_", "", new Font("黑体", Font.BOLD, 25));
        this.defaultStr = hint;
        this.text.setText(hint);
        this.text.setColor(new Color(0x98A4A4A4, true));
        this.addObj(text);

        this.active = true;
    }

    public void clear() {
        this.stringBuilder.delete(0, this.stringBuilder.length());
    }

    public String getResult() {
        if (this.submitted) {
            this.submitted = false;
            return this.stringBuilder.toString();
        }
        return null;
    }





}
