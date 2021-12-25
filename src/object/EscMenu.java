package object;

import component.animation.Animator;
import graphics.Image;
import object.GUI.Buttons.NormalButton;

public class EscMenu extends OthelloObject{
    private OthelloObject EscMenu;
    private NormalButton Back;
    private NormalButton Save;
    private NormalButton Reload;
    private Animator EscMenuA;
    private Animator EscMenuB;
    private boolean wantBack;
    private boolean wantSave;
    private boolean wantReload;

    public EscMenu(){
        super("GamePause");

        this.EscMenu = new OthelloObject("EscMenu");
        this.setSprite(new Image("User_info_backGround"));
        this.setSize(500,150);
        this.EscMenu.setAlpha(0);

        this.Back = new NormalButton("Back");
        this.Back.setActive(false);
        this.Back.setPosition(0,100);

        this.Save = new NormalButton("Sav3");
        this.Save.setActive(false);
        this.Save.setPosition(0,0);

        this.Reload = new NormalButton("Reload");
        this.Reload.setActive(false);
        this.Reload.setPosition(0,-100);

        this.addObj(this.EscMenu);
        this.addObj(this.Back);
        this.addObj(this.Save);
        this.addObj(this.Reload);

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

    public void setBack(boolean flag){
        this.wantBack = flag;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if(this.Back.isClicked()){
            this.wantBack = true;
        }
    }
}
