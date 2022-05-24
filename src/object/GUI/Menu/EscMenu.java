package object.GUI.Menu;

import component.animation.Animator;
import graphics.Image;
import object.GUI.Buttons.NormalButton;
import object.OthelloObject;

public class EscMenu extends OthelloObject {
    private final OthelloObject EscMenu;
    private final NormalButton Quit;
    private final NormalButton Save;
    private final NormalButton Load;
    private final NormalButton Restart;
    private final Animator EscMenuA;
    private final Animator EscMenuB;
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

        this.Quit = new NormalButton("Quit");
        this.Quit.setActive(false);
        this.Quit.setPosition(0,120);

        this.Save = new NormalButton("Save");
        this.Save.setActive(false);
        this.Save.setPosition(0,40);

        this.Load = new NormalButton("Load");
        this.Load.setActive(false);
        this.Load.setPosition(0,-40);

        this.Restart = new NormalButton("Restart");
        this.Restart.setActive(false);
        this.Restart.setPosition(0,-120);

        this.addObj(this.EscMenu);
        this.addObj(this.Quit);
        this.addObj(this.Save);
        this.addObj(this.Load);
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
        this.Quit.setActive(flag);
        this.Save.setActive(flag);
        this.Load.setActive(flag);
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

    public void setQuit(boolean flag){
        this.wantBack = flag;
    }

    public void setEscSave(boolean flag){
        this.wantSave = flag;
    }

    public void setLoad(boolean flag){
        this.wantReload = flag;
    }

    public void setRestart(boolean flag){
        this.wantRestart = flag;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if(this.Quit.isClicked()){
            this.wantBack = true;
        }
        if(this.Save.isClicked()){
            this.wantSave = true;
        }
        if(this.Load.isClicked()){
            this.wantReload = true;
        }
        if(this.Restart.isClicked()){
            this.wantRestart = true;
        }
    }
}
