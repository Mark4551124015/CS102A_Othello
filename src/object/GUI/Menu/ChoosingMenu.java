package object.GUI.Menu;

import graphics.Text;
import input.Controller;
import input.InputCallback;
import newData.Vct;
import object.GUI.Buttons.ChoosingButton;
import object.OthelloObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class ChoosingMenu extends OthelloObject implements InputCallback {
    private ArrayList<String> list;
    private ArrayList<ChoosingButton> buttons;
    private final int menuPos=-70;
    private boolean submitted;
    private String Result;
    private Text title;
    private OthelloObject menu;

    public ChoosingMenu(ArrayList<String> list, String title){
        super("ChoosingMenu");
        this.title=new Text(this.id+"_title",title,new Font("黑体", Font.BOLD, 20));
        this.title.setPosition(0,-100);
        this.menu=new OthelloObject("central");
        this.addObj(this.menu);
        this.menu.setPosition(0,menuPos);
        this.list = list;
        this.buttons=new ArrayList<>(0);
        for (int index_1=0; index_1 < this.list.size(); index_1++) {
            this.buttons.add(index_1, new ChoosingButton(this.id, list.get(index_1)));
            this.buttons.get(index_1).setPosition(0,index_1*this.buttons.get(index_1).getImage().getUnitSize().y);
            this.menu.addObj(this.buttons.get(index_1));
        }
        Controller.registerCallback(this);

    }

    public void renew(ArrayList<String> list) {
        for( ChoosingButton index : this.buttons) {
            index.setActive(false);
            index.setVisibility(false);
            index.detachParentObj();
        }
        for (int index_1=0; index_1 < this.list.size(); index_1++) {
            this.buttons.add(index_1, new ChoosingButton(this.id, this.list.get(index_1)));
            this.buttons.get(index_1).setPosition(0,index_1*this.buttons.get(index_1).getImage().getUnitSize().y);
            this.menu.addObj(this.buttons.get(index_1));
        }
    }
    public void submit(){
        this.submitted = true;
    }

    public boolean isSubmitted(){
        return this.submitted;
    }

    public void setSubmitted(boolean flag) {
        this.submitted = flag;
    }

    public void clearChoice() {
        for( ChoosingButton index : this.buttons) {
            index.setChoosingStatus(false);
        }
        this.submitted = false;
    }

    public String getResult() {
        if (this.submitted) {
            return this.Result;
        }
        return null;
    }

    public void setActive(boolean flag){
        for( ChoosingButton index : this.buttons) {
            index.setActive(flag);
        }

    }

    @Override
    public void onKeyTyped(KeyEvent e) {
        this.onKeyPressed(e);
        if (this.isVisible()) {
            if ((int)e.getKeyChar() == KeyEvent.VK_ENTER && this.Result != null) {
                this.submit();
            }
        }
    }

    @Override
    public void onMouseWheelMoved(MouseWheelEvent e) {
    }


    @Override
    public void update(double dt){
        super.update(dt);
        if (!this.submitted) {
            for (int index_1 = 0; index_1 < this.buttons.size(); index_1++) {
                if (this.buttons.get(index_1).isChosen()) {
                    this.Result = this.buttons.get(index_1).getChoice();
                    this.submitted = true;
                }
            }
        }
    }

    @Override
    public void onMousePressed(MouseEvent e) {
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
    }

    @Override
    public void onMouseMoved(Vct mousePos) {

    }


    @Override
    public void onKeyPressed(KeyEvent e) {

    }

    @Override
    public void onKeyReleased(KeyEvent e) {

    }


}
