package object;

import component.animation.Animator;
import graphics.Image;
import object.GUI.Buttons.NormalButton;

public class EscMenu extends OthelloObject{
    private OthelloObject EscMenu;
    private NormalButton Back;
    private NormalButton Save;
    private NormalButton Reload;
    private NormalButton Restart;
    private Animator EscMenuA;
    private Animator EscMenuB;
    private boolean wantBack;
    private boolean wantSave;
    private boolean wantReload;
    private boolean wantRestart;

    public EscMenu(){
        super("GamePause");

        this.EscMenu = new OthelloObject("EscMenu");
        this.setImage(new Image("User_info_backGround"));
        this.setSize(500,150);
        this.EscMenu.setAlpha(0);

        this.Back = new NormalButton("Back");
        this.Back.setActive(false);
        this.Back.setPosition(0,120);

        this.Save = new NormalButton("Sav3");
        this.Save.setActive(false);
        this.Save.setPosition(0,40);

        this.Reload = new NormalButton("Reload");
        this.Reload.setActive(false);
        this.Reload.setPosition(0,-40);

        this.Restart = new NormalButton("Restart");
        this.Restart.setActive(false);
        this.Restart.setPosition(0,-120);

        this.addObj(this.EscMenu);
        this.addObj(this.Back);
        this.addObj(this.Save);
        this.addObj(this.Reload);
        this.addObj(this.Restart);

        this.EscMenuA = new Animator(0);
        this.EscMenuB = new Animator(0);
        this.addComponent(this.EscMenuA);
        this.addComponent(this.EscMenuB);
    }

    public void setEscMenuAlpha(int a){
        this.EscMenu.setAlpha(a);
    }

    public void setEscMenuActive(boolean flag){
        this.Back.setActive(flag);
        this.Save.setActive(flag);
        this.Reload.setActive(flag);
        this.Restart.setActive(flag);
    }

    public boolean isWantBack(){
        return this.wantBack;
    }

    public boolean isWantSave(){
        return this.wantSave;
    }

    public boolean isWantReload(){
        return this.wantReload;
    }

    public boolean isWantRestart(){
        return this.wantRestart;
    }

    public void setBack(boolean flag){
        this.wantBack = flag;
    }

    public void setEscSave(boolean flag){
        this.wantSave = flag;
    }

    public void setReload(boolean flag){
        this.wantReload = flag;
    }

    public void setRestart(boolean flag){
        this.wantRestart = flag;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if(this.Back.isClicked()){
            this.wantBack = true;
        }
        if(this.Save.isClicked()){
            this.wantSave = true;
        }
        if(this.Reload.isClicked()){
            this.wantReload = true;
        }
        if(this.Restart.isClicked()){
            this.wantRestart = true;
        }
    }
}
